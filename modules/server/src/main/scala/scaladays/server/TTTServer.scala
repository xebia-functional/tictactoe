package scaladays.server

import scaladays.models.http.*
import scaladays.models.ids.*
import cats.effect.Async
import cats.implicits.*
import scaladays.models.*

trait TTTServer[F[_]]:

  def login(nickname: Nickname): F[PlayerId]

object TTTServer:

  def impl[F[_]: Async](): TTTServer[F] =
    new TTTServer[F]:

      override def login(nickname: Nickname): F[PlayerId] =
        PlayerId()
