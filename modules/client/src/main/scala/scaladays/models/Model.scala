package scaladays.models

import scaladays.models.ids.Nickname

final case class Model[F[_]](
    nickname: Nickname
  )

object Model:

  def init[F[_]]: Model[F] = Model[F](Nickname(""))
