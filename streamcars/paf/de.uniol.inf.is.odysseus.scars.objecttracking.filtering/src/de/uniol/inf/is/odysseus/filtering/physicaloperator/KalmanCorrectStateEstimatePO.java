/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateEstimatePO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> extends AbstractFilterPO<M> {

	
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		// list of connections
		Connection[] objConList = (Connection[]) object.getMetadata().getConnectionList().toArray();
		
		// traverse connection list and filter
		for(Connection connected : objConList ) {
			compute(connected);
		}
		
		return object;
		
		}
	
	public void compute(Connection connected) {
		
		
		
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>) connected.getRight();
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>) connected.getLeft();
	
		double[] measurementNew = newTuple.getMeasurementValues();
		double[] measurementOld = oldTuple.getMeasurementValues();
		
		double[][] gain = oldTuple.getMetadata().getGain();
		
		getFilterFunction().addParameter(HashConstants.NEW_MEASUREMENT, measurementNew);
		getFilterFunction().addParameter(HashConstants.OLD_MEASUREMENT, measurementOld);
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
	
		double[][] gainResult = (double[][]) getFilterFunction().compute();
	
		//set gain
		((MetaAttributeContainer<M>) connected.getRight()).getMetadata().setGain(gainResult);

	
}
}
