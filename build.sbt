import Dependencies._

ThisBuild / version      := "0.0.1"
ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "get37",
    libraryDependencies ++= serviceDependencies,
    resolvers ++= projectResolvers
  )
