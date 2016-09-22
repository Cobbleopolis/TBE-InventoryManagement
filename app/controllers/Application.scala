package controllers

import javax.inject.Inject

import play.api.Mode
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSClient
import play.api.mvc._
import util.ISBNUtil


class Application @Inject()(implicit ws: WSClient, environment: play.api.Environment, config: play.api.Configuration) extends Controller {

    implicit val mode: Mode.Mode = environment.mode

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def lookupISBN(isbn: String) = Action.async {
        ISBNUtil.isbnLookup(isbn).map(books => {
            Ok(views.html.isbnLookup(books))
        })
    }

}