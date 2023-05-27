package scaladays.server

import scaladays.models.http.*
import scaladays.models.ids.*
import cats.effect.Async
import cats.implicits.*
import scaladays.kafka.{EventStorage, LoginStorage}
import scaladays.models.*

trait TTTServer[F[_]]:

  def login(nickname: Nickname): F[PlayerId]

object TTTServer:

  def impl[F[_]: Async](eventStorage: EventStorage[F],
                        loginStorage: LoginStorage[F]): TTTServer[F] =
    new TTTServer[F]:

      override def login(nickname: Nickname): F[PlayerId] =
        for
          _        <- eventStorage.login(nickname)
          pId      <- PlayerId()
          playerId <- loginStorage.upsertPlayer(pId, nickname)
        yield playerId
