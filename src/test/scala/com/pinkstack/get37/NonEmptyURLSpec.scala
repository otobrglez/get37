package com.pinkstack.get37

import com.pinkstack.get37.types.*
import zio.Task
import zio.ZIO.{attempt, fromTry, succeed}
import zio.test.Assertion.*
import zio.test.assertZIO

import java.net.MalformedURLException
import scala.util.Try

object NonEmptyURLSpec extends zio.test.junit.JUnitRunnableSpec:
  def spec = suite(" NonEmptyURL")(
    test("apply")(
      assertZIO(fromTry(Try(NonEmptyURL.apply("x:x:x"))).exit)(
        fails(isSubtype[MalformedURLException](anything))
      )
    ),
    test("tryIt")(
      assertZIO(fromTry(NonEmptyURL.tryIt("x:x:x")).exit)(
        fails(isSubtype[MalformedURLException](anything))
      )
    ),
    test("withProtocolAndHost")(
      assertZIO(
        succeed(NonEmptyURL("https://epic.blog/test").withProtocolAndHost)
      )(equalTo("https://epic.blog"))
    ),
    test("toPath (HTML)")(
      assertZIO(
        NonEmptyURL("https://epic.blog/index").toPath(true)
      )(equalTo(os.pwd / "epic.blog" / "index.html"))
    ),
    test("toPath (non-HTML)")(
      assertZIO(
        NonEmptyURL("https://epic.blog/index").toPath()
      )(equalTo(os.pwd / "epic.blog" / "index"))
    )
  )
