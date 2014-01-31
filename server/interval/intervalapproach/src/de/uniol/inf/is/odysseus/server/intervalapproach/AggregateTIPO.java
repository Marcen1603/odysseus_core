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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

public abstract class AggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<?>>
		extends AggregatePO<Q, R, W> implements IHasMetadataMergeFunction<Q>, IStatefulOperator {

	protected IMetadataMergeFunction<Q> metadataMerge;

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
	}

	public AggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations, boolean fastGrouping) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, fastGrouping);
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
	protected synchronized void updateSA(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd) {
//		System.err.println("");
//		System.err
//				.println("-------------------------------------------------------------------------");
//		System.err.println("INPUT " + elemToAdd);
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
			List<_Point> pl = new ArrayList<_Point>();

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

			// Sort the list of points ascending
			Iterator<_Point> pointIter = pl.iterator();
			_Point p1 = null;
			_Point p2 = null;
			// get the first point
			// FIXME This looks useless, if pointerIter has no next element a NullPointer exception will be thrown in the next line anyway (CKu 20140123)
			if (pointIter.hasNext()) {
				p1 = pointIter.next();
			}
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

						// This next case can only happen if p1 and p2 are from
						// the one element and the element is a sub part of an
						// the other element
						// ------------------------------------------------
						// ......AAAAAAAAAAAAAAAA..........................
						// .........EEEEEEE................................
						// In this case handle area from p1 und p2

					} else if (p1.isStart() && p2.isEnd()) {
						// Add new element as a combination from current value
						// and new
						// element for new time interval
					    // FIXME Use logger
						if (lastPartialAggregate == null) {
							System.err.println("ONLY FOR DEBUGGER!!");
							System.err.println(sa.toString());
						}
						Q newMeta = metadataMerge.mergeMetadata(
								lastPartialAggregate.getMetadata(),
								elemToAdd.getMetadata());
						newMeta.setStartAndEnd(p1.point, p2.point);
						saInsert(sa,
								calcMerge(lastPartialAggregate, elemToAdd),
								newMeta);

					} else if (p1.isEnd() && p2.isStart()) {
						// New element has a part that is newer than the partial
						// aggregate
						@SuppressWarnings("unchecked")
						Q newTI = (Q) elemToAdd.getMetadata().clone();
						newTI.setStartAndEnd(p1.point, p2.point);
						saInsert(sa, calcInit(elemToAdd), newTI);
					} else if (p1.isEnd() && p2.isEnd()) { // Element after
						// OldEnd && NewEnd
						if (p2.newElement()) {
							@SuppressWarnings("unchecked")
							Q newTI = (Q) elemToAdd.getMetadata().clone();
							newTI.setStartAndEnd(p1.point, p2.point);
							saInsert(sa, calcInit(elemToAdd), newTI);
						} else { // New End && Old End
							@SuppressWarnings("unchecked")
							Q newTI = (Q) lastPartialAggregate.getMetadata()
									.clone();
							newTI.setStartAndEnd(p1.point, p2.point);
							saInsert(sa, lastPartialAggregate, newTI);
						}
					}
					// Remember the last seen partial aggregate (not the new
					// element)
					lastPartialAggregate = (p1.newElement()) ? lastPartialAggregate
							: p1.element_agg;
				} else { // if (p1.point.before(p2.point))
					lastPartialAggregate = (p2.newElement())? lastPartialAggregate: p2.element_agg;
				}

				p1 = p2;
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace(sa.toString());
		}
//		System.err.println(sa.toString());
	}

	// Updates SA by splitting all partial aggregates before split point
	@SuppressWarnings("unchecked")
	protected synchronized void updateSA(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PointInTime splitPoint) {

		ITimeInterval t_probe = new TimeInterval(splitPoint, splitPoint.plus(1));

		// Determine elements in this sweep area containing splitpoint
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		while (qualifies.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			if (element_agg.getMetadata().getStart().before(splitPoint)) {
				// TODO: Is removal necessary or is update of metadata enough?
				// Remove current element
				// sa.remove(element_agg);
				// and split into two new elements
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> copy = new PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>(
						element_agg, true);

				copy.setMetadata((Q) element_agg.getMetadata().clone());
				element_agg.getMetadata().setEnd(splitPoint);
				sa.insert(element_agg);
				copy.getMetadata().setStart(splitPoint);
				sa.insert(copy);
			}

		}
	}

	private void saInsert(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> elem,
			Q t) {
		// System.out.println("SA Insert "+elem+" "+t);
		elem.setMetadata(t);
		sa.insert(elem);
	}

}
