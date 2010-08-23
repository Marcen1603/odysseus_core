/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;

/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 * 
 */
public class FilterGainPO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {

	private AbstractMetaDataCreationFunction metaDataCreationFunction;
	private SchemaHelper helper;
	
	public FilterGainPO() {
		super();
	}

	public FilterGainPO(FilterGainPO<M> copy) {
		super(copy);
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
	}

	public void compute(Connection connected, MVRelationalTuple<M> tuple, SchemaIndexPath oldPath, SchemaIndexPath newPath) {
		metaDataCreationFunction.compute(connected, (MVRelationalTuple<StreamCarsMetaData>)tuple, oldPath, newPath);
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {

		if( helper == null ) 
			helper = new SchemaHelper(getOutputSchema());
		
		// traverse connection list and filter
		SchemaIndexPath oldPath = helper.getSchemaIndexPath(getOldObjListPath());
		SchemaIndexPath newPath = helper.getSchemaIndexPath(getNewObjListPath());		
		for (Connection connected : object.getMetadata().getConnectionList()) {
			compute(connected, object, oldPath, newPath);
		}

		return object;
	}

	@Override
	public AbstractPipe clone() {
		return new FilterGainPO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	// Getter & Setter

	public AbstractMetaDataCreationFunction getMetaDataCreationFunction() {
		return metaDataCreationFunction;
	}

	public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction metaDataCreationFunction) {
		this.metaDataCreationFunction = metaDataCreationFunction;
	}
}
