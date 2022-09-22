package com.pinkstack.get37

import zio.Task
import zio.ZIO.attempt

import java.net.URL as SystemURL

object URLUtils:
  def make(url: String): Task[SystemURL] = attempt(new SystemURL(url))

  given Conversion[SystemURL, String] with
    def apply(url: SystemURL): String = url.toString
