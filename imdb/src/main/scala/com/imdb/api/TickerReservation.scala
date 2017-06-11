package com.imdb.api

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.imdb.config.AppSettings._
import MovieProtocol._
import com.redis.RedisClient

trait RestApi {

  def registerMovie(movieRegister: MovieRegister): ReturnInfo
  def reserveMovie(movieReservation: MovieReservation): ReturnInfo
  def getMovie(movieId: String, screenId: String): ReturnInfo
  def fetchMovie(movieId: String, screenId: String): MovieInfo

  val route =
    pathPrefix("movies") {
      path("") {
        get {
          complete("Movie Reservation")
        }
      } ~
      (get & parameters('id, 'screen)) { (imdbId, screenId) =>
        complete {
          getMovie(imdbId, screenId)
        }
      } ~
      path("Register") {
        (post & entity(as[MovieRegister])) { register =>
          val movieInfo = registerMovie(register)
          complete(movieInfo)
        }
      } ~
      path("Reserve") {
        (post & entity(as[MovieReservation])) { reservation =>
          val movieInfo = reserveMovie(reservation)
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

  def registerMovie(movieRegister: MovieRegister): ReturnInfo = {

    val movieInfo = MovieInfo(movieRegister.imdbId, movieRegister.availableSeats, 0, movieRegister.screenId, "N/A")
    val movieJson = movieInfo.toJson.toString

    redis.set(movieRegister.imdbId+movieRegister.screenId, movieJson)
    println(s"* Register Movie - Movie Returned: ${movieInfo}\n")

    val returnInfo = ReturnInfo(true, "Operation Succeeded", movieInfo.imdbId, movieInfo.availableSeats, 0,
      movieInfo.screenId, movieInfo.movieTitle)
    returnInfo
  }

  // localhost:8080/movies?id=tt0111162&screen=san francisco movies
  def fetchMovie(movieId: String, screenId: String): MovieInfo = {

    val redisInfo = redis.get(movieId + screenId)

    redisInfo match {

      case Some(i) => {
        println(s"* GetMovie - Movie Returned: ${movieId}, ${screenId}\n")
        i.parseJson.convertTo[MovieInfo]
      }

      case None => {
        println(s"* GetMovie - Movie ${movieId}, ${screenId} not found\n")
        MovieInfo("N/A", 0, 0, "N/A", "N/A")
      }
    }
  }

  // localhost:8080/movies?id=tt0111162&screen=san francisco movies
  def getMovie(movieId: String, screenId: String): ReturnInfo = {

//    var returnInfo: ReturnInfo = ???
    val movieInfo = fetchMovie(movieId, screenId)

    movieInfo.imdbId match {

      case "N/A" => {
        println(s"* GetMovie - Movie ${movieId}, ${screenId} not found\n")
        val movieInfo = MovieInfo("N/A", 0, 0, "N/A", "N/A")
        ReturnInfo(false, "Movie not found", movieInfo.imdbId, movieInfo.availableSeats, 0,
          movieInfo.screenId, movieInfo.movieTitle)
      }

      case _ => {
        println(s"* GetMovie - Movie Returned: ${movieId}, ${screenId}\n")
        ReturnInfo(true, "Operation Succeeded", movieInfo.imdbId, movieInfo.availableSeats, 0,
          movieInfo.screenId, movieInfo.movieTitle)
      }
    }
  }

  def reserveMovie(movieReservation: MovieReservation): ReturnInfo = {

    var returnInfo = getMovie(movieReservation.imdbId, movieReservation.screenId)

    returnInfo.operationStatus match {
      case true => {
        println(s"* ReserveMovie - Movie Returned: ${returnInfo.imdbId}, ${returnInfo.screenId}")
      }
      case false => {
        println(s" * ReserveMovie - Movie ${movieReservation.imdbId}, ${movieReservation.screenId} not found\n")
        return returnInfo
      }
    }

    if(returnInfo.reservedSeats == returnInfo.availableSeats) {
      println(s"* ReserveMovie - No Seats Available")
      returnInfo.operationStatus = false
      returnInfo.msg = "No Seats Available"
      return returnInfo
    } else {
      returnInfo.reservedSeats += 1
    }

    val saveInfo = MovieInfo(returnInfo.imdbId, returnInfo.availableSeats, returnInfo.reservedSeats, returnInfo.screenId, returnInfo.movieTitle)
    val saveJson = saveInfo.toJson.toString

    redis.set(returnInfo.imdbId+returnInfo.screenId, saveJson)

    returnInfo
  }

  def shutDown: Unit = {
    bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
  }
}


