# get37 🪠

[get37] is a [Scala] / [ZIO] based web scraper/spider built as part
of [technical assignment](./assignment.pdf) at [13|37][1337].

<div align="center">

![get37 in action](https://github.com/otobrglez/get37/blob/master/get37.gif)

</div>

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/otobrglez/get37/tree/master.svg?style=shield&circle-token=05d2aaa7bab5bf7af48f31089663c8ec1c220883)](https://dl.circleci.com/status-badge/redirect/gh/otobrglez/get37/tree/master)

## 🏃‍♂️ Usage

After the project is [assembled (instructions)](#-development) into "über-JAR", you can simply use it like this:

```bash
$ java -jar target/*/get37.jar https://tretton37.com
$ java -jar target/*/get37.jar --maxFibers 10 --preFetchDelay 70 --maxDepth 4 https://zio.dev
$ java -jar target/*/get37.jar --help # for more help
```

[get37] currently supports three configuration flags that can be passed along when the tool is started.

- `maxFibers`, set to `10` by default tells the ZIO runtime how many [concurrent fibers](https://blog.rockthejvm.com/zio-fibers/) can be used when sub-requests are beeing made.
- `preFetchDelay`, set to `10` milliseconds by defaul, adds a time delay before the sub-sequential requests are made.
- `maxDepth`, set to `3` by default will serve as hard-limit when the spider tries to go deeper into the sites structure.

## 🏗 Development

This project uses [Nix Shell (shell.nix)](./shell.nix) for project dependencies management. JDK and SBT are only dependencies.

```bash
$ sbt "run https://tretton37.com"
```

To build "über-JAR" this project uses [sbt-assembly](https://github.com/sbt/sbt-assembly) and [sbt-native-packager](https://github.com/sbt/sbt-native-packager) plugins.

```bash
$ sbt assembly
$ java -jar target/*/get37.jar
```

## Testing

This project also comes with [tests](src/test) that can be invoked with `SBT` and [CircleCI setup](https://app.circleci.com/pipelines/github/otobrglez/get37?branch=master).

```bash
$ sbt test
```

## Dependencies

- [zio](https://zio.dev) - High-performance, type-safe, composable asynchronous and concurrent programming library and framework for Scala.
- [zio-cli](https://github.com/zio/zio-cli) - Powerful command-line applications framework for ZIO.
- [zio-http (ex-zhttp)](https://github.com/zio/zio-http) - A scala library for building HTTP apps. It is powered by [ZIO](https://zio.dev) and [Netty](https://netty.io/) and aims at being the defacto solution for writing, highly scalable and performant web applications using idiomatic Scala.
- [jsoup](https://jsoup.org/) - is a Java library for working with real-world HTML. It provides a very convenient API for fetching URLs and extracting and manipulating data, using the best of HTML5 DOM methods and CSS selectors. Although in this project is only used for content/link extraction.
- [os-lib](https://github.com/com-lihaoyi/os-lib) - a simple, flexible, high-performance Scala interface to common OS filesystem and subprocess APIs


## Resources

- [Experimenting with recursion and ZIO](https://blog.knoldus.com/experimenting-with-recursion-and-zio/)
- [5 lessons learned from my continuing awesome journey with ZIO](https://medium.com/wix-engineering/5-lessons-learned-from-my-continuing-awesome-journey-with-zio-66319d12ed7c)
- [Guy Rutenberg - Make Offline Mirror of a Site using `wget`](https://www.guyrutenberg.com/2014/05/02/make-offline-mirror-of-a-site-using-wget/)
- [EFF - Mirroring your site](https://www.eff.org/keeping-your-site-alive/mirroring-your-site)
- [Aiswarya Prakasan - 10 minute command line apps with ZIO CLI](https://www.slideshare.net/AiswaryaPrakasan/10-minute-command-line-apps-with-zio-cli)

## Author

[Oto Brglez](https://github.com/otobrglez)

![Twitter Follow](https://img.shields.io/twitter/follow/otobrglez?style=social)

[scala]: https://www.scala-lang.org/

[zio]: https://zio.dev/

[get37]: https://github.com/otobrglez/get37

[1337]: https://1337.tech/
