package scaladays.config

import scaladays.models.ids.{Nickname, PlayerId}
import scaladays.models.{Configuration, HttpConfiguration, KafkaConfiguration}

import cats.effect.std.Dispatcher
import cats.effect.{Async, Resource}
import cats.implicits.*

import org.apache.kafka.clients.admin.{AlterConfigOp, ConfigEntry, NewTopic}
import org.apache.kafka.common.config.ConfigResource
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.KeyValueStore

import org.http4s.ember.server.EmberServerBuilder
import org.http4s.{Response, Status}

import com.comcast.ip4s.{Host, Port}
import fs2.kafka.vulcan.{AvroSettings, SchemaRegistryClientSettings}
import fs2.kafka.{AdminClientSettings, KafkaAdminClient}
import org.typelevel.log4cats.Logger

trait ConfigurationService[F[_]]:

  def config: Configuration

  def httpServer: EmberServerBuilder[F]

  def schemaRegistrySettings: AvroSettings[F]

object ConfigurationService:

  def impl[F[_]: Async: Logger]: F[ConfigurationService[F]] =
    def bootServer(httpConfiguration: HttpConfiguration): EmberServerBuilder[F] =
      EmberServerBuilder
        .default[F]
        .withHostOption(Host.fromString(httpConfiguration.host))
        .withPort(Port.fromInt(httpConfiguration.port).get)
        .withMaxHeaderSize(8 * 1024)
        .withIdleTimeout(scala.concurrent.duration.Duration.Inf)
        .withErrorHandler { case e =>
          Logger[F]
            .error(e)("Error in http server")
            .as(
              Response[F](Status.InternalServerError).putHeaders(org.http4s.headers.`Content-Length`.zero)
            )
        }
        .withOnWriteFailure { (optReq, response, failure) =>
          Logger[F].error(failure)(
            s"Error writing http response: \n\t- ${optReq.toString} \n\t- ${response.toString}"
          )
        }

    for
      conf     <- SetupConfiguration.loadConfiguration[F, Configuration]
      settings <- SchemaRegistryClientSettings[F](conf.kafka.schemaRegistry.uri)
                    .withMaxCacheSize(conf.kafka.schemaRegistry.cacheSize)
                    .createSchemaRegistryClient
                    .map(AvroSettings(_))
    yield new ConfigurationService[F]:
      override lazy val config: Configuration                   = conf
      override lazy val httpServer: EmberServerBuilder[F]       = bootServer(
        conf.http.server
      )
      override lazy val schemaRegistrySettings: AvroSettings[F] = settings
