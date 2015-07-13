package de.uniol.inf.is.odysseus.parallelization.interoperator.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

public class TransformationResult {
	
	public enum State{
		SUCCESS, FAILED;
	}

	private UUID uniqueIdentifier = UUID.randomUUID();
	private State state;
	private List<AbstractStaticFragmentAO> fragmentOperators = new ArrayList<AbstractStaticFragmentAO>();
	private UnionAO unionOperator;

	public TransformationResult(State state){
		this.state = state;
	}
	
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

	public List<AbstractStaticFragmentAO> getFragmentOperators() {
		return fragmentOperators;
	}

	public void setFragmentOperators(List<AbstractStaticFragmentAO> fragmentOperators) {
		this.fragmentOperators.addAll(fragmentOperators);
	}

	public void addFragmentOperator(AbstractStaticFragmentAO fragmentOperator) {
		this.fragmentOperators.add(fragmentOperator);
	}

	public UUID getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public boolean validate() {
		if (!fragmentOperators.isEmpty() && unionOperator != null && state != null){
			return true;
		}
		return false;
	}

	public State getState() {
		return state;
	}
}
