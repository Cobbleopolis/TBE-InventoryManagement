package controllers

import javax.inject.Inject

import models.Book
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class Application @Inject()(ws: WSClient) extends Controller {

    implicit val bookReads: Reads[Book] = (
        (JsPath \ "volumeInfo" \ "title").read[String] and
            (JsPath \ "volumeInfo" \ "authors").read[List[String]]
        ) (Book.apply _)

    implicit val bookWrites: Writes[Book] = Json.writes[Book]

    val gapiRequest = ws.url("https://www.googleapis.com/books/v1/volumes")
        .withHeaders("Accept" -> "application/json")
        .withRequestTimeout(10000.millis)

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def lookupISBN(isbn: String) = Action.async {
        gapiRequest.withQueryString("q" -> ("isbn:" + isbn)).get().map(response => {
            val books: List[Book] = (response.json \ "items").as[List[Book]]
            Ok(views.html.isbnLookup(books))
        })
    }

}