package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

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
	private ExecutorService executor;

	public ThreadedAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping, IMetadataMergeFunction<Q> metadataMerge,
			int degree) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping, metadataMerge);
		// Create a factory that produces daemon threads with a naming pattern
		// and
		// a priority
		BasicThreadFactory factory = new BasicThreadFactory.Builder()
				.namingPattern("%d").daemon(true).build();
		// Create an executor service for single-threaded execution
		executor = Executors.newFixedThreadPool(degree, factory);
		this.setDegree(degree);
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
			sa = groups.get(groupID);
			if (sa == null) {
				sa = new AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
				groups.put(groupID, sa);
			}
		}

		synchronized (sa) {
			if (sa != null) {
				ThreadedAggregateTIPORunnable<Q, R, W> runnable = new ThreadedAggregateTIPORunnable<Q, R, W>(
						this, sa, object, groupID);
				executor.execute(runnable);
			}
		}
	}

	@Override
	protected void process_close() {
		super.process_close();
		executor.shutdown();
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
