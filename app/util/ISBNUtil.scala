package util

import models.Book
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import scala.concurrent.duration._

object ISBNUtil {

    val googleBooksReads: Reads[Book] = (
        (JsPath \ "title").read[String] and
            (JsPath \ "authors").read[List[String]]
        ) (Book.apply _)

    def googleBooksLookup(isbn: String)(implicit ws: WSClient): Future[Seq[Book]] = {
        ws.url("https://www.googleapis.com/books/v1/volumes")
            .withHeaders("Accept" -> "application/json")
            .withRequestTimeout(10000.millis)
            .withQueryString("q" -> ("isbn:" + isbn)).get().map(response =>
            (response.json \\ "volumeInfo").map(jsVal => jsVal.as[Book](googleBooksReads))
        )
    }

}
