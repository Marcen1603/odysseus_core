/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.collection.FESortedPair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.GroupingHelper;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StreamGroupingWithAggregationPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>>
		extends AggregateTIPO<Q, R> {

	// private DefaultTISweepArea<R> outputSweepArea = new
	// DefaultTISweepArea<R>();
	final private ITransferArea<R, R> transferArea;
	private final Map<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> groups = new HashMap<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>>();
	private boolean dumpOnEveryObject = false;

	public StreamGroupingWithAggregationPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations,
			GroupingHelper<R> grHelper, Class<Q> metadataType) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				metadataType);
		setGroupingHelper(grHelper);
		transferArea = new TITransferArea<R, R>(1);
		transferArea.setSourcePo(this);
	}

	@Override
	public void setGroupingHelper(GroupingHelper<R> groupingHelper) {
		super.setGroupingHelper(groupingHelper);
		initAggFunctions();
	}

	public void setDumpOnEveryObject(boolean dumpOnEveryObject) {
		this.dumpOnEveryObject = dumpOnEveryObject;
	}

	public StreamGroupingWithAggregationPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		transferArea = new TITransferArea<R, R>(1);
		transferArea.setSourcePo(this);
	}

	public StreamGroupingWithAggregationPO(
			StreamGroupingWithAggregationPO<Q, R> agg) {
		super(agg);
		transferArea = agg.transferArea.clone();
		groups.putAll(agg.groups);
	}

	protected void initAggFunctions() {

		Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations = getAggregations();

		for (SDFAttributeList attrList : aggregations.keySet()) {
			if (SDFAttributeList.subset(attrList, getInputSchema())) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					FESortedPair<SDFAttributeList, AggregateFunction> p = new FESortedPair<SDFAttributeList, AggregateFunction>(
							attrList, e.getKey());
					setAggregationFunction(p, getGroupingHelper()
							.getInitAggFunction(p));
					setAggregationFunction(p, getGroupingHelper()
							.getMergerAggFunction(p));
					setAggregationFunction(p, getGroupingHelper()
							.getEvaluatorAggFunction(p));
				}
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getGroupingHelper().init();
	}

	@Override
	protected void process_done() {
		// Drain all groups
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
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
		// ReadOnly ist egal
		process(object);
	}


	private synchronized void process(R s) {
	
		createOutput(s.getMetadata().getStart());

		// Create group ID from input tupel
		Integer groupID = getGroupingHelper().getGroupID(s);
		// Find or create sweep area for group
		DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa = groups
				.get(groupID);
		if (sa == null) {
			sa = new DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>();
			groups.put(groupID, sa);
		//	System.out.println("Created new Sweep Area for group id " + groupID);
		}

		// Update sweep area with new element
		updateSA(sa, s);
	}

	private void createOutput(PointInTime timestamp) {
		// optional: Build partial aggregates with validity end until timestamp
		if (dumpOnEveryObject) {
			for (DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa : groups
					.values()) {
				updateSA(sa, timestamp);
			}

		}

		// Find minimal start time stamp from elements intersecting time stamp
		PointInTime border = timestamp;
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> iter = entry
					.getValue().peekElementsContaing(timestamp, true);
			while (iter.hasNext()) {
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> v = iter
						.next();
				if (v.getMetadata().getStart().before(border)) {
					border = v.getMetadata().getStart();
				}
			}
		}
		transferArea.newHeartbeat(border, 0);

		// Extract all Elements before current Time!
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().extractElementsBefore(timestamp);
			produceResults(results, entry.getKey());
		}
		
		/*
		System.out.println("Found Bordertime "+border+" at timestamp "+timestamp);
		for (Entry<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()){
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
		*/


	}

	private synchronized void produceResults(
			Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> results,
			Integer groupID) {
		while (results.hasNext()) {
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> e = results
					.next();
			PairMap<SDFAttributeList, AggregateFunction, R, ? extends ITimeInterval> r = calcEval(e);
			R out = getGroupingHelper().createOutputElement(groupID, r);
			out.setMetadata(e.getMetadata());
			transferArea.transfer(out);
		//	System.out.println("Move to tranfer area "+out);
		}
	}

	@Override
	public StreamGroupingWithAggregationPO<Q, R> clone() {
		return new StreamGroupingWithAggregationPO<Q, R>(this);
	}

	/**
	 * For an IPlanMigrationStrategy that directly manipulates the operator
	 * states.
	 * 
	 * @return State of {@link StreamGroupingWithAggregationPO}.
	 */
	public Map<Integer, DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>>> getEditableGroups() {
		return this.groups;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		createOutput(timestamp);
	}

}
