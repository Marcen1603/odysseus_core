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
import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * Klasse, mit deren Hilfe ein diskretes (!) rechtsoffenes Intervall zwischen
 * zwei Zeitpunkten (PointInTime) definiert werden kann die linke Grenze muss
 * immer kleiner oder gleich der rechten Grenze sein
 *
 * @author Marco Grawunder, Jonas Jacobi
 */

final public class TimeInterval extends AbstractBaseMetaAttribute implements ITimeInterval, Cloneable, Serializable {

	private static final long serialVersionUID = 2210545271466064814L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITimeInterval.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("TimeInterval", "start", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("TimeInterval", "end", SDFDatatype.TIMESTAMP));
		schema.add(SDFSchemaFactory.createNewMetaSchema("TimeInterval", Tuple.class, attributes, ITimeInterval.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	private static final TimeInterval forever = new TimeInterval(new PointInTime(0), PointInTime.getInfinityTime());

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
		// New: Allow elements with zero valid time
		if (!start.beforeOrEquals(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval [" + start + "," + end + ")");
		}
		this.start = start;
		this.end = end;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(2, false);
		t.setAttribute(0, start.isInfinite() ? null : start.getMainPoint());
		t.setAttribute(1, end.isInfinite() ? null : end.getMainPoint());
		values.add(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (start.isInfinite() ? null : start.point);
		case 1:
			return (K) (end.isInfinite() ? null : end.point);
		}
		return null;
	}

	@Override
	public void writeValue(Tuple<?> value) {
		Object v1 = value.getAttribute(0);
		Object v2 = value.getAttribute(1);

		if (v1 != null) {
			this.start = new PointInTime((long) v1);
		} else {
			this.start = PointInTime.INFINITY;
		}
		if (v2 != null) {
			this.end = new PointInTime((long) v2);
		} else {
			this.end = PointInTime.INFINITY;
		}

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
		if (!start.beforeOrEquals(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval [" + start + "," + end + ")");
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

	public static boolean startsBefore(ITimeInterval interval, ITimeInterval interval2) {
		return interval.getStart().before(interval2.getStart());
	}

	public static boolean startsBeforeOrEqual(ITimeInterval interval, ITimeInterval interval2) {
		return interval.getStart().beforeOrEquals(interval2.getStart());
	}

	/**
	 *
	 * @param left
	 *            linkes Intervall
	 * @param right
	 *            rechtes Intervall
	 * @return true, wenn sowohl der Startzeitpunkt von left vor dem Startzeitpunkt
	 *         von right liegt und der Endzeitpunkt von left vor ist ( NEIN! oder
	 *         gleich dem Endzeitpunkt von right ist)
	 */
	public static boolean totallyBefore(ITimeInterval left, ITimeInterval right) {
		return totallyBefore(left, right.getStart());
	}

	public static boolean totallyBefore(ITimeInterval interval, PointInTime point) {
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
		return right.getStart().beforeOrEquals(left.getStart()) && left.getEnd().beforeOrEquals(right.getEnd());
	}

	/**
	 * Liegt der Punkt innerhalb des Intervals
	 *
	 * @param interval
	 * @param timestamp
	 * @return
	 */
	public static boolean inside(ITimeInterval interval, PointInTime timestamp) {
		return interval.getStart().beforeOrEquals(timestamp) && interval.getEnd().after(timestamp);
	}

	public static TimeInterval intersection(ITimeInterval left, ITimeInterval right) {
		if (overlaps(left, right)) {
			// TODO fehler bei infinity (auch in anderen operationen vorhanden)
			PointInTime newLeft = PointInTime.max(left.getStart(), right.getStart());
			PointInTime newRight = PointInTime.min(left.getEnd(), right.getEnd());
			if (newLeft.before(newRight)) {
				return new TimeInterval(newLeft, newRight);
			}
		}
		return null;
	}

	/**
	 * Union of the left and the right interval, iff they are overlapping
	 */
	public static TimeInterval union(ITimeInterval left, ITimeInterval right) {
		if (overlaps(left, right)) {
			return new TimeInterval(PointInTime.min(left.getStart(), right.getStart()),
					PointInTime.max(left.getEnd(), right.getEnd()));
		}
		return null;
	}

	/**
	 * @return True, if the end of the left is equal to the start of the right or
	 *         the other way around.
	 */
	public static boolean areAdjacent(ITimeInterval left, ITimeInterval right) {
		return left.getEnd().equals(right.getStart()) || right.getEnd().equals(left.getStart());
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
		return ti.getStart().equals(this.getStart()) && ti.getEnd().equals(this.getEnd());
	}

	/*
	 * @TODO Difference method is returning a distance between time intervals and
	 * not the difference, example for an time interval A and an inside time
	 * interval B: A - B = two time intervals left and right to A.
	 *
	 * minus method implements the difference
	 */

	public static ITimeInterval[] difference(ITimeInterval left, ITimeInterval right) {
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
	 * Difference method is returning a distance between time intervals and not the
	 * difference, example for an time interval A and an inside time interval B: A -
	 * B = two time intervals left and right to A.
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
	public static List<TimeInterval> minus(ITimeInterval minuend, ITimeInterval subtrahend) {
		List<TimeInterval> difference = new ArrayList<TimeInterval>();

		if (minuend.equals(subtrahend) || !overlaps(minuend, subtrahend) || TimeInterval.inside(minuend, subtrahend)) {
			return difference;
		}

		if (minuend.getStart().equals(subtrahend.getStart())) {
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getEnd(), minuend.getEnd()));
				return difference;
			}
			difference.add(new TimeInterval(minuend.getEnd(), subtrahend.getEnd()));
			return difference;
		}

		if (minuend.getStart().before(subtrahend.getStart())) {
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(minuend.getStart(), subtrahend.getStart()));
				difference.add(new TimeInterval(subtrahend.getEnd(), minuend.getEnd()));
				return difference;
			}
			if (minuend.getEnd().before(subtrahend.getEnd())) {
				difference.add(new TimeInterval(minuend.getStart(), subtrahend.getStart()));
				return difference;
			}
			difference.add(new TimeInterval(minuend.getStart(), subtrahend.getStart()));
			return difference;
		}

		if (subtrahend.getStart().before(minuend.getStart())) {
			if (minuend.getEnd().before(subtrahend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getStart(), minuend.getStart()));
				difference.add(new TimeInterval(minuend.getEnd(), subtrahend.getEnd()));
				return difference;
			}
			if (subtrahend.getEnd().before(minuend.getEnd())) {
				difference.add(new TimeInterval(subtrahend.getStart(), minuend.getStart()));
				return difference;
			}
			difference.add(new TimeInterval(subtrahend.getStart(), minuend.getStart()));
			return difference;
		}

		return null;
	}

	/**
	 * Beim Vergleich werden zunchst die Startzeitpunkte und dann die Endzeitpunkte
	 * der Intervalle betrachtet
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
					"start point is not before end point in time interval [" + start + "," + end + ")");
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

	public static TimeInterval forever() {
		return forever.clone();
	}

	public boolean isEmpty() {
		return this.start.equals(this.end);
	}

	@Override
	public TimeInterval clone() {
		return new TimeInterval(this);
	}

	@Override
	public String getName() {
		return "TimeInterval";
	}

	/**
	 * cuts out all time intervals given in the interval list from the target time
	 * interval
	 * 
	 * @param target
	 *            the interval to cut
	 * @param stencil
	 *            list of intervals that should be cut out from the target
	 * @return a list of time intervals that remain from the target time interval
	 *         after cutting out. The list may be empty if the target time interval
	 *         is fully covered by the given intervals in the input list.
	 */
	public static ArrayList<TimeInterval> cutOutIntervals(ITimeInterval target, List<ITimeInterval> stencil) {
		ArrayList<TimeInterval> result = new ArrayList<TimeInterval>();

		TimeInterval left = new TimeInterval(target.getStart(), target.getEnd());
		AtomicBoolean broken = new AtomicBoolean(false);

		for (ITimeInterval right : stencil) {
			left = cutoutInterval(result, left, broken, right);

			if (broken.get()) {
				break;
			}
		}

		if (left != null) {
			result.add(left);
		}

		return result;
	}

	/**
	 * cuts out the time interval stencil from the time interval target
	 * 
	 * @param target
	 *            the time interval to be cut out
	 * @param stencil
	 *            the time interval to remove from target
	 * @return a list of remaining time intervals after cut out. may be empty if
	 *         target is completely covered by stencil.
	 */
	public static ArrayList<TimeInterval> cutOutInterval(ITimeInterval target, ITimeInterval stencil) {
		ArrayList<TimeInterval> result = new ArrayList<TimeInterval>();

		TimeInterval left = cutoutInterval(result, new TimeInterval(target.getStart(), target.getEnd()),
				new AtomicBoolean(false), stencil);

		if (left != null) {
			result.add(left);
		}

		return result;
	}

	/**
	 * 
	 * @param result
	 *            list for collecting resulting time intervals
	 * @param left
	 *            the left time interval (that should be cut out)
	 * @param broken
	 *            flag indicating if a calling loop should break because consecutive
	 *            elements will not change the result due to their ordering
	 * @param right
	 *            the time interval that should be cut out from the other interval
	 * @return the current remainder of the time interval (left) after cut out. may
	 *         be null if the time interval left is totally covered by right
	 */
	private static TimeInterval cutoutInterval(ArrayList<TimeInterval> result, TimeInterval left, AtomicBoolean broken,
			ITimeInterval right) {
		// handle different cases
		// if right is completely before left: ignore right
		if (right.getStart().beforeOrEquals(left.getStart()) && left.getStart().before(right.getEnd())
				&& right.getEnd().before(left.getEnd())) {
			// beginning of left is covered by right
			left = new TimeInterval(right.getEnd(), left.getEnd());
		} else if (right.getStart().beforeOrEquals(left.getStart()) && left.getEnd().beforeOrEquals(right.getEnd())) {
			// left is fully covered by right
			left = null;
			broken.set(true);
		} else if (left.getStart().before(right.getStart()) && right.getEnd().before(left.getEnd())) {
			// left is split by right
			result.add(new TimeInterval(left.getStart(), right.getStart()));
			left = new TimeInterval(right.getEnd(), left.getEnd());
		} else if (left.getStart().before(right.getStart()) && right.getStart().before(left.getEnd())
				&& left.getEnd().beforeOrEquals(right.getEnd())) {
			// end of left is covered by right
			result.add(new TimeInterval(left.getStart(), right.getStart()));
			left = null;
		} else if (left.getEnd().beforeOrEquals(right.getStart())) {
			// left is completely before right
			broken.set(true);
		}

		if (left != null && TimeInterval.isZeroLength(left)) {
			left = null;
			broken.set(true);
		}
		return left;
	}

	/**
	 * 
	 * @return true if start point and end point of the interval are equal
	 */
	public static boolean isZeroLength(ITimeInterval interval) {
		return interval.getStart().equals(interval.getEnd());
	}
}
