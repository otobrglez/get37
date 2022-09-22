package com.pinkstack.get37

import types.*
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

object Get37App extends ZIOAppDefault:
  def program = Spider.collect(NonEmptyURL("https://tretton37.com")) *> printLine("Completed.")

  def run = program.provide(ChannelFactory.auto ++ EventLoopGroup.auto())
