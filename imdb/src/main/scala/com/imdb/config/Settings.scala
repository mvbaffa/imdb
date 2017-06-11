package com.imdb.config

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

trait AppSettings {

  /*
  * Application
  * application.conf
  * */

  lazy private val configApp = ConfigFactory.load()

  /*
  * Application
  * Actors
  * */

  implicit val actorSystem: ActorSystem = ActorSystem("imdb-api-actorSystem")
  implicit val executionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

//  log.info(s"\n * \n * Service Settings Ready \n * \n")

}

object AppSettings extends AppSettings
