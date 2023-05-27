package scaladays.kafka

import scaladays.kafka.messages.Events.*

import cats.effect.{Async, Resource}
import cats.implicits.*

import fs2.kafka.vulcan.{avroSerializer, AvroSettings}
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords, ProducerSettings}
import vulcan.Codec

private[kafka] trait Producer[F[_], K, V]:

  def sendMessage(key: K, value: V): F[Unit]

private[kafka] object Producer:

  def impl[F[_]: Async, K: Codec, V: Codec](
      brokerAddress: String,
      schemaRegistrySettings: AvroSettings[F],
      clientId: String,
      topic: String): Resource[F, Producer[F, K, V]] =
    def settings: ProducerSettings[F, K, V] =
      ProducerSettings[F, K, V](
        avroSerializer[K].using(schemaRegistrySettings),
        avroSerializer[V].using(schemaRegistrySettings)
      ).withBootstrapServers(brokerAddress).withClientId(clientId)

    KafkaProducer
      .resource(settings)
      .map(kP => (key: K, value: V) => kP.produce(ProducerRecords.one(ProducerRecord(topic, key, value))).flatten.void)
