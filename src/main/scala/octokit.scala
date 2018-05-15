package laughedelic.octokit.rest

import scala.scalajs.js, js.|, js.annotation._

@js.native @JSImport("@octokit/rest", JSImport.Default)
class Octokit(
  options: js.UndefOr[Octokit.Options] = js.undefined
) extends js.Object {

  def authenticate(auth: Octokit.Auth): Unit = js.native

  def hasNextPage(link: Octokit.LinkLike): js.UndefOr[String] = js.native
  def hasPreviousPage(link: Octokit.LinkLike): js.UndefOr[String] = js.native

  def hasLastPage(link: Octokit.LinkLike): js.UndefOr[String] = js.native
  def hasFirstPage(link: Octokit.LinkLike): js.UndefOr[String] = js.native

  def getNextPage(
    link: Octokit.LinkLike,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getPreviousPage(
    link: Octokit.LinkLike,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getLastPage(
    link: Octokit.LinkLike,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getFirstPage(
    link: Octokit.LinkLike,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native
}

object Octokit {

  type Headers = js.Dictionary[String]
  type Json = js.Dynamic

  @js.native
  trait Response[Data] extends js.Object {
    /** This is the data you would see in [[https://developer.github.com/v3/]] */
    val data: Data = js.native
    /** Request metadata */
    val meta: ResponseMeta = js.native
  }

  type AnyResponse = Response[Json]

  @js.native
  trait ResponseMeta extends js.Object {
    val `x-ratelimit-limit`: String = js.native
    val `x-ratelimit-remaining`: String = js.native
    val `x-ratelimit-reset`: String = js.native
    val `x-github-request-id`: String = js.native
    val `x-github-media-type`: String = js.native
    val link: String = js.native
    val `last-modified`: String = js.native
    val etag: String = js.native
    val status: String = js.native
  }

  class Options(
    val baseUrl: js.UndefOr[String] = js.undefined,
    val timeout: js.UndefOr[Int] = js.undefined,
    val headers: js.UndefOr[Headers] = js.undefined,
    // NOTE: should be `http.Agent`, but I don't want to add a dependency on
    // Node.js facades for this single type. Besides it's an input, so it's
    // not so important to have a precise type here.
    val agent: js.UndefOr[js.Any] = js.undefined,
  ) extends js.Object

  sealed abstract class Auth(
    val `type`: String
  ) extends js.Object

  object Auth {
    class Basic(
      val username: String,
      val password: String,
    ) extends Auth("basic")

    class OAuthSecret(
      val key: String,
      val secret: String,
    ) extends Auth("oauth")

    class OAuthToken(val token: String) extends Auth("oauth")
    class Token(val token: String) extends Auth("token")
    class App(val token: String) extends Auth("app")
  }

  class Link(val link: String) extends js.Object
  class MetaLink(val meta: Link) extends js.Object

  type LinkLike = Link | MetaLink | String

  // This adds generated routes as methods to the Octokit class
  implicit def octokitGeneratedRoutes(octokit: Octokit): OctokitGeneratedRoutes =
    new OctokitGeneratedRoutes(octokit.asInstanceOf[js.Dynamic])
}
