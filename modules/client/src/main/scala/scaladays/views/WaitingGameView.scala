package scaladays.views

import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.models.{ClientError, Model, Msg, WebSocketMessage}
import tyrian.Html.*
import tyrian.*

object WaitingGameView:

  def startGameScreen(playerId: PlayerId, nickname: Nickname): Html[Msg] =
    div(
      List(
        p(cls := "lead mb-4")(s"Hi ${nickname.value}. Are you ready to play?"),
        div(id := "div-start-game", cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
          button(
            tpe := "button",
            cls := "btn btn-primary btn-lg px-4 gap-3",
            onClick(WebSocketMessage.WebSocketStatus.Connecting(playerId).asMsg)
          )("Join a game")
        )
      )
    )

  def waitingGameScreen(status: String): Html[Msg] =
    div(
      List(
        p(cls := "lead mb-4")(status),
        div(id := "div-waiting-game", cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
          div(cls := "spinner-border", Attribute("role", "status"))(span(cls := "visually-hidden")("Loading..."))
        )
      )
    )

  def waitingGameErrorScreen(status: String, errors: List[ClientError]): Html[Msg] =
    div(
      List(
        p(cls := "lead mb-4")(status),
        div(id := "div-waiting-game", cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
          button(
            tpe := "button",
            cls := "btn btn-primary btn-lg px-4 gap-3",
            onClick(Msg.Logout)
          )("Retry")
        )
      )
        ++ errors.map { error =>
          div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
            div(
              id  := "div-login-error",
              cls := "alert alert-danger mt-3 mb-0",
              attribute("role", "alert")
            )(error.reason)
          )
        }
    )
