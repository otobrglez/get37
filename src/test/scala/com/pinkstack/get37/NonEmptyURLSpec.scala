package com.pinkstack.get37

import com.pinkstack.get37.types.*
import zio.Task
import zio.ZIO.{attempt, fromTry}
import zio.test.Assertion.*
import zio.test.assertZIO

object NonEmptyURLSpec extends zio.test.junit.JUnitRunnableSpec:
  def spec = suite(" NonEmptyURL")(
    test("initialization")(
      assertZIO(fromTry(NonEmptyURL.tryIt("xxx:xxx:xx")))(
        isFailure(
          anything
        )
      )
    )
  )
