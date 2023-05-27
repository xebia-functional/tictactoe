package scaladays.kafka

import cats.effect.std.Dispatcher
import cats.effect.{Async, Resource}
import org.apache.kafka.streams.scala.StreamsBuilder
import fs2.kafka.vulcan.AvroSettings
import org.typelevel.log4cats.Logger
import scaladays.kafka.codecs.Codecs.given
import scaladays.kafka.messages.Events.TTTEvent
import scaladays.models.KafkaConfiguration
import scaladays.models.ids.*
import scaladays.models.Game

trait KafkaFacade[F[_]]:

  def eventStorage: EventStorage[F]

object KafkaFacade:

  def impl[F[_]: Async: Logger](
      conf: KafkaConfiguration,
      avroSettings: AvroSettings[F]): Resource[F, KafkaFacade[F]] =
    for
      producer      <- Producer.impl[F, EventId, TTTEvent](
                         conf.uri,
                         avroSettings,
                         conf.clientId,
                         conf.topics.inputTopic
                       )
    yield new KafkaFacade[F]:

      override lazy val eventStorage: EventStorage[F] = EventStorage.impl(producer)
