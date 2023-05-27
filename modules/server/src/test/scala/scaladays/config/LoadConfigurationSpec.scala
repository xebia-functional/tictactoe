package scaladays.config

import scaladays.config.SetupConfiguration
import scaladays.models.Configuration

import cats.effect.IO
import cats.syntax.all.*

import com.typesafe.config.ConfigFactory
import munit.CatsEffectSuite
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class LoadConfigurationSpec extends CatsEffectSuite:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  test("Configuration must load") {
    assertIO(
      SetupConfiguration.loadConfiguration[IO, Configuration].attempt.map(_.isRight),
      true
    )
  }
