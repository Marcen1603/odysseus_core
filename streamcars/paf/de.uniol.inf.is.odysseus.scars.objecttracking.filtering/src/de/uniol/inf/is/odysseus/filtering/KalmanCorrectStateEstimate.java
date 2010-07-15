package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateEstimate<M extends IProbability> implements ICorrectStateEstimateFunction<M> {

	@Override
	public double[] correctStateEstimate(double[] measurementOld,
			double[] measurementNew, double[][] gain, Object matrixes) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
