package scaladays.kafka.codecs

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.ByteArrayKeyValueStore
import org.apache.kafka.streams.scala.kstream.{Consumed, Joined, Materialized, Produced}
import org.apache.kafka.streams.state.{KeyValueBytesStoreSupplier, KeyValueStore, StoreBuilder, Stores}

object instances:

  opaque type KeySerde[A] = Serde[A]

  object KeySerde:
    def apply[A](s: Serde[A]): KeySerde[A] = s

  opaque type ValueSerde[A] = Serde[A]

  object ValueSerde:
    def apply[A](s: Serde[A]): ValueSerde[A] = s

  given consumed[K: KeySerde, V: ValueSerde]: Consumed[K, V]                   = Consumed.`with`[K, V]
  given produced[K: KeySerde, V: ValueSerde]: Produced[K, V]                   = Produced.`with`[K, V]
  given joined[K: KeySerde, V1: ValueSerde, V2: ValueSerde]: Joined[K, V1, V2] = Joined.`with`[K, V1, V2]

  def keyValueStore[K, V](
      storeName: String
    )(using k: KeySerde[K],
      v: ValueSerde[V]): StoreBuilder[KeyValueStore[K, V]] =
    Stores.keyValueStoreBuilder[K, V](Stores.persistentKeyValueStore(storeName), k, v)

  def materialized[K, V](
      storeName: String
    )(using k: KeySerde[K],
      v: ValueSerde[V]): Materialized[K, V, ByteArrayKeyValueStore] =
    Materialized.as[K, V, ByteArrayKeyValueStore](storeName)(k, v)
