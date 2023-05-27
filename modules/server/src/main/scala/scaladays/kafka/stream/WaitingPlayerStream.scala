package scaladays.kafka.stream

import cats.effect.Async
import cats.syntax.all.*
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.utils.SystemTime
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}
import org.apache.kafka.streams.state.Stores
import scaladays.kafka.codecs.VulcanSerdes
import scaladays.kafka.codecs.instances
import scaladays.kafka.messages.Events.*
import scaladays.kafka.stream.PlayerMatchTransformerSupplier
import scaladays.models.KafkaConfiguration
import scaladays.models.ids.{EventId, PlayerId}

object WaitingPlayerStream:

  def buildStream(kfg: KafkaConfiguration, builder: StreamsBuilder, serdes: VulcanSerdes): Unit =
    import serdes.given
    import instances.given

    def waitingPlayerToPlayerStream(): Unit =
      builder
        .stream[EventId, TTTEvent](kfg.topics.inputTopic)
        .flatMap[PlayerId, PlayerId] {
          case (_, TTTEvent(_, WaitingForMatch(playerId))) => Some((playerId, playerId))
          case _                                           => None
        }
        .to(kfg.topics.playerTopic)

    def deletePlayersAfterCreateMatch(): Unit =
      builder
        .stream[EventId, TTTEvent](kfg.topics.inputTopic)
        .flatMap[PlayerId, PlayerId] {
          case (_, TTTEvent(_, StartGame(_, playerId1, playerId2))) =>
            List((playerId1, null.asInstanceOf[PlayerId]), (playerId2, null.asInstanceOf[PlayerId]))
          case _ =>
            List.empty[(PlayerId, PlayerId)]
        }
        .to(kfg.topics.playerTopic)

    def playerToMatchStream(storeName: String): Unit =
      val store = instances.keyValueStore[String, PlayerId](storeName)
      /*
       * The `stream` and `to` methods in the Scala `StreamsBuilder` wrapper has implicits params for
       * `Consumed[K, V]` and `Produced[K, V]` and it use them for serialize and deserialize data.
       *
       * On the other hand, the `stream` and `to` methods in the Java class of `StreamsBuilder` doesn't
       * have those implicits, and if you call it without them it will use the default serdes for key/value
       * (see https://docs.confluent.io/platform/current/streams/developer-guide/datatypes.html#configuring-serdes)
       *
       * The problem is that `addStateStore` returns the inner Java `StreamsBuilder`, which means if you don't wrap the
       * result in the Scala wrapper, you will have a similar code but different behaviour
       * (the Scala one will use the implicits and the Java one will use the defaults, usually String or Byte[A]):
       *
       * builder
       *   .addStateStore(store)
       *   .stream[PlayerId, PlayerId](kfg.topics.playerTopic) // <- This is a Java call (default serdes)
       *
       * builder
       *   .stream[PlayerId, PlayerId](kfg.topics.playerTopic) // <- This is a Scala call (implicits)
       */
      new StreamsBuilder(builder.addStateStore(store))
        .stream[PlayerId, PlayerId](kfg.topics.playerTopic)
        .transform[EventId, TTTEvent](
          PlayerMatchTransformerSupplier(storeName),
          Named.as("PlayerProcessor"),
          storeName
        )
        .to(kfg.topics.inputTopic)

    waitingPlayerToPlayerStream()   // Event -> Player CTopic
    playerToMatchStream(kfg.stores.playerMatch)          // Player CTopic -> Match CTopic
    deletePlayersAfterCreateMatch() // Match CTopic -> Delete Player CTopic
