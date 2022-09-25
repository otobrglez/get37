# get37 🪠

[get37] is a [Scala] / [ZIO] based web scraper/spider built as part
of [technical assignment](./assignment.pdf) at [13|37][1337].

<div align="center">

![get37 in action](https://github.com/otobrglez/get37/blob/master/get37.gif)

</div>

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/otobrglez/get37/tree/master.svg?style=shield&circle-token=05d2aaa7bab5bf7af48f31089663c8ec1c220883)](https://dl.circleci.com/status-badge/redirect/gh/otobrglez/get37/tree/master)

## Development & packaging

```bash
sbt ~run
sbt assembly
java -jar target/*/get37.jar
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
