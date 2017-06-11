package com.imdb.api

import spray.json.DefaultJsonProtocol

case class MovieInfo(imdbId: String, availableSeats: Int, reservedSeats: Int, screenId: String, movieTitle: String)

object MovieInfoProtocol extends DefaultJsonProtocol {
  implicit val movieInfoFormat = jsonFormat5(MovieInfo.apply)
}