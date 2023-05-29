package scaladays.models

import scaladays.models.ids.{Nickname, PlayerId}

import tyrian.websocket.WebSocket

final case class Model[F[_]](
    nickname: Nickname,
    player: Player,
    contest: Contest,
    ws: Option[WebSocket[F]],
    errors: List[ClientError]
  )

object Model:

  def init[F[_]]: Model[F] = Model[F](Nickname(""), Player.Empty, Contest.Empty, None, List.empty)
