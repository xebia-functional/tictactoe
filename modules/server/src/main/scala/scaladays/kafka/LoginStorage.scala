package scaladays.kafka

import scaladays.models.ids.*
import cats.effect.Sync
import cats.implicits.*
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.typelevel.log4cats.Logger

trait LoginStorage[F[_]]:

  def upsertPlayer(playerId: PlayerId, nickname: Nickname): F[PlayerId]

object LoginStorage:

  def impl[F[_]: Sync: Logger](
      store: ReadOnlyKeyValueStore[Nickname, PlayerId],
      eventStorage: EventStorage[F]): LoginStorage[F] = (playerId: PlayerId, nickname: Nickname) =>
    for
      maybePlayerId <- Sync[F].catchNonFatal(Option(store.get(nickname))).recoverWith { e =>
                         Logger[F].error(s"Error logging a user: ${e.getMessage}") >> Sync[F]
                           .delay(Option.empty[PlayerId])
                       }
      _             <- Sync[F].whenA(maybePlayerId.isEmpty)(eventStorage.register(playerId, nickname))
    yield maybePlayerId.getOrElse(playerId)
