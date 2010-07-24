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
 *
 */
public class KalmanCorrectStateCovariancePO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>>  extends AbstractFilterPO<M> {
	
public void compute(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected) {
		
		
		
		MVRelationalTuple<M> oldTuple = connected.getRight();
		
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
		
		double[][] gain = oldTuple.getMetadata().getGain();
	
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
		getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
		
		double[][] resultCovariance = (double[][]) getFilterFunction().compute();
	
		//set new state covariance
		connected.getRight().getMetadata().setCovariance(resultCovariance);
	
}
}
