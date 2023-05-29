package scaladays.views

import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.models.{ClientError, Model, Msg, WebSocketMessage}
import tyrian.Html.*
import tyrian.*

object WaitingGameView:

  def startGameScreen(playerId: PlayerId, nickname: Nickname): Html[Msg] = ???    

  def waitingGameScreen(status: String): Html[Msg] = ???

  def waitingGameErrorScreen(status: String, errors: List[ClientError]): Html[Msg] = ???
