package scaladays

import scaladays.kafka.KafkaFacade
import scaladays.models.Configuration
import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.server.TTTServer
import cats.effect.{Async, Resource}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.KeyValueStore
import fs2.kafka.vulcan.AvroSettings
import org.typelevel.log4cats.Logger

trait Backend[F[_]]:

  def tttServer: Resource[F, TTTServer[F]]

object Backend:

  def impl[F[_]: Async: Logger](
      configuration: Configuration,
      builder: StreamsBuilder,
      schemaRegistrySettings: AvroSettings[F]): Backend[F] =
    new Backend[F]:

      lazy val kafkaFacade: Resource[F, KafkaFacade[F]] =
        KafkaFacade.impl(configuration.kafka, builder, schemaRegistrySettings)

      override lazy val tttServer: Resource[F, TTTServer[F]] =
        kafkaFacade.map(kFacade => TTTServer.impl(kFacade.eventStorage, kFacade.loginStorage))
