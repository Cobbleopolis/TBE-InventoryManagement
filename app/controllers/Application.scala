package controllers

import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api.libs.ws.WSClient
import play.api.mvc._
import util.ISBNUtil


class Application @Inject()(implicit ws: WSClient) extends Controller {

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def lookupISBN(isbn: String) = Action.async {
        ISBNUtil.googleBooksLookup(isbn).map(books => {
            Ok(views.html.isbnLookup(books))
        })
    }

}