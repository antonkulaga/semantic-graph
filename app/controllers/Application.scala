package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action{implicit request=>

    Ok(
      views.html.index(request)
    )
  }

//  def javascriptRoutes = Action { implicit request =>
//    Ok(Routes.javascriptRouter("jsRoutes")(routes.javascript.MessageController.getMessage)).as(JAVASCRIPT)
//  }

}