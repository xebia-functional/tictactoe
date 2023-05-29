package scaladays.config

import fs2.kafka.{AdminClientSettings, KafkaAdminClient}
import org.apache.kafka.clients.admin.{AlterConfigOp, ConfigEntry, NewTopic}
import org.apache.kafka.common.config.ConfigResource
import scaladays.models.KafkaConfiguration
import cats.effect.{Async, Resource}
import cats.implicits.*

trait KafkaSetup[F[_]]:
  def bootTopics(): F[Unit]

object KafkaSetup:

  def impl[F[_]: Async](kafkaConfiguration: KafkaConfiguration): KafkaSetup[F] = new KafkaSetup[F]:

    lazy val kafkaAdminClientResource: Resource[F, KafkaAdminClient[F]] =
      val adminClientSettings = AdminClientSettings.apply(kafkaConfiguration.bootstrapServers)
      KafkaAdminClient.resource(adminClientSettings)

    def createTopicUnless(
        client: KafkaAdminClient[F],
        topics: Set[String],
        topicName: String,
        numPartitions: Int,
        numReplicas: Int): F[Unit] =
      Async[F].unlessA(topics.contains(topicName))(
        client.createTopic(new NewTopic(topicName, numPartitions, numReplicas.toShort))
      )

    def createCompactedTopicUnless(
        client: KafkaAdminClient[F],
        topics: Set[String],
        topicName: String,
        numPartitions: Int,
        numReplicas: Int): F[Unit] =
      val topic = new ConfigResource(ConfigResource.Type.TOPIC, topicName)
      for
        _ <- createTopicUnless(client, topics, topicName, numPartitions, numReplicas)
        _ <- client.alterConfigs {
               Map(
                 topic -> List(
                   new AlterConfigOp(
                     new ConfigEntry("cleanup.policy", "compact"),
                     AlterConfigOp.OpType.SET
                   )
                 )
               )
             }
      yield ()

    override def bootTopics(): F[Unit] =
      kafkaAdminClientResource.use { client =>
        for
          names <- client.listTopics.names
          _     <- createTopicUnless(
                     client,
                     names,
                     kafkaConfiguration.topics.inputTopic,
                     kafkaConfiguration.numPartitions,
                     kafkaConfiguration.replicationFactor
                   )
          _     <- createCompactedTopicUnless(
                     client,
                     names,
                     kafkaConfiguration.topics.gameTopic,
                     kafkaConfiguration.numPartitions,
                     kafkaConfiguration.replicationFactor
                   )
          _     <- createCompactedTopicUnless(
                     client,
                     names,
                     kafkaConfiguration.topics.playerTopic,
                     kafkaConfiguration.numPartitions,
                     kafkaConfiguration.replicationFactor
                   )
        yield ()
      }
