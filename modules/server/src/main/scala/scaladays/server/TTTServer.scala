package scaladays.server

import scaladays.kafka.messages.Events
import scaladays.kafka.{EventHandler, EventStorage, LoginStorage}
import scaladays.models.http.*
import scaladays.models.ids.*
import cats.effect.Async
import cats.implicits.*
import scaladays.models.{Game, Piece, Position}

trait TTTServer[F[_]]:

  def login(nickname: Nickname): F[PlayerId]

  def processClientAction(clientAction: ClientAction): F[Unit]

  def response(playerId: PlayerId): fs2.Stream[F, Game]

object TTTServer:

  def impl[F[_]: Async](
      eventStorage: EventStorage[F],
      eventHandler: EventHandler[F],
      loginStorage: LoginStorage[F]): TTTServer[F] =
    new TTTServer[F]:

      override def login(nickname: Nickname): F[PlayerId] =
        for
          _        <- eventStorage.login(nickname)
          pId      <- PlayerId()
          playerId <- loginStorage.upsertPlayer(pId, nickname)
        yield playerId

      override def response(playerId: PlayerId): fs2.Stream[F, Game] =
        eventHandler.processEvent(playerId)

      override def processClientAction(clientAction: ClientAction): F[Unit] =
        clientAction match
          case JoinGame(playerId)                      =>
            eventStorage.waitForMatch(playerId).void
          case Turn(playerId, gameId, piece, position) =>
            eventStorage
              .turnGame(
                gameId,
                playerId,
                Position(position.x, position.y),
                piece
              ).void
