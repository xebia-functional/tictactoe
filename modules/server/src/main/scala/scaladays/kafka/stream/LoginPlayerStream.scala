package scaladays.kafka.stream

import cats.effect.Async
import cats.syntax.all.*
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.Materialized
import scaladays.kafka.codecs.{instances, VulcanSerdes}
import scaladays.kafka.messages.Events.*
import scaladays.models.KafkaConfiguration
import scaladays.models.ids.{EventId, Nickname, PlayerId}

object LoginPlayerStream:

  def buildStream(kfg: KafkaConfiguration, builder: StreamsBuilder, serdes: VulcanSerdes): Unit =
    import serdes.given
    import instances.given
    builder
      .stream[EventId, TTTEvent](kfg.topics.inputTopic)
      .flatMap[Nickname, PlayerId] {
        case (_, TTTEvent(_, RegisterPlayer(playerId, nickname))) => Some((nickname, playerId))
        case _                                                    => None
      }
      .toTable(
        named = Named.as("PlayersTableProcessor"),
        materialized = instances.materialized(kfg.stores.registerPlayer)
      )
