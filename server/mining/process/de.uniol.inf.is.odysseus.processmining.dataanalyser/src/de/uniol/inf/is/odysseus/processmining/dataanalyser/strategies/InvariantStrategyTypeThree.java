package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.IInvariantResult;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantResultTypeThree;

/**
 * 
 * @author Philipp Geers
 *
 */
public class InvariantStrategyTypeThree<T extends IMetaAttribute> extends
		InvariantStategyBase<T> {

	Set<String> newNodes;
	Set<String> changedNodes;
	Set<String> removedNodes;

	public InvariantStrategyTypeThree() {
		super();
	}

	private void determineChangeAffectedNodes(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentGraph,
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentGraph) {
		Set<String> commonNodes = Sets.newHashSet();
		changedNodes = Sets.newHashSet();

		// Determine all new Nodes
		newNodes = Sets.newHashSet(currentGraph.vertexSet());
		newNodes.removeAll(mostRecentGraph.vertexSet());
		
		//Determine all removed Nodes
		removedNodes = Sets.newHashSet(mostRecentGraph.vertexSet());
		removedNodes.removeAll(currentGraph.vertexSet());
		// Determine the intersection of both nodesets
		commonNodes = Sets.newHashSet(mostRecentGraph.vertexSet());
		commonNodes.retainAll(currentGraph.vertexSet());

		for (String commonNode : commonNodes) {
			int currentEdges = currentGraph.incomingEdgesOf(commonNode).size() +
					currentGraph.outgoingEdgesOf(commonNode).size();
			int mostRecentEdges =mostRecentGraph.incomingEdgesOf(commonNode).size() +
					mostRecentGraph.outgoingEdgesOf(commonNode).size();
			if (currentEdges !=  mostRecentEdges) {
				changedNodes.add(commonNode);
			}
		}
	}

	@Override
	public IInvariantResult calculateStrategy(Tuple<T> mostRecent,
			Tuple<T> current) {
		updateData(mostRecent, current);

		DirectedWeightedPseudograph<String, DefaultWeightedEdge> currentGraph = getGraph(transferHelper
				.getDirectlyFollowRelations(currentModel));
		DirectedWeightedPseudograph<String, DefaultWeightedEdge> mostRecentGraph = getGraph(transferHelper
				.getDirectlyFollowRelations(mostRecentModel));

		determineChangeAffectedNodes(mostRecentGraph, currentGraph);
		InvariantResultTypeThree result = new InvariantResultTypeThree();
		result.setChangedNodes(changedNodes);
		result.setNewNodes(newNodes);
		result.setMissingNodes(removedNodes);
		return result;
	}
}
