package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;

public class ThreadedAggregateTIPORunnable<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>> implements Runnable{

	private final ThreadedAggregateTIPO<Q, R, W> tipo;
	private final AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;
	private final R object;
	private Long groupID;

	
	public ThreadedAggregateTIPORunnable(
			ThreadedAggregateTIPO<Q, R, W> threadedAggregateTIPO,
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R object, Long groupID) {
				this.tipo = threadedAggregateTIPO;
				this.sa = sa;
				this.object = object;
				this.groupID = groupID;				
	}

	@Override public void run() {
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = tipo.updateSA(
				sa, object, tipo.isOutputPA());

		tipo.createOutput(results, groupID, object.getMetadata().getStart());
	  }
}
