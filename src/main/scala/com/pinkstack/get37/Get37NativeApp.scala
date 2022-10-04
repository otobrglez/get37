package com.pinkstack.get37

import zio.*
import ZIO.succeed
import Console.printLine

object Get37NativeApp extends ZIOAppDefault:
  def program =
    for
      _ <- printLine("This is just my app")
      _ <- printLine("Done")
    yield ()

  def run = program
