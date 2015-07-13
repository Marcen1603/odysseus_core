package de.uniol.inf.is.odysseus.parallelization.interoperator.postoptimization;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

public class PostOptimizationElement {
	private int degreeOfParallelization;
	private UnionAO startOperator;
	private AbstractStaticFragmentAO endOperator;
	private boolean allowsModificationAfterUnion;

	public AbstractStaticFragmentAO getEndOperator() {
		return endOperator;
	}

	public void setEndOperator(AbstractStaticFragmentAO endOperator) {
		this.endOperator = endOperator;
	}

	public UnionAO getStartOperator() {
		return startOperator;
	}

	public void setStartOperator(UnionAO startOperator) {
		this.startOperator = startOperator;
	}

	public int getDegreeOfParallelization() {
		return degreeOfParallelization;
	}

	public void setDegreeOfParallelization(int degreeOfParallelization) {
		this.degreeOfParallelization = degreeOfParallelization;
	}

	public void setAllowsModificationAfterUnion(
			boolean allowsModificationAfterUnion) {
		this.allowsModificationAfterUnion = allowsModificationAfterUnion;

	}

	public boolean allowsModificationAfterUnion() {
		return this.allowsModificationAfterUnion;
	}
}
