package scaladays.models

import scaladays.models.ids.*

enum Piece:
  case Cross, Circle

final case class Position(x: Int, y: Int)

object Position:
  given Ordering[Position] = (x: Position, y: Position) => if (x.x == y.x) x.y - y.y else x.x - y.x

enum GameState:
  case CrossTurn, CircleTurn, Processing, CrossWin, CircleWin, Tie

final case class Movement(position: Position, piece: Piece, confirmed: Boolean = true)

final case class Game(gameId: GameId, crossPlayer: PlayerId, circlePlayer: PlayerId, state: GameState, movements: List[Movement])
