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
public class KalmanCorrectStateCovariancePO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer>  extends AbstractFilterPO<M> {
	
	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		// list of connections
		Connection[] objConList = (Connection[]) object.getMetadata().getConnectionList().toArray();
		
		// traverse connection list and filter
		for(Connection connected : objConList ) {
			compute(connected);
		}
		
		return object;
		
		}	
	
	@Override
	public void compute(Connection connected) {
		
		
		
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>) connected.getRight();
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>) connected.getLeft();
		
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
		
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		
		double[][] gain = oldTuple.getMetadata().getGain();
	
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
		getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
		getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
		double[][] resultCovariance = (double[][]) getFilterFunction().compute();
	
		//set new state covariance
		((MetaAttributeContainer<M>) connected.getRight()).getMetadata().setCovariance(resultCovariance);
	
}
}
