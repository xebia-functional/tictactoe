package scaladays.kafka.stream

import cats.effect.Async
import cats.syntax.all.*
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.Branched
import scaladays.kafka.codecs.{VulcanSerdes, instances}
import scaladays.kafka.messages.Events.*
import scaladays.models.ids.{EventId, GameId}
import scaladays.models.{Game, KafkaConfiguration}

import java.time.Instant

object GameStream:

  def buildStream(kfg: KafkaConfiguration, builder: StreamsBuilder, serdes: VulcanSerdes): Unit =
    import serdes.given
    import instances.given

    def matchStream(): Unit =
      // TODO
      ???

    matchStream() // StartMatch -> Match
