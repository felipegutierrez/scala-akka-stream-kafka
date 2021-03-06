package org.github.felipegutierrez.explore.akka.classic.falttolerance

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props, Terminated}

object WatchingActors {

//  def main(args: Array[String]): Unit = {
//    run()
//  }

  def run() = {
    import ParentWatcher._
    val system = ActorSystem("WatchingActors")
    val parentWatcherActor = system.actorOf(Props[ParentWatcher], "parentWatcher")
    parentWatcherActor ! StartChild("child3")
    val child3 = system.actorSelection("/user/parentWatcher/child3")
    // make sure that child3 has been created
    child3 ! s"[1] child3, are you still there?"
    Thread.sleep(1000)
    child3 ! s"[2] child3, are you still there?"
    child3 ! PoisonPill
    for (i <- 3 to 50) child3 ! s"[$i] child3, are you still there?"
  }

  // This is a better parent actor
  class ParentWatcher extends Actor with ActorLogging {

    import ParentWatcher._

    override def preStart(): Unit = {
      log.info("Starting ParentWatcher")
    }

    override def receive: Receive = {
      case StartChild(name) =>
        log.info(s"[ParentWatcher] Starting child $name")
        val child = context.actorOf(Props[Child], name)
        context.watch(child)
      case Terminated(actorRef) =>
        log.info(s"the reference that I am watching ${actorRef.path.name} has been stopped")
        log.info("the reference that I am watching has been stopped")
    }
  }

  class Child extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  object ParentWatcher {

    case class StartChild(name: String)

    case class StopChild(name: String)

    case object Stop

  }

}
