package scaladays.clients

import scaladays.models.http.{JoinGame, Login, LoginResponse, Turn}
import scaladays.models.ids.{GameId, Nickname, PlayerId}
import scaladays.models.*
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

object ScalaDaysClient:

  def impl[F[_]: Async](host: String, port: Int): ScalaDaysClient[F] = new ScalaDaysClient[F] with Http4sClientDsl[F]:

    lazy val httpUri = Uri.unsafeFromString(s"http://$host:$port")

    lazy val wsUri = Uri.unsafeFromString(s"ws://$host:$port")

    given EntityEncoder[F, Login] = jsonEncoderOf

    given EntityDecoder[F, LoginResponse] = jsonOf

    lazy val client = FetchClientBuilder[F].create

    override def getLogin(nickname: Nickname): Cmd[F, Msg] =
      // TODO
      ???
