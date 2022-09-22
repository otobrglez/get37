import sbt._

object Dependencies {
  type Version = String
  type Modules = Seq[ModuleID]

  object Versions {
    val zio: Version        = "2.0.2"
    val zioLogging: Version = "2.1.1"
  }

  lazy val logging: Modules = Seq(
    "ch.qos.logback" % "logback-classic" % "1.4.1"
  ) ++ Seq(
    "dev.zio" %% "zio-logging",
    "dev.zio" %% "zio-logging-slf4j"
  ).map(_ % Versions.zioLogging)

  lazy val zio: Modules = Seq(
    "dev.zio" %% "zio",
    "dev.zio" %% "zio-test",
    "dev.zio" %% "zio-test-sbt",
    "dev.zio" %% "zio-streams",
    "dev.zio" %% "zio-test-junit"
  ).map(_ % Versions.zio) ++ Seq(
    "dev.zio" %% "zio-cli" % "0.2.8",
    "io.d11"  %% "zhttp"   % "2.0.0-RC11" // TODO: This will become "zio-http", once released to public
  )

  lazy val jsoup: Modules = Seq(
    "org.jsoup" % "jsoup" % "1.15.3"
  )

  lazy val libOs: Modules = Seq(
    "com.lihaoyi" %% "os-lib" % "0.8.1"
  )

  lazy val serviceDependencies: Modules = zio ++ jsoup ++ libOs ++ logging

  lazy val projectResolvers: Seq[MavenRepository] = Seq(
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype staging" at "https://oss.sonatype.org/content/repositories/staging",
    "Java.net Maven2 Repository" at "https://download.java.net/maven/2/"
  )
}
