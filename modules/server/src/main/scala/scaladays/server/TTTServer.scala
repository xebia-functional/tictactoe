package scaladays.server

import scaladays.kafka.messages.Events
import scaladays.kafka.{EventHandler, EventStorage, LoginStorage}
import scaladays.models.http.*
import scaladays.models.ids.*
import cats.effect.Async
import cats.implicits.*
import scaladays.models.*

trait TTTServer[F[_]]:

  def login(nickname: Nickname): F[PlayerId]

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

