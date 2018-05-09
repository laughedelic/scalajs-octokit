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
    Seq(
      Seq(s"def ${name}("),
      params.map { param =>
        val default: String =
          if (param.required) "" else " = js.native"
        s"  ${param.name}: ${param.scalaType}${default},"
      },
      Seq("): Future[js.Any]"),
    ).flatten.mkString("\n")
  }
}

object Param {
  implicit def rw: RW[Param] = macroRW

  def jsToScala(tpe: String): String = tpe match {
    case "integer" | "string" | "boolean" => tpe.capitalize
    case "object" | "any" => s"js.${tpe.capitalize}"
    case _ if tpe.endsWith("[]") =>
      jsToScala(tpe.stripSuffix("[]")).mkString("js.Array[", "", "]")
  }
}
case class Param(
  @upickle.key("type") tpe: String,
  name: String,
  description: String = "",
  required: Boolean = false,
) {
  def scalaType: String = Param.jsToScala(tpe)
}


object Generator {

  def main(args: Array[String]): Unit = {
    val parsed = read[Map[String, Map[String, Route]]](new File("routes-for-api-docs.json"))

    val types = for {
        group: Map[String, Route] <- parsed.values.toSet
        route: Route <- group.values.toSet
        param: Param <- route.params.toSet
      } yield param.tpe
    println(types)

    val route = parsed("issues")("create")
    println(route.scaladoc)
    println(route.toMethod("create"))
  }
}
