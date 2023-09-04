package scaladays.clients

import scaladays.models.http.{JoinGame, Login, LoginResponse, Turn}
import scaladays.models.ids.{GameId, Nickname, PlayerId}
import scaladays.models.{WebSocketMessage, *}
import cats.effect.Async
import cats.implicits.*
import org.http4s.Method.PATCH
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dom.FetchClientBuilder
import org.http4s.{EntityDecoder, EntityEncoder, Request, Uri}
import tyrian.cmds.Logger
import tyrian.websocket.*
import tyrian.{Cmd, Sub}
import io.circe.parser.*
import io.circe.syntax.*

trait ScalaDaysClient[F[_]]:

  def getLogin(nickname: Nickname): Cmd[F, Msg]

  def connect(playerId: PlayerId): Cmd[F, Msg]

  def handleWebSocket(ws: WebSocket[F]): Sub[F, Msg]

  def publishWs(playerId: PlayerId, gameId: GameId, movement: Movement, ws: WebSocket[F]): Cmd[F, Msg]

  def disconnectWebSocket(ws: WebSocket[F]): Cmd[F, Msg]

object ScalaDaysClient:

  def impl[F[_]: Async](host: String, port: Int): ScalaDaysClient[F] = new ScalaDaysClient[F] with Http4sClientDsl[F]:

    lazy val httpUri = Uri.unsafeFromString(s"http://$host:$port")

    lazy val wsUri = Uri.unsafeFromString(s"ws://$host:$port")

    given EntityEncoder[F, Login] = jsonEncoderOf

    given EntityDecoder[F, LoginResponse] = jsonOf

    lazy val client = FetchClientBuilder[F].create

    override def getLogin(nickname: Nickname): Cmd[F, Msg] =
      Cmd.Run(
        if nickname.value == "" then Msg.LoginError(InvalidNickname).pure[F]
        else
          client
            .expectOption[LoginResponse](
              req = Request(uri = httpUri / "login", method = PATCH).withEntity(Login(nickname))
            )
            .map {
              case Some(LoginResponse(playerId)) => Msg.LoginSuccess(playerId)
              case None                          => Msg.LoginError(NotFoundNickname)
            }.recover(err => Msg.LoginError(UnexpectedServerError(err.getMessage)))
      )(identity)

    override def connect(playerId: PlayerId): Cmd[F, Msg] =
      val uri = wsUri / "player" / playerId.show / "join"
      WebSocket.connect[F, Msg](
        address = uri.renderString,
        onOpenMessage = JoinGame(playerId).asJson.noSpacesSortKeys,
        keepAliveSettings = KeepAliveSettings.default
      ) {
        case WebSocketConnect.Error(err) =>
          WebSocketMessage.WebSocketStatus.ConnectionError(WebSocketError(err)).asMsg

        case WebSocketConnect.Socket(ws) =>
          WebSocketMessage.WebSocketStatus.Connected[F](Some(ws)).asMsg
      }

    override def handleWebSocket(ws: WebSocket[F]): Sub[F, Msg] =
      ws.subscribe {
        case WebSocketEvent.Receive(message) =>
          decode[Game](message).fold(
            e => WebSocketMessage.WebSocketStatus.ConnectionError(WebSocketError(e.getMessage)).asMsg,
            game => Msg.GameUpdate(game)
          )
        case WebSocketEvent.Heartbeat        => WebSocketMessage.WebSocketStatus.Nop.asMsg
        case WebSocketEvent.Open             => WebSocketMessage.WebSocketStatus.Nop.asMsg
        case _                               => WebSocketMessage.WebSocketStatus.ConnectionError(WebSocketError("Unknown websocket message")).asMsg
      }

    override def publishWs(playerId: PlayerId, gameId: GameId, movement: Movement, ws: WebSocket[F]): Cmd[F, Msg] =
      val turn = Turn(playerId, gameId, movement.piece, movement.position)
      ws.publish(turn.asJson.noSpacesSortKeys)
