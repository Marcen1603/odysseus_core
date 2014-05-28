/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

public class StreamGroupingWithAggregationPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> {

	static final Logger logger = LoggerFactory
			.getLogger(StreamGroupingWithAggregationPO.class);

	final private ITransferArea<W, W> transferArea;
	private final Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups = new HashMap<>();
	private int dumpAtValueCount = -1;
	private long createOutputCounter = 0;
	private boolean outputPA = false;
	private boolean drainAtDone = true;
	private boolean drainAtClose = false;

	public StreamGroupingWithAggregationPO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping);
		transferArea = new TITransferArea<W, W>();
	}

	@Override
	public void setGroupProcessor(IGroupProcessor<R, W> groupProcessor) {
		super.setGroupProcessor(groupProcessor);
	}

	public void setDumpAtValueCount(int dumpAtValueCount) {
		this.dumpAtValueCount = dumpAtValueCount;
	}

	public void setOutputPA(boolean outputPA) {
		this.outputPA = outputPA;
	}

	public boolean isOutputPA() {
		return outputPA;
	}

	public void setDrainAtDone(boolean drainAtDone) {
		this.drainAtDone = drainAtDone;
	}

	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			g.init();
			transferArea.init(this, getSubscribedToSource().size());
			groups.clear();
		}
	}

	@Override
	protected void process_done() {
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			if (drainAtDone) {
				// Drain all groups
				drainGroups();
			}
		}
	}

	private void drainGroups() {
		for (Entry<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().iterator();
			produceResults(results, entry.getKey());
			entry.getValue().clear();
		}
		transferArea.done(0);
	}

	@Override
	protected void process_close() {
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			logger.debug("closing " + this.getName());
			if (drainAtClose) {
				drainGroups();
			}
		}

	}

	@Override
	protected boolean isDone() {
		return super.isDone() && transferArea.size() == 0;
	}

	@Override
	protected void process_next(R object, int port) {

		// System.err.println("AGGREGATE DEBUG MG: IN " + object);
		// Determine if there is any data from previous runs to write
		// createOutput(object.getMetadata().getStart());

		// Create group ID from input object
		Long groupID = getGroupProcessor().getGroupID(object);
		// Find or create sweep area for group
		DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = groups
				.get(groupID);
		if (sa == null) {
			sa = new DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
			groups.put(groupID, sa);
			// System.out.println("Created new Sweep Area for group id " +
			// groupID+ " --> #"+groups.size());
		}

		// Update sweep area with new element
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = updateSA(
				sa, object, outputPA);
		if (debug){
			System.err.println(sa);
		}
		if (results.size() > 0) {
			produceResults(results, groupID);
		}

		// Is there any new output to write now?
		createOutput(object.getMetadata().getStart());
	}

	private void createOutput(PointInTime timestamp) {
		// Extract all Elements before current Time!
		cleanUpSweepArea(timestamp);

		// optional: Build partial aggregates with validity end until timestamp
		createAddOutput(timestamp);

		// Find minimal start time stamp from elements intersecting time stamp
		transferArea.newHeartbeat(findMinTimestamp(timestamp), 0);

		if (debug){
			transferArea.dump();
		}

		// THIS HEARTBEAT IS TO HIGH!!
		// transferArea.newHeartbeat(timestamp, 0);
	}

	private PointInTime findMinTimestamp(PointInTime timestamp) {
		PointInTime border = timestamp;
		for (Entry<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			PointInTime sa_min_ts = entry.getValue().getMinTs();
			if (sa_min_ts != null) {
				if (sa_min_ts.before(border)) {
					border = sa_min_ts;
				}
			}
			// WTF ... ??
			// Iterator<PairMap<SDFSchema, AggregateFunction,
			// IPartialAggregate<R>, Q>> iter = entry
			// .getValue().peekElementsContaing(timestamp, false);
			// while (iter.hasNext()) {
			// PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> v
			// = iter
			// .next();
			// if (v.getMetadata().getStart().before(border)) {
			// border = v.getMetadata().getStart();
			// }
			// }
		}
		return border;
	}

	public void cleanUpSweepArea(PointInTime timestamp) {
		for (Entry<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			// /System.err.println(entry.getValue());
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().extractElementsBefore(timestamp);
			if (debug){
				System.err.println(entry.getValue());
			}
			produceResults(results, entry.getKey());
		}
	}

	public void createAddOutput(PointInTime timestamp) {
		if (dumpAtValueCount > 0) {

			createOutputCounter++;
			if (createOutputCounter >= dumpAtValueCount) {
				createOutputCounter = 0;

				for (Entry<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
						.entrySet()) {
					// System.err.println("Updating "+entry.getKey());
					List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = updateSA(
							entry.getValue(), timestamp);
					if (results.size() > 0) {
						produceResults(results, entry.getKey());
					}
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void produceResults(
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> results,
			Long groupID) {
		for (PairMap<SDFSchema, AggregateFunction, W, Q> e : results) {
			W out = getGroupProcessor().createOutputElement(groupID, e);
			out.setMetadata((Q) e.getMetadata().clone());
			transferArea.transfer(out);
		}
	}

	private void produceResults(
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results,
			Long groupID) {
		while (results.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> e = results
					.next();
			W out = null;
			if (outputPA) {
				out = getGroupProcessor().createOutputElement2(groupID, e);
			} else {
				PairMap<SDFSchema, AggregateFunction, W, ? extends ITimeInterval> r = calcEval(
						e, true);
				out = getGroupProcessor().createOutputElement(groupID, r);
			}
			out.setMetadata(e.getMetadata());
			transferArea.transfer(out);
		}

	}

	/**
	 * For an IPlanMigrationStrategy that directly manipulates the operator
	 * states.
	 * 
	 * @return State of {@link StreamGroupingWithAggregationPO}.
	 */
	public Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getEditableGroups() {
		return this.groups;
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		createOutput(punctuation.getTime());
		transferArea.sendPunctuation(punctuation, port);
	}

}
