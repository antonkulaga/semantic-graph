package org.denigma.graphs.ui
import org.scalajs.{dom, jquery}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSApp
import jquery.jQuery
import org.denigma.binding.extensions._

@JSExport
object ui  extends JSApp
{
  @JSExport
  def main(): Unit = {
    jQuery(".bottom.sidebar").dyn.sidebar("show")
  }
}