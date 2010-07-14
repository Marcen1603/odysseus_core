/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author mase
 *
 */
public class KalmanGainFunction<M extends IProbability> implements IGainFunction<M> {

	@Override
	public Object computeGain(MVRelationalTuple<M> Old, MVRelationalTuple<M> New,
			Object[] matrixes) {
		// TODO Auto-generated method stub
		return null;
	}

}
