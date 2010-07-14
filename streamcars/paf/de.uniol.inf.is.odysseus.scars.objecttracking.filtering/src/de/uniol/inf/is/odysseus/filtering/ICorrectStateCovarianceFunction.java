/**
 * Interace for the implementation of a function for correcting
 * the state error covariance matrix
 */
package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author dtwumasi
 *
 */
public interface ICorrectStateCovarianceFunction<M extends IProbability> {

	public MVRelationalTuple<M> computeStateCovariance(MVRelationalTuple<M> Old, Object Gain, Object[] matrixes);

}
