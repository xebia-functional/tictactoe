package scaladays.main

import cats.effect.kernel.Async
import scaladays.clients.ScalaDaysClient
import scaladays.models.*
import tyrian.Cmd
import tyrian.cmds.Logger
import cats.implicits.*
import scaladays.models.ids.{GameId, PlayerId}
import tyrian.websocket.WebSocket

trait Update[F[_]]:
  def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg])

object Update:

  def impl[F[_]: Async](scalaDaysClient: ScalaDaysClient[F]): Update[F] = new Update[F]:

    override def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg]) =

      case msg @ Msg.UpdateNickname(nickname) =>
        (model.copy(nickname = nickname), Cmd.None)

      case msg @ Msg.LoginError(UnexpectedServerError(error)) =>
        (model.copy(errors = msg.error :: model.errors), Logger.errorOnce(s"Server error: $error"))

      case msg @ Msg.LoginError(err) =>
        (model.copy(errors = err :: model.errors), Cmd.None)

      case msg @ Msg.LoginRequest(nickname) =>
        (model.copy(nickname = nickname, player = Player.Waiting, errors = Nil), scalaDaysClient.getLogin(nickname))

      case msg @ Msg.LoginSuccess(playerId) =>
        (model.copy(player = Player.Registered(playerId), errors = Nil), Cmd.None)

      case msg @ Msg.Logout =>
        (Model.init, Cmd.None)

      case msg @ Msg.WebSocketStatus(WebSocketMessage.WebSocketStatus.Connecting(playerId)) =>
        (model.copy(contest = Contest.InProgress("Creating a new game")), scalaDaysClient.connect(playerId))

      case msg @ Msg.WebSocketStatus(WebSocketMessage.WebSocketStatus.ConnectionError(error)) =>
        (model.copy(contest = Contest.InProgress("Something went wrong"), errors = error :: model.errors), Logger.errorOnce(s"Websocket error: ${error.reason}"))

      case msg @ Msg.WebSocketStatus(WebSocketMessage.WebSocketStatus.Connected[F](ws)) =>
        (model.copy(ws = ws, contest = Contest.InProgress("Waiting for another player to join the game"), errors = Nil), Cmd.None)

      case msg @ Msg.GameUpdate(g) =>
        (model.copy(contest = Contest.Registered(g)), Cmd.None)
