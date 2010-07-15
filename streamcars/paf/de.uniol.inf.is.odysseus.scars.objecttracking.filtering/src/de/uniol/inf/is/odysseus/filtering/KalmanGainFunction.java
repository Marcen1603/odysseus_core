/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/**
 * @author mase
 *
 */
public class KalmanGainFunction<M extends IProbability> implements IGainFunction<M> {

	@Override
	public double[][] computeGain(double[][] oldCovariance, double[][] newCovariance, Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}



}
