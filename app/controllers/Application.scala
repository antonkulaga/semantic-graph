package controllers

import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.io.Source


object Application extends Controller {


  def index(): Action[AnyContent] =  Action {
    implicit request=>
      Ok(views.html.index(request))
  }





}