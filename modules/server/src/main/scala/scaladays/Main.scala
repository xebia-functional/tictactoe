package scaladays

import scaladays.models.ServiceError

import cats.effect.{ExitCode, IO, IOApp}

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] =
    Server
      .serve[IO]
      .compile
      .drain
      .handleErrorWith { error =>
        logger.error(error)("Unrecoverable error") >>
          IO.raiseError[ExitCode](ServiceError(error.getMessage))
      }
      .as(ExitCode.Success)