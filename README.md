# get37 🪠

[get37] is a [Scala] / [ZIO] based web scraper/spider built as part
of [technical assignment](./assignment.pdf) at [13|37][1337].

<div align="center">

![get37 in action](https://github.com/otobrglez/get37/blob/master/get37.gif)

</div>

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/otobrglez/get37/tree/master.svg?style=shield&circle-token=05d2aaa7bab5bf7af48f31089663c8ec1c220883)](https://dl.circleci.com/status-badge/redirect/gh/otobrglez/get37/tree/master)

## Usage

```bash
$ java -jar target/*/get37.jar https://tretton37.com
$ java -jar target/*/get37.jar --maxFibers 10 --preFetchDelay 70 --maxDepth 4 https://zio.dev
$ java -jar target/*/get37.jar --help # for more help
```

[get37] currently supports three configuration flags that can be passed along when the tool is started.

- `maxFibers`, set to `10` by default tells the ZIO runtime how many [concurrent fibers](https://blog.rockthejvm.com/zio-fibers/) can be used when sub-requests are beeing made.
- `preFetchDelay`, set to `10` milliseconds by defaul, adds a time delay before the sub-sequential requests are made.
- `maxDepth`, set to `3` by default will serve as hard-limit when the spider tries to go deeper into the sites structure.

## Development & packaging

This project uses [Nix Shell (shell.nix)](./shell.nix) for project dependencies management. JDK and SBT are only dependencies.

```bash
$ sbt "run https://tretton37.com"
```

To build "über-JAR" this project uses [sbt-assembly](https://github.com/sbt/sbt-assembly) and [sbt-native-packager](https://github.com/sbt/sbt-native-packager) plugins.

```bash
$ sbt assembly
$ java -jar target/*/get37.jar
```

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
