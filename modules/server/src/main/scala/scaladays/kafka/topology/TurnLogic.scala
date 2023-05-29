package scaladays.kafka.topology

import cats.implicits.*
import scaladays.kafka.messages.Events.*
import scaladays.models.ids.*
import scaladays.models.{Game, GameState, Movement}

object TurnLogic:

  def processTurn(turn: AggMessage[EventId, TurnGame], game: Game): Either[RejectEvent, Game] =
    ???
    // TODO
