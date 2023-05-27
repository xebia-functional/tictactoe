package scaladays.kafka

import cats.effect.std.Dispatcher
import cats.effect.{Async, Resource}
import org.apache.kafka.streams.scala.StreamsBuilder
import fs2.kafka.vulcan.AvroSettings
import org.typelevel.log4cats.Logger
import scaladays.kafka.codecs.Codecs.given
import scaladays.kafka.codecs.VulcanSerdes
import scaladays.kafka.messages.Events.TTTEvent
import scaladays.models.KafkaConfiguration
import scaladays.models.ids.*
import scaladays.models.Game

trait KafkaFacade[F[_]]:

  def eventStorage: EventStorage[F]

  def loginStorage: LoginStorage[F]

object KafkaFacade:

  def impl[F[_]: Async: Logger](
      conf: KafkaConfiguration,
      builder: StreamsBuilder,
      avroSettings: AvroSettings[F]): Resource[F, KafkaFacade[F]] =
    for
      dispatcher    <- Dispatcher.parallel[F]
      producer      <- Producer.impl[F, EventId, TTTEvent](
                         conf.uri,
                         avroSettings,
                         conf.clientId,
                         conf.topics.inputTopic
                       )
      serdes        <- Resource.eval(VulcanSerdes.build[F](avroSettings, dispatcher))
      kTopology      = KTopology.impl(builder, conf, serdes)
      kafkaStreams  <- kTopology.buildTopology
      playerStorage <- Resource.eval(kTopology.createStore(kafkaStreams))
    yield new KafkaFacade[F]:

      override lazy val eventStorage: EventStorage[F] = EventStorage.impl(producer)

      override lazy val loginStorage: LoginStorage[F] =
        LoginStorage.impl(playerStorage, eventStorage)
