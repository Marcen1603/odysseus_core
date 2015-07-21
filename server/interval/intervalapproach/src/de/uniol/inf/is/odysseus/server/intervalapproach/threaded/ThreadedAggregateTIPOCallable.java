package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.List;
import java.util.concurrent.Callable;

import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;

public class ThreadedAggregateTIPOCallable<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		implements Callable<String> {

	private final ThreadedAggregateTIPO<Q, R, W> tipo;
	private final AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;
	private final R object;
	private Long groupID;

	public ThreadedAggregateTIPOCallable(
			ThreadedAggregateTIPO<Q, R, W> threadedAggregateTIPO,
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R object, Long groupID) {
		this.tipo = threadedAggregateTIPO;
		this.sa = sa;
		this.object = object;
		this.groupID = groupID;
	}

	@Override
	public String call() throws Exception {
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = tipo
				.updateSA(sa, object, tipo.isOutputPA());
		String name = Thread.currentThread().getName();
		int threadNumber = Integer.parseInt(name); // threads are named as
													// integer values
		tipo.createOutput(results, groupID, object.getMetadata().getStart(),
				threadNumber - 1);
		return "Done";
	}
}
