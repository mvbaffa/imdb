import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import com.imdb.client.RequestActor
import com.imdb.protocols.Protocols.{GetIp, MovieInfo}

import scala.concurrent.Await

object Main extends App {

  import com.imdb.config.AppSettings._
  log.info(s"\n * \n * Imdb API Started \n * \n")

  sys.addShutdownHook(shutdown)

  val requestActor = actorSystem.actorOf(RequestActor.props, requestActorName)

  implicit val timeout = Timeout(30 seconds)

  requestActor ? GetIp("https://api.ipify.org?format=json")
  requestActor ? MovieInfo("tt0111161")
//  val answer = Await.result(requestActor ? MovieInfo("tt0111161"), timeout.duration)
//  println(s"answer = $answer")

  actorSystem.whenTerminated

  private def shutdown {
    log.info(" \n\n * Shuttingdown Imdb API \n * Wait... \n *")
    Thread.sleep(5000)
    log.info(" \n * Done shutting down. \n")
  }
}
