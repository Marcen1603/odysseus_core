/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

/**
 * @author dtwumasi
 * @param <M>
 *
 */
public class KalmanGainFunctionPO extends AbstractFilterPO {

	@Override
	public MVRelationalTuple<StreamCarsMetaData> computeAll(MVRelationalTuple<StreamCarsMetaData> object) {
	
		
		// list of connections
	    Connection[] objConList = (Connection[]) ((IConnectionContainer) object.getMetadata()).getConnectionList().toArray();
	
	    // traverse connection list and filter
	    for(Connection connected : objConList ) {
		compute(connected,null, null );
	     }
	
	return object;
	
	}
	
	public void compute(Connection connected, ArrayList<int[]> mesurementValuePathsTupleNew, ArrayList<int[]> mesurementValuePathsTupleOld) {
		
			
			MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
			MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
		
			double[][] covarianceNew = newTuple.getMetadata().getCovariance();
			double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
			getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
			getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
			double[][] gain = (double[][]) getFilterFunction().compute();
		
			//set gain
			((MetaAttributeContainer<StreamCarsMetaData>) connected.getRight()).getMetadata().setGain(gain);
	
		
	}
}
