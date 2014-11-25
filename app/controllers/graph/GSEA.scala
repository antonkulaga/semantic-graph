package controllers.graph

import org.scalax.semweb.rdf.IRI
import play.api.libs.json._
import play.api.libs.functional.syntax._

object GSEA {


  implicit val nodeReads: Reads[Node] = (
    (JsPath \ "id").read[String].map(_.toInt) and
      (JsPath \ "name").read[String] and
      (JsPath \ "size").read[String].map(_.toInt)
    )(Node.apply _)

  implicit val edgesReads: Reads[Edge] = (
    (JsPath \ "from").read[Int] and
      (JsPath \ "to").read[Int] and
      (JsPath \ "overlaps").read[Int]
    )(Edge.apply _)

  case class Node(id:Int,name:String,size:Int)

  case class Edge(from:Int,to:Int,overlaps:Int)

}
