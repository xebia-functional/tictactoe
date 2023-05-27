package scaladays

import scala.scalajs.js.annotation.*
import scaladays.models.*
import scaladays.views.*
import cats.effect.IO
import cats.effect.IO.asyncForIO
import scaladays.clients.ScalaDaysClient
import tyrian.Html.*
import tyrian.*
import tyrian.cmds.*
import scaladays.main.Update
import tyrian.TyrianApp

type ModelIO = Model[IO]

@JSExportTopLevel("TyrianApp")
object Main extends TyrianApp[Msg, ModelIO]:

  lazy val scalaDaysClient: ScalaDaysClient[IO] = ScalaDaysClient.impl("localhost", 28082)
  lazy val updateAlg: Update[IO] = Update.impl[IO](scalaDaysClient)

  def init(flags: Map[String, String]): (ModelIO, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  def update(model: ModelIO): Msg => (ModelIO, Cmd[IO, Msg]) =
    updateAlg.update(model)

  def view(model: ModelIO): Html[Msg] =
    model match
      case Model(nickname, Player.Empty, Nil) =>
        MainView.mainScreen(nickname)

      case Model(nickname, Player.Empty, errors) =>
        MainView.errorMainScreen(nickname, errors)

      case Model(nickname, Player.Waiting, _) =>
        MainView.waitingLoginScreen(nickname)

      case model =>
        MainView.errorScreen(model)

  def subscriptions(model: ModelIO): Sub[IO, Msg] = Sub.None
