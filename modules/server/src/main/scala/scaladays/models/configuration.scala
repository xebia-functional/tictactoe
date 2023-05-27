package scaladays.models

import pureconfig.*
import pureconfig.generic.derivation.default.*

final case class HttpConfiguration(host: String, port: Int) derives ConfigReader

final case class SchemaRegistryConfiguration(uri: String, cacheSize: Int) derives ConfigReader

final case class Topics(
    inputTopic: String,
    playerTopic: String,
    gameTopic: String
  ) derives ConfigReader

final case class Stores(playerMatch: String, registerPlayer: String) derives ConfigReader

final case class KafkaConfiguration(
    uri: String,
    schemaRegistry: SchemaRegistryConfiguration,
    bootstrapServers: String,
    clientId: String,
    numPartitions: Int,
    replicationFactor: Int,
    groupId: String,
    applicationId: String,
    topics: Topics,
    stores: Stores
  ) derives ConfigReader

final case class ServerConfiguration(
    server: HttpConfiguration,
    health: HttpConfiguration
  ) derives ConfigReader

final case class Configuration(
    kafka: KafkaConfiguration,
    http: ServerConfiguration
  ) derives ConfigReader
