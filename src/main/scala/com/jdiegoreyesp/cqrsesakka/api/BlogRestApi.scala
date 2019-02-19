package com.jdiegoreyesp.cqrsesakka.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.jdiegoreyesp.cqrsesakka.vo.{PostContent, PostId}
import com.jdiegoreyesp.cqrsesakka.service.BlogService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.AutoDerivation

trait JsonSupport extends FailFastCirceSupport with AutoDerivation

trait RestApi extends Directives with JsonSupport {
  def route: Route
}

trait BlogRestApi extends RestApi with BlogService {
  override def route: Route =
    pathPrefix("api" / "blog") {
      (pathEndOrSingleSlash & post) {
        // POST /api/blog/
        entity(as[PostContent]) { content =>
          onSuccess(addPost(content)) { added =>
            complete((StatusCodes.Created, added))
          }
        }
      } ~
        pathPrefix(JavaUUID.map(PostId(_))) { id =>
          pathEndOrSingleSlash {
            get {
              // GET /api/blog/:id
              onSuccess(getPost(id)) {
                case Right(content) => complete((StatusCodes.OK, content))
                case Left(error) => complete((StatusCodes.NotFound, error))
              }
            } ~
              put {
                // PUT /api/blog/:id
                entity(as[PostContent]) { content =>
                  onSuccess(updatePost(id, content)) {
                    case Right(updated) => complete((StatusCodes.OK, updated))
                    case Left(error) => complete((StatusCodes.NotFound, error))
                  }
                }
              }
          }
        }
    }
}
