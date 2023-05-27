package scaladays.models

trait ClientError(val reason: String)

final case class UnexpectedServerError(error: String) extends ClientError("There was a server error")

object NotFoundNickname extends ClientError("Server couldn't find the nickname")
object InvalidNickname  extends ClientError("Invalid nickname")
object UnknownError extends ClientError("Unexpected error: Please check the console to get more info")