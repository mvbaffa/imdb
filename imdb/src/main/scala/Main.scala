import Main.shutdown
import com.imdb.client.RequestActor
import com.imdb.protocols.Protocols.MovieInfo

object Main extends App {

  import com.imdb.config.AppSettings._
  log.info(s"\n * \n * Imdb API Started \n * \n")

  sys.addShutdownHook(shutdown)

  val requestActor = actorSystem.actorOf(RequestActor.props, requestActorName)
  requestActor ! MovieInfo("")

  actorSystem.whenTerminated

  private def shutdown {
    log.info(" \n\n * Shuttingdown Imdb API \n * Wait... \n *")
    Thread.sleep(5000)
    log.info(" \n * Done shutting down. \n")
  }
}
