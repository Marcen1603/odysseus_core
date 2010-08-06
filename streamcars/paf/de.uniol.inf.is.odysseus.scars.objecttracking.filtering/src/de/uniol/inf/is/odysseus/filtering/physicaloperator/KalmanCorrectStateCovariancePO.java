/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateCovariancePO<M extends IProbability & IConnectionContainer>  extends AbstractFilterPO<M> {
	
	
	public KalmanCorrectStateCovariancePO() {
	super();
	}
	
	public KalmanCorrectStateCovariancePO(FilterAO<M> filterAO) {
		this.setFilterFunction(filterAO.getFilterFunction());
		this.setNewObjListPath(filterAO.getNewObjListPathInt());
		this.setOldObjListPath(filterAO.getOldObjListPathInt());
	}

	public KalmanCorrectStateCovariancePO(KalmanCorrectStateCovariancePO<M> copy) {
		if (copy.getFilterFunction().getFunctionID() == 1) {
			this.setFilterFunction(new KalmanCorrectStateCovarianceFunction(copy.getFilterFunction().getParameters()));
		}
		
		this.setNewObjListPath(copy.getNewObjListPath().clone());
		this.setOldObjListPath(copy.getOldObjListPath().clone());
	
	}

	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		
		
		
		// list of connections
		Connection[] objConList = new Connection[object.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}
				
		// traverse connection list and filter
		for(Connection connected : objConList ) {
			compute(connected, null, null);
		}
		
		return object;
		
		}
	
	
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew,
			ArrayList<int[]> measurementValuePathsTupleOld) {
		
		
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
		
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
		
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		
		double[][] gain = oldTuple.getMetadata().getGain();
	
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
		getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
		getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
		double[][] resultCovariance = (double[][]) getFilterFunction().compute();
	
		//set new state covariance
		((MetaAttributeContainer<StreamCarsMetaData>) connected.getRight()).getMetadata().setCovariance(resultCovariance);
	
	 }
	


}