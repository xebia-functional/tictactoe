package scaladays.kafka

import scaladays.kafka.messages.Events.*
import cats.effect.{Async, Resource}
import cats.implicits.*
import fs2.kafka.vulcan.{avroDeserializer, avroSerializer, AvroSettings}
import fs2.kafka.{AutoOffsetReset, ConsumerRecord, ConsumerSettings, KafkaConsumer}
import vulcan.Codec

private[kafka] trait Consumer[F[_], K, V]:

  def consumer[A](clientId: String)(process: (K, V) => F[Option[A]]): fs2.Stream[F, A]

private[kafka] object Consumer:

  def impl[F[_]: Async, K: Codec, V: Codec](
      brokerAddress: String,
      schemaRegistrySettings: AvroSettings[F],
      topic: String,
      groupId: String): Consumer[F, K, V] =
    new Consumer[F, K, V]:

      def consumerSettings(clientId: String): ConsumerSettings[F, K, V] =
        ConsumerSettings[F, K, V](
          avroDeserializer[K].using(schemaRegistrySettings),
          avroDeserializer[V].using(schemaRegistrySettings)
        ).withBootstrapServers(brokerAddress).withClientId(clientId).withGroupId(
            s"$groupId-$clientId"
          ).withAutoOffsetReset(
            AutoOffsetReset.Latest
          )

      override def consumer[A](clientId: String)(process: (K, V) => F[Option[A]]): fs2.Stream[F, A] =
        KafkaConsumer
          .stream(consumerSettings(clientId))
          .subscribeTo(topic)
          .partitionedRecords
          .map { partitionStream =>
            partitionStream.evalMapFilter { committable => process(committable.record.key, committable.record.value) }
          }
          .parJoinUnbounded
