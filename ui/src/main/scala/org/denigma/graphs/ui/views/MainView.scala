package org.denigma.graphs.ui.views

import org.denigma.binding.views.BindableView
import org.denigma.binding.views.utils.ViewInjector
import org.denigma.graphs.ui.ui._
import org.scalajs.dom
import org.scalajs.dom.HTMLElement
import org.scalajs.jquery._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Try
import org.denigma.binding.extensions._

class MainView(val elem:HTMLElement) extends BindableView{

  override def name = "main"

  override val params:Map[String,Any] = Map.empty

  val sidebarParams =  js.Dynamic.literal(exclusive = false, dimPage = false)

  ViewInjector.register("GraphSlide", (el,params)=>Try(new GraphSlide(el,params)))

  def attachBinders() = {this.binders = BindableView.defaultBinders(this)}

  override def activateMacro(): Unit = {
    extractors.foreach(_.extractEverything(this))
  }

  override def bindView(el:HTMLElement) = {
    super.bindView(el)
    val sb = jQuery(".left.sidebar")
    sb.dyn.sidebar(sidebarParams).sidebar("show")
    sb.dyn.dimmer("hide")
  }

}
