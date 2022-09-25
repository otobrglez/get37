package com.pinkstack.get37

import java.net.URL
import scala.util.Try
import zio.ZIO.{attempt, succeed}
import zio.Console.printLine
import zio.Task

object types:
  opaque type NonEmptyURL <: URL = URL
  object NonEmptyURL:
    def apply(s: String): NonEmptyURL        = new java.net.URL(s)
    def tryIt(s: String): Try[NonEmptyURL]   = Try(new NonEmptyURL(s))
    def safe(s: String): Option[NonEmptyURL] = tryIt(s).toOption

  extension (url: URL)
    def withProtocolAndHost: String = url.getProtocol + "://" + url.getHost

    def toPath(isHtml: Boolean = false): Task[os.Path] =
      for
        modPathA <- succeed(
          if url.getPath.isBlank || url.getPath == "/" then "index"
          else url.getPath.replaceFirst("/", "")
        )
        modPath  <- succeed(modPathA.replaceFirst("""(\/\.\.\/)|(\.\.\/)""", ""))
        rawPath  <- attempt(os.pwd / url.getHost / os.RelPath(modPath))
        path     <- succeed(if rawPath.ext.isBlank && isHtml then os.Path(rawPath.toString + ".html") else rawPath)
      yield path

  given Conversion[NonEmptyURL, String] with
    override def apply(neu: NonEmptyURL): String = neu.toString

  given Conversion[String, NonEmptyURL] with
    override def apply(s: String): NonEmptyURL = NonEmptyURL(s)
