package com.jdiegoreyesp.cqrsesakka.service

import akka.actor.Props
import akka.pattern.ask
import com.jdiegoreyesp.cqrsesakka.akka.AkkaConfiguration
import com.jdiegoreyesp.cqrsesakka.vo.{PostContent, PostId}
import com.jdiegoreyesp.cqrsesakka.repository.BlogRepositoryActor

import scala.concurrent.Future

trait BlogService extends AkkaConfiguration {

  import com.jdiegoreyesp.cqrsesakka.entity.BlogEntity._

  private val blogEntity = actorRefFactory.actorOf(Props[BlogRepositoryActor])

  def getPost(id: PostId): Future[MaybePost[PostContent]] =
    (blogEntity ? GetPost(id)).mapTo[MaybePost[PostContent]]
  def addPost(content: PostContent): Future[PostAdded] =
    (blogEntity ? AddPost(content)).mapTo[PostAdded]
  def updatePost(id: PostId, content: PostContent) : Future[MaybePost[PostUpdated]] =
    (blogEntity ? UpdatePost(id, content)).mapTo[MaybePost[PostUpdated]]
}
