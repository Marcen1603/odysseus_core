package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class KalmanCorrectStateEstimate<M extends IProbability> implements ICorrectStateEstimateFunction<M> {

	@Override
	public MVRelationalTuple<M> computeStateEstimate(MVRelationalTuple<M> Old,
			Object Gain, Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}

}
