package de.uniol.inf.is.odysseus.intervalapproach;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;


// TODO: Noch mal ber die Grenzen nachdenken (Wann <=, wann <)
// TODO: Gibt es evtl. effizientere Algorithmen?

/**
 * Klasse, mit deren Hilfe ein diskretes (!) rechtsoffenes Intervall zwischen
 * zwei Zeitpunkten (PointInTime) definiert werden kann die linke Grenze muss
 * immer kleiner oder gleich der rechten Grenze sein
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */

public class TimeInterval implements ITimeInterval, Cloneable, Serializable {

	private static final long serialVersionUID = 2210545271466064814L;

	private static final TimeInterval forever = new TimeInterval(
			new PointInTime(0, 0), new PointInTime());

	private PointInTime start;

	private PointInTime end;

	public TimeInterval(PointInTime start, PointInTime end) {
		init(start, end);
	}

	public TimeInterval(PointInTime start) {
		init(start, PointInTime.getInfinityTime());
	}

	public TimeInterval(){
		init(new PointInTime(System.currentTimeMillis(),0), PointInTime.getInfinityTime());
	}
	
	public TimeInterval(TimeInterval original) {
		PointInTime start = original.getStart().clone();
		if (original.getStart().isInfinite()) {
			start.setInfinite();
		}
		PointInTime end = original.getEnd().clone();
		if (original.getEnd().isInfinite()) {
			end.setInfinite();
		}
		init(start, end);
	}

	private void init(PointInTime start, PointInTime end) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.start = start;
		setEnd(end);
	}

	public boolean isEndInfinite() {
		return end.isInfinite();
	}

	public void setEnd(PointInTime end) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.end = end;
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
		// return left.getEnd().beforeOrEquals(right.getStart());
		// ACHTUNG: Rechtsoffenes Intervall, d.h. der letzte Punkte geh�rt nicht
		// mehr dazu
		return left.getEnd().beforeOrEquals(right.getStart());
	}

//	/**
//	 * Berhren sich die beiden Intervall an einer der beiden Grenzen
//	 * 
//	 * @param left
//	 *            Linkes Intervall
//	 * @param right
//	 *            Rechtes Intervall
//	 * @return true, wenn das Ende von left und das Start von right oder wenn
//	 *         der Start von left und das Ende von right zusammenfallen
//	 * 
//	 *         ACHTUNG! Rechtsoffenes Intervall --> evtl. Klassennamen anpassen?
//	 */
	// public static boolean meets(TimeInterval left, TimeInterval right) {
	// return left.getEnd().equals(right.getStart()) ||
	// left.getStart().equals(right.getEnd());
	// }
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

	public static TimeInterval intersection(ITimeInterval left,
			ITimeInterval right) {
		if (overlaps(left, right)) {
			// TODO fehler bei infinity (auch in anderen operationen vorhanden)
			PointInTime newLeft = PointInTime.max(left.getStart(), right
					.getStart());
			PointInTime newRight = PointInTime.min(left.getEnd(), right
					.getEnd());
			if (newLeft.before(newRight)) {
				return new TimeInterval(newLeft, newRight);
			}
		}
		return null;
	}

	public static TimeInterval union(ITimeInterval left, ITimeInterval right) {
		if (overlaps(left, right)) {
			return new TimeInterval(PointInTime.min(left.getStart(), right
					.getStart()), PointInTime
					.max(left.getEnd(), right.getEnd()));
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
		// if (this == obj)
		// return true;
		// if (!super.equals(obj))
		// return false;
		// if (getClass() != obj.getClass())
		// return false;
		// final TimeInterval other = (TimeInterval) obj;
		// if (end == null) {
		// if (other.getEnd() != null)
		// return false;
		// } else if (!end.equals(other.getEnd()))
		// return false;
		// if (start == null) {
		// if (other.getStart() != null)
		// return false;
		// } else if (!start.equals(other.getStart()))
		// return false;
		// return true;
	}

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
		} else {
			return new ITimeInterval[] { left, null };
		}
	}

	/**
	 * Beim Vergleich werden zunchst die Startzeitpunkte und dann die
	 * Endzeitpunkte der Intervalle betrachtet
	 * 
	 */
	public int compareTo(ITimeInterval toCompare) {
		int s = this.getStart().compareTo(toCompare.getStart());
		if (s == 0) { // Wenn Startpunkte gleich sind, die Endpunkte
			// vergleichen
			s = this.getEnd().compareTo(toCompare.getEnd());
		}
		return s;
	}

	public PointInTime getStart() {
		return start;
	}

	public void setStart(PointInTime start) {
		if (!start.before(end) && !(start.isInfinite() && end.isInfinite())) {
			throw new IllegalArgumentException(
					"start point is not before end point in time interval ["
							+ start + "," + end + ")");
		}
		this.start = start;
	}

	public PointInTime getEnd() {
		return end;
	}

	@Override
	public String toString() {
		//return "[" + getStart().toString() + "," + getEnd().toString() + ")";
		return getStart().toString() + "|" + getEnd().toString();
	}

	public String toString(PointInTime baseTime) {
		return "[" + getStart().minus(baseTime).toString() + ","
				+ getEnd().minus(baseTime).toString() + ")";
	}

	public boolean isValid() {
		PointInTime current = PointInTime.currentPointInTime();
		return this.getStart().beforeOrEquals(current)
				&& this.getEnd().after(current);
	}

	public static TimeInterval forever() {
		return forever.clone();
	}

	@Override
	public TimeInterval clone() {
		return new TimeInterval(this);
	}

}
