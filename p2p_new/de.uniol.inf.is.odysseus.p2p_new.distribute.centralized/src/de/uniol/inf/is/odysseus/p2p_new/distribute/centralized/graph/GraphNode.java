package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class GraphNode {
	private String operatorType = "";
	private int operatorID = -1;
	private IPhysicalOperator operator = null;
	private boolean isSink = false;
	private boolean isSource = false;
	private List<Subscription<GraphNode>> sinkSubscriptions = new ArrayList<Subscription<GraphNode>>();
	private List<Subscription<GraphNode>> sourceSubscriptions = new ArrayList<Subscription<GraphNode>>();
	private boolean isOld = true;

	public GraphNode(IPhysicalOperator o, int id, boolean isOld) {
		if(o != null) {
			this.setOperatorType(o.getClass().getName());
			this.setOperator(o);
			this.setOperatorID(id);
			this.setIsSink(o.isSink());
			this.setIsSource(o.isSource());
			this.setOld(isOld);
		}
	}

	public GraphNode(GraphNode other) {
		this.setIsSource(other.isSource());
		this.setIsSink(other.isSink());
		this.setOperator(other.getOperator());
		this.setOperatorID(other.getOperatorID());
		this.setOperatorType(other.operatorType);
		this.setOld(other.isOld);
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public IPhysicalOperator getOperator() {
		return operator;
	}

	public void setOperator(IPhysicalOperator operator) {
		this.operator = operator;
	}


	public void subscribeSink(GraphNode sink, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		Subscription<GraphNode> sub = new Subscription<GraphNode>(
				sink, sinkInPort, sourceOutPort, schema);
		if (!this.sinkSubscriptions.contains(sub)) {
			this.sinkSubscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
		}
	}

	public void unsubscribeSink(GraphNode sink, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		unsubscribeSink(new Subscription<GraphNode>(sink,
				sinkInPort, sourceOutPort, schema));
	}


	public void unsubscribeSink(Subscription<GraphNode> subscription) {
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeFromSource(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}

	}


	public Collection<Subscription<GraphNode>> getSinkSubscriptions() {
		return Collections.unmodifiableCollection(this.sinkSubscriptions);
	}

	public void subscribeToSource(GraphNode source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		Subscription<GraphNode> sub = new Subscription<GraphNode>(
				source, sinkInPort, sourceOutPort, schema);
		if (!this.sourceSubscriptions.contains(sub)) {
			this.sourceSubscriptions.add(sub);
			source.subscribeSink(this, sinkInPort, sourceOutPort, schema);
		}

	}

	public void unsubscribeFromSource(Subscription<GraphNode> subscription) {
		boolean subContained = this.sourceSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeSink(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	public void unsubscribeFromSource(GraphNode source, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		unsubscribeFromSource(new Subscription<GraphNode>(source,sinkInPort,sourceOutPort,schema));

	}


	public Collection<Subscription<GraphNode>> getSubscribedToSource() {
		return Collections.unmodifiableCollection(this.sourceSubscriptions);
	}

	public boolean isSink() {
		return isSink;
	}

	public boolean isSource() {
		return isSource;
	}

	public void setIsSink(boolean b) {
		isSink = b;
	}

	public void setIsSource(boolean b) {
		isSource = b;
	}

	public boolean isOld() {
		return isOld;
	}

	public void setOld(boolean isOld) {
		this.isOld = isOld;
	}

	public int getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(int operatorID) {
		this.operatorID = operatorID;
	}
	
	public GraphNode clone() {
		return new GraphNode(this);
	}
	
	/**
	 * Method to compare the inputs of two GraphNodes
	 * 
	 * @return true iff every source-subscription matches a subscription of another GraphNode
	 * ("match" in the sense of same target, same sourceOutPort, sinkInPort and schema). False otherwise.
	 *
	 * @param other another GraphNode, whose Source-connections should be compared to this one's
	 */
	public boolean hasSameSources(GraphNode other) {
		Collection<Subscription<GraphNode>> thisSubs = this.getSubscribedToSource();
		Collection<Subscription<GraphNode>> otherSubs = other.getSubscribedToSource();
		if (thisSubs.size() != otherSubs.size()) {
			return false;
		}
		for (Subscription<GraphNode> s1 : thisSubs) {
			boolean foundmatch = false;
			for (Subscription<GraphNode> s2 : otherSubs) {
				if (s1.equals(s2)) {
					foundmatch = true;
				}
			}
			if (!foundmatch) {
				return false;
			}
		}
		return true;
	}

	// retrieves length of the path from this node to a the farthest away Source-node
	public int getDepth() {
		if(this.getSubscribedToSource().isEmpty()) {
			return 0;
		} else {
			int highestDepth = -1;
			for(Subscription<GraphNode> sub : this.getSubscribedToSource()) {
				int depth = sub.getTarget().getDepth();
				if(depth > highestDepth) {
					highestDepth = depth;
				}
			}
			return highestDepth + 1;
		}
	}
	
	/**
	 *  returns a List of the IDs of every operator leading to this operator
	 */
	public List<Integer> getIDsOfAllSources() {
		List<Integer> result = new ArrayList<Integer>();
		result.add(this.getOperatorID());
		if(!this.getSubscribedToSource().isEmpty()) {
			for(Subscription<GraphNode> sub : this.getSubscribedToSource()) {
				result.addAll(sub.getTarget().getIDsOfAllSources());
			}
		}
		return result;
	}
	
	public List<GraphNode> collectConnectedGraphNodes(List<GraphNode> result) {
		if(result.contains(this)) {
			return result;
		}
		result.add(this);
		// collect all the sources
		if(!this.getSubscribedToSource().isEmpty()) {
			for(Subscription<GraphNode> sub : this.getSubscribedToSource()) {
				// only collect GraphNodes from Nodes which weren't already visited
				if(!result.contains(sub.getTarget())) {
					result.addAll(sub.getTarget().collectConnectedGraphNodes(result));
				}
			}
		}
		// collect all the sinks
		if(!this.getSinkSubscriptions().isEmpty()) {
			for(Subscription<GraphNode> sub : this.getSinkSubscriptions()) {
				// only collect GraphNodes from Nodes which weren't already visited
				if(!result.contains(sub.getTarget())) {
					result.addAll(sub.getTarget().collectConnectedGraphNodes(result));
				}
			}
		}
		return result;
	}
}
