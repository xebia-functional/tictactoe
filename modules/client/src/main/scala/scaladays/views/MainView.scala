package scaladays.views

import scaladays.models.Msg
import scaladays.models.ids.Nickname
import scaladays.views.MainView.welcome
import tyrian.Html.*
import tyrian.*

object MainView:

  def mainScreen(nickname: Nickname): Html[Msg] =
    div(welcome(nickname))

  private def welcome(nickname: Nickname): List[Elem[Msg]] =
    List(
      p(cls := "lead mb-4")("Welcome! Please insert your nickname to login."),
      div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
        div(cls := "form-floating mb-3")(
          input(
            tpe := "text",
            cls := "form-control",
            id := "div-login",
            placeholder := "Nickname",
            onInput(str => Msg.UpdateNickname(Nickname(str)))
          ),
          label(`for` := "div-login")("Nickname")
        )
      ),
      div(cls := "d-grid gap-2 d-sm-flex justify-content-sm-center")(
        button(cls := "btn btn-primary btn-lg px-4 gap-3", tpe := "button", disabled)("Login")
      )
    )