import sbt._

object Dependencies{

  private[this] val dependencies: Seq[ModuleID] = Seq(
    "io.scalac"               %% "reactive-rabbit"          % "1.1.2",
    "com.typesafe.akka"       %% "akka-stream"              % "2.4.8",
    "com.github.nscala-time"  %% "nscala-time"              % "2.12.0",
    "org.scalatest"           %% "scalatest"                % "2.2.6",
    "com.typesafe.play"       %% "play-json"                % "2.5.2",
    "com.typesafe.play"       %% "play-ws"                  % "2.5.2",
    "ch.qos.logback"           % "logback-classic"          % "1.1.7",
    "org.codehaus.janino"      % "janino"                   % "3.0.6"
  )

  val generalDependencies: Seq[ModuleID] = dependencies
}