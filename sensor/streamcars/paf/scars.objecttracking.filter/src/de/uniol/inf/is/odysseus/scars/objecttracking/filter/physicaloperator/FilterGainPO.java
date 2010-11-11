/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;

/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 *
 */
public class FilterGainPO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer & IGain> extends AbstractFilterPO<M> {

	private AbstractMetaDataCreationFunction<M> metaDataCreationFunction;
	public FilterGainPO() {
		super();
	}

	public FilterGainPO(FilterGainPO<M> copy) {
		super(copy);
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
	}

	public void compute(IConnection connected, MVRelationalTuple<M> tuple) {
		metaDataCreationFunction.compute(connected, (MVRelationalTuple<M>)tuple, this.getParameters());
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		object.getMetadata().setObjectTrackingLatencyStart("Filter Gain");
		// traverse connection list and filter
		for (IConnection connected : object.getMetadata().getConnectionList()) {
			compute(connected, object);
		}
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Gain");
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
