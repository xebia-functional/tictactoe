package scaladays.kafka.stream

import java.time.Instant

import scaladays.kafka.messages.Events.{StartGame, TTTEvent}
import scaladays.models.ids.{EventId, GameId, PlayerId}

import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{Transformer, TransformerSupplier}
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore

private[stream] trait PlayerMatchTransformer extends Transformer[PlayerId, PlayerId, KeyValue[EventId, TTTEvent]]

private[stream] object PlayerMatchTransformer:

  def apply(storeName: String): PlayerMatchTransformer = new PlayerMatchTransformer:

    var ctx: ProcessorContext = ctx

    lazy val keyStore = "lastRecord"

    lazy val lastPlayerStore: KeyValueStore[String, PlayerId] = ctx.getStateStore(storeName)

    override def transform(K: PlayerId, V: PlayerId): KeyValue[EventId, TTTEvent] =
      if Option(V).isDefined then
        Option(lastPlayerStore.get(keyStore)) match
          case Some(playerId) =>
            lastPlayerStore.delete(keyStore)
            Thread.sleep(4000)
            KeyValue.pair(EventId.unsafe(), TTTEvent(Instant.now(), StartGame(GameId.unsafe(), playerId, V)))
          case None           =>
            lastPlayerStore.put(keyStore, V)
            null
      else
        null

    override def init(context: ProcessorContext): Unit =
      ctx = context
      lastPlayerStore.delete(keyStore)

    override def close(): Unit = ()

private[stream] trait PlayerMatchTransformerSupplier
    extends TransformerSupplier[PlayerId, PlayerId, KeyValue[EventId, TTTEvent]]

private[stream] object PlayerMatchTransformerSupplier:

  def apply(
      storeName: String): TransformerSupplier[PlayerId, PlayerId, KeyValue[EventId, TTTEvent]] =
    () => PlayerMatchTransformer(storeName)
