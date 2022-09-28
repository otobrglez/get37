import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbt.Keys._

import Dependencies._

ThisBuild / version      := "0.0.1"
ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, JavaAppPackaging, AssemblyPlugin, GraalVMNativeImagePlugin)
  .settings(
    name := "get37",
    libraryDependencies ++= serviceDependencies,
    resolvers ++= projectResolvers,

    /** Main Class */
    Compile / mainClass              := Some("com.pinkstack.get37.Get37NativeApp"),
    Compile / packageBin / mainClass := (Compile / mainClass).value,
    Compile / run / mainClass        := (Compile / mainClass).value,
    assembly / mainClass             := (Compile / mainClass).value,
    GraalVMNativeImage / mainClass   := (Compile / mainClass).value,

    /** Assembly
      */
    assembly / assemblyJarName       := "get37.jar",
    assembly / assemblyMergeStrategy := {
      // case "META-INF/io.netty.versions.properties" => MergeStrategy.last
      // case "module-info.class"                     => MergeStrategy.first
      // case x                                       => (assembly / assemblyMergeStrategy).value(x)
      case "META-INF/io.netty.versions.properties" => MergeStrategy.last
      case x if x.contains("META-INF")             => (assembly / assemblyMergeStrategy).value.apply(x)
      case x if x.contains("io.netty")             => MergeStrategy.last
      case x                                       => MergeStrategy.first
    },
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),

    /** BuildInfo
      */
    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.pinkstack.get37",

    /** Scala Compiler
      */
    scalacOptions ++= Seq(
      "-encoding",
      "utf8",
      "-deprecation",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-explain"
    ),

    /** GraalVM
      */


    // This works only on Linux
    // fork                           := true,
    // javaOptions += s"-agentlib:native-image-agent=config-output-dir=./src/graal/native-image",

    containerBuildImage            := Some("graalvm-ce:ol7-java11-21.3.3"),
    graalVMNativeImageGraalVersion := Some("22.2.0"),
    // nativeImageOptions += s"-H:ReflectionConfigurationFiles=${target.value / "native-image-configs" / "reflect-config.json"}",
    // +     nativeImageOptions += s"-H:ConfigurationFileDirectories=${target.value / "native-image-configs" }",
    // +
    graalVMNativeImageOptions ++= Seq(
      // "--static",
      // "--libc=musl",
      "--no-server",
      "--no-fallback",
      // "--native-image-info",
      "--verbose",
      // "--install-exit-handlers",
      "--enable-http",
      "--enable-https",
      "--enable-all-security-services",
      "--enable-url-protocols=http,https",
      "-H:+ReportUnsupportedElementsAtRuntime",
      "-H:+ReportExceptionStackTraces",
      "-H:+PrintClassInitialization",
      "-H:+RemoveSaturatedTypeFlows",
      "-H:EnableURLProtocols=http,https",
      "-H:IncludeResources=.*\\.properties",
      "-H:ReflectionConfigurationFiles=/opt/graalvm/stage/resources/reflect-config.json",
      // s"-H:ConfigurationFileDirectories=/opt/graalvm/stage/resources",
      "-H:+JNI",
      "-Dio.netty.noUnsafe=true",
      "-Dio.netty.leakDetection.level=DISABLED",
      "--initialize-at-build-time=scala.Symbol$",
      "--initialize-at-build-time=scala.runtime.Statics$VM",
      "--initialize-at-build-time=ch.qos.logback.classic.Level",
      "--initialize-at-build-time=ch.qos.logback.classic.Logger",
      "--initialize-at-build-time=ch.qos.logback.classic.PatternLayout",
      "--initialize-at-build-time=ch.qos.logback.classic.joran.action.ConsolePluginAction",
      "--initialize-at-build-time=ch.qos.logback.core.CoreConstants",
      "--initialize-at-build-time=ch.qos.logback.core.joran.action.NestedBasicPropertyIA$1",
      "--initialize-at-build-time=ch.qos.logback.core.joran.action.NestedComplexPropertyIA$1",
      "--initialize-at-build-time=ch.qos.logback.core.pattern.parser.Parser",
      "--initialize-at-build-time=ch.qos.logback.core.pattern.parser.TokenStream$1",
      "--initialize-at-build-time=ch.qos.logback.core.spi.AppenderAttachableImpl",
      "--initialize-at-build-time=ch.qos.logback.core.status.InfoStatus",
      "--initialize-at-build-time=ch.qos.logback.core.subst.NodeToStringTransformer$1",
      "--initialize-at-build-time=ch.qos.logback.core.subst.Parser$1",
      "--initialize-at-build-time=ch.qos.logback.core.subst.Token",
      "--initialize-at-build-time=ch.qos.logback.core.subst.Tokenizer$1",
      "--initialize-at-build-time=ch.qos.logback.core.util.Duration",
      "--initialize-at-build-time=ch.qos.logback.core.util.Loader",
      "--initialize-at-build-time=ch.qos.logback.core.util.StatusPrinter",
      "--initialize-at-build-time=org.slf4j.LoggerFactory",
      "--initialize-at-build-time=org.slf4j.MDC",
      "--initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder",
      // o
      "--initialize-at-build-time=ch.qos.logback.classic.model.processor.LogbackClassicDefaultNestedComponentRules",
      "--initialize-at-build-time=ch.qos.logback.core.model.processor.ChainedModelFilter$1",
      "--initialize-at-build-time=ch.qos.logback.core.model.processor.DefaultProcessor$1",
      "--initialize-at-build-time=ch.qos.logback.core.model.processor.ImplicitModelHandler$1",
      // eo
      "--initialize-at-run-time=io.netty.channel.DefaultChannelId",
      "--initialize-at-run-time=io.netty.channel.DefaultFileRegion",
      "--initialize-at-run-time=io.netty.channel.epoll.Epoll",
      "--initialize-at-run-time=io.netty.channel.epoll.EpollEventArray",
      "--initialize-at-run-time=io.netty.channel.epoll.EpollEventLoop",
      "--initialize-at-run-time=io.netty.channel.epoll.Native",
      "--initialize-at-run-time=io.netty.channel.kqueue.KQueue",
      "--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventArray",
      "--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventLoop",
      "--initialize-at-run-time=io.netty.channel.kqueue.Native",
      "--initialize-at-run-time=io.netty.channel.unix.Errors",
      "--initialize-at-run-time=io.netty.channel.unix.IovArray",
      "--initialize-at-run-time=io.netty.channel.unix.Limits",
      "--initialize-at-run-time=io.netty.util.AbstractReferenceCounted",
      "--initialize-at-run-time=io.netty.util.internal.logging.Log4JLogger"
    )
  )
