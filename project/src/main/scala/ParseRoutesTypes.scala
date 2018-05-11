package laughedelic.sbt.octokit

import upickle.default._
import upickle.default.{Reader => R, macroR}
import java.io.File

case class MethodType(
  params: Map[ParamName, Param] = Map(),
  // headers: Map[String, String] = Map(),
) {
  def paramTypes: Map[ParamName, ParamType] = params.collect {
    // FIXME: handle sub-parameters instead of filtering them out
    case (name, paramType: ParamType) if !name.contains('.') =>
      val escapedName = name match {
        case "type" | "object" | "private" | "protected" => s"`${name}`"
        case _ => name
      }
      escapedName -> paramType
  }
}
object MethodType { implicit def r: R[MethodType] = macroR }


trait Param
object Param {
  import ujson.Js
  import scala.util.Try
  // This ugly workaround means just `reader[ParamType] | reader[ParamAlias]`
  implicit def r: R[Param] =
    reader[Js.Value].map[Param] {
      json => Try {
        readJs[ParamType](json)
      }.recover {
        case e: ujson.AbortJsonProcessingException =>
          readJs[ParamAlias](json)
      }.getOrElse {
        sys.error(s"Neither `type` nor `alias`: ${json}")
      }
    }
}

case class ParamType(
  @upickle.key("type") tpe: String,
  required: Boolean = false,
  allowNull: Boolean = false,
) extends Param {
  def scalaType: String = {
    val t = jsTypeToScala(tpe)
    val tOrNull = if(allowNull) s"${t} | Null" else t
    if (required) tOrNull else s"js.UndefOr[${tOrNull}]"
  }
}
object ParamType {
  implicit def r: R[ParamType] = macroR
}

case class ParamAlias(
  alias: String,
  deprecated: String = "",
) extends Param
object ParamAlias { implicit def r: R[ParamAlias] = macroR }



object ParseRoutesTypes {

  def apply(file: File): RoutesTypes = read[RoutesTypes](file)
  def apply(): RoutesTypes = apply(new File("routes.json"))
}
