package laughedelic.sbt.octokit

import upickle.default._
import upickle.default.{ReadWriter => RW, macroRW}
import java.io.File
import java.nio.file.{ Files, Paths }
import scala.collection.JavaConverters._

object Route { implicit def rw: RW[Route] = macroRW }
case class Route(
  @upickle.key("name") title: String,
  description: String = "",
  documentationUrl: String,
  params: List[Param] = Nil,
)

object Param {
  implicit def rw: RW[Param] = macroRW

  def jsToScala(tpe: String): String = tpe match {
    case "string" | "boolean" => tpe.capitalize
    case "object" | "any" => s"js.${tpe.capitalize}"
    case "integer" | "number" => "Int"
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
  def scalaType: String = {
    val t = Param.jsToScala(tpe)
    if (required) t else s"js.UndefOr[${t}]"
  }
}


object Generator {

  type Namespace = String
  type Method = String
  type Routes = Map[Namespace, Map[Method, Route]]
  type Lines = Seq[String]

  implicit class LinesOps(val lines: Lines) extends AnyVal {
    def prefix(pref: String): Lines = lines.map { pref + _ }
    def indent(n: Int = 2): Lines = prefix(Seq.fill(n)(" ").mkString)
  }

  def scaladoc(route: Route): Lines = {
    val body = Seq(
      Seq(""),
      if (route.description.isEmpty) Seq() else {
        route.description.split('\n').toSeq :+ ""
      },
      for {
        param <- route.params
          if param.description.nonEmpty
      } yield
        s"@param ${param.name} ${param.description}",
      Seq("", s"@see [[${route.documentationUrl}]]"),
    ).flatten
    Seq(
      Seq(s"/** ${route.title}"),
      body.prefix("  * "),
      Seq( "  */"),
    ).flatten
  }

  // TODO: see https://github.com/octokit/rest.js/pull/732
  private val returnType = "Github.AnyResponse"

  def methodSignature(method: Method, route: Route): Lines = {
    Seq(
      Seq(s"def ${method}("),
      route.params.map { param =>
        val default: String =
          if (param.required) "" else " = js.undefined"
        s"  `${param.name}`: ${param.scalaType}${default},"
      },
      Seq(s"): Future[${returnType}] = "),
    ).flatten
  }

  def methodBody(
    namespace: Namespace,
    method: Method,
    route: Route
  ): Lines = {
    Seq(
      Seq(
        s"githubDynamic.${namespace}.${method}(",
        "  js.Dynamic.literal(",
      ),
      route.params.map { param =>
        s"""    "${param.name}" -> `${param.name}`,"""
      },
      Seq(
        "  )",
        s").asInstanceOf[js.Promise[${returnType}]].toFuture",
      )
    ).flatten
  }

  def methodDefinition(
    namespace: Namespace,
    method: Method,
    route: Route
  ): Lines = Seq(
    scaladoc(route),
    methodSignature(method, route),
    methodBody(namespace, method, route).indent(),
  ).flatten

  def template(content: Lines): Lines = Seq(
    Seq(
      "package laughedelic.octokit.rest", "",
      "import scala.scalajs.js",
      "import scala.concurrent.Future", "",
      "class GithubGeneratedRoutes(val githubDynamic: js.Dynamic) {", "",
    ),
    content.indent(),
    Seq("", "}"),
  ).flatten

  def routeObjects(routes: Routes): Lines = {
    routes.toSeq.flatMap { case (namespace, methods) =>
      Seq(
        Seq("", s"object ${namespace} {"),
        methods.toSeq.flatMap { case (method, route) =>
          "" +: methodDefinition(namespace, method, route)
        }.indent(),
        Seq("}")
      ).flatten
    }
  }

  def generateRoutes(routes: Routes): Lines =
    template(routeObjects(routes))

  def main(args: Array[String]): Unit = {
    val parsed = read[Routes](new File("routes-for-api-docs.json"))

    if (args.isEmpty) {
      Files.write(
        Paths.get("src/main/scala/routes.scala"),
        generateRoutes(parsed).asJava
      )
    } else {
      val namespace = args.lift(0).getOrElse("issues")
      val method = args.lift(1).getOrElse("create")
      val route = parsed(namespace)(method)

      println(
        methodDefinition(namespace, method, route).mkString("\n")
      )
    }
  }
}
