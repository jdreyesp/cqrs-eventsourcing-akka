package com.jdiegoreyesp.cqrsesakka.repository

import akka.pattern.pipe
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.jdiegoreyesp.cqrsesakka.vo.PostId

import scala.concurrent.{Future, Promise}

class BlogRepositoryActor extends PersistentActor {

  import com.jdiegoreyesp.cqrsesakka.entity.BlogEntity._
  import context._

  private var state = BlogState()

  override def persistenceId: String = "blog"

  override def receiveCommand: Receive = {
    case GetPost(id) => sender() ! state(id)
    case AddPost(content) =>
      handleEvent(PostAdded(PostId(), content)) pipeTo sender()
      ()
    case UpdatePost(id, content) =>
      state(id) match {
        case response @ Left(_) => sender() ! response
        case Right(_) =>
          handleEvent(PostUpdated(id, content)) pipeTo sender()
          ()
      }
  }

  private def handleEvent[E <: BlogEvent](e: => E): Future[E] = {
    val p = Promise[E]
    persist(e) { event =>
      p.success(event)
      state += event
      system.eventStream.publish(event)
      if (lastSequenceNr != 0 && lastSequenceNr % 1000 == 0)
        saveSnapshot(state)
    }
    p.future
  }

  override def receiveRecover: Receive = {
    case event : BlogEvent => state += event
    case SnapshotOffer(_, snapshot: BlogState) => state = snapshot
  }

}
