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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AggregateTIPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>, W extends IClone>
		extends AggregatePO<Q, R, W> {

	protected IMetadataMergeFunction<Q> metadataMerge;

	class _Point implements Comparable<_Point> {
		public PointInTime point;
		private boolean isStartPoint;
		PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg;

		public _Point(
				PointInTime p,
				boolean isStartPoint,
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg) {
			this.point = p;
			this.isStartPoint = isStartPoint;
			this.element_agg = element_agg;
		}

		@Override
		public int compareTo(_Point p2) {
			int c = this.point.compareTo(p2.point);
			if (c == 0) {
				if (this.isStartPoint && !p2.isStartPoint) { // Endpunkte liegen
																// immer vor
					// Startpunkten
					c = 1;
				} else if (!this.isStartPoint && p2.isStartPoint) {
					c = -1;
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

	public AggregateTIPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
	}

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
			DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd) {
		R e_probe = elemToAdd;
		Q t_probe = elemToAdd.getMetadata();

		// Extract elements in this sweep area that overlaps the time interval
		// of elem
		Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		// No overlapping --> INIT: new Partial Aggregate
		if (!qualifies.hasNext()) {
			saInsert(sa, calcInit(e_probe), t_probe);
		} else {
			// Overlapping --> Partial Aggregates need to be touched
			SortedSet<_Point> pl = new TreeSet<_Point>();

			// Determine the list of all points of the overlapped elements in
			// the sweep area
			while (qualifies.hasNext()) {
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
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

			// Sort the list of points ascending
			Iterator<_Point> iter = pl.iterator();
			_Point p1 = null;
			_Point p2 = null;
			// get the first point
			if (iter.hasNext()) {
				p1 = iter.next();
			}
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate = p1.element_agg;
			while (iter.hasNext()) {
				p2 = iter.next();

				// Test all possible cases
				// Because the list is sorted, p2 cannot be before p1 and the
				// case p1 == p2 would
				// lead to an interval with length 0 which is not allowed
				// ATTENTION: Handle only the interval between p1 and p2
				// the next interval is treted in the next iteration!
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
							Q newMeta = p1.element_agg.getMetadata();
							newMeta.setStartAndEnd(p1.point, p2.point);
							saInsert(sa, calcInit(elemToAdd), newMeta);
						} else {// p2.newElement()
								// Insert element again with shorter interval
								// (start to start)
							Q newMeta = p1.element_agg.getMetadata();
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
						Q newTI = elemToAdd.getMetadata();
						newTI.setStartAndEnd(p1.point, p2.point);
						saInsert(sa, calcInit(elemToAdd), newTI);
					} else if (p1.isEnd() && p2.isEnd()) { // Element after
						// OldEnd && NewEnd
						if (p2.newElement()) {
							Q newTI = elemToAdd.getMetadata();
							newTI.setStartAndEnd(p1.point, p2.point);
							saInsert(sa, calcInit(elemToAdd), newTI);
						} else { // New End && Old End
							Q newTI = lastPartialAggregate.getMetadata();
							newTI.setStartAndEnd(p1.point, p2.point);
							saInsert(sa, lastPartialAggregate, newTI);
						}
					}
				}

				// Remember the last seen partial aggregate (not the new
				// element)
				lastPartialAggregate = (p1.newElement()) ? lastPartialAggregate
						: p1.element_agg;
				p1 = p2;
			}
		}
		// System.err.println(sa.toString());
	}

	// Updates SA by splitting all partial aggregates before split point
	@SuppressWarnings("unchecked")
	protected synchronized void updateSA(
			DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PointInTime splitPoint) {

		ITimeInterval t_probe = new TimeInterval(splitPoint, splitPoint.plus(1));

		// Determine elements in this sweep area containing splitpoint
		Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.extractOverlaps(t_probe);
		while (qualifies.hasNext()) {
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			if (element_agg.getMetadata().getStart().before(splitPoint)) {
				// TODO: Is removal necessary or is update of metadata enough?
				// Remove current element
				// sa.remove(element_agg);
				// and split into two new elements
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> copy = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>(
						element_agg, true);

				copy.setMetadata((Q) element_agg.getMetadata().clone());
				element_agg.getMetadata().setEnd(splitPoint);
				sa.insert(element_agg);
				copy.getMetadata().setStart(splitPoint);
				sa.insert(copy);
			}

		}
	}

	private PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> saInsert(
			DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> elem,
			Q t) {
		// System.out.println("SA Insert "+elem+" "+t);
		elem.setMetadata(t);
		sa.insert(elem);
		return elem;
	}

}
