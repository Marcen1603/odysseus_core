package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class PostOptimizationElement {
	private int degreeOfParallelization;
	private UnionAO startOperator;
	private AbstractFragmentAO endOperator;
	private boolean allowsModificationAfterUnion;

	public AbstractFragmentAO getEndOperator() {
		return endOperator;
	}

	public void setEndOperator(AbstractFragmentAO endOperator) {
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
