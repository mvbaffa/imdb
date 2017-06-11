package com.imdb.api

import spray.json.DefaultJsonProtocol

case class MovieRegister(imdbId: String, availableSeats: Int, screenId: String)
case class MovieInfo(imdbId: String, var availableSeats: Int, var reservedSeats: Int, screenId: String, var movieTitle: String)

object MovieInfoProtocol extends DefaultJsonProtocol {
  implicit val movieInfoFormat = jsonFormat5(MovieInfo.apply)
}

object MovieRegisterProtocol extends DefaultJsonProtocol {
  implicit val movieRegisterformat = jsonFormat3(MovieRegister.apply)
}