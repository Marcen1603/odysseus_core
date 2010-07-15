/**
 * Interface for the implementation of a state estimate correction function
 */
package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author dtwumasi
 *
 */
public interface ICorrectStateEstimateFunction<M extends IProbability> {

	public double[] correctStateEstimate(double[] measurementOld, double[] measurementNew, double[][] Gain, Object matrixes);
}
