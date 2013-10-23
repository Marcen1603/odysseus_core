package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

public class GraphNodeSwapper {
	private static final Logger LOG = LoggerFactory.getLogger(GraphNodeSwapper.class);
	
	public static Graph pullSelectionsAboveWindows(Graph g) {
		Collection<Pair<GraphNode,GraphNode>> eligiblePairs = eligiblePairs(g, SlidingAdvanceTimeWindowTIPO.class, SelectPO.class);
		if(eligiblePairs.isEmpty()) {
			return null;
		} else {
			Graph graphCopy = g.clone();
			for(Pair<GraphNode,GraphNode> pair : eligiblePairs) {
				// since we found the eligible pairs in the old graph but are now working on a copy of the graph,
				// we have to get the corresponding Nodes of this new graph via their associated IDs
				GraphNode windowNode = graphCopy.getGraphNode(pair.getE1().getOperatorID());
				GraphNode selectNode = graphCopy.getGraphNode(pair.getE2().getOperatorID());
				switchUnaryGraphNodes(windowNode, selectNode);
			}
			return graphCopy;
		}
	}
	
	public static Graph pullSelectionsAboveJoins(Graph g) {
		Collection<Pair<GraphNode,GraphNode>> eligiblePairs = eligiblePairs(g, JoinTIPO.class, SelectPO.class);
		List<Pair<GraphNode,GraphNode>> toRemove = new ArrayList<Pair<GraphNode,GraphNode>>();
		for(Pair<GraphNode,GraphNode> pair : eligiblePairs) {
			// the SelectPO's subscription to the JoinTIPO has to be its ONLY sink-subscription, since we can't swap otherwise
			if(pair.getE2().getSinkSubscriptions().size() > 1) {
				toRemove.add(pair);
			}
		}
		for(Pair<GraphNode,GraphNode> removed: toRemove) {
			eligiblePairs.remove(removed);
		}
		if(eligiblePairs.isEmpty()) {
			return null;
		} else {
			Graph graphCopy = g.clone();
			for(Pair<GraphNode,GraphNode> pair : eligiblePairs) {
				// since we found the eligible pairs in the old graph but are now working on a copy of the graph,
				// we have to get the corresponding Nodes of this new graph via their associated IDs
				GraphNode joinNode = graphCopy.getGraphNode(pair.getE1().getOperatorID());
				GraphNode selectNode = graphCopy.getGraphNode(pair.getE2().getOperatorID());
				if(selectNode.getSinkSubscriptions().size() == 1) {
					switchUnarySourceNodeWithBinarySink(joinNode, selectNode);
				}
			}
			return graphCopy;
		}
	}
	
	/**
	 * Finds out, if the given graph has instances of typeOfSink,
	 * which have sources of typeOfSource and return them as pairs in a collection
	 * Only new GraphNodes can be switched with one another, old ones are being left alone.
	 */
	private static Collection<Pair<GraphNode,GraphNode>> eligiblePairs(Graph g, Class<?> typeOfSink, Class<?> typeOfSource) {
		Collection<Pair<GraphNode,GraphNode>> eligiblePairs = new TreeSet<Pair<GraphNode,GraphNode>>();
		Collection<GraphNode> matchingSinks = g.getNodesGroupedByOpType().get(typeOfSink.getName());
		Collection<GraphNode> matchingSources = g.getNodesGroupedByOpType().get(typeOfSource.getName());

		if(matchingSinks != null && matchingSources != null) {
			for(GraphNode gn : matchingSinks) {

				if(!gn.isOld()) {
					continue;
				}
				if(gn.getSubscribedToSource().isEmpty() || gn.getSinkSubscriptions().isEmpty()) {
					continue;
				}
				for(Subscription<GraphNode> sub : gn.getSubscribedToSource()) {
					if(matchingSources.contains(sub.getTarget()) && !sub.getTarget().isOld()) {
						eligiblePairs.add(new Pair<GraphNode,GraphNode>(gn,sub.getTarget()));
					}
				}
			}
		}
		return eligiblePairs;
	}
	
	// switches two GraphNodes, which must be connected with one another and have only one source- and sinksubscription each
	private static void switchUnaryGraphNodes(GraphNode originalSink, GraphNode originalSource) {
		LOG.debug("Swapping GraphNode of type " + originalSink.getOperatorType() + " with GraphNode of type " + originalSource.getOperatorType());
		// disconnect the given sink from the given source
		originalSource.unsubscribeSink(originalSource.getSinkSubscriptions().iterator().next());

		// save the source-subscription of the source-node and disconnect it
		Subscription<GraphNode> sourceSubOfOriginalSource = originalSource.getSubscribedToSource().iterator().next();
		originalSource.unsubscribeFromSource(sourceSubOfOriginalSource);

		// save the sink-subscription of the sink-node and disconnect it
		Subscription<GraphNode> sinkSubFromOriginalSink = originalSink.getSinkSubscriptions().iterator().next();
		originalSink.unsubscribeSink(sinkSubFromOriginalSink);

		// connect the sink to the original source's source
		originalSink.subscribeToSource(sourceSubOfOriginalSource.getTarget(), 0,
				sourceSubOfOriginalSource.getSourceOutPort(), sourceSubOfOriginalSource.getSchema());
		
		// connect the original sink to the original source, which is the new sink
		originalSink.subscribeSink(originalSource, 0, 0, originalSink.getOperator().getOutputSchema());

		// connect the original source, i.e. the new sink to the original sink's sink
		originalSource.subscribeSink(sinkSubFromOriginalSink.getTarget(), sinkSubFromOriginalSink.getSinkInPort(), 0,
				originalSource.getOperator().getOutputSchema());
	}
	
	private static void switchUnarySourceNodeWithBinarySink(GraphNode originalSink, GraphNode originalSource) {
		LOG.debug("Swapping GraphNode of type " + originalSink.getOperatorType() + " with GraphNode of type " + originalSource.getOperatorType());
		int sinkInPort = originalSource.getSinkSubscriptions().iterator().next().getSinkInPort();
		// disconnect the given sink from the given source, the source can only have one sinksubscription, this was checked earlier
		originalSource.unsubscribeSink(originalSource.getSinkSubscriptions().iterator().next());

		// save the source-subscription of the source-node and disconnect it
		Subscription<GraphNode> sourceSubOfOriginalSource = originalSource.getSubscribedToSource().iterator().next();
		originalSource.unsubscribeFromSource(sourceSubOfOriginalSource);

		// save the sink-subscriptions of the sink-node and disconnect it
		Collection<Subscription<GraphNode>> sinkSubsFromOriginalSink = originalSink.getSinkSubscriptions();
		for(Subscription<GraphNode> sinkSub : sinkSubsFromOriginalSink) {
			originalSink.unsubscribeSink(sinkSub);
		}

		// connect the sink to the original source's source and take care to use the proper sinkInPort
		originalSink.subscribeToSource(sourceSubOfOriginalSource.getTarget(), sinkInPort,
				sourceSubOfOriginalSource.getSourceOutPort(), sourceSubOfOriginalSource.getSchema());
		
		// connect the original sink to the original source, which is the new sink
		originalSink.subscribeSink(originalSource, 0, 0, originalSink.getOperator().getOutputSchema());

		// connect the original source, i.e. the new sink to the original sink's sinks
		for(Subscription<GraphNode> sinkSub : sinkSubsFromOriginalSink) {
			originalSource.subscribeSink(sinkSub.getTarget(), sinkSub.getSinkInPort(), 0,sinkSub.getSchema());
		}
				
	}

	
}
