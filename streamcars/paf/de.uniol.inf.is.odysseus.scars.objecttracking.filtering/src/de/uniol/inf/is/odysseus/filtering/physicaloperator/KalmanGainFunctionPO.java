/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanGainFunctionPO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

	public KalmanGainFunctionPO() {
	super();	
	}
	
	public KalmanGainFunctionPO(FilterAO<M> filterAO) {
		super();
		this.setParameters(filterAO.getParameters());
		
	}
	
	public KalmanGainFunctionPO(KalmanGainFunctionPO<M> copy) {
		HashMap<Integer,Object> parametercopy = new HashMap<Integer,Object>(copy.getFilterFunction().getParameters());
		if (copy.getFilterFunction().getFunctionID() == 1) {
			this.setFilterFunction(new KalmanGainFunction(parametercopy));
		}
		
		
		
	
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
	
		
		// list of connections
		Connection[] objConList = new Connection[object.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}
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


	
	@Override
	public AbstractPipe clone() {
		return new KalmanGainFunctionPO<M>(this);
	}
}
