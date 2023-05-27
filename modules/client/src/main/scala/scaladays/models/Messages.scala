package scaladays.models

import scaladays.models.ids.Nickname

enum Msg:
  case UpdateNickname(nickname: Nickname)