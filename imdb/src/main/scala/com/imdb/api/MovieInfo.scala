package com.imdb.api

import spray.json.DefaultJsonProtocol

case class MovieRegister(imdbId: String, availableSeats: Int, screenId: String)
case class MovieReservation(imdbId: String, screenId: String)
case class MovieInfo(imdbId: String, var availableSeats: Int, var reservedSeats: Int, screenId: String, var movieTitle: String)
case class ReturnInfo(var operationStatus: Boolean, var msg: String, imdbId: String, var availableSeats: Int,
                      var reservedSeats: Int, screenId: String, var movieTitle: String)

object MovieProtocol extends DefaultJsonProtocol {
  implicit val movieInfoFormat = jsonFormat5(MovieInfo.apply)
  implicit val movieRegisterformat = jsonFormat3(MovieRegister.apply)
  implicit val MovieReservationformat = jsonFormat2(MovieReservation.apply)
  implicit val ReturnInfoformat = jsonFormat7(ReturnInfo.apply)
}
