package scaladays.kafka.topology

import cats.implicits.*
import scaladays.kafka.messages.Events.*
import scaladays.models.ids.*
import scaladays.models.{Game, GameState, Movement}

object TurnLogic:

  def processTurn(turn: AggMessage[EventId, TurnGame], game: Game): Either[RejectEvent, Game] =
    val (eventId, turnGame) = (turn.a, turn.b)
    game.state match
      case GameState.CrossTurn if turnGame.playerId != game.crossPlayer      =>
        RejectEvent(turnGame.playerId, eventId, "Opponent turn").asLeft[Game]
      case GameState.CircleTurn if turnGame.playerId != game.circlePlayer    =>
        RejectEvent(turnGame.playerId, eventId, "Opponent turn").asLeft[Game]
      case GameState.Tie | GameState.CrossWin | GameState.CircleWin          =>
        RejectEvent(turnGame.playerId, eventId, "Game already ended").asLeft[Game]
      case GameState.CrossTurn | GameState.CircleTurn | GameState.Processing =>
        game.addMove(Movement(turnGame.position, turnGame.piece)).asRight
