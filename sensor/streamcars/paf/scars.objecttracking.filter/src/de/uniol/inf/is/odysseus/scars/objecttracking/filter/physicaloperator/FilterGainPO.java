/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;

/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 * 
 */
public class FilterGainPO<M extends IProbability & IConnectionContainer & IGain> extends AbstractFilterPO<M> {

	private AbstractMetaDataCreationFunction<M> metaDataCreationFunction;
	private SchemaHelper helper;
	
	public FilterGainPO() {
		super();
	}

	public FilterGainPO(FilterGainPO<M> copy) {
		super(copy);
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
	}

	public void compute(Connection connected, MVRelationalTuple<M> tuple) {
		metaDataCreationFunction.compute(connected, (MVRelationalTuple<M>)tuple, this.getParameters());
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		if( helper == null ) 
			helper = new SchemaHelper(getOutputSchema());
		
		// traverse connection list and filter
		for (Connection connected : object.getMetadata().getConnectionList()) {
			compute(connected, object);
		}

		return object;
	}

	@Override
	public FilterGainPO<M> clone() {
		return new FilterGainPO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	public AbstractMetaDataCreationFunction<M> getMetaDataCreationFunction() {
		return metaDataCreationFunction;
	}

	public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction<M> metaDataCreationFunction) {
		this.metaDataCreationFunction = metaDataCreationFunction;
	}
}
