# [Scala.js] facades for [octokit/rest.js]

[![](https://travis-ci.com/laughedelic/scalajs-octokit.svg?branch=master)](https://travis-ci.com/laughedelic/scalajs-octokit)
[![](http://img.shields.io/github/release/laughedelic/scalajs-octokit/all.svg)](https://github.com/laughedelic/scalajs-octokit/releases/latest)
[![](https://img.shields.io/badge/license-MPL--2.0-blue.svg)](https://www.tldrlegal.com/l/mpl-2.0)
[![](https://img.shields.io/badge/contact-gitter_chat-dd1054.svg)](https://gitter.im/laughedelic/scalajs-octokit)

[![](https://www.scala-js.org/assets/badges/scalajs-0.6.17.svg)](https://www.scala-js.org)
[![](https://img.shields.io/badge/octokit/rest.js-15.5.0-blue.svg)](https://www.tldrlegal.com/l/mpl-2.0)

This project contains Scala.js facades for [octokit/rest.js], the official GitHub REST API v3 client for Node.js.

## 🚧 WORK IN PROGRESS 🚧

_This project is in active development, there are no published releases yet. Things may break without a warning, so don't rely on it._

## Usage

### Installation

<details><summary>🛠 Check installation instructions later, when there is a published release...</summary>

1. Add Octokit dependency to your project. It's important that the version of the underlying JS library matches the one this facade is built for.

    * If it's a Node.js project where you manage dependencies with npm, run
        ```shell
        npm install @octokit/rest@15.5.0
        ```

    * If it's a Scala.js project use [scalajs-bundler] and add to your `build.sbt`:
        ```scala
        Compile/npmDependencies += "@octokit/rest" -> "15.5.0"
        ```

2. Add facades dependency to your `build.sbt`:
    ```scala
    resolvers += Resolver.jcenterRepo
    libraryDependencies += "laughedelic" %%% "scalajs-octokit" % "<version>"
    ```
    (see the latest release version on the badge above)

</details>

### Example

Here's a simple usage example:

```scala
import laughedelic.octokit.rest._

// Non-authenticated client with default parameters:
val octokit = new Octokit()

// A simple request (https://developer.github.com/v3/repos/#get)
octokit.repos.get(
  owner = "octokit",
  repo = "rest.js"
).map { response =>
  // Check some data in the response:
  println(response.data.full_name) // ==> "octokit/rest.js"
  // Or print the whole thing:
  println(js.JSON.stringify(response.data, space = 2))
}
```

## Project structure

Most of the code in this project is generated, so you won't find it in the repository. See that code you need to clone the project and run `sbt compile`.

There is a bit of code which cannot be generated and is written manually: [`src/main/scala/octokit.scala`](src/main/scala/octokit.scala). It defines types for the `Octokit` client, its options, authentication and pagination.

## Documentation

There's no documentation in the project (yet), but you may find useful these resources:
* octokit/rest.js [readme](https://github.com/octokit/rest.js#restjs) and [API docs](https://octokit.github.io/rest.js)
* GitHub [REST API v3 docs](https://developer.github.com/v3)

## Related projects

* [scalajs-io/github-api-node](https://github.com/scalajs-io/github-api-node): partial facade for the [Github.js](http://github-tools.github.io/github) library
    > Github.js provides a minimal higher-level wrapper around Github's API

* [47deg/github4s](http://47deg.github.io/github4s): JVM/JS-compatible Scala wrapper for (a part of) the GitHub API
    > Github4s is based on a Free Monad Architecture, which helps decoupling of program declaration from program interpretation

#### How is this project different

* This is a Scala.js-only ([facades](https://www.scala-js.org/doc/interoperability/facade-types.html)) library, no JVM
* It builds on the official JS client for node which covers _all available GitHub REST API v3_ (including latest previews)
* The code is automatically generated using the same source as the underlying JS library, which means it should be easy to maintain and keep in sync

[Scala.js]: https://www.scala-js.org
[octokit/rest.js]: https://github.com/octokit/rest.js
[scalajs-bundler]: https://github.com/scalacenter/scalajs-bundler
