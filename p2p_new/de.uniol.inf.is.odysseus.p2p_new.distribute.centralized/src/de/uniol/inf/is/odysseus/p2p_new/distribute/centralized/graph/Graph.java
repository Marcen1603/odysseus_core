package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class Graph {
	Map<String,List<GraphNode>> nodesGroupedByOpType = new HashMap<String, List<GraphNode>>();
	
	public Graph(Map<Integer, IPhysicalOperator> oldPlan, Map<Integer, IPhysicalOperator> newPlan) {
		List<GraphNode> temp = new ArrayList<GraphNode>();
		for(Entry<Integer, IPhysicalOperator> e : oldPlan.entrySet()) {
			GraphNode gn = new GraphNode(e.getValue(), e.getKey(), true);
			temp.add(gn);
		}
		for(Entry<Integer, IPhysicalOperator> e : newPlan.entrySet()) {
			GraphNode gn = new GraphNode(e.getValue(), e.getKey(), false);
			temp.add(gn);
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
		this.createConnections();
	}
	
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

	@SuppressWarnings("unchecked")
	private void createConnections() {
		for(String opType : nodesGroupedByOpType.keySet()) {
			for(GraphNode gn : nodesGroupedByOpType.get(opType)) {
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
}
