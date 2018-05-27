package laughedelic.sbt.octokit

import upickle.default._
import java.io.File
import java.nio.file.{ Files, Paths }
import scala.collection.JavaConverters._

object Generator {

  def scaladoc(methodDoc: MethodDoc): Lines = {
    val body = Seq(
      Seq(""),
      if (methodDoc.description.isEmpty) Seq() else {
        methodDoc.description.split('\n').toSeq :+ ""
      },
      for {
        param <- methodDoc.params
          if param.description.nonEmpty
      } yield
        s"@param ${param.name} ${param.description}",
      Seq("", s"@see [[${methodDoc.documentationUrl}]]"),
    ).flatten
    Seq(
      Seq(s"/** ${methodDoc.title}"),
      body.prefix("  * "),
      Seq( "  */"),
    ).flatten
  }

  // TODO: see https://github.com/octokit/rest.js/pull/732
  private val returnType = "Octokit.AnyResponse"

  def methodSignature(methodName: MethodName, methodType: MethodType): Lines = {
    Seq(
      Seq(s"def ${methodName}("),
      methodType.paramTypes.map { case (paramName, param) =>
        val default: String = if (param.required) "" else " = js.undefined"
        s"  ${paramName}: ${param.scalaType}${default},"
      },
      Seq(
        s"  headers: js.UndefOr[Octokit.Headers] = ${methodType.defaultHeaders}",
        s"): Future[${returnType}] = ",
      ),
    ).flatten
  }

  def methodBody(
    namespace: Namespace,
    methodName: MethodName,
    methodType: MethodType
  ): Lines = {
    Seq(
      Seq(
        s"octokitJS.${namespace}.${methodName}(",
        "  js.Dynamic.literal(",
      ),
      methodType.paramTypes.map { case (paramName, param) =>
        val paramValue: String =
          if (param.scalaType.contains('|'))
            s"js.use(${paramName}).as[js.Any]"
          else paramName
        s"""    "${paramName}" -> ${paramValue},"""
      },
      Seq(
        s"""    "headers" -> headers""",
         "  )",
        s").asInstanceOf[js.Promise[${returnType}]].toFuture",
      )
    ).flatten
  }

  def methodDefinition(
    namespace: Namespace,
    methodName: MethodName,
    methodType: MethodType
  ): Lines = Seq(
    // scaladoc(methodDoc),
    methodSignature(methodName, methodType),
    methodBody(namespace, methodName, methodType).indent(),
  ).flatten

  def template(content: Lines): Lines = Seq(
    Seq(
      "package laughedelic.octokit.rest", "",
      "import scala.scalajs.js, js.|",
      "import scala.concurrent.Future", "",
      "class OctokitGeneratedRoutes(private val octokitJS: js.Dynamic) {",
    ),
    content.indent(),
    Seq("", "}"),
  ).flatten

  def routeObjects(types: RoutesTypes): Lines = {
    types.toSeq.flatMap { case (namespace, methods) =>
      Seq(
        Seq("", s"object ${namespace} {"),
        methods.toSeq.flatMap { case (methodName, methodType) =>
          "" +: methodDefinition(namespace, methodName, methodType)
        }.indent(),
        Seq("}")
      ).flatten
    }
  }

  def generateRoutes(routes: RoutesTypes): Lines =
    template(routeObjects(routes))

  def main(args: Array[String]): Unit = {
    // val routesDocs  = ParseRoutesDocs()
    val routesTypes = ParseRoutesTypes(new File("routes.json"))

    args.toList match {
      case Nil => {
        Files.write(
          Paths.get("target/routes.scala"),
          generateRoutes(routesTypes).asJava
        )
      }
      case List("types") => {
        val types = for {
          (nmspcN, nmspc) <- routesTypes.toSeq
          (methdN, methd) <- nmspc.toSeq
          (paramN, param) <- methd.paramTypes.toSeq
          if (param.tpe.startsWith("object"))
        } yield {
          s"${nmspcN}.${methdN}.${paramN}${param.tpe.stripPrefix("object")}"
        }
        types.sorted.foreach(println)
      }
      case namespace :: methodName :: _ => {
        val methodType = routesTypes(namespace)(methodName)
        println(
          methodDefinition(namespace, methodName, methodType).mkString("\n")
        )
      }
    }
  }
}
