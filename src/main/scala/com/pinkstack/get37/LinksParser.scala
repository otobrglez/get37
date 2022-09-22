package com.pinkstack.get37

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import zhttp.http.Response
import zio.ZIO.{attempt, collectAllPar, succeed}
import zio.{Task, ZIO}

import scala.jdk.CollectionConverters.*

object LinksParser:
  import types.*
  type Href = String

  private def adjustHref(url: NonEmptyURL)(href: Href): Href =
    if href.startsWith(url.withProtocolAndHost) then href
    else if href.startsWith("mailto:") then href
    else if href.startsWith("//") then href
    else if href.startsWith("/") then url.withProtocolAndHost + href
    else if href.startsWith("#") then url.withProtocolAndHost + "/" + href
    else if !href.startsWith("//") && !href.startsWith("http") then url.withProtocolAndHost + "/" + href
    else href

  private def filterExternal(url: NonEmptyURL)(href: Href): Boolean =
    href.startsWith(url.withProtocolAndHost)

  private def parseHrefsWith(document: Document, cssQuery: String, attributeKey: String = "href"): Task[Array[Href]] =
    attempt(document.select(cssQuery).asScala.toArray.map(_.attr(attributeKey)))

  def parse(url: NonEmptyURL, response: Response): ZIO[Any, Throwable, Set[NonEmptyURL]] =
    for
      document <- response.body.asString.map(Jsoup.parse)
      contentLinks = parseHrefsWith(document, "a[href]")
      mediaLinks   = parseHrefsWith(document, "[src]", "src")
      importsLinks = parseHrefsWith(document, "link[href]")
      rawHrefs <- (contentLinks <&> mediaLinks <&> importsLinks).map(t => t._1 ++ t._2 ++ t._3)
      hrefs    <- succeed(rawHrefs.map(adjustHref(url)).filter(filterExternal(url)).toSet)
      links    <- collectAllPar(hrefs.map(href => attempt(NonEmptyURL.apply(href))))
    yield links
