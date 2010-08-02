package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class NearestNeighborEvaluationPO<M extends IProbability & IConnectionContainer> extends AbstractHypothesisEvaluationPO<M> {

	public double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld, ArrayList<int[]> mesurementValuePathsNew, ArrayList<int[]> mesurementValuePathsOld) {
		return 0;
	}

	public void initAlgorithmParameter() {

	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new NearestNeighborEvaluationPO<M>(this);
	}

	public NearestNeighborEvaluationPO() {

	}

	public NearestNeighborEvaluationPO(NearestNeighborEvaluationPO<M> clone) {
		super(clone);
	}

}