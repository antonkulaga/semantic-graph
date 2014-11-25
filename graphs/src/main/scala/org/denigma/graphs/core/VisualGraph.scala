package org.denigma.graphs.core

import org.denigma.graphs.layouts.GraphLayout

trait VisualGraph extends GraphTypes
{
  self=>


  var nodes = Map.empty[NodeId,Node]
  var edges = Map.empty[EdgeId,Edge]



  def addNode(id:NodeId,data:NodeData, view:ViewOfNode):Node

  def addEdge(id:EdgeId,from:Node,to:Node, data:EdgeData,view:ViewOfEdge):Edge


  def removeEdge(id:EdgeId): this.type = edges.get(id) match {
    case Some(e)=>   edges = edges - id; this
    case None=> throw new Exception("node that should be removed is not found"); this
  }

  def removeNode(id:NodeId):this.type = nodes.get(id) match {
    case Some(n)=> nodes = nodes - id; this
    case None => throw new Exception("node that should be removed is not found"); this
  }


  type MyLayout = GraphLayout{
      type Node >: self.Node
      type Edge >: self.Edge
  }

  var layouts:List[MyLayout] = List.empty


  def addLayout(layout:MyLayout,activate:Boolean = false) = {
    this.layouts = layout::layouts
    if(activate)layout.start(nodes.values.toList,edges.values.toList)
  }


}
