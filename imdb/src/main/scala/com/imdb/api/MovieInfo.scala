package com.imdb.api

import spray.json.DefaultJsonProtocol

case class MovieImdb(title: String, year: Int, rated: String, released: String, runtime: String)
case class MovieRegister(imdbId: String, availableSeats: Int, screenId: String)
case class MovieReservation(imdbId: String, screenId: String)
case class MovieInfo(imdbId: String, var availableSeats: Int, var reservedSeats: Int, screenId: String, var movieTitle: String)
case class ReturnInfo(var operationStatus: Boolean, var msg: String, movieInfo: MovieInfo)

object MovieProtocol extends DefaultJsonProtocol {
  implicit val movieInfoFormat = jsonFormat5(MovieInfo.apply)
  implicit val movieRegisterformat = jsonFormat3(MovieRegister.apply)
  implicit val MovieReservationformat = jsonFormat2(MovieReservation.apply)
  implicit val returnInfoformat = jsonFormat3(ReturnInfo.apply)
  implicit val movieImdbformat = jsonFormat5(MovieImdb.apply)
}
