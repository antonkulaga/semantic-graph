package controllers


import controllers.graph.GSEA
import controllers.graph.GSEA.{Edge, Node}
import org.jboss.netty.handler.codec.http.HttpRequest
import play.api.{Logger, Play}
import play.api.Play.current
import controllers.Application._
import controllers.graph.GSEA
import controllers.graph.GSEA.{Edge, Node}
import org.denigma.binding.messages.GraphMessages
import org.denigma.binding.picklers.rp
import org.denigma.binding.play.{AuthRequest, PickleController, UserAction}
import org.scalajs.spickling.playjson._
import org.scalax.semweb.rdf._
import org.scalax.semweb.rdf.vocabulary.WI
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc.{RequestHeader, Controller, Result}
import play.mvc.Http

import scala.concurrent.Future
import scala.io.Source
import scala.util.Random
import scalax.collection.edge.Implicits._
import scalax.collection.edge.LDiEdge
import scalax.collection.immutable.Graph // shortcuts
/**
 * Graph controller
 */
object GraphController extends Controller with PickleController
{
  protected var graph:Option[ Graph[Res, LDiEdge]] = None


  def readFrom(path:String)(implicit request:RequestHeader) = {
    val url = routes.Assets.at(path).absoluteURL(secure = false)(request)
    Source.fromURL(url).getLines().reduce(_ + _)
  }

  def parseGraph(implicit request:RequestHeader): (Seq[Node], Seq[Edge]) = {
    val json: JsValue = Json.parse(this.readFrom("/data/cellular.json"))
    val ns = (json \ "nodes").as[JsArray]
    val es = (json \ "edges").as[JsArray]
    val edges = es.value.map(e => e.as[GSEA.Edge])
    val nodes = ns.value.map(v => v.as[GSEA.Node])
    (nodes,edges)
  }

  protected def initGraph(implicit request:GraphRequest) = {
    lazy val (nodes,edges) = parseGraph

    lazy val semNodes: Map[Int, Res] = nodes.map(n=>n.id->vocabulary.WI.re(n.name.replace(" ","_"))).toMap[Int,Res]

    lazy val semEdges = for{ e <- edges if semNodes.contains(e.from) && semNodes.contains(e.to)  } yield  (semNodes(e.from) ~+>  semNodes(e.to) ) ( WI.re(e.overlaps.toString) )

    play.Logger.error("READY edges = "+semEdges.toString)

    Graph(semEdges:_*)
  }



  type GraphRequest = AuthRequest[GraphMessages.GraphMessage]

  type GraphResult = Future[Result]


  def graphEndpoint = UserAction.async(this.unpickle[GraphMessages.GraphMessage]()){implicit request=>
    this.onGraphMessage(request.body)
  }

//  def traverse(exp:GraphMessages.NodeExplore) = {
//    val n: graph.NodeT = this.graph.get(exp.resource)
//    val quads = graph.edges.collect{
//      case e if e.from==n
//      =>
//        Quad(exp.resource,e.label.asInstanceOf[IRI],e.to.value.asInstanceOf[IRI],IRI(WI.RESOURCE))
//      case e if e.to==n
//      =>
//        Quad(e.from.value.asInstanceOf[IRI],e.label.asInstanceOf[IRI],exp.resource,IRI(WI.RESOURCE))
//    }
//
//    Future.successful(Ok(rp.pickle(quads.toList)).as("application/json"))
//  }

  def onGraphMessage(message:GraphMessages.GraphMessage)(implicit request:GraphRequest):GraphResult = message match {

    case exp:GraphMessages.NodeExplore=>
      if(graph.isEmpty) graph = Some(this.initGraph)

      val quads = this.graph.map{
        case g=>    g.edges.collect{
            case e   =>  Quad(e.from,e.label.asInstanceOf[IRI],e.to.value.asInstanceOf[IRI],IRI(WI.RESOURCE))
          }
      }.get


      Future.successful(Ok(rp.pickle(quads.toList)).as("application/json"))

    case other=>onBadGraphMessage(message)
  }


  def onBadGraphMessage(message: GraphMessages.GraphMessage)(implicit request: GraphRequest): GraphResult = Future.successful(BadRequest(Json.obj("status" ->"KO","message"->"wrong message type!")).as("application/json"))







}