package scaladays.kafka

import scaladays.kafka.messages.Events
import scaladays.models.http.*
import scaladays.models.ids.{EventId, GameId, PlayerId}
import cats.effect.Async
import cats.implicits.*
import scaladays.models.{Game, Movement}

trait EventHandler[F[_]]:

  def processEvent(playerId: PlayerId): fs2.Stream[F, Game]

object EventHandler:

  def impl[F[_]: Async](consumer: Consumer[F, GameId, Game]): EventHandler[F] = new EventHandler[F]:

    override def processEvent(playerId: PlayerId): fs2.Stream[F, Game] = ???
