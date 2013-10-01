package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class Graph {
	Map<String,List<GraphNode>> nodesGroupedByOpType = new HashMap<String, List<GraphNode>>();
	
	
	public Graph() {

	}
	
	/**
	 * 
	 * @param oldPlan running operators, which are considered old
	 * @param newPlan operators of a new query, not yet running and thus considered to be new
	 */
	public Graph(Map<Integer, IPhysicalOperator> oldPlan, Map<Integer, IPhysicalOperator> newPlan) {
		if(oldPlan == null && newPlan == null) {
			return;
		}
		List<GraphNode> temp = new ArrayList<GraphNode>();
		if(oldPlan != null) {
			for(Entry<Integer, IPhysicalOperator> e : oldPlan.entrySet()) {
				GraphNode gn = new GraphNode(e.getValue(), e.getKey(), true);
				temp.add(gn);
			}
		}
		if(newPlan != null) {
			for(Entry<Integer, IPhysicalOperator> e : newPlan.entrySet()) {
				GraphNode gn = new GraphNode(e.getValue(), e.getKey(), false);
				temp.add(gn);
			}
		}
		for(GraphNode gn : temp) {
			if(nodesGroupedByOpType.containsKey(gn.getOperatorType())) {
				nodesGroupedByOpType.get(gn.getOperatorType()).add(gn);
			} else {
				ArrayList<GraphNode> list = new ArrayList<GraphNode>();
				list.add(gn);
				nodesGroupedByOpType.put(gn.getOperatorType(), list);
			}
		}
		this.createConnections(nodesGroupedByOpType);
	}

	/**
	 * returns a new Graph based on the given one but deep-copies all the GraphNodes and their associations
	 * @param graph the graph to copy
	 */
	public Graph(Graph graph) {
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			List<GraphNode> oldGraphNodes = graph.getNodesGroupedByOpType().get(opType);
			List<GraphNode> newGraphNodes = new ArrayList<GraphNode>();
			for(GraphNode gn : oldGraphNodes) {
				newGraphNodes.add(gn.clone());
			}
			this.nodesGroupedByOpType.put(opType, newGraphNodes);
		}
		// now re-establish all the connections (can't do that earlier, since we need ALL the nodes in place)
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			List<GraphNode> oldGraphNodes = graph.getNodesGroupedByOpType().get(opType);
			for(GraphNode oldGn : oldGraphNodes) {
				// since we cloned every graphnode including its operatorID,
				// we should have the corresponding new GraphNode in the map already
				GraphNode newGn = this.getGraphNode(oldGn.getOperatorID());
				if(oldGn.isSink()) {
					for(ISubscription<GraphNode> sub : oldGn.getSubscribedToSource()) {
						GraphNode oldSource = sub.getTarget();
						GraphNode newSource = this.getGraphNode(oldSource.getOperatorID());
						int sinkInPort = sub.getSinkInPort();
						int sourceOutPort = sub.getSourceOutPort();
						SDFSchema schema = sub.getSchema();
						newGn.subscribeToSource(newSource, sinkInPort, sourceOutPort, schema.clone());
					}
				}
				if(oldGn.isSource()) {
					for(ISubscription<GraphNode> sub : oldGn.getSinkSubscriptions()) {
						GraphNode oldSink = sub.getTarget();
						GraphNode newSink = this.getGraphNode(oldSink.getOperatorID());
						int sinkInPort = sub.getSinkInPort();
						int sourceOutPort = sub.getSourceOutPort();
						SDFSchema schema = sub.getSchema();
						newGn.subscribeSink(newSink, sinkInPort, sourceOutPort, schema.clone());
					}
				}
			}
		}
	}
	
	/**
	 *  adds a new plan to the current graph
	 *  @Param opsAreNew specifies whether or not the nodes based on the provided operators should be marked as new or old
	 *  @return this Graph object including the added GraphNodes
	 */
	public Graph addPlan(Map<Integer,IPhysicalOperator> ops, boolean opsAreNew) {
		List<GraphNode> temp = new ArrayList<GraphNode>();
		for(Entry<Integer, IPhysicalOperator> e : ops.entrySet()) {
			GraphNode gn = new GraphNode(e.getValue(), e.getKey(), opsAreNew);
			temp.add(gn);
		}
		Map<String,List<GraphNode>> tempMap = new HashMap<String,List<GraphNode>>();
		for(GraphNode gn : temp) {
			if(tempMap.containsKey(gn.getOperatorType())) {
				tempMap.get(gn.getOperatorType()).add(gn);
			} else {
				ArrayList<GraphNode> list = new ArrayList<GraphNode>();
				list.add(gn);
				tempMap.put(gn.getOperatorType(), list);
			}
		}
		// only create the connections of the new nodes
		this.createConnections(tempMap);
		// then merge the results with this Graph, and we should have new set of interconnected GraphNodes
		for(String s : tempMap.keySet()) {
			if(this.nodesGroupedByOpType.containsKey(s)) {
				this.nodesGroupedByOpType.get(s).addAll(tempMap.get(s));
			} else {
				this.nodesGroupedByOpType.put(s, tempMap.get(s));
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	private void createConnections(Map<String,List<GraphNode>> nodes) {
		for(String opType : nodes.keySet()) {
			for(GraphNode gn : nodes.get(opType)) {
				// collect all the connections between the operators and connect their respective GraphNodes the same way
				IPhysicalOperator o = gn.getOperator();
				if(gn.isSink()) {
					for(ISubscription<IPhysicalOperator> sub : ((ISubscriber<?,? extends ISubscription<IPhysicalOperator>>)o).getSubscribedToSource()) {
						gn.subscribeToSource(this.getGraphNode(sub.getTarget()),sub.getSinkInPort(),sub.getSourceOutPort(),sub.getSchema());
					}
				}
				if(gn.isSource()) {
					for(ISubscription<IPhysicalOperator> sub : ((ISubscribable<?,? extends ISubscription<IPhysicalOperator>>)o).getSubscriptions()) {
						gn.subscribeSink(this.getGraphNode(sub.getTarget()),sub.getSinkInPort(),sub.getSourceOutPort(),sub.getSchema());
					}
				}
			}
		}
	}

	public GraphNode getGraphNode(IPhysicalOperator o) {
		for(String opType : nodesGroupedByOpType.keySet()) {
			for(GraphNode gn : nodesGroupedByOpType.get(opType)) {
				if(gn.getOperator().equals(o)) {
					return gn;
				}
			}
		}
		return null;
	}
	
	public GraphNode getGraphNode(int operatorID) {
		for(String opType : nodesGroupedByOpType.keySet()) {
			for(GraphNode gn : nodesGroupedByOpType.get(opType)) {
				if(gn.getOperatorID() == operatorID) {
					return gn;
				}
			}
		}
		return null;
	}

	public Map<String, List<GraphNode>> getNodesGroupedByOpType() {
		return nodesGroupedByOpType;
	}

	public void setNodesGroupedByOpType(
			Map<String, List<GraphNode>> nodesGroupedByOpType) {
		this.nodesGroupedByOpType = nodesGroupedByOpType;
	}
	
	public Graph clone() {
		return new Graph(this);
	}

	
	/**
	 * This method removes a node from the Graph, but doesn't touch the GraphNode-object or its associations with other GraphNodes
	 * Its subscriptions have thus to be handled separately!
	 * @param gn the GraphNode to delete from the Graph
	 */
	public void removeNode(GraphNode gn) {
		String opType = gn.getOperatorType();
		if(this.nodesGroupedByOpType.keySet().contains(opType)) {
			List<GraphNode> nodes = this.nodesGroupedByOpType.get(opType);
			nodes.remove(gn);
			// if the removed node was the last one of its kind, we can remove the entry
			if(nodes.isEmpty()) {
				this.nodesGroupedByOpType.remove(opType);
			}
		}
	}

	/**
	 * add an additonal node and specify another node in this graph which the new one should connect to
	 * @param node the GraphNode you want to add
	 * @param nodeToBeConnected the ID of the node the new GraphNode is supposed to be connected to
	 * @param connectNodeAsSource true, if the new node is supposed to be connected to the nodeToBeConnected as source,
	 * false if it should connect as a sink
	 */
	public void addAdditionalNode(GraphNode node, int nodeToBeConnected, boolean connectNodeAsSource, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		GraphNode other = this.getGraphNode(nodeToBeConnected);
		if(other == null) {
			System.out.println("Can't connect node " + node.getOperatorID() + ", because the target-node " + nodeToBeConnected + " doesn't exist in the graph.");			
			return;
		}
		if(this.getNodesGroupedByOpType().containsKey(node.getOperatorType())) {
			this.getNodesGroupedByOpType().get(node.getOperatorType()).add(node);
		} else {
			List<GraphNode> l = new ArrayList<GraphNode>();
			l.add(node);
			this.getNodesGroupedByOpType().put(node.getOperatorType(),l);
		}
		if(connectNodeAsSource) {
			other.subscribeToSource(node, sinkInPort, sourceOutPort, schema);
		} else {
			other.subscribeSink(node, sinkInPort, sourceOutPort, schema);
		}
	}
	public void addNode(GraphNode gn) {
		String type = gn.getOperatorType();
		if(this.nodesGroupedByOpType.containsKey(type)) {
			// only add, if it isn't in the Graph already
			if(this.getGraphNode(gn.getOperatorID()) == null) {
				nodesGroupedByOpType.get(type).add(gn);
			}
		} else {
			List<GraphNode> l = new ArrayList<GraphNode>();
			l.add(gn);
			nodesGroupedByOpType.put(type,l);
		}
	}

	/** This method will set the subscriptions of the underlying physical operator exactly as the
	 * layout of this Graph specifies. The old connections will be wiped beforehand, this should only be
	 * called when an optimised query will be made "ready for shipping", so right before the generation of a
	 * PhysicalQueryPartAdvertisement.
	 */ 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reconnectAssociatedOperatorsAccordingToGraphLayout(boolean leaveOldOnesAlone) {
		// unsubscribe all Operators from their sources/sinks
		for(IPhysicalOperator o : this.getAllOperatorsInvolved()) {
			if(o.isSink()) {
				((ISink<?>)o).unsubscribeFromAllSources();
			}
			if(o.isSource()) {
				((ISource<?>)o).unsubscribeFromAllSinks();
			}
		}
		// connect them according to this graph's connections
		for(GraphNode gn : this.getGraphNodesUngrouped(leaveOldOnesAlone)) {
			// Sink Subscriptions
			if(gn.isSource()) {
				for(Subscription<GraphNode> sub : gn.getSinkSubscriptions()) {
					GraphNode sinkNode = sub.getTarget();
					if(!leaveOldOnesAlone || !sinkNode.isOld()) {
						ISink sink = (ISink)sinkNode.getOperator();
						((ISource<?>)gn.getOperator()).subscribeSink(sink, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
					}
				}
			}
			// Source Subscriptions
			if(gn.isSink()) {
				for(Subscription<GraphNode> sub : gn.getSubscribedToSource()) {
					GraphNode sourceNode = sub.getTarget();
					if(!leaveOldOnesAlone || !sourceNode.isOld()) {
						ISource source = (ISource)sourceNode.getOperator();
						((ISink<?>)gn.getOperator()).subscribeToSource(source, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
					}
				}
			}
		}
	}
	
	public List<IPhysicalOperator> getAllOperatorsInvolved() {
		List<IPhysicalOperator> result = new ArrayList<IPhysicalOperator>();
		for(GraphNode gn : this.getGraphNodesUngrouped(false)) {
			result.add(gn.getOperator());
		}
		return result;
	}
	
	public List<GraphNode> getGraphNodesUngrouped(boolean onlyNew) {
		List<GraphNode> result = new ArrayList<GraphNode>();
		for(String s : this.getNodesGroupedByOpType().keySet()) {
			for(GraphNode gn : this.getNodesGroupedByOpType().get(s)) {
				if(!onlyNew || !gn.isOld()) {
					result.add(gn);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the Set of GraphNodes, which aren't connected to any other sinks, i.e. are at the top.
	 * This just returns pipes and sources though, since the point of collecting these is to put JxtaSenderPOs on top.
	 */
	public List<GraphNode> getSinkNodes() {
		List<GraphNode> result = new ArrayList<GraphNode>();
		for(GraphNode gn : this.getGraphNodesUngrouped(false)) {
			if(gn.isSource()) {
				result.add(gn);
			}
		}
		return result;
	}
}
