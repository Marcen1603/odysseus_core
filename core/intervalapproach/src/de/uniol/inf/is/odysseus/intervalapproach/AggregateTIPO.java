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

public abstract class AggregateTIPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>>
		extends AggregatePO<Q, R, R> {

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

	public AggregateTIPO(AggregateTIPO<Q, R> aggregatePO) {
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
			R elem) {
		R e_probe = elem;
		Q t_probe = elem.getMetadata();

		// Determine elements in this sweep area that overlaps the time interval
		// of elem
		Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.queryOverlaps(t_probe);
		if (!qualifies.hasNext()) { // insert new partial aggregate
			saInsert(sa, calcInit(e_probe), t_probe);
		} else {
			// Generate list of all points
			SortedSet<_Point> pl = new TreeSet<_Point>();
			// Add intersecting sweep area elements to this point list
			// remove element from sweep area
			while (qualifies.hasNext()) {
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
						.next();
				sa.remove(element_agg);
				ITimeInterval t_agg = element_agg.getMetadata();
				pl.add(new _Point(t_agg.getStart(), true, element_agg));
				pl.add(new _Point(t_agg.getEnd(), false, element_agg));
			}
			// Add the new Element
			pl.add(new _Point(t_probe.getStart(), true, null));
			pl.add(new _Point(t_probe.getEnd(), false, null));

			// List of points is sorted ascending
			Iterator<_Point> iter = pl.iterator();
			_Point p1 = null;
			_Point p2 = null;
			if (iter.hasNext()) {
				p1 = iter.next();
			}
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> curr_agg = p1.element_agg;
			while (iter.hasNext()) {
				p2 = iter.next();
				// Ansonsten waere das ein leeres
				// Intervall, das nicht betrachtet
				// werden muss
				if (p1.point.before(p2.point)) {

					// Two new elements are (NewStart-->NewEnd)
					if (!p2.newElement()) {
						curr_agg = p2.element_agg;
					}

					Q newTI = metadataMerge.mergeMetadata(
							curr_agg.getMetadata(), t_probe);
					newTI.setStartAndEnd(p1.point, p2.point);

					if (p1.isStart() && p2.isStart()) { // Element vorher
						if (!p1.newElement() && p2.newElement()) { // OldStart
																	// -->
																	// NewStart
							// Create new element with shorter validity
							saInsert(sa, curr_agg, newTI);
						} else { // NewStart --> OldStart
							saInsert(sa, calcInit(elem), newTI);
						}
						// In all other cases the elements would not intersect

					} else if (p1.isStart() && p2.isEnd()) {
						// Add new element combined from current value and new
						// element
						// for new time interval
						saInsert(sa, calcMerge(curr_agg, elem), newTI);

					} else if (p1.isEnd() && p2.isStart()) {
						// No intersection --> new element
						saInsert(sa, calcInit(elem), newTI);
					} else if (p1.isEnd() && p2.isEnd()) { // Element after
						// OldEnd && NewEnd
						if (!p1.newElement() && p2.newElement()) {
							saInsert(sa, calcInit(elem), newTI);
						} else { // New End && Old End
							saInsert(sa, curr_agg, newTI);
						}
					}
				}
				p1 = p2;
			}
		}
	}

	// Updates SA by splitting all partial aggregates before split point
	protected synchronized void updateSA(
			DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			PointInTime splitPoint) {

		ITimeInterval t_probe = new TimeInterval(splitPoint, splitPoint.plus(1));

		// Determine elements in this sweep area containing splitpoint
		Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.queryOverlaps(t_probe);
		while (qualifies.hasNext()) {
			PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
					.next();
			if (element_agg.getMetadata().getStart().before(splitPoint)) {
				// TODO: Is removal necessary or is update of metadata enough?
				// Remove current element
				sa.remove(element_agg);
				// and split into two new elements
				PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> copy = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>(
						element_agg);
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
