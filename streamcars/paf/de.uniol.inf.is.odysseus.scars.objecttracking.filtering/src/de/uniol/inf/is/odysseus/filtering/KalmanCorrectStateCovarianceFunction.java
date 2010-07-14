package de.uniol.inf.is.odysseus.filtering;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateCovarianceFunction<M extends IProbability> implements ICorrectStateCovarianceFunction<M> {

	@Override
	public MVRelationalTuple<M> computeStateCovariance(MVRelationalTuple<M> Old,
			ArrayList<int[]> mesurementValuePathsTupleOld, Double[][] Gain, Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}

}
