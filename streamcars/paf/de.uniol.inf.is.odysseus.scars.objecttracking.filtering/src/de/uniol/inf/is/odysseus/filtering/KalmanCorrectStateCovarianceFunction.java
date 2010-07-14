package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class KalmanCorrectStateCovarianceFunction<M extends IProbability> implements ICorrectStateCovarianceFunction<M> {

	@Override
	public MVRelationalTuple<M> computeStateCovariance(MVRelationalTuple<M> Old,
			Object Gain, Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}

}
