package util

import models.Book
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import scala.concurrent.duration._

object ISBNUtil {

    implicit val googleBooksReads: Reads[Book] = (
        (JsPath \ "volumeInfo" \ "title").read[String] and
            (JsPath \ "volumeInfo" \ "authors").read[List[String]]
        ) (Book.apply _)

    def googleBooksLookup(isbn: String)(implicit ws: WSClient): Future[List[Book]] = {
        val gapiRequest = ws.url("https://www.googleapis.com/books/v1/volumes")
            .withHeaders("Accept" -> "application/json")
            .withRequestTimeout(10000.millis)
        gapiRequest.withQueryString("q" -> ("isbn:" + isbn)).get().map(response => (response.json \ "items").as[List[Book]])
    }

}
