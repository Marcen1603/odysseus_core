package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AggregateTIPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>>
		extends AggregatePO<Q, R, R> {
	private Class<Q> metadataType;

	public Class<Q> getMetadataType() {
		return metadataType;
	}

	public void setMetadataType(Class<Q> metadataType) {
		this.metadataType = metadataType;
	}

	class _Point implements Comparable<_Point> {
		public PointInTime p;
		boolean startP;
        PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg;

		public _Point(
				PointInTime p,
				boolean startP,
                PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg) {
			this.p = p;
			this.startP = startP;
			this.element_agg = element_agg;
		}

		@Override
		public int compareTo(_Point p2) {
			int c = this.p.compareTo(p2.p);
			if (c == 0) {
				if (this.startP && !p2.startP) { // Endpunkte liegen immer vor
					// Startpunkten
					c = 1;
				} else if (!this.startP && p2.startP) {
					c = -1;
				}
			}
			return c;
		}

		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((p == null) ? 0 : p.hashCode());
			result = PRIME * result + (startP ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final _Point other = (_Point) obj;
			if (p == null) {
				if (other.p != null)
					return false;
			} else if (!p.equals(other.p))
				return false;
			if (startP != other.startP)
				return false;
			return true;
		}

		boolean isDach() {
			return element_agg == null;
		}

		@Override
		public String toString() {
			return (startP ? "s" : "e") + (isDach() ? "^" : "") + p;
		}
	}

	public AggregateTIPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			List<SDFAttribute> groupingAttributes,
            Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations,
			Class<Q> metadataType) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		setMetadataType(metadataType);
	}

	public AggregateTIPO(AggregateTIPO<Q, R> aggregatePO) {
		super(aggregatePO);
		this.metadataType = aggregatePO.metadataType;
	}

	public AggregateTIPO(SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			List<SDFAttribute> groupingAttributes,
            Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
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
			R element) {
        Iterator<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> qualifies = sa
				.queryOverlaps(element.getMetadata());
		// System.out.println("updateSA MinTS: "+sa.getMinTs()+"  Size:"+sa.size()+" mit "+element);
		R e_probe = element;
		Q t_probe = element.getMetadata();
		// sa.dumpContent(false);
		if (!qualifies.hasNext()) {
			// System.out.println("updateSA initiales Einfuegen");
			saInsert(sa, calcInit(e_probe), t_probe);
		} else {
			// Punktliste generieren
			SortedSet<_Point> pl = new TreeSet<_Point>();
			// Erst die Elemente der SweepArea
			while (qualifies.hasNext()) {
                PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> element_agg = qualifies
						.next();
				sa.remove(element_agg);
				ITimeInterval t_agg = element_agg.getMetadata();
				pl.add(new _Point(t_agg.getStart(), true, element_agg));
				pl.add(new _Point(t_agg.getEnd(), false, element_agg));
			}
			// Dann das zu vergleichende Element
			pl.add(new _Point(t_probe.getStart(), true, null));
			pl.add(new _Point(t_probe.getEnd(), false, null));

			// System.out.println("Punktliste "+pl);

			Iterator<_Point> iter = pl.iterator();
			_Point p1 = null;
			_Point p2 = null;
			if (iter.hasNext()) {
				p1 = iter.next();
			}
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> curr_agg = p1.element_agg;
			while (iter.hasNext()) {
				p2 = iter.next();
				if (p1.p.before(p2.p)) { // Ansonsten w�re das ein leeres
					// Intervall, das nicht betrachtet
					// werden muss
					// TODO ggflls. Metadaten aggregieren
					Q newTI;
					try {
						newTI = getMetadataType().newInstance();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					newTI.setStart(p1.p);
					newTI.setEnd(p2.p);

					// Merken, falls zwei Dachpunkte in der Folge sind (S^-->E^)
					if (!p2.isDach()) {
						curr_agg = p2.element_agg;
					}
					if (p1.startP && p2.startP) { // Element vorher
						if (!p1.isDach() && p2.isDach()) { // S --> S^
							saInsert(sa, curr_agg, newTI);
						} else { // S^ --> S
							saInsert(sa, calcInit(element), newTI);
						}
						// alle anderen F�lle gehen nicht, weil sich die
						// Intervalle schneiden!
					} else if (p1.startP && !p2.startP) { // Schnitt (f�r alle
						// gleich!)
						saInsert(sa, calcMerge(curr_agg, element), newTI);
					} else if (!p1.startP && p2.startP) { // Zwischelement E^
						// --> S
						// Muss ein Init auf dem neuen Element sein, da es hier
						// keinen Schnitt gibt
						saInsert(sa, calcInit(element), newTI);
					} else if (!p1.startP && !p2.startP) { // Element danach
						if (!p1.isDach() && p2.isDach()) { // E --> E^
							saInsert(sa, calcInit(element), newTI);
						} else { // E^ --> E
							saInsert(sa, curr_agg, newTI);
						}
					}
				}
				p1 = p2;
			}
		}
	}

    private PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> saInsert(
            DefaultTISweepArea<PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q>> sa,
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, Q> elem,
			Q t) {
		// System.out.println("SA Insert "+elem);
		elem.setMetadata(t);
		sa.insert(elem);
		return elem;
	}
	
}
