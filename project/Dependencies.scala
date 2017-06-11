import sbt._

object Dependencies{

  //Dependencies Version Declarations
  val scalaTestVersion         = "2.2.6"
  val logBackVersion           = "1.1.7"
  val janinoVersion            = "3.0.6"
  val akkaActorVersion         = "2.5.2"
  val akkaStreamVersion        = "2.5.2"
  val akkaHttpVersion          = "10.0.7"
  var redisScalaVersion        = "3.4"

  private[this] val dependencies: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                 % scalaTestVersion,
    "ch.qos.logback"           % "logback-classic"           % logBackVersion,
    "org.codehaus.janino"      % "janino"                    % janinoVersion,
    "com.typesafe.akka"        % "akka-actor_2.11"           % akkaActorVersion,
    "com.typesafe.akka"        % "akka-stream_2.11"          % akkaStreamVersion,
    "com.typesafe.akka"        % "akka-http-core_2.11"       % akkaHttpVersion,
    "com.typesafe.akka"        % "akka-http_2.11"            % akkaHttpVersion,
    "com.typesafe.akka"        % "akka-http-spray-json_2.11" % akkaHttpVersion,
    "net.debasishg"           %% "redisclient"               % redisScalaVersion
  )

  val generalDependencies: Seq[ModuleID] = dependencies
}