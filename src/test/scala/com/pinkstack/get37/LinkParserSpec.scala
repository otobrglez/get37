package com.pinkstack.get37

import com.pinkstack.get37.LinksParser.parse
import com.pinkstack.get37.types.*
import zhttp.http.{Response, Status}
import zio.test.Assertion.*
import zio.test.Spec.TestCase
import zio.test.{assertZIO, Assertion}
import zio.Task

object LinkParserSpec extends zio.test.junit.JUnitRunnableSpec:

  private def containsUrl(s: String): Assertion[Iterable[NonEmptyURL]] = contains[NonEmptyURL](s)
  private def containsUrls(urls: String*)                              = urls.map(containsUrl).reduce((a, b) => a && b)

  private def parseHtml(url: NonEmptyURL, html: String): Task[Set[NonEmptyURL]] =
    parse(url, Response.html(html, Status.Ok))

  def spec = suite("LinkParser")(
    test("parses a[href], [src] and link[href]") {
      assertZIO(
        parseHtml(
          NonEmptyURL("https://epic.blog"),
          """<html>
            |<head>
            |<link rel="stylesheet" href="//somecdn.somewhere/test.css" />
            |<link rel="stylesheet" href="css/some.css" />
            |<script src="js/fancy.js"></script>
            |</head>
            |<body>
            |<a href="">Empty href</a>
            |<a href="#home">Empty anchor</a>
            |<a href="about">About</a>
            |<a href="https://epic.blog/me">Me</a>
            |<a href="/more">More</a>
            |<img src="images/image.jpg" />
            |Extra cases that are filtered out
            |<a href="mailto::otobrglez@gmail.com">Mail</a>
            |<a href="https://hackernews.com">HN external link</a>
            |<a href="ftp://hackernews.com">Some other protocol</a>
            |</body></html>""".stripMargin
        )
      )(
        containsUrls(
          "https://epic.blog/css/some.css",
          "https://epic.blog/js/fancy.js",
          "https://epic.blog/",
          "https://epic.blog/#home",
          "https://epic.blog/about",
          "https://epic.blog/me",
          "https://epic.blog/more",
          "https://epic.blog/images/image.jpg"
        )
      )
    },
    test("handles broken HTML")(
      assertZIO(parseHtml(NonEmptyURL("https://epic.blog"), "br0ken"))(equalTo(Set.empty[NonEmptyURL]))
    )
  )
