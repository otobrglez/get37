package com.pinkstack.get37

import com.pinkstack.get37.types.NonEmptyURL
import zhttp.http.Response
import zio.Task
import zio.ZIO.{attempt, ifZIO, succeed}

object Resource:
  sealed trait Result:
    def path: os.Path

  final case class Exists(path: os.Path)  extends Result
  final case class Created(path: os.Path) extends Result

  private def urlToPath(url: NonEmptyURL, isHtml: Boolean = false): Task[os.Path] =
    for
      modPath <- succeed(if url.getPath.isBlank then "index" else url.getPath.replaceFirst("/", ""))
      rawPath <- attempt(os.pwd / url.getHost / os.RelPath(modPath))
      path    <- succeed(if rawPath.ext.isBlank && isHtml then os.Path(rawPath.toString + ".html") else rawPath)
    yield path

  def persist(url: NonEmptyURL, response: Response, isHtml: Boolean = false): Task[Result] =
    for
      path   <- urlToPath(url, isHtml)
      result <- ifZIO(succeed(os.exists(path)))(
        succeed(Exists(path)),
        response.body.asArray
          .map(content => os.write(path, content, createFolders = true))
          .ignoreLogged *> succeed(Created(path))
      )
    yield result

  def persistHtml(url: NonEmptyURL, response: Response): Task[Result] = persist(url, response, true)
