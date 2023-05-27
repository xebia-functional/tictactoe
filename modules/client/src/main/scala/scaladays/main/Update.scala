package scaladays.main

import cats.effect.Async
import scaladays.models.*
import tyrian.Cmd
import tyrian.cmds.Logger

trait Update[F[_]]:
  def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg])

object Update:

  def impl[F[_]: Async](): Update[F] = new Update[F]:

    override def update(model: Model[F]): Msg => (Model[F], Cmd[F, Msg]) =
      case Msg.UpdateNickname(nickname) =>
        (model.copy(nickname = nickname), Cmd.None)

      case _ =>
        (model, Logger.debug("Erm"))