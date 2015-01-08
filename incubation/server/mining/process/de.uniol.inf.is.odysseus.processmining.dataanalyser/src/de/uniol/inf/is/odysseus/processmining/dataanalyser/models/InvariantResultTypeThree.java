package de.uniol.inf.is.odysseus.processmining.dataanalyser.models;

import java.util.Set;

import com.google.common.collect.Sets;

public class InvariantResultTypeThree implements IInvariantResult {
	Set<String> changedNodes;
	Set<String> newNodes;
	Set<String> missingNodes;
	StringBuilder sb = new StringBuilder();

	public InvariantResultTypeThree() {
	}

	public Set<String> getChangedNodes() {
		return changedNodes;
	}

	public void setChangedNodes(Set<String> changedNodes) {
		this.changedNodes = changedNodes;
	}

	public Set<String> getNewNodes() {
		return newNodes;
	}

	public void setNewNodes(Set<String> newNodes) {
		this.newNodes = newNodes;
	}

	public Set<String> getMissingNodes() {
		return missingNodes;
	}

	public void setMissingNodes(Set<String> missingNodes) {
		this.missingNodes = missingNodes;
	}

	@Override
	public InvariantResultStrategyEnum getStrategy() {
		Set<String> allAffectedNodes = Sets.newHashSet(this.changedNodes);
		allAffectedNodes.addAll(this.getNewNodes());
		if (!allAffectedNodes.isEmpty()) {
			return InvariantResultStrategyEnum.UPDATE;
		}

		return InvariantResultStrategyEnum.DELAY;
	}

	@Override
	public void print() {
		sb.append("STRATEGY: " + getStrategy() + "\n");
		sb.append("AFFECTED AND CHANGED NODES: ");
		if (changedNodes != null && newNodes != null) {
			sb.append("NODES: " + changedNodes + " NEW NODES: " + newNodes + " MISS: "+missingNodes);
		} else {
			sb.append("NULL");
		}

		System.out.println(sb.toString());

	}
}
