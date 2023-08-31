package scaladays.models

import java.util.UUID

import io.chrisdavenport.fuuid.FUUID

object ids:

  opaque type PlayerId = FUUID
  opaque type GameId = FUUID

  object PlayerId:
    def apply(id: FUUID): PlayerId = id

  object GameId:
    def apply(id: FUUID): GameId = id