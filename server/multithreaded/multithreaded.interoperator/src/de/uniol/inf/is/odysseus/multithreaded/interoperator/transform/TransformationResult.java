package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class TransformationResult {

	private UUID uniqueIdentifier = UUID.randomUUID();
	
	private List<AbstractFragmentAO> fragmentOperators = new ArrayList<AbstractFragmentAO>();
	private UnionAO unionOperator;

	// default is true, e.g. if partial aggregates are used, a modification
	// after union is not possible
	private boolean allowsModificationAfterUnion = true;

	public boolean allowsModificationAfterUnion() {
		return allowsModificationAfterUnion;
	}

	public void setAllowsModificationAfterUnion(
			boolean allowsModificationAfterUnion) {
		this.allowsModificationAfterUnion = allowsModificationAfterUnion;
	}

	public UnionAO getUnionOperator() {
		return unionOperator;
	}

	public void setUnionOperator(UnionAO unionOperator) {
		this.unionOperator = unionOperator;
	}

	public List<AbstractFragmentAO> getFragmentOperators() {
		return fragmentOperators;
	}

	public void setFragmentOperators(List<AbstractFragmentAO> fragmentOperators) {
		this.fragmentOperators.addAll(fragmentOperators);
	}

	public void addFragmentOperator(AbstractFragmentAO fragmentOperator) {
		this.fragmentOperators.add(fragmentOperator);
	}

	public UUID getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public boolean validate() {
		if (!fragmentOperators.isEmpty() && unionOperator != null){
			return true;
		}
		return false;
	}
}