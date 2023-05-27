package scaladays.models

import cats.effect.Async
import scaladays.models.ids.*
import org.http4s.Uri
import tyrian.websocket.*

enum Player:

  case Empty
  
  case Waiting
  
  case Registered(playerId: PlayerId)