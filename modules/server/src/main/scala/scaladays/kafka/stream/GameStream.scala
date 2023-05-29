package scaladays.kafka.stream

import cats.effect.Async
import cats.syntax.all.*
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.Branched
import scaladays.kafka.codecs.{VulcanSerdes, instances}
import scaladays.kafka.messages.Events.*
import scaladays.kafka.topology.TurnLogic
import scaladays.models.ids.{EventId, GameId}
import scaladays.models.{Game, KafkaConfiguration}

import java.time.Instant

object GameStream:

  def buildStream(kfg: KafkaConfiguration, builder: StreamsBuilder, serdes: VulcanSerdes): Unit =
    import serdes.given
    import instances.given

    def matchStream(): Unit =
      builder
        .stream[EventId, TTTEvent](kfg.topics.inputTopic)
        .flatMap[GameId, Game] {
          case (_, TTTEvent(_, StartGame(gameId, playerId1, playerId2))) =>
            Some((gameId, Game.init(gameId, playerId1, playerId2)))
          case _                                                         => None
        }
        .to(kfg.topics.gameTopic)

    def turnStream(): Unit =
      val VALID_TURN   = "Valid-Turn"
      val INVALID_TURN = "Invalid-Turn"
      val NAME_BRANCH  = "Turn-Branch-"
      val matchTable   = builder.table[GameId, Game](kfg.topics.gameTopic)
      val branches     = builder
        .stream[EventId, TTTEvent](kfg.topics.inputTopic)
        .flatMap[GameId, AggMessage[EventId, TurnGame]] {
          case (eventId, TTTEvent(_, TurnGame(gameId, playerId, position, piece))) =>
            Some((gameId, AggMessage[EventId, TurnGame](eventId, TurnGame(gameId, playerId, position, piece))))
          case _                                                                   => None
        }
        .leftJoin(matchTable)(TurnLogic.processTurn)
        .split(Named.as(NAME_BRANCH))
        .branch((_, eitherMatch) => eitherMatch.isRight, Branched.as(VALID_TURN))
        .branch((_, eitherMatch) => eitherMatch.isLeft, Branched.as(INVALID_TURN))
        .noDefaultBranch()

      branches(s"$NAME_BRANCH$VALID_TURN").flatMapValues(_.toOption).to(kfg.topics.gameTopic)
      branches(s"$NAME_BRANCH$INVALID_TURN")
        .flatMapValues(_.swap.toOption)
        .map((_, re) => (EventId.unsafe(), TTTEvent(Instant.now(), re)))
        .to(kfg.topics.inputTopic)

    matchStream() // StartMatch -> Match
    turnStream()  // ProcessTurn
