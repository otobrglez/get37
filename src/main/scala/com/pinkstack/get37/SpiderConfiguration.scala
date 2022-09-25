package com.pinkstack.get37

/** Configuration for scraping part of spider
  * @param maxFibers
  *   Number of fibers used in fetching.
  * @param preFetchDelay
  *   Delay in milliseconds before next layer fetching is invoked.
  * @param maxDepth
  *   Maximal depth of data collection/scraping.
  */
final case class SpiderConfiguration(maxFibers: BigInt = 10, preFetchDelay: BigInt = 10, maxDepth: BigInt = 3)
