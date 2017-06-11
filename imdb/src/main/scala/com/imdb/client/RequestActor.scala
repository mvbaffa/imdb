package com.imdb.client

import akka.actor.{Actor, Props}
import JsonProtocol._
import akka.http.scaladsl.model._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.imdb.config.AppSettings._
import com.imdb.protocols.Protocols.{GetIp}

import scala.concurrent.{Await, Future}

object RequestActor {

  def props(): Props = Props(new RequestActor())

  def getIp(url: String) : Unit = {

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(uri = url))

    responseFuture map { res =>
      res.status match {
        case OK =>
          Unmarshal(res.entity).to[IpInfo].map { info =>
            println(s"The information for my ip is: $info")
          }
        case _ =>
          Unmarshal(res.entity).to[String].map { body =>
            println(s"The response status is ${res.status} and response body is ${body}")
          }
      }
    }
  }
}

class RequestActor extends Actor {

  def receive: Receive = {

    case GetIp(url) => {
      RequestActor.getIp(url)
    }

    case msg =>
      log.info(s"[${self.path.name}]: UNKNOWN MESSAGE: $msg FROM ${sender.path}")
  }
}