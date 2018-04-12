package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class TemporalAggregationPO<M extends ITimeInterval, T extends Tuple<M>> extends AggregationPO<M, T> {

	public TemporalAggregationPO(AggregationPO<M, T> other) {
		super(other);
	}
	
	@Override
	protected synchronized void process_next(final T object, final int port) {
		
		// Make each object non-temporal
		
		// Transfers too much :(
		
		
		super.process_next(object, port);
	}

}
