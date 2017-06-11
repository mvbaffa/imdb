package com.imdb.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.imdb.config.AppSettings._
import MovieRegisterProtocol._
import MovieInfoProtocol._
import com.redis.RedisClient

trait RestApi {

  def registerMovie(movieRegister: MovieRegister): MovieInfo
  def getMovie(movieId: String): MovieInfo

  val route =
    pathPrefix("movies") {
      path("All") {
        get {
          complete("Movie Get called")
        }
      } ~
      (get & path(Segment)) { id =>
        complete {
          getMovie(id)
        }
      } ~
      path("Register") {
        (post & entity(as[MovieRegister])) { register =>
          val movieInfo = registerMovie(register)
          complete(movieInfo)
        }
      } ~
      path("Info") {
        (post & entity(as[MovieInfo])) { movie =>
          complete(movie)
        }
      }
    }
}

class TickerReservation(portNumber: Int) extends RestApi {

  val redis = new RedisClient("localhost", 6379)
  val bindingFuture = Http().bindAndHandle(route, "localhost", portNumber)

  def registerMovie(movieRegister: MovieRegister): MovieInfo = {

    val movieInfo = MovieInfo(movieRegister.imdbId, movieRegister.availableSeats, 0, movieRegister.screenId, "N/A")

    redis.sadd(movieRegister.imdbId, movieInfo)
    println(s"\n Movie Registered: ${movieInfo}")

    movieInfo
  }

  def getMovie(movieId: String): MovieInfo = {

    val redisInfo = redis.smembers(movieId).get
    println(s"\n Movie Returned: ${redisInfo}")

    MovieInfo("N/A", 0, 0, "N/A", "N/A")
//    val movieInfo = MovieInfo(redisInfo.imdbId, redisInfo.availableSeats, 0, movieRegister.screenId, "N/A")
  }

  def shutDown: Unit = {
    bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
  }
}


