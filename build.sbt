import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbt.Keys._

import Dependencies._

ThisBuild / version      := "0.0.1"
ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, JavaAppPackaging, AssemblyPlugin)
  .settings(
    name                             := "get37",
    libraryDependencies ++= serviceDependencies,
    resolvers ++= projectResolvers,
    assembly / mainClass             := Some("com.pinkstack.get37.Get37App"),
    assembly / assemblyJarName       := "get37.jar",
    assembly / assemblyMergeStrategy := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.last
      case "module-info.class"                     => MergeStrategy.first
      case x                                       => (assembly / assemblyMergeStrategy).value(x)
    },
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    buildInfoKeys                    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage                 := "com.pinkstack.get37",
    scalacOptions ++= Seq(
      "-encoding",
      "utf8",
      "-feature",
      "-language:implicitConversions",
      "-language:higherKinds"
    )
  )
