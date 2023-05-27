package scaladays.models

import scaladays.models.ids._

import cats.Show
import cats.implicits.*

import io.chrisdavenport.fuuid.FUUID
import io.circe
import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder, Json}

object http:

  final case class Login(nickname: Nickname)
  final case class LoginResponse(id: PlayerId)

  sealed trait ClientAction
  final case class JoinGame(id: PlayerId) extends ClientAction
  final case class Turn(id: PlayerId, gameId: GameId, piece: Piece, position: Position) extends ClientAction

  object Login:
    given Decoder[Login] = ???
    given Encoder[Login] = ???

  object LoginResponse:
    given Encoder[LoginResponse] = ???
    given Decoder[LoginResponse] = ???
