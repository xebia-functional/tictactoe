package scaladays.models

import io.circe.{Decoder, Encoder}
import scaladays.models.ids.{GameId, PlayerId}
import cats.implicits.*
import io.circe.generic.semiauto.*
import scaladays.models.GameState.CrossTurn

import scala.util.Random

enum Piece:
  case Cross, Circle

object Piece:

  given Decoder[Piece] = Decoder.decodeString.emap {
    case "Circle" => Piece.Circle.asRight[String]
    case "Cross" => Piece.Cross.asRight[String]
    case str => s"Error decoding $str as a piece".asLeft[Piece]
  }

  given Encoder[Piece] = Encoder.encodeString.contramap[Piece](_.toString)

  extension (piece: Piece)
    def symbol: String = piece match
      case Piece.Cross => "╳"
      case Piece.Circle => "⃝"

    def cssClass: String = piece match
      case Piece.Cross => "cross"
      case Piece.Circle => "circle"

    def other: Piece = piece match
      case Piece.Cross => Piece.Circle
      case Piece.Circle => Piece.Cross


final case class Position(x: Int, y: Int)

object Position:
  given Decoder[Position] = deriveDecoder[Position]
  given Encoder[Position] = deriveEncoder[Position]
  given Ordering[Position] = (x: Position, y: Position) => if (x.x == y.x) x.y - y.y else x.x - y.x



enum GameState:
  case CrossTurn, CircleTurn, Processing, CrossWin, CircleWin, Tie

object GameState:
  def randomStarter: GameState = if(Random.nextBoolean) GameState.CrossTurn else GameState.CircleTurn

  given Decoder[GameState] = Decoder.decodeString.emap {
    case "CrossTurn" => GameState.CrossTurn.asRight[String]
    case "CircleTurn" => GameState.CircleTurn.asRight[String]
    case "Processing" => GameState.Processing.asRight[String]
    case "CrossWin" => GameState.CrossWin.asRight[String]
    case "CircleWin" => GameState.CircleWin.asRight[String]
    case "Tie" => GameState.Tie.asRight[String]
    case str => s"Error decoding $str as a game state".asLeft[GameState]
  }

  given Encoder[GameState] = Encoder.encodeString.contramap[GameState](_.toString)

final case class Movement(position: Position, piece: Piece, confirmed: Boolean = true)

object Movement:
  given Decoder[Movement] = deriveDecoder[Movement]
  given Encoder[Movement] = deriveEncoder[Movement]


final case class Game(gameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId, state: GameState, movements: List[Movement])

object Game:
  given Decoder[Game] = deriveDecoder[Game]
  given Encoder[Game] = deriveEncoder[Game]

  def init(gameId: GameId, playerId1: PlayerId, playerId2: PlayerId): Game =
    Game(gameId, playerId1, playerId2, CrossTurn, Nil)

  extension (positions: List[Position])
    def sameRow(row: Int): Boolean = positions.count(_.x == row) == 3
    def sameColumn(column: Int): Boolean = positions.count(_.y == column) == 3
    def anyLine: Boolean = (1 to 3).map(i => sameRow(i) || sameColumn(i)).exists(identity)
    def diagonal: Boolean = positions.count(p => p.x == p.y) == 3
    def contraDiagonal: Boolean = positions.toSet.intersect(Set(Position(1,3), Position(2,2), Position(3,1))).size == 3
    def hasWon: Boolean = anyLine || diagonal || contraDiagonal

  extension (moves: List[Movement])
    def winsPiece(piece: Piece): Boolean = moves.filter(_.piece == piece).map(_.position).hasWon
    def winsOpponent(piece: Piece): Boolean = winsPiece(piece.other)

  extension (self: Game)
    def addMove(movement: Movement): Game =
      val newMove: Movement = movement.copy(confirmed = true)
      val newMovements: List[Movement] = newMove :: self.movements
      val moveBy: Piece = movement.piece
      val playerIsWinner: Boolean = newMovements.winsPiece(moveBy)
      val opponentIsWinner: Boolean = newMovements.winsOpponent(moveBy)
      val allMoves: Boolean = newMovements.size == 9
      val newState = (moveBy, playerIsWinner, opponentIsWinner, allMoves) match
        case (Piece.Cross,  true,   _,      _)     => GameState.CrossWin
        case (Piece.Cross,  _,      true,   _)     => GameState.CircleWin
        case (Piece.Circle, true,   _,      _)     => GameState.CircleWin
        case (Piece.Circle, _,      true,   _)     => GameState.CrossWin
        case (Piece.Cross,  false,  false,  false) => GameState.CircleTurn
        case (Piece.Circle, false,  false,  false) => GameState.CrossTurn
        case (_,            false,  false,  true)  => GameState.Tie
      self.copy(state = newState, movements = newMovements)

