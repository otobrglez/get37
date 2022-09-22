package com.pinkstack.get37

import com.pinkstack.get37.URLUtils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.Console.printLine
import zio.ZIO.{attempt, collectAllPar, collectAllParDiscard, fail, failCause, foreachParDiscard, fromOption, succeed}
import zio.*

import java.net.URL
import scala.jdk.CollectionConverters.*
import scala.util.Try
import os.*

object Spider:
  import URLUtils.given_Conversion_SystemURL_String

  type Href = String

  private def filterLinks(href: Href)(using baseUrl: URL): Boolean =
    href.startsWith(baseUrl.getProtocol + "://" + baseUrl.getHost) &&
      !href.startsWith("//") &&
      !href.contains("#")

  private def mutateLinks(href: Href)(using baseUrl: URL): Href =
    if href.startsWith("/") && !href.startsWith("//")
    then baseUrl.getProtocol + "://" + baseUrl.getHost + href
    else href

  private def adjustLinksTo(hrefs: Array[Href])(using baseUrl: URL): Array[Href] =
    hrefs.map(mutateLinks).filter(filterLinks)

  private def adjustTo(baseUrl: URL)(hrefs: Array[Href]) = adjustLinksTo(hrefs)(using baseUrl)

  private def readLinksWith(
    document: Document,
    cssQuery: String,
    attributeKey: String = "href"
  ): ZIO[Any, Throwable, Array[Href]] =
    for rawHrefs <- attempt(document.select(cssQuery).asScala.toArray.map(_.attr(attributeKey)))
    yield rawHrefs

  def ensureLocalPathOf(url: URL, cleanParent: Boolean = false): ZIO[Any, Throwable, (Boolean, os.Path)] =
    for
      parentPath <- attempt(os.pwd / url.getHost)
      _          <- attempt(if cleanParent then os.remove.all(os.pwd / url.getHost))
      _          <- attempt(os.makeDir.all(parentPath)).ignore
      newPath    <- succeed(url.getPath.replaceFirst("/", "")).map(s => if s.isEmpty then "index" else s)
      _          <- attempt(
        if newPath.count(_ == "/".charAt(0)) > 0 then
          os.makeDir.all(parentPath / newPath.split("/").toList.reverse.tail)
      ).ignoreLogged
      filePath   <- succeed(parentPath / os.RelPath(newPath))
      exists     <- succeed(os.exists(filePath))
    yield (exists, filePath)

  def storeResponse(
    localPath: os.Path,
    response: zhttp.http.Response,
    writeComplete: Promise[Throwable, (os.Path, Boolean)]
  ): ZIO[Any, Throwable, Boolean] =
    if !os.exists(localPath) then
      response.body.asArray
        .map(content => os.write(localPath, content, createFolders = true))
        .ignoreLogged *> writeComplete.succeed(localPath, true)
      // TODO: Improve error handling and ignoring. False cases are currently silently hidden.
      // TODO: Rewrite this function!

      // .map(_ => true)
      // .catchAll(th => succeed(false))
    else writeComplete.succeed(localPath, false)

  def request(isFirst: Boolean = true)(url: URL): ZIO[EventLoopGroup & ChannelFactory, Throwable, Unit] =
    ensureLocalPathOf(url, isFirst).flatMap {
      case (true, _)          => ZIO.unit
      case (false, localPath) =>
        for
          response <- Client.request(url)
          _        <- (response.status.code, response.contentType) match
            case (200, Some(contentType: String)) if contentType.startsWith("text/html") =>
              for
                writeCompleted <- Promise.make[Throwable, (os.Path, Boolean)]
                _              <- storeResponse(localPath, response, writeCompleted)
                document       <- response.body.asString.map(Jsoup.parse)
                contentLinks = readLinksWith(document, "a[href]").map(adjustTo(url))
                mediaLinks   = readLinksWith(document, "[src]", "src").map(adjustTo(url))
                importsLinks = readLinksWith(document, "link[href]").map(adjustTo(url))
                links  <- collectAllPar(List(contentLinks, mediaLinks, importsLinks)).map(_.flatten)
                result <- writeCompleted.await
                _      <- result match {
                  case (path, true) => printLine(s"Downloaded ${url} to ${path}")
                  case _            => ZIO.unit
                }
                _      <- foreachParDiscard(links.map(raw => new URL(raw)))(request(false))
                  .withParallelism(4)
                  .ignoreLogged
              yield ()
            case (200, Some(contentType: String))                                        =>
              for
                writeComplete <- Promise.make[Throwable, (os.Path, Boolean)]
                _             <- storeResponse(localPath, response, writeComplete)
                _             <- writeComplete.await
              yield ()
            case _                                                                       => ZIO.unit
        yield ()
    }
