package laughedelic.sbt

import upickle.default.Reader
import ujson.Js
import scala.util.Try

package object octokit {

  type Namespace = String
  type MethodName = String
  type ParamName = String

  type RoutesDocs  = Map[Namespace, Map[MethodName, MethodDoc]]
  type RoutesTypes = Map[Namespace, Map[MethodName, MethodType]]

  type Lines = Seq[String]

  implicit class LinesOps(val lines: Lines) extends AnyVal {
    def prefix(pref: String): Lines = lines.map { pref + _ }
    def indent(n: Int = 2): Lines = prefix(Seq.fill(n)(" ").mkString)
  }

  def jsTypeToScala(tpe: String): String = tpe match {
    case "string" | "boolean" => tpe.capitalize
    case "object" | "any" => s"js.${tpe.capitalize}"
    case "integer" | "number" => "Int"
    case _ if tpe.contains('|') =>
      tpe.split('|').map(_.trim)
        .map(jsTypeToScala)
        .mkString(" | ")
    case _ if tpe.endsWith("[]") =>
      jsTypeToScala(tpe.stripSuffix("[]"))
        .mkString("js.Array[", "", "]")
    case _ => tpe
  }

  def escapeScalaIdent(ident: String): String = ident match {
    case "type" | "object" | "private" | "protected" => s"`${ident}`"
    case _ => ident
  }

  def eitherOf[T, A <: T, B <: T](implicit
    readerA: Reader[A],
    readerB: Reader[B],
  ): Reader[T] = {
    import upickle.default._
    reader[Js.Value].map[T] {
      json => Try {
        readJs[A](json)
      }.recover {
        case e: ujson.AbortJsonProcessingException =>
          readJs[B](json)
      }.getOrElse {
        sys.error(s"Couldn't parse neither of the alternatives: ${json}")
      }
    }
  }
}
