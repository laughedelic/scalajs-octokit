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

  def scaladoc: String = {
    Seq(
      Seq(title, ""),
      if (description.isEmpty) Seq() else {
        description.split('\n').toSeq :+ ""
      },
      for {
        param <- params
          if param.description.nonEmpty
        // default = param.default
        //   .map { value => s" (default: `${value}`)"}
        //   .getOrElse("")
      } yield
        s"@param ${param.name} ${param.description}",
      Seq("", s"@see ${documentationUrl}")
    ).flatten.mkString(
        "/** ",
      "\n  * ",
      "\n  */"
    )
  }

  def toMethod(name: String): String = {
    params.map { param =>
      s"${param.name}: ${param.tpe}${param.default}"
    }.mkString(
      s"def ${name}(\n  ",
      ",\n  ",
      "\n): Future[js.Any]"
    )
  }
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
  def default: String = if (required) "" else " = js.native"
}


object Generator {

  def main(args: Array[String]): Unit = {
    val parsed = read[Map[String, Map[String, Route]]](new File("routes-for-api-docs.json"))
    val route = parsed("gists")("create")
    println(route)
    println(route.scaladoc)
    println(route.toMethod("create"))
  }
}
