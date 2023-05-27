package scaladays

import scaladays.config.{ConfigurationService, KafkaSetup}
import scaladays.server.*
import cats.effect.{Async, ExitCode, Resource}
import cats.implicits.*
import org.http4s.server
import org.http4s.server.middleware.CORS
import org.http4s.server.websocket.WebSocketBuilder
import org.typelevel.log4cats.Logger

object Server:

  def serve[F[_]: Async: Logger]: fs2.Stream[F, ExitCode] =
    for
      configService <- fs2.Stream.eval(ConfigurationService.impl)
      builder <- fs2.Stream.resource(configService.builder)
      _ <- fs2.Stream.eval(KafkaSetup.impl(configService.config.kafka).bootTopics())
      backend = Backend.impl[F](configService.config, builder, configService.schemaRegistrySettings)
      ttt <- fs2.Stream.resource(backend.tttServer)
      routes = WebServer.routes(ttt)
      stream        <- fs2.Stream.eval(
                         configService.httpServer
                           .withHttpApp(
                             CORS.policy.withAllowOriginAll.withAllowCredentials(false).apply(
                               (HealthCheck.healthService <+> routes).orNotFound)
                           )
                           .build
                           .use(_ => Async[F].never[ExitCode])
                       )
    yield stream
