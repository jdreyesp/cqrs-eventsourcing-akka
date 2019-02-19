package com.jdiegoreyesp.cqrsesakka.vo

import java.util.UUID

import io.circe.{Decoder, Encoder}

class PostId(val id: UUID) extends AnyVal {
  override def toString: String = id.toString
}

object PostId {

  def apply(): PostId = new PostId(UUID.randomUUID())
  def apply(id: UUID) : PostId = new PostId(id)

  implicit val postIdDecoder : Decoder[PostId] = Decoder.decodeUUID.map(PostId(_))
  implicit val postIdEncoder : Encoder[PostId] = Encoder.encodeUUID.contramap(_.id)
}