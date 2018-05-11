package laughedelic.sbt

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
}
