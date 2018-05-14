# [Scala.js] facades for [octokit/rest.js]

[![](https://travis-ci.com/laughedelic/scalajs-octokit.svg?branch=master)](https://travis-ci.com/laughedelic/scalajs-octokit)
[![](http://img.shields.io/github/release/laughedelic/scalajs-octokit/all.svg)](https://github.com/laughedelic/scalajs-octokit/releases/latest)
[![](https://img.shields.io/badge/@octokit/rest-15.4.1-blue.svg)](https://www.tldrlegal.com/l/mpl-2.0)
[![](https://img.shields.io/badge/license-MPL--2.0-blue.svg)](https://www.tldrlegal.com/l/mpl-2.0)
[![](https://img.shields.io/badge/contact-gitter_chat-dd1054.svg)](https://gitter.im/laughedelic/scalajs-octokit)

This project contains Scala.js facades for [octokit/rest.js], the official GitHub REST API v3 client for Node.js.

## Usage

1. Add Octokit dependency to your project.

    * If it's a Node.js project where you manage dependencies with npm, run
        ```shell
        npm install @octokit/rest@15.4.1
        ```

    * If it's a Scala.js project use [scalajs-bundler] and add to your `build.sbt`:
        ```scala
        Compile/npmDependencies += "@octokit/rest" -> "15.4.1"
        ```

2. Add facades dependency to your `build.sbt`:
    ```scala
    resolvers += Resolver.jcenterRepo
    libraryDependencies += "laughedelic" %%% "scalajs-probot" % "<version>"
    ```
    (see the latest release version on the badge above)


## Related projects

* [scalajs-io/github-api-node](https://github.com/scalajs-io/github-api-node): partial facade for the [Github.js](http://github-tools.github.io/github) library
    > Github.js provides a minimal higher-level wrapper around Github's API

* [47deg/github4s](http://47deg.github.io/github4s): JVM/JS-compatible Scala wrapper for (a part of) the GitHub API
    > Github4s is based on a Free Monad Architecture, which helps decoupling of program declaration from program interpretation

[Scala.js]: https://www.scala-js.org
[octokit/rest.js]: https://github.com/octokit/rest.js
[scalajs-bundler]: https://github.com/scalacenter/scalajs-bundler
