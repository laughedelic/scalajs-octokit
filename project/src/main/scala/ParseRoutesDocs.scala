package laughedelic.sbt.octokit

import upickle.default._
import upickle.default.{ReadWriter => RW, macroRW}
import java.io.File

case class MethodDoc(
  @upickle.key("name") title: String,
  description: String = "",
  documentationUrl: String,
  params: List[ParamDoc] = Nil,
)

object MethodDoc { implicit def rw: RW[MethodDoc] = macroRW }


case class ParamDoc(
  name: String,
  description: String = "",
)

object ParamDoc { implicit def rw: RW[ParamDoc] = macroRW }

object ParseRoutesDocs {

  def apply(file: File): RoutesDocs = read[RoutesDocs](file)
  def apply(): RoutesDocs = apply(new File("routes-for-api-docs.json"))
}
