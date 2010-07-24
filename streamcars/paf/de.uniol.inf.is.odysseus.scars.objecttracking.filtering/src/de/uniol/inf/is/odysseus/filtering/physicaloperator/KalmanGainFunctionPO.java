/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;

/**
 * @author dtwumasi
 * @param <M>
 *
 */
public class KalmanGainFunctionPO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>  > extends AbstractFilterPO<M> {

	public void compute(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected) {
		
		
			
			MVRelationalTuple<M> oldTuple = connected.getRight();
			MVRelationalTuple<M> newTuple = connected.getLeft();
		
			double[][] covarianceNew = newTuple.getMetadata().getCovariance();
			double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
			getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
			getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
			double[][] gain = (double[][]) getFilterFunction().compute();
		
			//set gain
			connected.getRight().getMetadata().setGain(gain);
	
		
	}
}
