package com.pinkstack.get37

import java.net.URL
import scala.util.Try

object types:
  opaque type NonEmptyURL <: URL = URL
  object NonEmptyURL:
    def apply(s: String): NonEmptyURL        = new java.net.URL(s)
    def tryIt(s: String): Try[NonEmptyURL]   = Try(new NonEmptyURL(s))
    def safe(s: String): Option[NonEmptyURL] = tryIt(s).toOption

  extension (url: URL) 
    def withProtocolAndHost: String = url.getProtocol + "://" + url.getHost
    
  given Conversion[NonEmptyURL, String] with
    override def apply(neu: NonEmptyURL): String = neu.toString

  given Conversion[String, NonEmptyURL] with
    override def apply(s: String): NonEmptyURL = NonEmptyURL(s)
