import com.imdb.protocols.Protocols.{StopTickerReservation}
import com.imdb.api.TickerReservationActor

object Main extends App {

  import com.imdb.config.AppSettings._
  log.info(s"\n * \n * Imdb API Started \n * \n")

  sys.addShutdownHook(shutdown)

  val ticketReservationActor = actorSystem.actorOf(TickerReservationActor.props, ticketReservationActorName)

  actorSystem.whenTerminated

  private def shutdown {
    log.info(" \n\n * Shuttingdown Imdb API \n * Wait... \n *")
    Thread.sleep(5000)
    ticketReservationActor ! StopTickerReservation
    log.info(" \n * Done shutting down. \n")
  }
}
