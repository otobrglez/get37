package com.pinkstack.get37

import com.pinkstack.get37.LinksParser.parse
import com.pinkstack.get37.Resource.Result.{Created, Exists}
import com.pinkstack.get37.types.*
import zhttp.http.{Response, Status}
import zio.Task
import zio.ZIO.attempt
import zio.test.Assertion.*
import zio.test.assertZIO

object ResourceSpec extends zio.test.junit.JUnitRunnableSpec:
  private def rm(path: os.Path): Task[Unit] = attempt(os.remove(path))

  private val testIndexPath  = os.pwd / "epic.blog" / "index.html"
  private val rootUrl        = NonEmptyURL("https://epic.blog")
  private val sampleResponse = Response.html("<html></html>")

  def spec = suite("ResourceSpec")(
    test("persistHtml")(
      rm(testIndexPath) *>
        assertZIO(
          Resource.persistHtml(rootUrl, sampleResponse)
        )(equalTo(Created(testIndexPath)))
        *>
        assertZIO(
          Resource.persistHtml(rootUrl, sampleResponse)
        )(equalTo(Exists(testIndexPath)))
    )
  )
