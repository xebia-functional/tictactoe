package scaladays.models

import java.util.UUID

import cats.effect.Sync
import cats.implicits.*

import io.chrisdavenport.fuuid.FUUID
import io.circe.generic.semiauto.*
import io.circe.{Decoder as CirceDecoder, Encoder as CirceEncoder}

object ids:
  opaque type EventId  = FUUID
  opaque type PlayerId = FUUID
  opaque type GameId   = FUUID
  opaque type Nickname = String

  object PlayerId:
    def apply(id: FUUID): PlayerId             = id
    def apply[F[_]: Sync](): F[PlayerId]       = FUUID.randomFUUID
    def unsafe(): PlayerId                     = FUUID.fromUUID(UUID.randomUUID)
    def unapply(arg: String): Option[PlayerId] = FUUID.fromStringOpt(arg)

    extension (playerId: PlayerId)
      def value: FUUID = playerId
      def show: String = value.show

  object GameId:
    def apply(id: FUUID): GameId = id
    def unsafe(): GameId         = FUUID.fromUUID(UUID.randomUUID)

    extension (gameId: GameId) def value: FUUID = gameId

  object Nickname:
    def apply(nickname: String): Nickname = nickname

    extension (nickname: Nickname) def value: String = nickname

  object EventId:

    def apply(id: FUUID): EventId       = id
    def apply[F[_]: Sync](): F[EventId] = FUUID.randomFUUID
    def unsafe(): EventId               = FUUID.fromUUID(UUID.randomUUID)

    extension (eventId: EventId) def value: FUUID = eventId