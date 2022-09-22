package com.pinkstack.get37

import com.pinkstack.get37.types.*
import zhttp.http.Response
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.Console.printLine
import zio.ZIO.{attempt, foreachParDiscard, ifZIO, succeed, when}
import zio.{durationInt, Console, IO, Task, ZIO}

import java.io.IOException

object Spider:
  type Depth     = Int
  type SpiderEnv = EventLoopGroup & ChannelFactory

  private def printProgress(url: NonEmptyURL, depth: Depth): Resource.Result => IO[IOException, Unit] = {
    case Resource.Created(path) => printLine(s"[L:$depth] ⬇️ Downloaded ${url} to ${path}")
    case Resource.Exists(path)  => printLine(s"[L:$depth] 👌 Resource ${url} already exists at ${path}")
  }

  private def collect(maxDepth: Depth = 3)(url: NonEmptyURL): ZIO[SpiderEnv, Throwable, Unit] =
    when(maxDepth > 0)(
      Client
        .request(url)
        .map(r => (r.status.code, r.contentType, r))
        .flatMap {
          case (200, Some(contentType: String), response) if contentType.startsWith("text/html") =>
            for
              _     <- Resource.persistHtml(url, response).flatMap(printProgress(url, maxDepth))
              links <- LinksParser.parse(url, response)
              _     <- ZIO.sleep(1.millis)
              out   <- foreachParDiscard(links)(collect(maxDepth - 1)).withParallelism(10)
            yield out
          case (200, _, response)                                                                =>
            Resource.persist(url, response).map(printProgress(url, maxDepth))
          case _                                                                                 =>
            ZIO.unit
        }
    ).unit

  def collect(url: NonEmptyURL): ZIO[SpiderEnv, Throwable, Unit] = collect(3)(url)
