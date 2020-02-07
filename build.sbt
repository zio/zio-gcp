ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "org.mikail"

developers := List(
  Developer(
    "mikail-khan",
    "Mika'il Khan",
    "mikail.dev.io@gmail.com",
    url("https://github.com/mikail-khan")
  )
)

lazy val root = project
  .in(file("."))
  .settings(skip in publish := true)
  .aggregate(
    core,
    firestore,
    pubsub,
    storage
  )

lazy val core = project
  .in(file("zio-gcp-core"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC17"
    )
  )
  .settings(
    scalafmtOnCompile := true,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-Ywarn-unused:imports"
    )
  )

lazy val firestore = project
  .in(file("zio-gcp-firestore"))
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "dev.zio"          %% "zio"                   % "1.0.0-RC17",
      "dev.zio"          %% "zio-interop-guava"     % "28.2.0.0",
      "com.google.cloud" % "google-cloud-firestore" % "1.32.2"
    )
  )
  .settings(
    scalafmtOnCompile := true,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-Ywarn-unused:imports"
    )
  )
  .dependsOn(
    core
  )

  lazy val pubsub = project
  .in(file("zio-gcp-pubsub"))
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "dev.zio"          %% "zio"                % "1.0.0-RC17",
      "dev.zio"          %% "zio-interop-guava"  % "28.2.0.0",
      "com.google.cloud" % "google-cloud-pubsub" % "1.102.1"
    )
  )
  .settings(
    scalafmtOnCompile := true,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-Ywarn-unused:imports"
    )
  )
  .dependsOn(
    core
  )
  
lazy val storage = project
  .in(file("zio-gcp-storage"))
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "dev.zio"          %% "zio"                 % "1.0.0-RC17",
      "dev.zio"          %% "zio-interop-guava"   % "28.2.0.0",
      "com.google.cloud" % "google-cloud-storage" % "1.103.1"
    )
  )
  .settings(
    scalafmtOnCompile := true,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-Ywarn-unused:imports"
    )
  )
  .dependsOn(
    core
  )

