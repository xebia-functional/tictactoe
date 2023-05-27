package scaladays.server

import cats.effect.Async

import org.http4s.*
import org.http4s.dsl.*

object HealthCheck:

  def healthService[F[_]: Async]: HttpRoutes[F] = ???
