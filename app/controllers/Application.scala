package controllers

import play.api.mvc.{Controller, AnyContent, Action}

object Application extends Controller {


  def index(): Action[AnyContent] =  Action {
    implicit request=>
      Ok(views.html.index(request))
  }

}