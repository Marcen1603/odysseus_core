/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

/**
 * @author dtwumasi
 *
 */
public class UpdateMetaDataPO<M extends IProbability & IConnectionContainer>  extends AbstractFilterPO<M> {
	
	private AbstractMetaDataUpdateFunction updateMetaDataFunction;
	
	public AbstractMetaDataUpdateFunction getUpdateMetaDataFunction() {
		return updateMetaDataFunction;
	}

	public void setUpdateMetaDataFunction(
			AbstractMetaDataUpdateFunction updateMetaDataFunction) {
		this.updateMetaDataFunction = updateMetaDataFunction;
	}

	public UpdateMetaDataPO() {
	super();
	}
	
	public UpdateMetaDataPO(FilterAO<M> filterAO) {
		super();
		
		this.setParameters(filterAO.getParameters());
	}

	public UpdateMetaDataPO(UpdateMetaDataPO<M> copy) {
		super();
		HashMap<Integer,Object> parametercopy = new HashMap<Integer,Object>(copy.getUpdateMetaDataFunction().getParameters());
		this.setUpdateMetaDataFunction(new KalmanCorrectStateCovarianceFunction(parametercopy));			
		}
		
	

	public void compute(Connection connected) {
			updateMetaDataFunction.compute(connected);
		
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
			compute(connected);
		}
		
		return object;
		
		}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new UpdateMetaDataPO(this);
	}
	}




