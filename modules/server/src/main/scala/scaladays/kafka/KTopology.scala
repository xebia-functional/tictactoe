package scaladays.kafka

import cats.effect.{Async, Resource}
import cats.syntax.all.*
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Joined, Produced}
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.state.{QueryableStoreTypes, ReadOnlyKeyValueStore}
import org.apache.kafka.streams.{KafkaStreams, StoreQueryParameters, StreamsConfig}
import scaladays.kafka.codecs.Codecs.given
import scaladays.kafka.codecs.VulcanSerdes
import scaladays.kafka.messages.Events.{AggMessage, TTTEvent, TurnGame}
import scaladays.kafka.stream.{GameStream, LoginPlayerStream, WaitingPlayerStream}
import scaladays.models.{Game, KafkaConfiguration}
import scaladays.models.ids.{EventId, GameId, Nickname, PlayerId}

import java.util.Properties

private[kafka] trait KTopology[F[_]]:

  def buildTopology: Resource[F, KafkaStreams]
  def createStore(ks: KafkaStreams): F[ReadOnlyKeyValueStore[Nickname, PlayerId]]

private[kafka] object KTopology:

  def impl[F[_]: Async](
      builder: StreamsBuilder,
      kafkaConfiguration: KafkaConfiguration,
      serdes: VulcanSerdes): KTopology[F] =

    def kafkaStreams: Resource[F, KafkaStreams] =
      Resource.make(
        Async[F].delay {
          val topology = builder.build()
          val props    = new Properties
          props.put(
            StreamsConfig.APPLICATION_ID_CONFIG,
            kafkaConfiguration.applicationId
          )
          props.put(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            kafkaConfiguration.uri
          )
          props.put(
            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
            Serdes.stringSerde.getClass
          )
          println(topology.describe())
          new KafkaStreams(topology, props)
        }
      )(ks => Async[F].delay(ks.close()))

    new KTopology[F]:

      override def buildTopology: Resource[F, KafkaStreams] =
        for
          _  <- Resource.pure(LoginPlayerStream.buildStream(kafkaConfiguration, builder, serdes))
          _  <- Resource.pure(WaitingPlayerStream.buildStream(kafkaConfiguration, builder, serdes))
          _  <- Resource.pure(GameStream.buildStream(kafkaConfiguration, builder, serdes))
          ks <- kafkaStreams
          _  <- Resource.eval(Async[F].delay(ks.start()))
        yield ks

      override def createStore(ks: KafkaStreams): F[ReadOnlyKeyValueStore[Nickname, PlayerId]] =
        Async[F].delay(
          ks.store(
            StoreQueryParameters.fromNameAndType(
              kafkaConfiguration.stores.registerPlayer,
              QueryableStoreTypes.keyValueStore[Nickname, PlayerId]
            )
          )
        )
