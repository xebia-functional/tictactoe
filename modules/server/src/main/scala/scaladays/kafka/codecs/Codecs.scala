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
    // TODO - Build uuidCodec using Codec.string
    ???

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
    // TODO
    ???

  given startMatchCodec: Codec[StartGame] =
    Codec.record(name = "StartMatch", namespace = eventsNamespace) { field =>
      (
        field("gameId", _.gameId),
        field("crossPlayer", _.crossPlayer),
        field("circlePlayer", _.circlePlayer)
      ).mapN(StartGame.apply)
    }

  given endMatchCodec: Codec[EndGame] =
  // TODO
    ???

  given turnMatchCodec: Codec[TurnGame] =
  // TODO
    ???

  given registerPlayerCodec: Codec[RegisterPlayer] =
  // TODO
    ???

  given rejectEventCodec: Codec[RejectEvent] =
  // TODO
    ???

  given eventCodec: Codec[Event] = Codec.union[Event] { alt =>
    alt[Login] |+| alt[WaitingForMatch] |+| alt[StartGame] |+| alt[
      EndGame
    ] |+| alt[TurnGame] |+| alt[RegisterPlayer] |+| alt[RejectEvent]
  }

  given pieceCodec: Codec[Piece] = Codec.enumeration[Piece](
    name = "Piece",
    eventsNamespace,
    Piece.values.map(_.toString),
    _.toString,
    str => Piece.values.toList.find(_.toString === str).toRight(AvroError(s"Can't decode Piece with value: $str"))
  )

  given gameStateCodec: Codec[GameState] =
  // TODO
    ???

  given positionCodec: Codec[Position] =
  // TODO
    ???
