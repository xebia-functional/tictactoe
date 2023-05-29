package scaladays

import scala.scalajs.js.annotation.*
import scaladays.clients.ScalaDaysClient
import scaladays.models.*
import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.views.{MainView, WaitingGameView}
import cats.effect.IO
import cats.effect.IO.asyncForIO
import cats.implicits.*
import org.http4s.Uri
import tyrian.Html.*
import tyrian.*
import tyrian.cmds.*
import io.chrisdavenport.fuuid.FUUID
import scaladays.main.Update
import tyrian.TyrianApp

type ModelIO = Model[IO]

@JSExportTopLevel("TyrianApp")
object Main extends TyrianApp[Msg, ModelIO]:

  lazy val scalaDaysClient: ScalaDaysClient[IO] = ScalaDaysClient.impl("localhost", 28082)
  lazy val updateAlg: Update[IO]                = Update.impl[IO](scalaDaysClient)

  def init(flags: Map[String, String]): (ModelIO, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  def update(model: ModelIO): Msg => (ModelIO, Cmd[IO, Msg]) =
    updateAlg.update(model)

  def view(model: ModelIO): Html[Msg] =
    model match
      case Model(nickname, Player.Empty, Contest.Empty, _, Nil) =>
        MainView.mainScreen(nickname)

      case Model(nickname, Player.Empty, Contest.Empty, _, errors) =>
        MainView.errorMainScreen(nickname, errors)

      case Model(nickname, Player.Waiting, Contest.Empty, None, _) =>
        MainView.waitingLoginScreen(nickname)

      case Model(nickname, Player.Registered(playerId), Contest.Empty, None, _) =>
        WaitingGameView.startGameScreen(playerId, nickname)

      case Model(_, Player.Registered(_), Contest.InProgress(m), Some(_), Nil) =>
        WaitingGameView.waitingGameScreen(m)

      case Model(_, Player.Registered(_), Contest.InProgress(m), None, Nil) =>
        WaitingGameView.waitingGameScreen(m)

      case Model(_, Player.Registered(_), Contest.InProgress(m), None, errors) =>
        WaitingGameView.waitingGameErrorScreen(m, errors)

      case Model(nickname, Player.Registered(playerId), Contest.Registered(game), _, _) =>
        MainView.errorMainScreen(nickname, List(UnexpectedServerError("View not implemented")))

      case model =>
        MainView.errorScreen(model)

  def subscriptions(model: ModelIO): Sub[IO, Msg] = Sub.None
