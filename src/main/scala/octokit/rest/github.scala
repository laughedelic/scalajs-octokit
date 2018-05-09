package laughedelic.octokit.rest

import scala.scalajs.js, js.|

@js.native @js.annotation.JSGlobal
class Github(
  options: js.UndefOr[Github.Options] = js.undefined
) extends js.Object {

  def authenticate(auth: Github.Auth): Unit = js.native
  def hasNextPage(link: Github.Link): js.UndefOr[String] = js.native
  def hasPreviousPage(link: Github.Link): js.UndefOr[String] = js.native
  def hasLastPage(link: Github.Link): js.UndefOr[String] = js.native
  def hasFirstPage(link: Github.Link): js.UndefOr[String] = js.native

  def getNextPage(
    link: Github.Link
  ): js.Promise[Github.AnyResponse] = js.native
  def getNextPage(
    link: Github.Link,
    headers: js.Dictionary[js.Any] = js.native
  ): js.Promise[Github.AnyResponse] = js.native

  def getPreviousPage(
    link: Github.Link
  ): js.Promise[Github.AnyResponse] = js.native
  def getPreviousPage(
    link: Github.Link,
    headers: js.Dictionary[js.Any] = js.native
  ): js.Promise[Github.AnyResponse] = js.native

  def getLastPage(
    link: Github.Link
  ): js.Promise[Github.AnyResponse] = js.native
  def getLastPage(
    link: Github.Link,
    headers: js.Dictionary[js.Any] = js.native
  ): js.Promise[Github.AnyResponse] = js.native

  def getFirstPage(
    link: Github.Link
  ): js.Promise[Github.AnyResponse] = js.native
  def getFirstPage(
    link: Github.Link,
    headers: js.Dictionary[js.Any] = js.native
  ): js.Promise[Github.AnyResponse] = js.native
}

object Github {

  type Json = js.Any
  type Date = String

  @js.native
  trait AnyResponse extends js.Object {
    /** This is the data you would see in https://developer.github.com/v3/ */
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

  @js.native
  trait EmptyParams extends js.Object {}

  @js.native
  trait Options extends js.Object {
    val baseUrl: js.UndefOr[String] = js.native
    val timeout: js.UndefOr[Int] = js.native
    // val headers?: OptionsHeaders
    // val agent?: http.Agent
  }

  // @js.native
  // trait OptionsHeaders extends js.Object {
  //   [header: String]: Json
  // }

  @js.native
  trait AuthBasic extends js.Object {
    // val type: "basic" = js.native
    val username: String = js.native
    val password: String = js.native
  }

  @js.native
  trait AuthOAuthToken extends js.Object {
    // val type: "oauth" = js.native
    val token: String = js.native
  }

  @js.native
  trait AuthOAuthSecret extends js.Object {
    // val type: "oauth" = js.native
    val key: String = js.native
    val secret: String = js.native
  }

  @js.native
  trait AuthUserToken extends js.Object {
    // val type: "token" = js.native
    val token: String = js.native
  }

  @js.native
  trait AuthJWT extends js.Object {
    // val type: "integration" = js.native
    val token: String = js.native
  }

  type Auth =
    AuthBasic |
    AuthOAuthToken |
    AuthOAuthSecret |
    AuthUserToken |
    AuthJWT

  @js.native
  sealed trait LinkString extends js.Object {
    val link: String = js.native
  }

  @js.native
  sealed trait LinkMeta extends js.Object {
    val meta: LinkString = js.native
  }

  type Link =
    LinkString |
    LinkMeta |
    String

  // This adds generated routes as methods to the Github class
  implicit def githubGeneratedRoutes(github: Github): GithubGeneratedRoutes =
    new GithubGeneratedRoutes(github.asInstanceOf[js.Dynamic])
}
