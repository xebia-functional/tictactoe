package scaladays.server

import cats.effect.Async

import org.http4s.*
import org.http4s.dsl.*

object HealthCheck:

  def healthService[F[_] : Async]: HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "hello" => Ok("World!")
    }