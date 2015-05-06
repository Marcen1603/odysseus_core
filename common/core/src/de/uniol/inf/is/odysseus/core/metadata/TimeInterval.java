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
package de.uniol.inf.is.odysseus.core.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

// TODO: Noch mal ber die Grenzen nachdenken (Wann <=, wann <)
// TODO: Gibt es evtl. effizientere Algorithmen?

/**
 * Klasse, mit deren Hilfe ein diskretes (!) rechtsoffenes Intervall zwischen
 * zwei Zeitpunkten (PointInTime) definiert werden kann die linke Grenze muss
 * immer kleiner oder gleich der rechten Grenze sein
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */

final public class TimeInterval extends AbstractBaseMetaAttribute implements
		ITimeInterval, Cloneable, Serializable {

	private static final long serialVersionUID = 2210545271466064814L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITimeInterval.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(
			classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("TimeInterval", "start",
				SDFDatatype.TIMESTAMP, null));
		attributes.add(new SDFAttribute("TimeInterval", "end",
				SDFDatatype.TIMESTAMP, null));
		schema.add(SDFSchemaFactory.createNewMetaSchema("TimeInterval",
				Tuple.class, attributes, ITimeInterval.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	private static final TimeInterval forever = new TimeInterval(
			new PointInTime(0), PointInTime.getInfinityTime());

	private PointInTime start;
	private PointInTime end;

	public TimeInterval(PointInTime start, PointInTime end) {
		init(start, end);
	}

	public TimeInterval(PointInTime start) {
		init(start, PointInTime.getInfinityTime());
	}

	public TimeInterval() {
		init(PointInTime.getInfinityTime(), PointInTime.getInfinityTime());
	}

	public TimeInterval(TimeInterval original) {
		PointInTime start = original.getStart().clone();
		PointInTime end = original.getEnd().clone();
		init(start, end);
	}

	protected void init(PointInTime start, PointInTime end) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(2, false);
		t.setAttribute(0, start.getMainPoint());
		t.setAttribute(1, end.getMainPoint());
		values.add(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (Long) start.point;
		case 1:
			return (K) (Long) end.point;
		}
		return null;
	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.start = new PointInTime((long)value.getAttribute(0));
		this.end = new PointInTime((long)value.getAttribute(1));
	}
	
	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new TimeIntervalInlineMetadataMergeFunction();
	}

	public boolean isEndInfinite() {
		return end.isInfinite();
	}

	@Override
	public void setEnd(PointInTime end) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.end = end;
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		init(start, end);
	}

	public boolean isPoint() {
		return start.equals(end);
	}

	public boolean includes(PointInTime p) {
		return p.before(this.getEnd()) && p.afterOrEquals(this.getStart());
	}

	public static boolean startsBefore(ITimeInterval interval,
			ITimeInterval interval2) {
		return interval.getStart().before(interval2.getStart());
	}

	public static boolean startsBeforeOrEqual(ITimeInterval interval,
			ITimeInterval interval2) {
		return interval.getStart().beforeOrEquals(interval2.getStart());
	}

	/**
	 * 
	 * @param left
	 *            linkes Intervall
	 * @param right
	 *            rechtes Intervall
	 * @return true, wenn sowohl der Startzeitpunkt von left vor dem
	 *         Startzeitpunkt von right liegt und der Endzeitpunkt von left vor
	 *         ist ( NEIN! oder gleich dem Endzeitpunkt von right ist)
	 */
	public static boolean totallyBefore(ITimeInterval left, ITimeInterval right) {
		return totallyBefore(left, right.getStart());
	}

	public static boolean totallyBefore(ITimeInterval interval,
			PointInTime point) {
		// ACHTUNG: Rechtsoffenes Intervall, d.h. der letzte Punkte geh�rt
		// nicht
		// mehr dazu
		return interval.getEnd().beforeOrEquals(point);
	}

	public static boolean totallyAfter(ITimeInterval left, ITimeInterval right) {
		return totallyBefore(right, left);
	}

	public static boolean overlaps(ITimeInterval left, ITimeInterval right) {
		return !(totallyBefore(left, right) || totallyAfter(left, right));
	}

	public boolean overlaps(ITimeInterval t) {
		return overlaps(this, t);
	}

	/**
	 * Liegt das linke Intervall innerhalb des rechten
	 * 
	 * @param left
	 *            Linkes Intervall
	 * @param right
	 *            Rechtes Intervall
	 * @return true, wenn left inside right, false sonst
	 */
	public static boolean inside(ITimeInterval left, ITimeInterval right) {
		return right.getStart().beforeOrEquals(left.getStart())
				&& left.getEnd().beforeOrEquals(right.getEnd());
	}

	/**
	 * Liegt der Punkt innerhalb des Intervals
	 * 
	 * @param interval
	 * @param timestamp
	 * @return
	 */
	public static boolean inside(ITimeInterval interval, PointInTime timestamp) {
		return interval.getStart().beforeOrEquals(timestamp)
				&& interval.getEnd().after(timestamp);
	}

	public static TimeInterval intersection(ITimeInterval left,
			ITimeInterval right) {
		if (overlaps(left, right)) {
			// TODO fehler bei infinity (auch in anderen operationen vorhanden)
			PointInTime newLeft = PointInTime.max(left.getStart(),
					right.getStart());
			PointInTime newRight = PointInTime.min(left.getEnd(),
					right.getEnd());
			if (newLeft.before(newRight)) {
				return new TimeInterval(newLeft, newRight);
			}
		}
		return null;
	}

	public static TimeInterval union(ITimeInterval left, ITimeInterval right) {
		if (overlaps(left, right)) {
			return new TimeInterval(PointInTime.min(left.getStart(),
					right.getStart()), PointInTime.max(left.getEnd(),
					right.getEnd()));
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((end == null) ? 0 : end.hashCode());
		result = PRIME * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		ITimeInterval ti = (ITimeInterval) obj;
		return ti.getStart().equals(this.getStart())
				&& ti.getEnd().equals(this.getEnd());
	}

	/*
	 * @TODO Difference method is returning a distance between time intervals
	 * and not the difference, example for an time interval A and an inside time
	 * interval B: A - B = two time intervals left and right to A.
	 * 
	 * minus method implements the difference
	 */

	public static ITimeInterval[] difference(ITimeInterval left,
			ITimeInterval right) {
		if (inside(left, right)) {
			return new ITimeInterval[] { null, null };
		}
		if (overlaps(left, right)) {
			ITimeInterval[] ret = new ITimeInterval[2];
			if (left.getStart().before(right.getStart())) {
				ret[0] = new TimeInterval(left.getStart(), right.getStart());
			}
			if (left.getEnd().after(right.getEnd())) {
				ret[1] = new TimeInterval(right.getEnd(), left.getEnd());
			}
			return ret;
		}
		return new ITimeInterval[] { left, null };
	}

	/**
	 * Difference method is returning a distance between time intervals and not
	 * the difference, example for an time interval A and an inside time
	 * interval B: A - B = two time intervals left and right to A.
	 * 
	 * The method implements the behaviour difference method should have.
	 * 
	 * @TODO wrap equal if-clause bodies later; and create temporary points for
	 *       starts and ends.
	 * 
	 * @param minuend
	 * @param subtrahend
	 * @author Jendrik Poloczek
	 * @return
	 */
	public static List<TimeInterval> minus(ITimeInterval minuend,
			ITimeInterval subtrahend) {
		List<TimeInterval> difference = new ArrayList<TimeInterval>();

		if (minuend.equals(subtrahend) || !overlaps(minuend, subtrahend)
				|| TimeInterval.inside(minuend, subtrahend)) {
			return difference;
		}

		if (minuend.getStart().equals(subtrahend.getStart())) {
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getEnd(), minuend
						.getEnd()));
				return difference;
			}
			difference.add(new TimeInterval(minuend.getEnd(), subtrahend
					.getEnd()));
			return difference;
		}

		if (minuend.getStart().before(subtrahend.getStart())) {
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(minuend.getStart(), subtrahend
						.getStart()));
				difference.add(new TimeInterval(subtrahend.getEnd(), minuend
						.getEnd()));
				return difference;
			}
			if (minuend.getEnd().before(subtrahend.getEnd())) {
				difference.add(new TimeInterval(minuend.getStart(), subtrahend
						.getStart()));
				return difference;
			}
			difference.add(new TimeInterval(minuend.getStart(), subtrahend
					.getStart()));
			return difference;
		}

		if (subtrahend.getStart().before(minuend.getStart())) {
			if (minuend.getEnd().before(subtrahend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getStart(), minuend
						.getStart()));
				difference.add(new TimeInterval(minuend.getEnd(), subtrahend
						.getEnd()));
				return difference;
			}
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getStart(), minuend
						.getStart()));
				return difference;
			}
			difference.add(new TimeInterval(subtrahend.getStart(), minuend
					.getStart()));
			return difference;
		}

		return null;
	}

	/**
	 * Beim Vergleich werden zunchst die Startzeitpunkte und dann die
	 * Endzeitpunkte der Intervalle betrachtet
	 * 
	 */
	@Override
	public int compareTo(ITimeInterval toCompare) {
		int s = this.getStart().compareTo(toCompare.getStart());
		if (s == 0) { // Wenn Startpunkte gleich sind, die Endpunkte
			// vergleichen
			s = this.getEnd().compareTo(toCompare.getEnd());
		}
		return s;
	}

	@Override
	public PointInTime getStart() {
		return start;
	}

	@Override
	public void setStart(PointInTime start) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.start = start;
	}

	@Override
	public PointInTime getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return getStart().toString() + "|" + getEnd().toString();
	}

	public static TimeInterval parseTimeInterval(String str) {
		String[] parts = str.split("\\|");
		PointInTime start = PointInTime.parsePointInTime(parts[0].trim());
		PointInTime end = PointInTime.parsePointInTime(parts[1].trim());
		return new TimeInterval(start, end);
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "[" + getStart().minus(baseTime).toString() + ","
				+ getEnd().minus(baseTime).toString() + ")";
	}

	@Override
	public String csvToString(WriteOptions options) {
		return getStart().toString() + options.getDelimiter()
				+ getEnd().toString();
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return "start" + delimiter + "end";
	}

	public static TimeInterval forever() {
		return forever.clone();
	}

	@Override
	public TimeInterval clone() {
		return new TimeInterval(this);
	}

	@Override
	public String getName() {
		return "TimeInterval";
	}
}
