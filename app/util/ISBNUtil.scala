package util

import com.google.api.services.books.Books
import com.google.api.services.books.model
import com.google.api.services.books.model.{Volume, Volumes}
import models.Book
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.duration._

import scala.collection.JavaConverters._

object ISBNUtil {

    val openLibraryReads: Reads[Book] = (
        (JsPath \ "title").read[String] and
            (JsPath \ "authors").read[List[Map[String, String]]].map(_.map(_ ("name")))
        ) (Book.apply _)

    def googleBooksLookup(isbn: String)(implicit ws: WSClient, config: Configuration): Future[Seq[Option[Book]]] = Future {
        GAPIUtil.books.volumes().list("isbn:" + isbn)
            .setKey(config.underlying.getString("google.books.apiKey"))
            .setMaxResults(40L)
            .setFields("items/volumeInfo")
            .setProjection("lite")
            .setShowPreorders(true)
            .execute().getItems.asScala.map(volume => {
            try {
                val volumeInfo = volume.getVolumeInfo
                Option(Book(volumeInfo.getTitle, volumeInfo.getAuthors.asScala.toList))
            } catch {
                case e: Exception => None
            }

        })
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

    def isbnLookup(isbn: String)(implicit ws: WSClient, config: Configuration): Future[Seq[Option[Book]]] = {
        for {
            googleBooks <- googleBooksLookup(isbn)
            openLibBooks <- openLibraryLookup(isbn)
        } yield {
            googleBooks :+ openLibBooks
        }
    }

}
