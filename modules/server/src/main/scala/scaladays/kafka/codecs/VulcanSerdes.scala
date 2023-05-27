package scaladays.kafka.codecs

import cats.effect.std.Dispatcher
import cats.effect.{Async, Sync}
import cats.syntax.all.*
import fs2.kafka.Headers
import fs2.kafka.vulcan.{AvroSettings, avroDeserializer, avroSerializer}
import org.apache.kafka.common.header
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Joined, Materialized, Produced}
import org.apache.kafka.streams.state.{KeyValueBytesStoreSupplier, KeyValueStore, StoreBuilder, Stores}
import scaladays.kafka.codecs.Codecs.given
import scaladays.kafka.codecs.instances.{KeySerde, ValueSerde}
import scaladays.kafka.messages.Events.*
import scaladays.models.{Game, KafkaConfiguration}
import scaladays.models.ids.{EventId, GameId, Nickname, PlayerId}
import vulcan.Codec

import scala.reflect.ClassTag

final case class VulcanSerdes(
    eventIdKey: KeySerde[EventId],
    nicknameKey: KeySerde[Nickname],
    tttEventValue: ValueSerde[TTTEvent],
    playerIdValue: ValueSerde[PlayerId]
  ):

  given KeySerde[EventId]                         = eventIdKey
  given KeySerde[Nickname]                        = nicknameKey
  given ValueSerde[TTTEvent]                      = tttEventValue
  given ValueSerde[PlayerId]                      = playerIdValue

object VulcanSerdes:

  private type Ser[F[_], A] = (String, Headers, A) => F[Array[Byte]]

  private type Des[F[_], A] = (String, Headers, Array[Byte]) => F[A]

  private def serde[F[_]: Sync, A](
      dispatcher: Dispatcher[F],
      serializer: Ser[F, A],
      deserializer: Des[F, A]): Serde[A] =
    Serdes.serdeFrom(
      new org.apache.kafka.common.serialization.Serializer[A]:

        override def serialize(topic: String, data: A): Array[Byte] =
          if data == null then Array.empty
          else
            dispatcher.unsafeRunSync(serializer(topic, Headers.empty, data))

      ,
      new org.apache.kafka.common.serialization.Deserializer[A]:

        override def deserialize(topic: String, data: Array[Byte]): A =
          if data == null || data.isEmpty then null.asInstanceOf[A]
          else
            dispatcher.unsafeRunSync(deserializer(topic, Headers.empty, data))

    )

  private def serdeKey[F[_]: Sync, A: Codec](avroSettings: AvroSettings[F], dispatcher: Dispatcher[F]): F[KeySerde[A]] =
    (
      avroSerializer[A].using(avroSettings).forKey,
      avroDeserializer[A].using(avroSettings).forKey
    ).mapN((s, d) => KeySerde(serde[F, A](dispatcher, s.serialize, d.deserialize)))

  private def serdeValue[F[_]: Sync, A: Codec](
      avroSettings: AvroSettings[F],
      dispatcher: Dispatcher[F]): F[ValueSerde[A]] =
    (
      avroSerializer[A].using(avroSettings).forValue,
      avroDeserializer[A].using(avroSettings).forValue
    ).mapN((s, d) => ValueSerde(serde[F, A](dispatcher, s.serialize, d.deserialize)))

  def build[F[_]: Async](avroSettings: AvroSettings[F], dispatcher: Dispatcher[F]): F[VulcanSerdes] =
    (
      serdeKey[F, EventId](avroSettings, dispatcher),
      serdeKey[F, Nickname](avroSettings, dispatcher),
      serdeValue[F, TTTEvent](avroSettings, dispatcher),
      serdeValue[F, PlayerId](avroSettings, dispatcher)
    ).mapN(VulcanSerdes.apply)
