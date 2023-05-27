package scaladays.kafka

import java.time.Instant

import scaladays.kafka.messages.Events.{Event, TTTEvent, *}
import scaladays.models.ids.*

import cats.effect.Async
import cats.implicits.*

import scaladays.models.{GameState, Piece, Position}

trait EventStorage[F[_]]:

  def login(nickname: Nickname): F[EventId]

  def register(playerId: PlayerId, nickname: Nickname): F[EventId]

  def waitForMatch(playerId: PlayerId): F[EventId]

  def startGame(GameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId): F[EventId]

  def endGame(GameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId, gameState: GameState): F[EventId]

  def turnGame(GameId: GameId, playerId: PlayerId, position: Position, piece: Piece): F[EventId]

  def rejectEvent(playerId: PlayerId, eventId: EventId, reason: String): F[EventId]

object EventStorage:

  def impl[F[_]: Async](producer: Producer[F, EventId, TTTEvent]): EventStorage[F] = new EventStorage[F]:

    private def toEvent(event: Event): F[(EventId, TTTEvent)] =
      for
        fd <- Async[F].monotonic
        eId <- EventId()
      yield (eId, TTTEvent(Instant.ofEpochMilli(fd.toMillis), event))

    private def sendEvent(event: Event): F[EventId] =
      for
        (eId, event) <- toEvent(event)
        _ <- producer.sendMessage(eId, event)
      yield eId

    override def login(nickname: Nickname): F[EventId] =
      sendEvent(Login(nickname))

    override def register(playerId: PlayerId, nickname: Nickname): F[EventId] =
      sendEvent(RegisterPlayer(playerId, nickname))

    override def waitForMatch(playerId: PlayerId): F[EventId] =
      sendEvent(WaitingForMatch(playerId))

    override def startGame(GameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId): F[EventId] =
      sendEvent(StartGame(GameId, crossPlayer, circlePlayer))

    override def endGame(
        GameId: GameId,
        crossPlayer: PlayerId,
        circlePlayer: PlayerId,
        gameState: GameState): F[EventId] =
      sendEvent(EndGame(GameId, crossPlayer: PlayerId, circlePlayer: PlayerId, gameState))

    override def turnGame(GameId: GameId, playerId: PlayerId, position: Position, piece: Piece): F[EventId] =
      sendEvent(TurnGame(GameId, playerId, position, piece))

    override def rejectEvent(playerId: PlayerId, eventId: EventId, reason: String): F[EventId] =
      sendEvent(RejectEvent(playerId, eventId, reason))
