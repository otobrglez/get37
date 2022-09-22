# get37

[get37] is a [Scala] / [ZIO] based web scraper specialised
for ["mirroring"](https://www.eff.org/keeping-your-site-alive/mirroring-your-site), built as part
of [technical assignment](./assignment.pdf) at [13|37][1337].

## Development & packaging

```bash
sbt ~run
sbt assembly
java -jar target/*/get37.jar
```

## Resources

- [Guy Rutenberg - Make Offline Mirror of a Site using `wget`](https://www.guyrutenberg.com/2014/05/02/make-offline-mirror-of-a-site-using-wget/)
- [EFF - Mirroring your site](https://www.eff.org/keeping-your-site-alive/mirroring-your-site)
- [Aiswarya Prakasan - 10 minute command line apps with ZIO CLI](https://www.slideshare.net/AiswaryaPrakasan/10-minute-command-line-apps-with-zio-cli)

## Author

[Oto Brglez](https://github.com/otobrglez)

[scala]: https://www.scala-lang.org/

[zio]: https://zio.dev/

[get37]: https://github.com/otobrglez/get37

[1337]: https://1337.tech/
