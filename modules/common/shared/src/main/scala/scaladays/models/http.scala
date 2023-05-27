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
    given Decoder[Login] = deriveDecoder[Login]
    given Encoder[Login] = deriveEncoder[Login]

  object LoginResponse:
    given Encoder[LoginResponse] = deriveEncoder[LoginResponse]
    given Decoder[LoginResponse] = deriveDecoder[LoginResponse]

  object JoinGame:
    given Decoder[JoinGame] = deriveDecoder[JoinGame]
    given Encoder[JoinGame] = deriveEncoder[JoinGame]

  object Turn:
    given Decoder[Turn] = deriveDecoder[Turn]
    given Encoder[Turn] = deriveEncoder[Turn]

  object ClientAction:
    given Decoder[ClientAction] = Turn
      .given_Decoder_Turn.widen[ClientAction]
      .or(JoinGame.given_Decoder_JoinGame.widen[ClientAction])