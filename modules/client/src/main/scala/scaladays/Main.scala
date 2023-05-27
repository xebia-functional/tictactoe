package scaladays

import scala.scalajs.js.annotation.*
import scaladays.models.*
import scaladays.views.*
import cats.effect.IO
import cats.effect.IO.asyncForIO
import tyrian.Html.*
import tyrian.*
import tyrian.cmds.*
import scaladays.main.Update
import tyrian.TyrianApp

type ModelIO = Model[IO]

@JSExportTopLevel("TyrianApp")
object Main extends TyrianApp[Msg, ModelIO]:

  lazy val updateAlg: Update[IO] = Update.impl[IO]()

  def init(flags: Map[String, String]): (ModelIO, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  def update(model: ModelIO): Msg => (ModelIO, Cmd[IO, Msg]) =
    updateAlg.update(model)

  def view(model: ModelIO): Html[Msg] =
    model match
      case Model(nickname) =>
        MainView.mainScreen(nickname)

  def subscriptions(model: ModelIO): Sub[IO, Msg] = Sub.None
