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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.state.AggregateTIPOState;

/**
 * This class represents the physical implementation of the aggregation operation in the
 * interval approach
 * @author Marco Grawunder
 *
 * @param <Q>: The metadata datatype
 * @param <R>: The type of the element that is read
 * @param <W>: The type of the element that is written
 */
public class AggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregatePO<Q, R, W> implements
		IStatefulOperator, IStatefulPO, IPhysicalOperatorKeyValueProvider {

	/**
	 * When combining different elements the meta data must be merged. Because the operator does not know, which
	 * meta data is used, the metadataMerge function is injection at transformation time 
	 */
	final private IMetadataMergeFunction<Q> metadataMerge;

	/**
	 * if set to a value higher than -1, every dumpAtValueCount elements are also written, even if no new elements
	 * has reached its final value. The result element has a shorter validity.
	 */
	private int dumpAtValueCount = -1;
	
	/**
	 * How many elements have been read since last dump of elements
	 */
	private long createOutputCounter = 0;
	
	/**
	 * The aggregation could output values or partial aggregates. 
	 */
	private boolean outputPA = false;
	/**
	 * if set to true, the current elements that are still stored to keep order are 
	 * written, when a done call from the input operator arrives
	 */
	private boolean drainAtDone = true;
	/**
	 * if set to true, the current elements that are still stored to keep order are 
	 * written, when a close call from the output operator arrives
	 */
	private boolean drainAtClose = false;

	
	/**
	 * Aggreation can create out of order elements. The transferArea is used to assure the right out order
	 */
	private ITransferArea<W, W> transferArea;
	
	/**
	 * For every group exists a sweep area that keeps the state for the aggregation
	 */
	private Map<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups = new HashMap<>();

	/**
	 * For information purposes: The start time stamp of the element that leaded to a sweep area cleanup 
	 */
	private PointInTime lastTimestamp;

	
	static final Logger logger = LoggerFactory.getLogger(AggregateTIPO.class);


	/**
	 * Create a new aggregation operator 
	 * @param inputSchema: The input schema of the operator
	 * @param outputSchema: The output schema of the operator
	 * @param groupingAttributes: The attributes for which grouping should be done
	 * @param aggregations: What aggregations should be done. The map contains first a schema, that contains the 
	 * attributes from the input schema that are used as input for the aggregation, the second map contains the aggreation functions for
	 * this input attributes and the output attribute (from the output schema) where the result of the aggregation should be stored.
	 * @param fastGrouping: If set to true, the grouping will be based the the java hashCode function for the grouping attributes. 
	 * This could be unsafe, as multiple different input elements could be mapped to the same hashCode. In such cases, fastGrouping should
	 * be false!
	 */
	public AggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping, IMetadataMergeFunction<Q> metadataMerge ) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping);
		this.metadataMerge = metadataMerge;
		transferArea = new TITransferArea<W, W>();
	}

	/**
	 * if set to a value higher than -1, every dumpAtValueCount elements are also written, even if no new elements
	 * has reached its final value. The result element has a shorter validity.
	 * @param dumpAtValueCount: A which rate, should additional output be created
	 */
	public void setDumpAtValueCount(int dumpAtValueCount) {
		this.dumpAtValueCount = dumpAtValueCount;
	}

	/**
	 * The aggreation can created concrete values as output, e.g. 20 for an AVG aggregation. If the aggregation is splitted
	 * partial aggregates can be used to keep the state, e.g. Sum = 100, Count = 5 for an average. 
	 * @param outputPA if set to true, partial aggregate will be used in the output instead of real values
	 */
	public void setOutputPA(boolean outputPA) {
		this.outputPA = outputPA;
	}

	/**
	 * Does this aggregation return partial aggregates
	 * @return
	 */
	public boolean isOutputPA() {
		return outputPA;
	}

	/**
	 * if set to true, the current elements that are still stored to keep order are 
	 * written, when a done call from the input operator arrives
	 */
	public void setDrainAtDone(boolean drainAtDone) {
		this.drainAtDone = drainAtDone;
	}

	/**
	 * if set to true, the current elements that are still stored to keep order are 
	 * written, when a close call from the output operator arrives
	 */
	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
	}

	/**
	 * The aggregation creates always a new element. So no input data needs to be cloned.
	 */
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
	protected void process_done(int port) {
		// has only one port, so process_done can be called when first input
		// port calls done
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			if (drainAtDone) {
				// Drain all groups
				drainGroups();
			}
		}
	}

	/**
	 * Iterate over all groups sweep areas,  create output and clear state
	 */
	private void drainGroups() {
		for (Entry<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().iterator();
			produceResults(results, entry.getKey());
			entry.getValue().clear();
		}
		// Send information to transfer area that no more elements will be delivered on port 0, so all data can be written
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
			logger.debug("closing " + this.getName() + " done");
		}

	}

	@Override
	public boolean isDone() {
		return super.isDone() && transferArea.size() == 0;
	}

	@Override
	protected void process_next(R object, int port) {

		// Remark: Typically, the transferArea would get a notification about the time progress
		// but here the aggregate operator keeps state for different groups, so the time progress
		// cannot be determined by the last read object, but by the oldest object in every group 
		// somehow similar to a logical port for every group
		
		if (debug) {
			System.err.println(object);
		}

		// Determine group ID from input object
		Long groupID = getGroupProcessor().getGroupID(object);

		// Find or create sweep area for group
		AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = groups
				.get(groupID);
		if (sa == null) {
			sa = new AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
			groups.put(groupID, sa);
		}

		// Update sweep area with new element and retrieve results, that can be written to transferArea (i.e. 
		// where the partial aggregate can receive no modification because it validity is before the
		// start time stamp of the current object (and the stream is ordered regarding time stamps)
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = updateSA(
				sa, object, outputPA);

		if (debug){
			System.err.println(sa);
		}
		// If the update has found elements to write, create output
		if (results.size() > 0) {
			produceResults(results, groupID);
		}

		// For special cases (e.g. dumpAtValueCount) or because of time progress, 
		// additional output could be send to the transferArea
		cleanUpAndcreateOutput(object.getMetadata().getStart());
	}

	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		// Keep punctuation order by sending to transfer area
		transferArea.sendPunctuation(punctuation, port);
		// Maybe new output can be created because of time progress
		cleanUpAndcreateOutput(punctuation.getTime());
	}
	
	/**
	 * This method is used by process and processPunctuation to create output on
	 * time progress and on element count
	 * @param timestamp
	 */
	private void cleanUpAndcreateOutput(PointInTime timestamp) {
		this.lastTimestamp = timestamp;
		// Extract all Elements before current Time, these elements cannot be touched/changed anymore
		// and send to transferArea
		cleanUpSweepArea(timestamp);

		// To raise the output rate, aggregations can be written earlier 
		createAddOutput(timestamp);

		// To determine the time progress for the transferArea
		// Find minimal start time stamp in all current groups. If no
		// group has an entry, the timestamp is the given timestamp
		PointInTime mints = findMinTimestamp(timestamp);
		// Inform transferArea about the time progress
		transferArea.newHeartbeat(mints, 0);

		if (debug) {
			System.err.println("CREATE OUTPUT " + mints);
			transferArea.dump();
		}

		// THIS HEARTBEAT IS TO HIGH!!
		// transferArea.newHeartbeat(timestamp, 0);
	}

	/**
	 * A helper method that looks in every sweep area to determine the minimum time stamp, i.e. the oldest starting 
	 * point that is still stored in the state (the watermark)
	 * @param timestamp The current time stamp of the input stream. If all groups are empty, this will be the minimum value
	 * @return
	 */
	private PointInTime findMinTimestamp(PointInTime timestamp) {
		PointInTime border = timestamp;
		for (AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> entry : groups
					.values()) {
				PointInTime sa_min_ts = entry.calcMinTs();
			if (sa_min_ts != null) {
				if (sa_min_ts.before(border)) {
					border = sa_min_ts;
				}
			}

		}
		return border;
	}

	/**
	 * Remove all elements that are before the time stamp (i.e. end time stamp is before the given time stamp) 
	 * and send to transferArea. These are all elements that could not be touched anymore and as so, cannot change
	 * @param timestamp The time stamp that states the current progress of the input stream 
	 */
	private void cleanUpSweepArea(PointInTime timestamp) {
		for (Entry<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
				.entrySet()) {
			// /System.err.println(entry.getValue());
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results = entry
					.getValue().extractElementsBefore(timestamp);
			if (debug) {
				System.err.println("AREA FOR GROUP " + entry.getKey());
				//System.err.println(entry.getValue().getSweepAreaAsString());
			}
			produceResults(results, entry.getKey());
		}
	}

	/**
	 * Allow to create additional output by cutting all current partial aggregate into two, one before the split point and one after the
	 * split point. By this aggregate with a long valid time interval can be split into multiple elements
	 * @param splitPoint the point where the elements should be split up
	 */
	private void createAddOutput(PointInTime splitPoint) {
		if (dumpAtValueCount > 0) {

			createOutputCounter++;
			if (createOutputCounter >= dumpAtValueCount) {
				createOutputCounter = 0;

				for (Entry<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groups
						.entrySet()) {
					// System.err.println("Updating "+entry.getKey());
					List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = updateSA(
							entry.getValue(), splitPoint);
					if (results.size() > 0) {
						produceResults(results, entry.getKey());
					}
				}

			}
		}
	}

	
	/**
	 * The output data is build from the result of the current aggregation, the values of the
	 * grouping attributes in this group and the meta data of the partial aggregate
	 * This function creates output elements and sends them to the transferArea
	 * @param results The calculated aggregation values
	 * @param groupID for which group should the output 
	 */
	private void produceResults(
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> results,
			Long groupID) {
		for (PairMap<SDFSchema, AggregateFunction, W, Q> e : results) {
			W out = getGroupProcessor().createOutputElement(groupID, e);
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) e.getMetadata().clone();
			out.setMetadata(newMeta);
			transferArea.transfer(out);
		}
	}

	/**
	 * The output data is build from the current partial aggregates, the values of the
	 * grouping attributes in this group and the meta data of the partial aggregate
	 * This function creates output elements and sends them to the transferArea 
	 * @param results: The partial aggregates for the input attributes
	 * @param groupID: The group for which the result should be created
	 */
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
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) e.getMetadata().clone();
			out.setMetadata(newMeta);
			transferArea.transfer(out);
		}

	}

	/**
	 * For an IPlanMigrationStrategy that directly manipulates the operator
	 * states.
	 * 
	 * @return State of {@link StreamGroupingWithAggregationPO}.
	 */
	public Map<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getEditableGroups() {
		return this.groups;
	}


	/**
	 * This method does for every group/sweep area the core calculation of the new aggregation state, when a new
	 * element is inserted. This algorithm is inspired by the online aggregation algorithm of [Hellerstein] and the stream
	 * algorithm from [Kraemer]. It allow to apply different aggregations functions on multiple attributes
	 * 
	 * Different cases are handled:
	 * 1) The element does not overlap with any existing elements --> Create a new partial aggregate (init)
	 * 2) The element overlaps with other existing elements --> build overlapping areas
	 *    a) part before or after an overlap --> init 
	 *    b) part overlapping --> merge
	 *    
	 *    ----------------------------------------------------------------------------------------------
	 *      ---pa1-------   ---pa2----------
	 *          ----------newElem-----
	 *          
	 *  -->
	 *     -pa1-
	 *          --pa1+nE-
	 *                   -ne
	 *                      --pa2+n2--
	 *                                --ne-- 
	 * @param sa The sweep area that should be updated
	 * @param elemToAdd The new element that should be inserted into the sweep area
	 * @param outputPA a boolean that states, if the output should be partial aggregates or evaluated values
	 * @return a list of aggregations that cannot be modified anymore, because of the progress of time
	 */
	protected List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, boolean outputPA) {
		// The list of found elements that cannot be changed anymore
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues = new LinkedList<>();
		assert (elemToAdd != null);

		Q t_probe = elemToAdd.getMetadata();

		// Extract elements in this sweep area that overlaps the time interval
		// of elem
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		// No overlapping --> INIT: new Partial Aggregate
		if (!qualifies.hasNext()) {
			saInsert(sa, calcInit(elemToAdd), t_probe);
		} else {
			// Overlapping --> Partial Aggregates need to be touched

			// 1. Determine the splitting points of the meta data of the new element (t_probe) and
			// all element in the sweep area that overlaps
			// 2. sort this list increasing to determine the areas with no overlap and with overlap
			List<_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> pl = getSortedIntersectionPoints(t_probe, qualifies);

			// Now iterate over all points
			Iterator<_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> pointIter = pl.iterator();
			// Compare always to points
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p1 = null;
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2 = null;

			// get the first point (Must be one, else qualifies would not have returned values)
			p1 = pointIter.next();
			// Remember the last partial aggregate (init with the first one)
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate = p1.getLoad();
	
			while (pointIter.hasNext()) {
				// p2 is the current point
				p2 = pointIter.next();

				// Test all possible cases
				// Because the list is sorted, p2 cannot be before p1 and the
				// case p1 == p2 would
				// lead to an interval with length 0 which is not allowed
				// ATTENTION: Handle only the interval between p1 and p2
				// the next interval is treated in the next iteration!
				if (p1.before(p2)) {

					// both point are start-elements, overlapping as follows
					// ---------------------------------------
					// Obj1 Xxxxxxxxx........
					// p1
					// Obj2 ...Yyyyyy........
					// p2
					// ---------------------------------------
					// in case of no overlapping the next point would have
					// been an end point
					if (p1.isStart() && p2.isStart()) {

						lastPartialAggregate = updateSAStartStart(sa,
								elemToAdd, p1, p2, lastPartialAggregate);

						// This next case can only happen if p1 and p2 are from
						// the one element and the element is a sub part of an
						// the other element
						// ------------------------------------------------
						// ......AAAAAAAAAAAAAAAA..........................
						// .........EEEEEEE................................
						// In this case handle area from p1 und p2

					} else if (p1.isStart() && p2.isEnd()) {
						// Add new element as a combination from current value
						// and new element for new time interval

						updateSAStartEnd(sa, elemToAdd, outputPA, returnValues,
								t_probe, p1, p2, lastPartialAggregate);

					} else if (p1.isEnd() && p2.isStart()) {
						updateSAEndStart(sa, elemToAdd, p1, p2);
					} else if (p1.isEnd() && p2.isEnd()) { // Element after
						updateSAEndEnd(sa, elemToAdd, p1, p2,
								lastPartialAggregate);
					}
					// Remember the last seen partial aggregate (not the new
					// element)
					lastPartialAggregate = (p1.newElement()) ? lastPartialAggregate
							: p1.getLoad();
				} else { // if (p1.point.before(p2.point))
					lastPartialAggregate = (p2.newElement()) ? lastPartialAggregate
							: p2.getLoad();
				}

				p1 = p2;
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace(sa.toString());
		}
		// System.err.println(sa.toString());
		return returnValues;
	}

	/**
	 * Handle the case where p1 and p2 are end time stamps
	 * @param sa: The sweep are
	 * @param elemToAdd: The element that should be added
	 * @param p1: the left point
	 * @param p2: the right point
	 * @param lastPartialAggregate: the aggregate that overlaps with the elemToAdd
	 */
	private void updateSAEndEnd(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p1,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> partialAggregate) {
		// Two case: The left ts is an elements contained in the sweep area
		// OldEnd && NewEnd
		if (p2.newElement()) {
			// In this case the old element need not be touched
			// but the new element must be handled (not inserted!) from p1 to p2
			//       ------- Old
			//          -------- New
			// -->
			//       ------- Old
			//              ---- New			
			updateSAEndStart(sa, elemToAdd, p1, p2);
		} else { // The left element is the new element New End && Old End
			// in this case the new element must not be touched
			// but the partial aggregate must get other valid intervals
			// it has to start later
			//       ------- Old
			// --------      New
			// -->   
			//         ----- Old
			// --------      New
			@SuppressWarnings("unchecked")
			Q newTI = (Q) partialAggregate.getMetadata().clone();
			newTI.setStartAndEnd(p1.getPoint(), p2.getPoint());
			saInsert(sa, partialAggregate, newTI);
		}
	}

	private void updateSAEndStart(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p1, _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2) {
		// New element has a part that is newer than the partial
		// aggregate
		@SuppressWarnings("unchecked")
		Q newTI = (Q) elemToAdd.getMetadata().clone();
		newTI.setStartAndEnd(p1.getPoint(), p2.getPoint());
		saInsert(sa, calcInit(elemToAdd), newTI);
	}

	private void updateSAStartEnd(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			boolean outputPA,
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues,
			Q t_probe,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p1,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate) {
		// In some cases its possible to create output earlier
		final boolean createNew;
		if (!outputPA && p2.before(t_probe.getStart())) {
			createNew = false;
			PairMap<SDFSchema, AggregateFunction, W, Q> v = calcEval(
					lastPartialAggregate, false);
			v.setMetadata(lastPartialAggregate.getMetadata());
			v.getMetadata().setEnd(p2.getPoint());
			returnValues.add(v);
		} else {
			createNew = !(p1.getPoint().equals(lastPartialAggregate.getMetadata()
					.getStart()) && p2.getPoint().equals(lastPartialAggregate
					.getMetadata().getEnd()));
		}

		@SuppressWarnings("unchecked")
		Q newMeta = (Q) metadataMerge.mergeMetadata(
				lastPartialAggregate.getMetadata(), elemToAdd.getMetadata()).clone();
		newMeta.setStartAndEnd(p1.getPoint(), p2.getPoint());
		saInsert(sa, calcMerge(lastPartialAggregate, elemToAdd, createNew),
				newMeta);
	}

	private PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> updateSAStartStart(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p1,
			_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate) {
		// One of both elements must be new, else there would be
		// no overlapping
		if (p1.newElement()) {
			// Insert new Element with interval from start to
			// start
			//
			Q meta = elemToAdd.getMetadata();
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) meta.clone();
			newMeta.setStartAndEnd(p1.getPoint(), p2.getPoint());
			saInsert(sa, calcInit(elemToAdd), newMeta);
			lastPartialAggregate = p2.getLoad();
		} else {// p2.newElement()
				// Insert element again with shorter interval
				// (start to start)
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) p1.getLoad().getMetadata().clone();
			newMeta.setStartAndEnd(p1.getPoint(), p2.getPoint());
			saInsert(sa, p1.getLoad(), newMeta);
		}
		return lastPartialAggregate;
	}

	public List<_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getSortedIntersectionPoints(
			Q t_probe,
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies) {
		List<_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> pl = new LinkedList<>();

		// Determine the list of all points of the overlapped elements in
		// the sweep area
		while (qualifies.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			ITimeInterval t_agg = element_agg.getMetadata();
			// Add the points with corresponding partial aggregates and info
			// if
			// point is start oder end into list of points
			pl.add(new _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>(t_agg.getStart(), true, element_agg));
			pl.add(new _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>(t_agg.getEnd(), false, element_agg));
		}
		// Add the time interval of the element to add in the list of points
		pl.add(new _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>(t_probe.getStart(), true, null));
		pl.add(new _Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>(t_probe.getEnd(), false, null));

		// Sort the List
		Collections.sort(pl);
		return pl;
	}

	// Updates SA by splitting all partial aggregates before split point
	@SuppressWarnings("unchecked")
	protected List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PointInTime splitPoint) {
		// System.err.println("BEFORE "+splitPoint);
		// System.err.println(sa.toString());
		ITimeInterval t_probe = new TimeInterval(splitPoint, splitPoint.plus(1));
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues = new LinkedList<>();

		// Determine elements in this sweep area containing splitpoint
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		while (qualifies.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			Q meta = element_agg.getMetadata();
			if (meta.getStart().before(splitPoint)) {
				PairMap<SDFSchema, AggregateFunction, W, Q> e = calcEval(
						element_agg, false);
				e.setMetadata((Q) meta.clone());
				e.getMetadata().setEnd(splitPoint);
				meta.setStart(splitPoint);
				returnValues.add(e);
			}
			saInsert(sa, element_agg, meta);
		}

		// System.err.println("AFTER ");
		// System.err.println(sa.toString());

		return returnValues;
	}

	private void saInsert(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> elem,
			Q t) {
		// System.out.println("SA Insert "+elem+" "+t);
		elem.setMetadata(t);
		sa.insert(elem);
	}

	@Override
	public Serializable getState() {
		AggregateTIPOState<Q, R, W> state = new AggregateTIPOState<Q, R, W>();
		state.setTransferArea(this.transferArea);
		state.setGroups(this.groups);
		state.setEval(super.getAllEvalFunctions());
		state.setInit(super.getAllInitFunctions());
		state.setMerger(super.getAllMergerFunctions());
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable s) {
		try {
			AggregateTIPOState<Q, R, W> state = (AggregateTIPOState<Q, R, W>) s;
			this.transferArea = state.getTransferArea();
			this.transferArea.setTransfer(this);
			this.groups = state.getGroups();
			for (FESortedClonablePair<SDFSchema, AggregateFunction> key : state
					.getEval().keySet()) {
				this.setEvalFunction(key, state.getEval().get(key));
			}
			for (FESortedClonablePair<SDFSchema, AggregateFunction> key : state
					.getInit().keySet()) {
				this.setInitFunction(key, state.getInit().get(key));
			}
			for (FESortedClonablePair<SDFSchema, AggregateFunction> key : state
					.getMerger().keySet()) {
				this.setMergeFunction(key, state.getMerger().get(key));
			}

		} catch (Throwable T) {
			logger.error("The serializable state to set for the AggregateTIPO is not a valid AggregateTIPOState!");
		}

	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("OutputQueueSize", transferArea.size() + "");
		map.put("Groups", groups.size()+"");
		map.put("LastInputTS", lastTimestamp+"");
		map.put("Watermark", transferArea.getWatermark() + "");
		return map;
	}
}

/**
 * This class represents points in time. The points are derived from time intervals. 
 * Each point stores the left or right side of the interval (isStartPoint = true --> left side, 
 * else right side)
 * Additionally, a load field can be used to keep reference data.
 * @author Marco Grawunder
 *
 */
class _Point<T> implements Comparable<_Point<T>> {
	/**
	 * The time stamp of the point
	 */
	final private PointInTime point;
	/**
	 * if set to true, this point was derived from a starting point of an interval, else it was derived from
	 * an end point
	 */
	final private boolean isStartPoint;
	
	/**
	 * Additional load that can be held
	 */
	final private T load;

	public _Point(
			PointInTime p,
			boolean isStartPoint,
			T element_agg) {
		this.point = p;
		this.isStartPoint = isStartPoint;
		this.load = element_agg;
	}

	@Override
	public int compareTo(_Point<T> p2) {
		int c = this.point.compareTo(p2.point);
		if (c == 0) {
			if (this.isStartPoint && !p2.isStartPoint) {
				// Endpunkte liegen immer vor Startpunkten
				c = 1;
			} else if (!this.isStartPoint && p2.isStartPoint) {
				c = -1;
			}
		}
		// start points of Partial aggregates are always before new elements
		// end points of Partial aggregates are always behind new elements
		if (c == 0) {
			if (this.newElement()) {
				if (this.isStartPoint) {
					c = 1;
				} else {
					c = -1;
				}
			}
		}
		return c;
	}

	public boolean isStart() {
		return isStartPoint;
	}

	public boolean isEnd() {
		return !isStartPoint;
	}
	
	public T getLoad() {
		return load;
	}
	
	public PointInTime getPoint() {
		return point;
	}
	
	public boolean before(_Point<T> other){
		return point.before(other.point);
	}

	public boolean before(PointInTime other){
		return point.before(other);
	}

	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((point == null) ? 0 : point.hashCode());
		result = PRIME * result + (isStartPoint ? 1231 : 1237);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final _Point<T> other = (_Point<T>) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		if (isStartPoint != other.isStartPoint)
			return false;
		return true;
	}

	boolean newElement() {
		return load == null;
	}

	@Override
	public String toString() {
		return (isStartPoint ? "s" : "e") + (newElement() ? "^" : "")
				+ point;
	}

}
