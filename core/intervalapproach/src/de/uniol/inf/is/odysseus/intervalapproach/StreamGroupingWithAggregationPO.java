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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class StreamGroupingWithAggregationPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> {

	static final Logger logger = LoggerFactory
			.getLogger(StreamGroupingWithAggregationPO.class);

	final private ITransferArea<W, W> transferArea;
	private final Map<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups = new HashMap<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>>();
	private int dumpAtValueCount = -1;
	private long createOutputCounter = 0;

	public StreamGroupingWithAggregationPO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			IGroupProcessor<R, W> grProcessor) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		setGroupProcessor(grProcessor);
		transferArea = new TITransferArea<W, W>();
	}

	public StreamGroupingWithAggregationPO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		transferArea = new TITransferArea<W, W>();
	}

	public StreamGroupingWithAggregationPO(
			StreamGroupingWithAggregationPO<Q, R, W> agg) {
		super(agg);
		transferArea = agg.transferArea.clone();
		groups.putAll(agg.groups);
	}

	@Override
	public void setGroupProcessor(IGroupProcessor<R, W> groupProcessor) {
		super.setGroupProcessor(groupProcessor);
	}

	public void setDumpAtValueCount(int dumpAtValueCount) {
		this.dumpAtValueCount = dumpAtValueCount;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getGroupProcessor().init();
		transferArea.init(this);
	}

	@Override
	protected void process_done() {
		// Drain all groups
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().iterator();
			produceResults(results, entry.getKey());
		}

		transferArea.done();
	}

	@Override
	protected boolean isDone() {
		return super.isDone() && transferArea.size() == 0;
	}

		
	@Override
	protected synchronized void process_next(R object, int port) {
		
		// Determine if there is any data from previous runs to write
		createOutput(object.getMetadata().getStart());

		// Create group ID from input object
		Integer groupID = getGroupProcessor().getGroupID(object);
		// Find or create sweep area for group
		DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = groups
				.get(groupID);
		if (sa == null) {
			sa = new DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
			groups.put(groupID, sa);
			// System.out.println("Created new Sweep Area for group id " +
			// groupID);
		}

		// Update sweep area with new element
		updateSA(sa, object);
	}

	private synchronized void createOutput(PointInTime timestamp) {
		// optional: Build partial aggregates with validity end until timestamp
		createOutputCounter++;
		if (dumpAtValueCount > 0 && createOutputCounter >= dumpAtValueCount) {
			createOutputCounter = 0;
			for (DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa : groups
					.values()) {
				updateSA(sa, timestamp);
			}

		}

		// Extract all Elements before current Time!
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().extractElementsBefore(timestamp);
			produceResults(results, entry.getKey());
		}

		// Find minimal start time stamp from elements intersecting time stamp
		PointInTime border = timestamp;
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> iter = entry
					.getValue().peekElementsContaing(timestamp, false);
			while (iter.hasNext()) {
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> v = iter
						.next();
				if (v.getMetadata().getStart().before(border)) {
					border = v.getMetadata().getStart();
				}
			}
		}
		transferArea.newHeartbeat(border, 0);

		// System.out.println(this+"Found Bordertime "+border+" at timestamp "+timestamp);
		// for (Entry<Integer, DefaultTISweepArea<PairMap<SDFSchema,
		// AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
		// .entrySet()){
		// System.out.println(entry.getKey()+" "+entry.getValue());
		// }

	}

	private synchronized void produceResults(
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results,
			Integer groupID) {
		while (results.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> e = results
					.next();
			PairMap<SDFSchema, AggregateFunction, W, ? extends ITimeInterval> r = calcEval(e);
			W out = getGroupProcessor().createOutputElement(groupID, r);
			out.setMetadata(e.getMetadata());
			transferArea.transfer(out);
			// System.out.println(this+"Move to tranfer area "+out);
		}
	}

	@Override
	public StreamGroupingWithAggregationPO<Q, R, W> clone() {
		return new StreamGroupingWithAggregationPO<Q, R, W>(this);
	}

	/**
	 * For an IPlanMigrationStrategy that directly manipulates the operator
	 * states.
	 * 
	 * @return State of {@link StreamGroupingWithAggregationPO}.
	 */
	public Map<Integer, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getEditableGroups() {
		return this.groups;
	}

	@Override
	public synchronized void processPunctuation(PointInTime timestamp, int port) {
		createOutput(timestamp);
	}

}
