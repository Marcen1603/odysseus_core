/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;



import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 *
 */
public class CreateMetaDataPO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

	private AbstractMetaDataCreationFunction metaDataCreationFunction;
	
	public CreateMetaDataPO() {
	super();	
	}
	
	
	public CreateMetaDataPO(CreateMetaDataPO<M> copy) {
		
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
		
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
	
	public void compute(Connection connected) {
		
		metaDataCreationFunction.compute(connected);
			
	
		
	}


	
	@Override
	public AbstractPipe clone() {
		return new CreateMetaDataPO<M>(this);
	}

	public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction metaDataCreationFunction) {
		this.metaDataCreationFunction = metaDataCreationFunction;
	}

	public AbstractMetaDataCreationFunction getMetaDataCreationFunction() {
		return metaDataCreationFunction;
	}
}
