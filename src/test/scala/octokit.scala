package laughedelic.octokit.rest.test

import laughedelic.octokit.rest._
import utest._
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global

object RoutesTests extends TestSuite{

  val tests = Tests{
    val octokit = new Octokit()

    "repos.get" - {
      octokit.repos.get(
        owner = "octokit",
        repo = "rest.js"
      ).map { response =>
        // println(js.JSON.stringify(response.data, space = 2))

        response.data.id ==> 711976
        response.data.full_name ==> "octokit/rest.js"
        response.data.owner.id ==> 3430433
      }
    }

    "gitdata.getTree" - {
      octokit.gitdata.getTree(
        owner = "octokit",
        repo = "rest.js",
        tree_sha = "master",
        // recursive = 1,
      ).map { response =>
        // println(js.JSON.stringify(response.data, space = 2))

        response.data.tree
          .asInstanceOf[js.Array[js.Dynamic]]
          .map { node => node.path.asInstanceOf[String] }
          .foreach(println)
        // TODO: change tree_sha to a tag and check several existing paths
      }
    }
  }
}
