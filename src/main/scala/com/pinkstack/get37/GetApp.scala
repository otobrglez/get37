package com.pinkstack.get37

import com.pinkstack.get37.URLUtils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.Console.printLine
import zio.ZIO.{attempt, collectAllPar, collectAllParDiscard, fail, failCause, foreachParDiscard, fromOption, succeed}
import zio.*

import java.net.URL
import scala.jdk.CollectionConverters.*
import scala.util.Try
import os.*

object GetApp extends ZIOAppDefault:
  def program =
    for
//      startPage <- URLUtils.make("https://epic.blog")
      startPage <- URLUtils.make("https://tretton37.com")

      _ <- Spider.request(true)(startPage)
      _ <- printLine("Completed.")
    yield ()

  def run = program.provide(
    ChannelFactory.auto ++ EventLoopGroup.auto()
  )
