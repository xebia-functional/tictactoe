package scaladays.models

import cats.effect.Async
import org.http4s.Uri
import scaladays.models.ids.*
import tyrian.websocket.*

enum Contest:

  case Empty

  case InProgress(status: String)

  case Registered(game: Game)