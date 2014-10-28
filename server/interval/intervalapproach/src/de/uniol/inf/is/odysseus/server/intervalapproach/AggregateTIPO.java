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

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

public class AggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregatePO<Q, R, W> implements IHasMetadataMergeFunction<Q>,
		IStatefulOperator, IStatefulPO {

	private IMetadataMergeFunction<Q> metadataMerge;
	
	private ITransferArea<W, W> transferArea;
	private Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups = new HashMap<>();
	private int dumpAtValueCount = -1;
	private long createOutputCounter = 0;
	private boolean outputPA = false;
	private boolean drainAtDone = true;
	private boolean drainAtClose = false;
	
	Logger logger = LoggerFactory.getLogger(AggregateTIPO.class);

	class _Point implements Comparable<_Point> {
		public PointInTime point;
		private boolean isStartPoint;
		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg;

		public _Point(
				PointInTime p,
				boolean isStartPoint,
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg) {
			this.point = p;
			this.isStartPoint = isStartPoint;
			this.element_agg = element_agg;
		}

		@Override
		public int compareTo(_Point p2) {
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
			final _Point other = (_Point) obj;
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
			return element_agg == null;
		}

		@Override
		public String toString() {
			return (isStartPoint ? "s" : "e") + (newElement() ? "^" : "")
					+ point;
		}

	}

	public AggregateTIPO(AggregateTIPO<Q, R, W> aggregatePO) {
		super(aggregatePO);
		this.metadataMerge = aggregatePO.metadataMerge.clone();
		metadataMerge.init();
		transferArea = new TITransferArea<W, W>();
	}

	public AggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
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
	protected void process_done(int port) {
		// has only one port, so process_done can be called when first input port calls done
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
			logger.debug("closing " +this.getName()+" done");
		}

	}

	@Override
	protected boolean isDone() {
		return super.isDone() && transferArea.size() == 0;
	}

	@Override
	protected void process_next(R object, int port) {

		///System.err.println("AGGREGATE DEBUG MG: IN " + object);
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
		transferArea.sendPunctuation(punctuation, port);
		createOutput(punctuation.getTime());
	}
	
	@Override
	public IMetadataMergeFunction<Q> getMetadataMerge() {
		return metadataMerge;
	}

	public void setMetadataMerge(IMetadataMergeFunction<Q> metadataMerge) {
		this.metadataMerge = metadataMerge;
	}

	// Dient dazu, alle Element in der Sweep-Area mit dem neuen Element zu
	// "verschneiden" und dabei ggf. neue Elemente zu
	// erzeugen
	// Erweitert um die M�glichkeit mehrere Aggregationsfunktionen auf
	// mehreren Attributen anwenden zu k�nnen
	// Methode nach [Kr�mer] Algorithmus 9 bzw. 10 funktioniert leider nicht
	// korrekt. Deswegen eigene Version
	protected List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, boolean outputPA) {
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues = new LinkedList<>();
		// System.err.println("");
		// System.err
		// .println("-------------------------------------------------------------------------");
		// System.err.println("INPUT " + elemToAdd);
		assert (elemToAdd != null);
		R newElement = elemToAdd;
		Q t_probe = elemToAdd.getMetadata();

		// Extract elements in this sweep area that overlaps the time interval
		// of elem
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		// No overlapping --> INIT: new Partial Aggregate
		if (!qualifies.hasNext()) {
			saInsert(sa, calcInit(newElement), t_probe);
		} else {
			// Overlapping --> Partial Aggregates need to be touched
			// List of points. Do not use a set, because elements can have same
			// start/end point!
			List<_Point> pl = getSortedIntersectionPoints(t_probe, qualifies);

			Iterator<_Point> pointIter = pl.iterator();
			_Point p1 = null;
			_Point p2 = null;
			// get the first point (Must be one)
			p1 = pointIter.next();
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate = p1.element_agg;
			while (pointIter.hasNext()) {
				p2 = pointIter.next();

				// Test all possible cases
				// Because the list is sorted, p2 cannot be before p1 and the
				// case p1 == p2 would
				// lead to an interval with length 0 which is not allowed
				// ATTENTION: Handle only the interval between p1 and p2
				// the next interval is treated in the next iteration!
				if (p1.point.before(p2.point)) {

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

						lastPartialAggregate = updateSAStartStart(sa, elemToAdd,
								p1, p2, lastPartialAggregate);

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
							: p1.element_agg;
				} else { // if (p1.point.before(p2.point))
					lastPartialAggregate = (p2.newElement()) ? lastPartialAggregate
							: p2.element_agg;
				}

				p1 = p2;
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace(sa.toString());
		}
		//System.err.println(sa.toString());
		return returnValues;
	}

	public void updateSAEndEnd(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			_Point p1,
			_Point p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate) {
		// OldEnd && NewEnd
		if (p2.newElement()) {
			updateSAEndStart(sa, elemToAdd, p1, p2);
		} else { // New End && Old End
			@SuppressWarnings("unchecked")
			Q newTI = (Q) lastPartialAggregate.getMetadata()
					.clone();
			newTI.setStartAndEnd(p1.point, p2.point);
			saInsert(sa, lastPartialAggregate, newTI);
		}
	}

	public void updateSAEndStart(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, _Point p1, _Point p2) {
		// New element has a part that is newer than the partial
		// aggregate
		@SuppressWarnings("unchecked")
		Q newTI = (Q) elemToAdd.getMetadata().clone();
		newTI.setStartAndEnd(p1.point, p2.point);
		saInsert(sa, calcInit(elemToAdd), newTI);
	}

	public void updateSAStartEnd(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			boolean outputPA,
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues,
			Q t_probe,
			_Point p1,
			_Point p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate) {
		// In some cases its possible to create output earlier
		final boolean createNew;
		if (!outputPA && p2.point.before(t_probe.getStart())) {
			createNew = false;
			PairMap<SDFSchema, AggregateFunction, W, Q> v = calcEval(lastPartialAggregate, false);
			v.setMetadata(lastPartialAggregate.getMetadata());
			v.getMetadata().setEnd(p2.point);
			returnValues.add(v);
		} else {
			createNew = !(p1.point.equals(lastPartialAggregate
					.getMetadata().getStart()) && p2.point
					.equals(lastPartialAggregate.getMetadata()
							.getEnd()));
		}

		Q newMeta = metadataMerge.mergeMetadata(
				lastPartialAggregate.getMetadata(),
				elemToAdd.getMetadata());
		newMeta.setStartAndEnd(p1.point, p2.point);
		saInsert(
				sa,
				calcMerge(lastPartialAggregate, elemToAdd,
						createNew), newMeta);
	}

	public PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> updateSAStartStart(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd,
			_Point p1,
			_Point p2,
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
			newMeta.setStartAndEnd(p1.point, p2.point);
			saInsert(sa, calcInit(elemToAdd), newMeta);
			lastPartialAggregate = p2.element_agg;
		} else {// p2.newElement()
				// Insert element again with shorter interval
				// (start to start)
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) p1.element_agg.getMetadata()
					.clone();
			newMeta.setStartAndEnd(p1.point, p2.point);
			saInsert(sa, p1.element_agg, newMeta);
		}
		return lastPartialAggregate;
	}

	public List<_Point> getSortedIntersectionPoints(
			Q t_probe,
			Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies) {
		List<_Point> pl = new LinkedList<_Point>();

		// Determine the list of all points of the overlapped elements in
		// the sweep area
		while (qualifies.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			ITimeInterval t_agg = element_agg.getMetadata();
			// Add the points with corresponding partial aggregates and info
			// if
			// point is start oder end into list of points
			pl.add(new _Point(t_agg.getStart(), true, element_agg));
			pl.add(new _Point(t_agg.getEnd(), false, element_agg));
		}
		// Add the time interval of the element to add in the list of points
		pl.add(new _Point(t_probe.getStart(), true, null));
		pl.add(new _Point(t_probe.getEnd(), false, null));

		// Sort the List
		Collections.sort(pl);
		return pl;
	}

	// Updates SA by splitting all partial aggregates before split point
	@SuppressWarnings("unchecked")
	protected List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PointInTime splitPoint) {
//		System.err.println("BEFORE "+splitPoint);
//		System.err.println(sa.toString());
		ITimeInterval t_probe = new TimeInterval(splitPoint, splitPoint.plus(1));
		List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues = new LinkedList<>();
		
		// Determine elements in this sweep area containing splitpoint
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		while (qualifies.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			if (element_agg.getMetadata().getStart().before(splitPoint)) {
				PairMap<SDFSchema, AggregateFunction, W, Q> e = calcEval(element_agg, false);
				e.setMetadata((Q) element_agg.getMetadata().clone());
				e.getMetadata().setEnd(splitPoint);
				element_agg.getMetadata().setStart(splitPoint);
				returnValues.add(e);
			}
			sa.insert(element_agg);
		}
		
//		System.err.println("AFTER ");
//		System.err.println(sa.toString());

		
		return returnValues;
	}

	private void saInsert(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> elem,
			Q t) {
		// System.out.println("SA Insert "+elem+" "+t);
		elem.setMetadata(t);
		sa.insert(elem);
	}

	@Override
	public Serializable getState() {
		
		AggregateTIPOState state = new AggregateTIPOState();
		state.transferArea = this.transferArea;
		state.groups = this.groups;
		return state;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable s) {
		
		try {
			
			AggregateTIPOState state = (AggregateTIPOState) s;
			this.transferArea = state.transferArea;
			this.transferArea.setTransfer(this);
			this.groups = state.groups;
			
		} catch(Throwable T) {
		
			logger.error("The serializable state to set for the AggregateTIPO is not a valid AggregateTIPOState!");
			
		}
		
	}
	
	/**
	 * The current state of an {@link AggregateTIPO} is defined by 
	 * its transfer area and its groups.
	 * 
	 * @author Michael Brand
	 *
	 */
	private class AggregateTIPOState implements Serializable {
		
		private static final long serialVersionUID = 9088231287860150949L;

		ITransferArea<W,W> transferArea;
		
		Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups;
		
	}

}
