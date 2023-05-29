package scaladays.models

import cats.effect.Async
import scaladays.models.ids.*
import org.http4s.Uri
import tyrian.websocket.*

object WebSocketMessage:

  enum WebSocketStatus:

    case Connecting(playerId: PlayerId)

    case Connected[F[_]: Async](ws: Option[WebSocket[F]])

    case ConnectionError(error: ClientError)

    case Nop

    case Disconnecting

    case Disconnected

    def asMsg: Msg = Msg.WebSocketStatus(this)

enum Msg:

  case UpdateNickname(nickname: Nickname)

  case LoginRequest(nickname: Nickname)

  case LoginSuccess(playerId: PlayerId)

  case Logout

  case LoginError(error: ClientError)

  case WebSocketStatus(status: WebSocketMessage.WebSocketStatus)

  case GameUpdate(game: Game)
