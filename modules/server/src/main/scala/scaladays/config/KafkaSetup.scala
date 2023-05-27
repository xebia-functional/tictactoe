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
        numReplicas: Int): F[Unit] = ???

    override def bootTopics(): F[Unit] =
      // TODO Using kafkaAdminClientResource call to createTopicUnless for inputTopic
      ???
