package com.imdb.api

import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.imdb.config.AppSettings._
import com.imdb.protocols.Protocols.StopTickerReservation
import MovieInfoProtocol._

trait RestApi {

  val route =
    pathPrefix("movies"){
      (post & entity(as[MovieInfo])) { movie =>
        complete("Movie Post called")
      } ~
      get {
        complete("Movie Get called")
      }
    }
}

class TickerReservation(portNumber: Int) extends RestApi {

  val bindingFuture = Http().bindAndHandle(route, "localhost", portNumber)

  def shutDown: Unit = {
    bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
  }
}


object TickerReservationActor {
  def props(): Props = Props(new TickerReservationActor())
}

class TickerReservationActor extends Actor {

  var ticketReservation: TickerReservation = new TickerReservation(portNumber)

  def receive: Receive = {

    case StopTickerReservation => {
      ticketReservation.shutDown
    }

    case msg =>
      log.info(s"[${self.path.name}]: UNKNOWN MESSAGE: $msg FROM ${sender.path}")
  }
}

