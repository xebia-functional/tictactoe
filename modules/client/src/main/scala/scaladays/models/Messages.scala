package scaladays.models

import scaladays.models.ids.*

enum Msg:
  case UpdateNickname(nickname: Nickname)