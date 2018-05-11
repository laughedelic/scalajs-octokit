package laughedelic.octokit.rest.test

import laughedelic.octokit.rest._
import utest._
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global

object HelloTests extends TestSuite{

  val tests = Tests{
    val octokit = new Octokit()

    "repos.get" - {
      octokit.repos.get(
        owner = "octokit",
        repo = "rest.js"
      ).map { response =>
        println(js.JSON.stringify(response.data, space = 2))

        response.data.id ==> 711976
        response.data.full_name ==> "octokit/rest.js"
        response.data.owner.id ==> 3430433
      }
    }

  }
}
