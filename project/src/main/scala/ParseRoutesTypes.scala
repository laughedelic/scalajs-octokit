package laughedelic.sbt.octokit

import upickle.default._
import upickle.default.{Reader => R, macroR}
import java.io.File

case class MethodType(
  params: Map[ParamName, Param] = Map(),
  headers: Map[String, String] = Map(),
) {
  def paramTypes: Map[ParamName, ParamType] = params.collect {
    // FIXME: handle sub-parameters instead of filtering them out
    case (name, paramType: ParamType) if !name.contains('.') =>
      escapeScalaIdent(name) -> paramType
  }
  def defaultHeaders: String = {
    if (headers.isEmpty) "js.undefined"
    else headers
      .map { case (k, v) => s""""${k}" -> "${v}"""" }
      .mkString("js.Dictionary(", ", ", "),")
  }
}
object MethodType { implicit def r: R[MethodType] = macroR }


trait Param
object Param {
  implicit def r: R[Param] = eitherOf(
    reader[ParamType],
    reader[ParamAlias],
  )
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
object ParamType { implicit def r: R[ParamType] = macroR }

case class ParamAlias(
  alias: String,
  deprecated: String = "",
) extends Param
object ParamAlias { implicit def r: R[ParamAlias] = macroR }



object ParseRoutesTypes {
  def apply(file: File): RoutesTypes = read[RoutesTypes](file)
  def apply(): RoutesTypes = apply(new File("routes.json"))
}
