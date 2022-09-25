package com.pinkstack.get37

import zio.Task
import zio.ZIO.attempt

trait ResourcefulSpec:
  def rm(path: os.Path): Task[Unit]    = attempt(os.remove(path))
  def rmAll(path: os.Path): Task[Unit] = attempt(os.remove.all(path))
