package scaladays.kafka.messages

import cats.implicits.*
import scaladays.models.{GameState, Piece, Position}
import scaladays.models.ids.*

import java.time.Instant

object Events:

  sealed trait Event

  final case class Login(nickname: Nickname) extends Event

  final case class RegisterPlayer(playerId: PlayerId, nickname: Nickname) extends Event

  final case class WaitingForMatch(playerId: PlayerId) extends Event

  final case class StartGame(
      gameId: GameId,
      crossPlayer: PlayerId,
      circlePlayer: PlayerId
    ) extends Event

  final case class EndGame(
      gameId: GameId,
      crossPlayer: PlayerId,
      circlePlayer: PlayerId,
      gameState: GameState
    ) extends Event

  final case class TurnGame(
      gameId: GameId,
      playerId: PlayerId,
      position: Position,
      piece: Piece
    ) extends Event

  final case class RejectEvent(playerId: PlayerId, rejectEventId: EventId, reason: String) extends Event

  final case class TTTEvent(time: Instant, event: Event)

  final case class AggMessage[A, B](a: A, b: B)
