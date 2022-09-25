package com.pinkstack.get37

import com.pinkstack.get37.LinksParser.parse
import com.pinkstack.get37.types.*
import zhttp.http.{Response, Status}
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{durationInt, Clock, Scope, Task, ULayer, ZLayer}
import zio.ZIO.attempt
import zio.test.Assertion.*
import zio.test.assertZIO
import zio.test.TestClock
import zio.test.TestAspect.{ignore, silent, timeout}

import java.net.UnknownHostException

object SpiderSpec extends zio.test.junit.JUnitRunnableSpec with ResourcefulSpec:
  private val simpleConfig = SpiderConfiguration(preFetchDelay = 0)

  def spiderLayer(config: SpiderConfiguration): ULayer[Spider.SpiderEnv] =
    ZLayer.succeed(config) ++ ChannelFactory.auto ++ EventLoopGroup.auto()

  def spec = suite("Spider")(
    test("simple test w/ epic.blog") {
      rmAll(os.pwd / "epic.blog") *>
        assertZIO(Spider.collect(NonEmptyURL("https://epic.blog")))(anything)
    }.provide(
      spiderLayer(simpleConfig.copy(maxDepth = 2))
    ) @@ timeout(10.seconds) @@ silent,
    test("simple test w/ tretton37.com") {
      rmAll(os.pwd / "tretton37.com") *>
        assertZIO(Spider.collect(NonEmptyURL("https://tretton37.com/")))(anything)
    }.provide(
      spiderLayer(simpleConfig.copy(maxDepth = 1))
    ) @@ timeout(20.seconds) @@ silent,
    test("failed scrape") {
      val someRandomDomain: String = "epic.blog.that.does.not.exist"

      assertZIO(Spider.collect(NonEmptyURL("https://" + someRandomDomain)).exit)(
        fails(isSubtype[UnknownHostException](anything))
      )
    }.provide(
      spiderLayer(simpleConfig.copy(maxDepth = 2))
    ) @@ timeout(10.seconds)
  )
