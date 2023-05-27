package scaladays.models

import scaladays.models.ids.*

enum Msg:
  case UpdateNickname(nickname: Nickname)

  case LoginRequest(nickname: Nickname)

  case LoginSuccess(playerId: PlayerId)

  case LoginError(error: ClientError)