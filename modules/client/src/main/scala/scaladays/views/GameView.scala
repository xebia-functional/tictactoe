package scaladays.views

import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.models.*
import tyrian.*
import tyrian.Html.*

object GameView:

  private def LabelView(piece: Piece): Html[Msg] = piece match
    case Piece.Cross  => div(cls := "piece cross")(Piece.Cross.symbol)
    case Piece.Circle => div(cls := "piece circle")(Piece.Circle.symbol)

  private def MyTurnAtrr(myTurn: Boolean) = if myTurn then default else disabled

  def gameScreen(nickname: Nickname, player: PlayerId, game: Game): Html[Msg] =
    val myPiece: Piece       = if game.crossPlayer == player then Piece.Cross else Piece.Circle
    val opponentPiece: Piece = if myPiece == Piece.Cross then Piece.Circle else Piece.Cross

    val message: String = (game.state, myPiece) match
      case (GameState.CircleTurn, Piece.Circle) => s"${nickname.value}, it's your turn."
      case (GameState.CircleTurn, _)            => s"It's your opponent's turn."
      case (GameState.CrossTurn, Piece.Cross)   => s"${nickname.value}, it's your turn."
      case (GameState.CrossTurn, _)             => s"It's your opponent's turn."
      case (GameState.CircleWin, Piece.Circle)  => s"Congrats ${nickname.value}, you have won the game! 🥳"
      case (GameState.CircleWin, _)             => s"Your opponent has won the game! 🤪"
      case (GameState.CrossWin, Piece.Cross)    => s"Congrats ${nickname.value}, you have won the game! 🥳"
      case (GameState.CrossWin, _)              => s"Your opponent has won the game! 🤪"
      case (GameState.Tie, _)                   => s"It's a tie! You and your opponent are both winners (or losers) 🤣"
      case (GameState.Processing, _)            => s"Processing the last move ⏳"

    val myTurn: Boolean = (game.state, myPiece) match
      case (GameState.CircleTurn, Piece.Circle) => true
      case (GameState.CrossTurn, Piece.Cross)   => true
      case _                                    => false

    def CellView(position: Position) = ???
