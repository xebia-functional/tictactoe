package scaladays.main

import cats.effect.kernel.Async
import scaladays.clients.ScalaDaysClient
import scaladays.models.*
import tyrian.Cmd
import tyrian.cmds.Logger
import cats.implicits.*
import scaladays.models.ids.{GameId, PlayerId}

trait Update[F[_]]:
  def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg])

object Update:

  def impl[F[_]: Async](scalaDaysClient: ScalaDaysClient[F]): Update[F] = new Update[F]:

    override def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg]) =
      case msg @ Msg.UpdateNickname(nickname) =>
        (model.copy(nickname = nickname), Cmd.None)

      case msg@Msg.LoginError(UnexpectedServerError(error)) =>
        (model.copy(errors = msg.error :: model.errors), Logger.errorOnce(s"Server error: $error"))

      case msg@Msg.LoginError(err) =>
        (model.copy(errors = err :: model.errors), Cmd.None)

      case msg@Msg.LoginRequest(nickname) =>
        (model.copy(nickname = nickname, player = Player.Waiting, errors = Nil), scalaDaysClient.getLogin(nickname))

      case msg@Msg.LoginSuccess(playerId) =>
        (model.copy(player = Player.Registered(playerId), errors = Nil), Cmd.None)