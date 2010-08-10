package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;

//import wekaNew.classifiers.lazy.IB1;
//import wekaNew.core.Attribute;
//import wekaNew.core.Instance;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class NearestNeighborEvaluationPO<M extends IProbability & IConnectionContainer> extends AbstractHypothesisEvaluationPO<M> {

//	private weka.core.neighboursearch.LinearNNSearch nn;

	public double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld, ArrayList<int[]> mesurementValuePathsNew, ArrayList<int[]> mesurementValuePathsOld) {
//		IB1 nn = new IB1();
//		Instance inst = new Instance(3);
//
//		Attribute att = new Attribute("speed");
//
//
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