package scaladays.server

import scala.concurrent.duration.*

import scaladays.models.http.{ClientAction, Login, LoginResponse}
import scaladays.models.ids.PlayerId

import cats.effect.Async
import cats.implicits.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.ParsingFailure
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.WebSocketFrame

import fs2.Stream
import org.typelevel.log4cats.Logger

import scala.concurrent.duration.*

object WebServer:

  def routes[F[_]: Async: Logger](
      ws: WebSocketBuilder[F],
      ticTacToe: TTTServer[F]): HttpRoutes[F] =
    given EntityDecoder[F, Login]         = jsonOf[F, Login]
    given EntityEncoder[F, LoginResponse] = jsonEncoderOf[LoginResponse]

    def processClientMessage(playerId: PlayerId, webSocketFrame: WebSocketFrame): F[Unit] = webSocketFrame match
      case WebSocketFrame.Text((str, _)) =>
        decode[ClientAction](str).fold(
          err => Logger[F].error(s"Message of player: $playerId cannot be processed: ${err.getMessage}"),
          _ => ().pure[F]
        )
      case WebSocketFrame.Close(_)       => Logger[F].info(s"Close connection for player $playerId")

    def sendResponse(playerId: PlayerId): fs2.Stream[F, WebSocketFrame] =
      fs2.Stream.awakeEvery(500.millis).map(_ => WebSocketFrame.Text("pong"))

    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case req @ PATCH -> Root / "login" =>
        req.as[Login].flatMap(loginReq => ticTacToe.login(loginReq.nickname)).flatMap(pId => Ok(LoginResponse(pId)))

      case GET -> Root / "player" / PlayerId(playerId) / "join" =>
        val send: fs2.Stream[F, WebSocketFrame]        = sendResponse(playerId)
        val receive: fs2.Pipe[F, WebSocketFrame, Unit] =
          in => in.evalMap(processClientMessage(playerId, _))
        ws.build(send, receive)
    }
