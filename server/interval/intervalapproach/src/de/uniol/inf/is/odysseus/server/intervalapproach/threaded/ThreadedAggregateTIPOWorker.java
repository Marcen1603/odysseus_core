package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;

public class ThreadedAggregateTIPOWorker<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends Thread {

	private ThreadedAggregateTIPO<Q, R, W> tipo;
	private ArrayBlockingQueue<Pair<Long, R>> queue;
	private int threadNumber;

	public ThreadedAggregateTIPOWorker(ThreadGroup threadGroup,
			ThreadedAggregateTIPO<Q, R, W> threadedAggregateTIPO, int threadNumber,
			ArrayBlockingQueue<Pair<Long, R>> queue) {
		super(threadGroup, "Threaded aggregate worker thread" + threadNumber);
		super.setDaemon(true);
		this.threadNumber = threadNumber;
		this.tipo = threadedAggregateTIPO;
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			if (isInterrupted()) {
				break;
			}
			
			// get values from queue
			Pair<Long, R> pair = null;
			Long groupId;
			R object;
			try {
				pair = queue.take();
			} catch (InterruptedException e) {
				break;
			}
			groupId = pair.getE1();
			object = pair.getE2();

			if (groupId != null && object != null){
				// get sweep area from groupId
				AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = tipo.getSweepAreaForGroup(groupId);
				
				// if sweep are is found, process aggregation
				if (sa != null){
					synchronized (sa) {
						List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = tipo
								.updateSA(sa, object, tipo.isOutputPA());
						tipo.createOutput(results, groupId, object.getMetadata().getStart(),
								threadNumber);
					}
				}
			}
		}

	}
}
