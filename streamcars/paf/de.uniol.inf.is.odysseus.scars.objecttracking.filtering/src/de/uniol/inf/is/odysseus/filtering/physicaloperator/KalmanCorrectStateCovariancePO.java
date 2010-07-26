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
	
	
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		// list of connections
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) object.getMetadata().getConnectionList().toArray();
		
		// traverse connection list and filter
		for(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected : objConList ) {
			compute(connected);
		}
		
		return object;
		
		}	
	
	public void compute(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected) {
		
		
		
		MVRelationalTuple<M> oldTuple = connected.getRight();
		MVRelationalTuple<M> newTuple = connected.getLeft();
		
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
		
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		
		double[][] gain = oldTuple.getMetadata().getGain();
	
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
		getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
		getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
		double[][] resultCovariance = (double[][]) getFilterFunction().compute();
	
		//set new state covariance
		connected.getRight().getMetadata().setCovariance(resultCovariance);
	
}
}
