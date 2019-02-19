package com.jdiegoreyesp.cqrsesakka.entity

import com.jdiegoreyesp.cqrsesakka.vo.{PostContent, PostId}

object BlogEntity {

    sealed trait BlogCommand

    final case class GetPost(id: PostId) extends BlogCommand
    final case class AddPost(content: PostContent) extends BlogCommand
    final case class UpdatePost(id: PostId, content: PostContent) extends BlogCommand

    sealed trait BlogEvent {
      val id: PostId
      val content: PostContent
    }

    final case class PostAdded(id: PostId, content: PostContent) extends BlogEvent
    final case class PostUpdated(id: PostId, content: PostContent) extends BlogEvent
    final case class PostNotFound(id: PostId) extends RuntimeException(s"Blog post not found with id $id")

    type MaybePost[+A] = Either[PostNotFound, A]

    final case class BlogState(posts: Map[PostId, PostContent]) {
      def apply(id: PostId): MaybePost[PostContent] = posts.get(id).toRight(PostNotFound(id))
      def +(event: BlogEvent): BlogState = BlogState(posts.updated(event.id, event.content))
    }

    object BlogState {
      def apply(): BlogState = BlogState(Map.empty)
    }

}
