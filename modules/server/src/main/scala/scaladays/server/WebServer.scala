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

import fs2.Stream
import org.typelevel.log4cats.Logger

object WebServer:

  def routes[F[_]: Async: Logger](ticTacToe: TTTServer[F]): HttpRoutes[F] =
    given EntityDecoder[F, Login]         = jsonOf[F, Login]
    given EntityEncoder[F, LoginResponse] = jsonEncoderOf[LoginResponse]

    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case req @ PATCH -> Root / "login" =>
        req.as[Login].flatMap(loginReq => ticTacToe.login(loginReq.nickname)).flatMap(pId => Ok(LoginResponse(pId)))
    }
