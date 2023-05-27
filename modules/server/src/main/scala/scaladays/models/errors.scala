package scaladays.models

import scala.util.control.NoStackTrace

final case class ServiceError(msg: String) extends RuntimeException(msg) with NoStackTrace
