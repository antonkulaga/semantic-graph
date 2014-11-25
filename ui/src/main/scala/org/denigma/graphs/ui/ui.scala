package org.denigma.graphs.ui

import org.denigma.binding.views.BindableView
import org.denigma.binding.views.utils.ViewInjector
import org.denigma.graphs.ui.views.{MainView, GraphSlide}
import org.scalajs.dom
import org.scalajs.dom.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.util.Try
import org.scalajs.jquery.jQuery

@JSExport("launcher")
object ui  extends  JSApp
{

  var mainView:MainView = null

  @JSExport
  def main(): Unit = {



    jQuery(dom.document).ready(this.onReady _)
    //jQuery(".top.sidebar").dyn.sidebar(sidebarParams).sidebar("show")
    //jQuery(".left.sidebar").dyn.sidebar(sidebarParams).sidebar("show")
  }

  def onReady() = {
    this.mainView = new MainView(dom.document.body)
    this.mainView.bindView(dom.document.body)
  }

}