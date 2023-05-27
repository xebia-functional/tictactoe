package scaladays.models

final case class Model[F[_]]()

object Model:

  def init[F[_]]: Model[F] = Model[F]()
