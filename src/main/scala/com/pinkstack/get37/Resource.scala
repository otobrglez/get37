package com.pinkstack.get37

import com.pinkstack.get37.types._
import zhttp.http.Response
import zio.Task
import zio.ZIO.{attempt, ifZIO, succeed}

/** All the files (HTML or non-HTML) that the spider deals with are named Resources. The main objective of Resource is
  * to assure the correct persistence of data that gets fetched.
  */
object Resource:
  enum Result:
    case Exists(p: os.Path)
    case Created(p: os.Path)
  import Result.*

  /** Assures that content of Response object is properly persisted to local file-system.
    * @param url
    *   URL is needed so that a path can be computed.
    * @param response
    *   Response that is returned when Request is made
    * @param isHtml
    *   Tells the persistence logic if it should override and/or attach the right extensions etc.
    * @return
    */
  def persist(url: NonEmptyURL, response: Response, isHtml: Boolean = false): Task[Result] =
    for
      path   <- url.toPath(isHtml)
      result <- ifZIO(succeed(os.exists(path)))(
        succeed(Exists(path)),
        response.body.asArray
          .map(content => os.write(path, content, createFolders = true))
          .ignoreLogged *> succeed(Created(path))
      )
    yield result

  def persistHtml(url: NonEmptyURL, response: Response): Task[Result] = persist(url, response, true)
