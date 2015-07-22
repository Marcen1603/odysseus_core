package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

public class ThreadedAggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> implements IThreadedPO {

	private int degree;
	private Map<Integer, ThreadedAggregateTIPOWorker<Q, R, W>> threadMap = new ConcurrentHashMap<Integer, ThreadedAggregateTIPOWorker<Q, R, W>>();
	private Map<Integer, ArrayBlockingQueue<Pair<Long, R>>> queueMap = new ConcurrentHashMap<Integer, ArrayBlockingQueue<Pair<Long, R>>>();
	private ThreadGroup workerThreadGroup;

	public ThreadedAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping, IMetadataMergeFunction<Q> metadataMerge,
			int degree) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping, metadataMerge);
		this.setDegree(degree);
		workerThreadGroup = new ThreadGroup("Threaded Aggregate worker threads");
		for (int i = 0; i < degree; i++) {
			ArrayBlockingQueue<Pair<Long, R>> blockingQueue = new ArrayBlockingQueue<Pair<Long, R>>(
					1000);
			ThreadedAggregateTIPOWorker<Q, R, W> worker = new ThreadedAggregateTIPOWorker<Q, R, W>(
					workerThreadGroup, this, i, blockingQueue);
			queueMap.put(i, blockingQueue);
			threadMap.put(i, worker);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {

		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			g.init();
			transferArea.init(this, degree);
			groups.clear();
			createOutputCounter = 0;
		}

		for (ThreadedAggregateTIPOWorker<Q, R, W> worker : threadMap.values()) {
			worker.start();
		}
	}

	@Override
	protected void process_next(R object, int port) {
		if (debug) {
			System.err.println(this + " READ " + object);
		}

		IGroupProcessor<R, W> g = getGroupProcessor();

		AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;

		Long groupID = null;
		synchronized (g) {
			// Determine group ID from input object
			groupID = g.getGroupID(object);
			// Find or create sweep area for group
			synchronized (groups) {
				sa = groups.get(groupID);
				if (sa == null) {
					sa = new AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
					groups.put(groupID, sa);
				}
			}
		}

		int threadNumber = (int) (groupID % degree);
		ArrayBlockingQueue<Pair<Long, R>> queue = queueMap.get(threadNumber);

		try {
			Pair<Long, R> pair = new Pair<Long, R>(groupID, object);
			queue.put(pair);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process_done(int port) {
		// interrupt worker threads to finish work
		workerThreadGroup.interrupt();

		// has only one port, so process_done can be called when first input
		// port calls done
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			if (drainAtDone) {
				// Drain all groups
				drainGroups();
			}
		}
		// Send information to transfer area that no more elements will be
		// delivered on port 0, so all data can be written
		if (debug) {
			System.err.println(this + " done");
		}

		for (int i = 0; i < degree; i++) {
			transferArea.done(i);
		}
	}

	@Override
	protected void process_close() {
		// interrupt worker threads to finish work
		workerThreadGroup.interrupt();
		super.process_close();
	}

	@Override
	public void createOutput(
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> existingResults,
			Long groupID, PointInTime timestamp, int inPort) {
		super.createOutput(existingResults, groupID, timestamp, inPort);
	}

	@Override
	public List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, boolean outputPA) {
		return super.updateSA(sa, elemToAdd, outputPA);
	}

	public int getDegree() {
		return degree;
	}

	@Override
	public void setDegree(int degree) {
		this.degree = degree;
	}

}
