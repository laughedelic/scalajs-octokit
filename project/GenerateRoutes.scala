package laughedelic.sbt.octokit

import upickle.default._
import upickle.default.{ReadWriter => RW, macroRW}
import java.io.File

object Route { implicit def rw: RW[Route] = macroRW }
case class Route(
  @upickle.key("name") title: String,
  description: String = "",
  documentationUrl: String,
  params: List[Param] = Nil,
  // enabledForApps: Boolean,
  // method: String,
  // path: String,
) {
}

object Param { implicit def rw: RW[Param] = macroRW }
case class Param(
  @upickle.key("type") tpe: String,
  name: String,
  description: String = "",
  required: Boolean = false,
  // location: String,
  // default: Option[String] = None,
) {
}


object Generator {

  def main(args: Array[String]): Unit = {
    val parsed = read[Map[String, Map[String, Route]]](new File("routes-for-api-docs.json"))
    val route = parsed("gists")("create")
    println(route)
  }
}
