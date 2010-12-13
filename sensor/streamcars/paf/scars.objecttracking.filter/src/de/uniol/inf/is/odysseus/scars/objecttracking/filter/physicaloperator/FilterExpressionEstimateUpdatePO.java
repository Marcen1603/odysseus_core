/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;


/**
 * @author dtwumasi
 *
 */
public class FilterExpressionEstimateUpdatePO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterExpressionPO<M> {

	public FilterExpressionEstimateUpdatePO() {
		super();
	}

	public FilterExpressionEstimateUpdatePO(FilterExpressionEstimateUpdatePO<M> copy) {
		super(copy);

	}

	@Override
	protected void process_open() throws OpenFailedException {

		super.process_open();

	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		
		// latency
		object.getMetadata().setObjectTrackingLatencyStart("Filter Est Update");
		
		// list of connections
		ArrayList<IConnection> objConList = object.getMetadata().getConnectionList();

		// traverse connection list and filter
		for (IConnection connected : objConList) {
			compute(connected.getLeftPath(), connected.getRightPath());
		}
		
		// latency
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Est Update");
		
		return object;
	}

	private void compute(TupleIndexPath scannedObjectTupleIndex, TupleIndexPath predictedObjectTupleIndex) {
		
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new FilterExpressionEstimateUpdatePO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

}
