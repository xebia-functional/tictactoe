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

    def CellView(position: Position) =
      val newMovement = Movement(position, myPiece, false)
      val newMsg = Msg.RequestNewMovement(game, newMovement)
      game.movements
        .find(_.position == position).fold {
          td(button(tpe := "button", cls := "btn btn-primary", MyTurnAtrr(myTurn), onClick(newMsg)))
        } { mov =>
          td(button(tpe := "button", cls := s"btn btn-primary done ${mov.piece.cssClass}", disabled) {
            if mov.confirmed then span(mov.piece.symbol)
            else div(cls := "spinner-border text-light", Attribute("role", "status"))()
          })
        }

    val restartButton: List[Html[Msg]] =
      if(List(GameState.CrossWin, GameState.CircleWin, GameState.Tie).contains(game.state)) then
        List(
          div(cls := "d-grid gap-2 mt-4 d-sm-flex justify-content-sm-center")(
            button(tpe := "button", cls := "btn btn-primary btn-lg px-4 gap-3")("Restart")
          )
        )
      else List.empty[Html[Msg]]


    div(
      List(
        p(cls := "lead mb-4")(message),
        div(cls := "container")(
          div(cls := "row align-items-center")(
            div(cls := "col-2")(
              div(cls := "label")("You"),
              LabelView(myPiece)
            ),
            div(id := "board", cls := "col-8")(
              table(
                tr(CellView(Position(1, 1)), CellView(Position(1, 2)), CellView(Position(1, 3))),
                tr(CellView(Position(2, 1)), CellView(Position(2, 2)), CellView(Position(2, 3))),
                tr(CellView(Position(3, 1)), CellView(Position(3, 2)), CellView(Position(3, 3)))
              )
            ),
            div(cls := "col-2")(
              div(cls := "label")("Opponent"),
              LabelView(opponentPiece)
            )
          )
        )
      ) ++ restartButton
    )
