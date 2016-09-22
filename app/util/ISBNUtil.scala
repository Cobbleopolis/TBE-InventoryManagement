package util

import models.Book
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.duration._

object ISBNUtil {

    val googleBooksReads: Reads[Book] = (
        (JsPath \ "title").read[String] and
            (JsPath \ "authors").read[List[String]]
        ) (Book.apply _)

    val openLibraryReads: Reads[Book] = (
        (JsPath \ "title").read[String] and
            (JsPath \ "authors").read[List[Map[String, String]]].map(_.map(_ ("name")))
        ) (Book.apply _)

    def googleBooksLookup(isbn: String)(implicit ws: WSClient): Future[Seq[Option[Book]]] = {
        ws.url("https://www.googleapis.com/books/v1/volumes")
            .withHeaders("Accept" -> "application/json")
            .withRequestTimeout(10000.millis)
            .withQueryString("q" -> ("isbn:" + isbn)).get().map(response =>
            (response.json \\ "volumeInfo").map(jsVal => jsVal.asOpt[Book](googleBooksReads))
        )
    }

    def openLibraryLookup(isbn: String)(implicit ws: WSClient): Future[Option[Book]] = {
        ws.url("https://openlibrary.org/api/books")
            .withHeaders("Accept" -> "application/json")
            .withRequestTimeout(10000.millis)
            .withQueryString("format" -> "json")
            .withQueryString("jscmd" -> "data")
            .withQueryString("bibkeys" -> ("ISBN:" + isbn)).get().map(response =>
                (response.json \ ("ISBN:" + isbn)).asOpt[Book](openLibraryReads)
        )
    }

    def isbnLookup(isbn: String)(implicit ws: WSClient): Future[Seq[Option[Book]]] = {
        for {
            googleBooks <- googleBooksLookup(isbn)
            openLibBooks <- openLibraryLookup(isbn)
        } yield {
            googleBooks :+ openLibBooks
        }
    }

}
