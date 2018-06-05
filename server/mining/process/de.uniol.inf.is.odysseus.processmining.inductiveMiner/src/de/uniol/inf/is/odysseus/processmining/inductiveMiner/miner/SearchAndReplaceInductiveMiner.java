package de.uniol.inf.is.odysseus.processmining.inductiveMiner.miner;

import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantResultStrategyEnum;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantResultTypeThree;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies.InvariantStrategyTypeThree;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Cut;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.ProcessTreeModel;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Cutter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.PartitionUtils;

public class SearchAndReplaceInductiveMiner<T extends IMetaAttribute> extends
		AbstractInductiveMiner<T> {
	Set<String> changeAffectedNodes;
	Set<String> newNodes;
	Set<String> removedNodes;
	InvariantStrategyTypeThree<T> invariantAnalyser;
	InvariantResultTypeThree changes;
	Cut newCut;

	public SearchAndReplaceInductiveMiner() {
		super(GeneratingStrategy.PARTITION_COMPARISON);
		invariantAnalyser = new InvariantStrategyTypeThree<T>();
	}

	@Override
	public ProcessTreeModel generate() {
		// changes.print();
		if (changes.getStrategy().equals(InvariantResultStrategyEnum.UPDATE)) {
			replaceAffectedCut(getChangeAffectedCut());
			ProcessTreeModel pt = new ProcessTreeModel(
					this.mostRecentTreeModel.getProcessTreeMap(),
					this.currentsFirstPartition.getGraph());
			return pt;
		}
		// No changes detected
		return this.mostRecentTreeModel;

	}

	private Cut getChangeAffectedCut() {
		Set<Cut> cutsContainingAffectedNodes = Sets.newHashSet();
		Cut changeAffectedCut = null;
		changeAffectedNodes.addAll(removedNodes);

		// Determine all cuts which contain all affected nodes from the last
		// generated Model
		for (Cut cut : this.mostRecentTreeModel.getProcessTreeMap().keySet()) {
			if (cut.getAllNodes().containsAll(changeAffectedNodes)) {
				cutsContainingAffectedNodes.add(cut);
			}
		}

		// Determine the cut with the fewest additional nodes beside the
		// affected ones
		for (Cut cut : cutsContainingAffectedNodes) {
			if (changeAffectedCut == null) {
				changeAffectedCut = cut;
			} else if (cut.getAllNodes().size() < changeAffectedCut
					.getAllNodes().size()) {
				changeAffectedCut = cut;
			}
		}
		if (changeAffectedCut == null) {
			// rootCut
			return this.mostRecentTreeModel.getRootCut();
		}
		return changeAffectedCut;
	}

	/**
	 * Replaces the change affected cut with new possible cuts
	 * 
	 * @param cut
	 */
	private void replaceAffectedCut(Cut cut) {
		Multimap<Cut, Cut> mostRecentProcessTreeMap = this.mostRecentTreeModel
				.getProcessTreeMap();

		int changes = changeAffectedNodes.size() + newNodes.size()
				+ removedNodes.size();
		if (cut.equals(mostRecentTreeModel.getRootCut())
				|| changes > mostRecentTreeModel.getRootCut().getAllNodes()
						.size()) {
			// Full Rebuild, only rootCut is affected
			this.mostRecentTreeModel.setProcessTreeMap(Cutter
					.getCutMapOf(currentsFirstPartition));
			return;
		}
		// Continue with update
		Cut parentOfAffectedCut = getParentCut(cut);

		// Remove all childs of the cut
		removeSubtree(cut, mostRecentProcessTreeMap);
		if (!parentOfAffectedCut.equals(cut)) {

			mostRecentProcessTreeMap.remove(parentOfAffectedCut, cut);
			mostRecentProcessTreeMap.asMap().remove(cut);
		} else {
			mostRecentProcessTreeMap.asMap().remove(parentOfAffectedCut);
		}

		// create subtree partition map
		Partition replacement = getNewPartition(cut);
		Multimap<Cut, Cut> newCuts = Cutter.getCutMapOf(replacement);

		// add all new cuts
		if (mostRecentProcessTreeMap.isEmpty()) {
			mostRecentProcessTreeMap.putAll(newCuts);
			this.mostRecentTreeModel
					.setProcessTreeMap(mostRecentProcessTreeMap);
		} else {
			mostRecentProcessTreeMap.put(parentOfAffectedCut,
					getRootCutFromMap(newCuts, replacement));
			mostRecentProcessTreeMap.putAll(newCuts);
		}

		updateParentsPartitionList(replacement);
	}

	/**
	 * Update all parental nodes
	 * 
	 * @param replacement
	 */
	private void updateParentsPartitionList(Partition replacement) {

		// Alle Knoten die von der Änderung betroffen sind die neue Partition
		// hinzufügen bzw. für die
		// alte ersetzen oder entfernen
		boolean replaced = false;
		for (Cut keys : this.mostRecentTreeModel.getProcessTreeMap().keySet()) {
			if (keys.getAllNodes().size() > replacement.getGraph().vertexSet()
					.size()) {
				List<Integer> replaceIndices = Lists.newArrayList();
				for (Partition updateNeededParentPartition : keys
						.getCutPartitions()) {

					// Falls Partition eine Eltern Partition darstellt
					if (updateNeededParentPartition.getGraph().vertexSet()
							.containsAll(changeAffectedNodes)
							&& !(updateNeededParentPartition.getGraph()
									.vertexSet().equals(changeAffectedNodes))) {

						// Füge allen affected-Partitionen die neuen Knoten
						// hinzu
						for (String newNode : newNodes) {
							updateNeededParentPartition.getGraph().addVertex(
									newNode);
							for (DefaultWeightedEdge edge : replacement
									.getGraph().edgesOf(newNode)) {
								String src = replacement.getGraph()
										.getEdgeSource(edge);
								String target = replacement.getGraph()
										.getEdgeTarget(edge);
								if (updateNeededParentPartition.getGraph()
										.containsVertex(src)
										&& updateNeededParentPartition
												.getGraph().containsVertex(
														target)) {
									updateNeededParentPartition.getGraph()
											.addEdge(src, target, edge);
								}
							}
						}

						// entferne alle gelöschten knoten
						for (String remove : this.removedNodes) {
							updateNeededParentPartition
									.getGraph()
									.removeAllEdges(
											updateNeededParentPartition
													.getGraph().edgesOf(remove));
							updateNeededParentPartition.getGraph()
									.removeVertex(remove);
						}

						Set<String> onlyChanged = Sets
								.newHashSet(changeAffectedNodes);
						onlyChanged.removeAll(removedNodes);
						// Entferne alle Kanten der betroffenen Knoten
						for (String changed : onlyChanged) {
							updateNeededParentPartition.getGraph()
									.removeAllEdges(
											updateNeededParentPartition
													.getGraph()
													.edgesOf(changed));
						}
						// füge neue Kanten der betroffenen knoten hinzu
						for (String changed : onlyChanged) {
							for (DefaultWeightedEdge edge : replacement
									.getGraph().edgesOf(changed)) {
								String src = replacement.getGraph()
										.getEdgeSource(edge);
								String target = replacement.getGraph()
										.getEdgeTarget(edge);
								if (updateNeededParentPartition.getGraph()
										.containsVertex(src)
										&& updateNeededParentPartition
												.getGraph().containsVertex(
														target)) {
									updateNeededParentPartition.getGraph()
											.addEdge(src, target, edge);
								}
							}
						}

					} else if (updateNeededParentPartition.getGraph()
							.vertexSet().equals(changeAffectedNodes)) {
						replaceIndices.add(keys.getCutPartitions().indexOf(
								updateNeededParentPartition));
						replaced = true;
					}

				}
				for (int index : replaceIndices) {

					keys.getCutPartitions().set(index, replacement);
				}
			}
		}

		if (!replaced) {
			// Kein Update möglich, sonder Fall
			this.mostRecentTreeModel.setProcessTreeMap(Cutter
					.getCutMapOf(currentsFirstPartition));
			return;
		}
	}

	private Partition getNewPartition(Cut cut) {
		Set<String> nodes = cut.getAllNodes();
		nodes.addAll(newNodes);
		nodes.removeAll(removedNodes);
		Partition replacingPartition = PartitionUtils.createNewPartitionFrom(
				nodes, currentsFirstPartition);
		return replacingPartition;
	}

	private void removeSubtree(Cut parentCut, Multimap<Cut, Cut> processTreeMap) {
		if (processTreeMap.containsKey(parentCut)) {
			for (Cut child : processTreeMap.get(parentCut)) {
				removeSubtree(child, processTreeMap);
				processTreeMap.asMap().remove(child);
			}
		}
	}

	private Cut getRootCutFromMap(Multimap<Cut, Cut> newCuts,
			Partition replacingPartition) {
		for (Cut cut : newCuts.keySet()) {
			if (cut.getAllNodes().equals(
					replacingPartition.getGraph().vertexSet())) {
				return cut;
			}
		}
		return null;
	}

	private Cut getParentCut(Cut child) {
		if (mostRecentTreeModel.incomingEdgesOf(child).size() == 1) {
			for (DefaultEdge parentEdge : mostRecentTreeModel
					.incomingEdgesOf(child)) {
				return mostRecentTreeModel.getEdgeSource(parentEdge);
			}
		}
		return mostRecentTreeModel.getRootCut();
	}

	@Override
	public void updateData(ProcessTreeModel mostRecentModell,
			Tuple<T> mostRecentTuple, Tuple<T> currentTuple) {
		updateData(mostRecentModell, currentTuple);
		changes = (InvariantResultTypeThree) invariantAnalyser
				.calculateStrategy(mostRecentTuple, currentTuple);

		this.changeAffectedNodes = changes.getChangedNodes();
		this.newNodes = changes.getNewNodes();
		this.removedNodes = changes.getMissingNodes();
	}
}
