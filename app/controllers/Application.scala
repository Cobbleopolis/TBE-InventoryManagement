package controllers

import javax.inject.Inject

import play.api.Mode
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSClient
import play.api.mvc._
import util.ISBNUtil


class Application @Inject()(implicit ws: WSClient, environment: play.api.Environment) extends Controller {

    implicit val mode: Mode.Mode = environment.mode

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def lookupISBN(isbn: String) = Action.async {
        ISBNUtil.googleBooksLookup(isbn).map(books => {
            if (books.nonEmpty)
                Ok(views.html.isbnLookup(books))
            else
                NotFound("No books were found with that ISBN")
        })
    }

}