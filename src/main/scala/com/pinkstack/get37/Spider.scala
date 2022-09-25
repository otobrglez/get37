package com.pinkstack.get37

import com.pinkstack.get37.Resource.Result
import com.pinkstack.get37.Resource.Result.{Created, Exists}
import com.pinkstack.get37.types.*
import zhttp.http.Response
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.Console.printLine
import zio.ZIO.{foreachParDiscard, service, when, whenCase, whenCaseZIO}
import zio.{durationInt, Console, IO, Task, ZIO}

import java.io.IOException

/** Spider is used for collecting the resources from a given URL. Collection starts at the initial url, the
  * URLs are then extracted from the resource if that resource is "html". Each URL is then visited. Visiting of
  * sequential URLs is spawned via new fibers. Requests are made via ZIO-HTTP, a ZIO wrapper on top of Netty. Some
  * optimisations are in place. For example, maxDepth defines how many layers deep should the collection go.
  */
object Spider:
  type Depth     = Int
  type SpiderEnv = SpiderConfiguration & EventLoopGroup & ChannelFactory

  private def printProgress(url: NonEmptyURL, depth: Depth): Result => IO[IOException, Unit] =
    case Created(path) => printLine(s"[L:$depth] ⬇️ Downloaded $url to $path")
    case Exists(path)  => printLine(s"[L:$depth] 👌 Resource $url already exists at $path")

  private def collect(maxDepth: Depth = 3)(url: NonEmptyURL): ZIO[SpiderEnv, Throwable, Unit] =
    for
      config <- service[SpiderConfiguration]
      out    <- when(maxDepth > 0)(
        for
          response <- Client.request(url).map(r => (r.status.code, r.contentType, r))
          result   <- whenCase(response) {
            case (200, Some(contentType: String), response) if contentType.startsWith("text/html") =>
              for
                _     <- Resource.persistHtml(url, response) flatMap printProgress(url, maxDepth)
                links <- LinksParser.parse(url, response)
                _     <- ZIO.sleep(config.preFetchDelay.toInt.millis)
                out   <- foreachParDiscard(links)(collect(maxDepth - 1)).withParallelism(config.maxFibers.toInt)
              yield out
            case (200, _, response)                                                                =>
              Resource.persist(url, response) flatMap printProgress(url, maxDepth)
          }
        yield result
      ).unit
    yield out

  def collect(url: NonEmptyURL): ZIO[SpiderEnv, Throwable, Unit] =
    service[SpiderConfiguration].flatMap(config => collect(config.maxDepth.toInt)(url))
