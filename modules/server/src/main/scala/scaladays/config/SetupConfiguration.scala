package scaladays.config

import scala.reflect.ClassTag

import cats.effect.Async
import cats.implicits.*

import com.typesafe.config.ConfigFactory
import org.typelevel.log4cats.Logger
import pureconfig.{ConfigReader, ConfigSource}

object SetupConfiguration:

  def loadConfiguration[F[_]: Async: Logger, C: ClassTag](using cr: ConfigReader[C]): F[C] =
    for
      classLoader <- Async[F].delay(
                       ConfigFactory.load(getClass().getClassLoader())
                     )
      config      <- Async[F].delay(
                       ConfigSource.fromConfig(classLoader).at("scaladays.workshop").loadOrThrow[C]
                     )
    yield config
