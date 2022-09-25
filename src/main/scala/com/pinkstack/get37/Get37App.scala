package com.pinkstack.get37

import com.pinkstack.get37.types.*
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.Console.printLine
import zio.cli.*
import zio.cli.HelpDoc.Span.text
import zio.{Task, ZLayer}

object Get37App extends ZIOCliDefault:
  private val maxFibersFlag: Options[BigInt]     = Options.integer("maxFibers").withDefault(10)
  private val preFetchDelayFlag: Options[BigInt] = Options.integer("preFetchDelay").withDefault(10)
  private val maxDepthFlag: Options[BigInt]      = Options.integer("maxDepth").withDefault(3)

  private val options = (maxFibersFlag ++ preFetchDelayFlag ++ maxDepthFlag).as(SpiderConfiguration.apply _)
  private val args: Args[NonEmptyURL] =
    Args.text("url").atLeast(1).atMost(1).map(_.flatten).map(_.headOption).mapOrFail {
      case Some(s) => NonEmptyURL.tryIt(s).toEither.left.map(e => HelpDoc.p(s"Problems with URL: ${e.getMessage}"))
      case None    => Left(HelpDoc.p("Initial URL is missing."))
    }

  private val program: (SpiderConfiguration, NonEmptyURL) => Task[Unit] = (configuration, url) =>
    (Spider.collect(url) *> printLine("Download completed."))
      .provide(ZLayer.succeed(configuration) ++ ChannelFactory.auto ++ EventLoopGroup.auto())

  private val getCommand = Command("get37", options, args)

  override val cliApp = CliApp.make(
    name = "get37",
    version = BuildInfo.version,
    summary =
      text("Web spider that traverses all the resources available on given URL and persist them to local file-system."),
    getCommand
  )(program.tupled)
