package scaladays.models

import cats.effect.Async
import scaladays.models.ids.*
import org.http4s.Uri
import tyrian.websocket.*

object WebSocketMessage:

  enum WebSocketStatus:

    case REPLACE_ME

    def asMsg: Msg = ???

enum Msg:

  case UpdateNickname(nickname: Nickname)

  case LoginRequest(nickname: Nickname)

  case LoginSuccess(playerId: PlayerId)

  case Logout

  case LoginError(error: ClientError)
