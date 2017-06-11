package com.imdb.api

import akka.actor.{Actor, Props}
import com.imdb.config.AppSettings.{log, portNumber}
import com.imdb.protocols.Protocols.StopTickerReservation


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