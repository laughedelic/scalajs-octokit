package laughedelic.octokit.rest

import scala.scalajs.js, js.|, js.annotation._

@js.native @JSImport("@octokit/rest", JSImport.Default)
class Octokit(
  options: js.UndefOr[Octokit.Options] = js.undefined
) extends js.Object {

  def authenticate(auth: Octokit.Auth): Unit = js.native

  def hasNextPage(link: Octokit.Link | String): js.UndefOr[String] = js.native
  def hasPreviousPage(link: Octokit.Link | String): js.UndefOr[String] = js.native

  def hasLastPage(link: Octokit.Link | String): js.UndefOr[String] = js.native
  def hasFirstPage(link: Octokit.Link | String): js.UndefOr[String] = js.native

  def getNextPage(
    link: Octokit.Link | String,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getPreviousPage(
    link: Octokit.Link | String,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getLastPage(
    link: Octokit.Link | String,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native

  def getFirstPage(
    link: Octokit.Link | String,
    headers: Octokit.Headers = js.native,
  ): js.Promise[Octokit.AnyResponse] = js.native
}

object Octokit {

  type Headers = js.Dictionary[js.Any]
  type Json = js.Dynamic
  type Date = String

  @js.native
  trait AnyResponse extends js.Object {
    /** This is the data you would see in [[https://developer.github.com/v3/]] */
    val data: Json = js.native

    /** Request metadata */
    val meta: ResponseMeta = js.native
  }

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
    // NOTE: should be `http.Agent`
    val agent: js.UndefOr[js.Any] = js.undefined,
  ) extends js.Object

  sealed abstract class Auth(
    val `type`: String
  ) extends js.Object

  class AuthBasic(
    val username: js.UndefOr[String] = js.undefined,
    val password: js.UndefOr[String] = js.undefined,
  ) extends Auth("basic")

  class AuthOAuthToken(
    val token: js.UndefOr[String] = js.undefined,
  ) extends Auth("oauth")

  class AuthOAuthSecret(
    val key: js.UndefOr[String] = js.undefined,
    val secret: js.UndefOr[String] = js.undefined,
  ) extends Auth("oauth")

  class AuthUserToken(
    val token: js.UndefOr[String] = js.undefined,
  ) extends Auth("token")

  class AuthJWT(
    val token: js.UndefOr[String] = js.undefined,
  ) extends Auth("integration")

  sealed trait Link extends js.Object

  sealed class LinkString(
    val link: js.UndefOr[String] = js.undefined,
  ) extends Link

  sealed class LinkMeta(
    val meta: js.UndefOr[LinkString] = js.undefined,
  ) extends Link

  // This adds generated routes as methods to the Octokit class
  implicit def octokitGeneratedRoutes(octokit: Octokit): OctokitGeneratedRoutes =
    new OctokitGeneratedRoutes(octokit.asInstanceOf[js.Dynamic])
}
