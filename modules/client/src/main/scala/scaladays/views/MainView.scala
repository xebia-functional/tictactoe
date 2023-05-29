package scaladays.views

import scaladays.models.UnknownError
import scaladays.models.ids.Nickname
import scaladays.models.{ClientError, Model, Msg}
import tyrian.Html.*
import tyrian.*

object MainView:

  def mainScreen(nickname: Nickname): Html[Msg] =
    div(welcome(nickname))

  def waitingLoginScreen(nickname: Nickname): Html[Msg] =
    div(welcome(nickname, true))

  def errorMainScreen(nickname: Nickname, errors: List[ClientError]): Html[Msg] =
    div(welcome(nickname) ++ alert(errors))

  def errorScreen[F[_]](model: Model[F]): Html[Msg] =
    println(model)
    div(alert(List(UnknownError)))

  private def welcome(nickname: Nickname, waiting: Boolean = false): List[Elem[Msg]] =
    List(
      p(cls := "lead mb-4")("Welcome! Please insert your nickname to login."),
      div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
        div(cls         := "form-floating mb-3")(
          input(
            tpe         := "text",
            cls         := "form-control",
            id          := "div-login",
            placeholder := "Nickname",
            onInput(str => Msg.UpdateNickname(Nickname(str)))
          ),
          label(`for` := "div-login")("Nickname")
        )
      ),
      div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
        if waiting then
          button(cls := "btn btn-primary btn-lg px-4 gap-3", tpe := "button", disabled)(
            span(
              cls := "spinner-border spinner-border-sm",
              Attribute("role", "status"),
              Attribute("aria-hidden", "true")
            )(),
            span(cls := "visually-hidden")("Loading...")
          )
        else
          button(tpe := "button", cls := "btn btn-primary btn-lg px-4 gap-3", onClick(Msg.LoginRequest(nickname)))(
            "Login"
          )
      )
    )

  private def alert(errors: List[ClientError]): List[Elem[Msg]] =
    errors.map { error =>
      div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
        div(
          id  := "div-login-error",
          cls := "alert alert-danger mt-3 mb-0",
          attribute("role", "alert")
        )(error.reason)
      )
    }
