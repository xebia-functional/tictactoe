package scaladays.models

import scaladays.models.ids.Nickname

final case class Model[F[_]](
    nickname: Nickname,
    player: Player,
    errors: List[ClientError]
  )

object Model:

  def init[F[_]]: Model[F] = Model[F](Nickname(""), Player.Empty, List.empty)
