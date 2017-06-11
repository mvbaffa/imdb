import Main.shutdown
import com.imdb.client.RequestActor
import com.imdb.protocols.Protocols.{GetIp, MovieInfo}

object Main extends App {

  import com.imdb.config.AppSettings._
  log.info(s"\n * \n * Imdb API Started \n * \n")

  sys.addShutdownHook(shutdown)

  val requestActor = actorSystem.actorOf(RequestActor.props, requestActorName)

  requestActor ! GetIp("https://api.ipify.org?format=json")
  requestActor ! MovieInfo("tt0111161")

  actorSystem.whenTerminated

  private def shutdown {
    log.info(" \n\n * Shuttingdown Imdb API \n * Wait... \n *")
    Thread.sleep(5000)
    log.info(" \n * Done shutting down. \n")
  }
}
