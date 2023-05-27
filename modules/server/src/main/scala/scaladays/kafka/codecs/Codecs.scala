package scaladays.kafka.codecs

import cats.syntax.all.*
import fs2.kafka.vulcan.AvroSettings
import io.chrisdavenport.fuuid.FUUID
import org.apache.avro.generic.GenericRecord
import scaladays.kafka.messages.Events.*
import scaladays.models.{Game, GameState, Movement, Piece, Position}
import scaladays.models.ids.{GameId, *}
import vulcan.{AvroError, Codec}

import java.time.Instant.ofEpochMilli
import scala.concurrent.duration.*
import scala.jdk.CollectionConverters.*

object Codecs:

  private val eventsNamespace = "events"

  private val gameNamespace = "game"

  given uuidCodec: Codec[FUUID] =
    Codec.string.imap[FUUID](x => FUUID.fromString(x).toOption.get)(_.show)

  given eventIdCodec: Codec[EventId] =
    uuidCodec.imap[EventId](EventId.apply)(_.value)

  given playerIdCodec: Codec[PlayerId] =
    uuidCodec.imap[PlayerId](PlayerId.apply)(_.value)

  given GameIdCodec: Codec[GameId] =
    uuidCodec.imap[GameId](GameId.apply)(_.value)

  given nicknameCodec: Codec[Nickname] =
    Codec.string.imap[Nickname](Nickname.apply)(_.value)

  given registerCodec: Codec[Login] =
    Codec.record(name = "Login", namespace = eventsNamespace)(
      _("nickname", _.nickname).map(Login.apply)
    )

  given waitingForMatchCodec: Codec[WaitingForMatch] =
    Codec.record(name = "WaitingForMatch", namespace = eventsNamespace)(
      _("playerId", _.playerId).map(WaitingForMatch.apply)
    )

  given startMatchCodec: Codec[StartGame] =
    Codec.record(name = "StartMatch", namespace = eventsNamespace) { field =>
      (
        field("gameId", _.gameId),
        field("crossPlayer", _.crossPlayer),
        field("circlePlayer", _.circlePlayer)
      ).mapN(StartGame.apply)
    }

  given endMatchCodec: Codec[EndGame] =
    Codec.record(name = "EndMatch", namespace = eventsNamespace) { field =>
      (
        field("gameId", _.gameId),
        field("crossPlayer", _.crossPlayer),
        field("circlePlayer", _.circlePlayer),
        field("gameState", _.gameState)
      ).mapN(EndGame.apply)
    }

  given turnMatchCodec: Codec[TurnGame] =
    Codec.record(name = "TurnMatch", namespace = eventsNamespace) { field =>
      (
        field("gameId", _.gameId),
        field("playerId", _.playerId),
        field("position", _.position),
        field("piece", _.piece)
      ).mapN(TurnGame.apply)
    }

  given registerPlayerCodec: Codec[RegisterPlayer] =
    Codec.record(name = "RegisterPlayer", namespace = eventsNamespace) { field =>
      (
        field("playerId", _.playerId),
        field("nickname", _.nickname)
      ).mapN(RegisterPlayer.apply)
    }

  given rejectEventCodec: Codec[RejectEvent] =
    Codec.record(name = "RejectEvent", namespace = eventsNamespace) { field =>
      (
        field("playerId", _.playerId),
        field("rejectEventId", _.rejectEventId),
        field("reason", _.reason)
      ).mapN(RejectEvent.apply)
    }

  given eventCodec: Codec[Event] = Codec.union[Event] { alt =>
    alt[Login] |+| alt[WaitingForMatch] |+| alt[StartGame] |+| alt[
      EndGame
    ] |+| alt[TurnGame] |+| alt[RegisterPlayer] |+| alt[RejectEvent]
  }

  given tttEventCodec: Codec[TTTEvent] =
    Codec.record(name = "TTTEvent", namespace = eventsNamespace) { field =>
      (
        field("time", _.time),
        field("event", _.event)
      ).mapN(TTTEvent.apply)
    }

  given pieceCodec: Codec[Piece] = Codec.enumeration[Piece](
    name = "Piece",
    eventsNamespace,
    Piece.values.map(_.toString),
    _.toString,
    str => Piece.values.toList.find(_.toString === str).toRight(AvroError(s"Can't decode Piece with value: $str"))
  )

  given gameStateCodec: Codec[GameState] = Codec.enumeration[GameState](
    name = "GameState",
    gameNamespace,
    GameState.values.map(_.toString),
    _.toString,
    str =>
      GameState.values.toList.find(_.toString === str).toRight(AvroError(s"Can't decode GameState with value: $str"))
  )

  given positionCodec: Codec[Position] =
    Codec.record(name = "Position", namespace = gameNamespace) { field =>
      (
        field("x", _.x),
        field("y", _.y)
      ).mapN(Position.apply)
    }

  given movementCodec: Codec[Movement] =
    Codec.record(name = "Movement", namespace = gameNamespace) { field =>
      (
        field("position", _.position),
        field("piece", _.piece),
        field("confirmed", _.confirmed)
      ).mapN(Movement.apply)
    }

  given matchCodec: Codec[Game] =
    Codec.record(name = "Game", namespace = gameNamespace) { field =>
      (
        field("gameId", _.gameId),
        field("crossPlayer", _.crossPlayer),
        field("circlePlayer", _.circlePlayer),
        field("state", _.state),
        field("movements", _.movements)
      ).mapN(Game.apply)
    }

  given aggregationCodec[A, B](using codecA: Codec[A], codecB: Codec[B]): Codec[AggMessage[A, B]] =
    Codec.record(name = "Aggregation", namespace = gameNamespace) { field =>
      (
        field("a", _.a),
        field("b", _.b)
      ).mapN(AggMessage.apply)
    }
