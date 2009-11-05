package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;

/**
 * @author Jonas Jacobi, Marco Grawunder
 * 
 */
public class PointInTime implements Comparable<PointInTime>, Cloneable, Serializable {

	private static final long serialVersionUID = -350811211489411617L;

	long point;

	long subpoint;

	boolean isInfinite = false;

	static volatile PointInTime infinityValue = null;
	private static final PointInTime zeroTime = new PointInTime(0, 0);

	public PointInTime(long point, long subpoint) {
		this.point = point;
		this.subpoint = subpoint;
	}

	public PointInTime() {
		isInfinite = true;
	}

	public PointInTime(PointInTime time) {
		this.point = time.point;
		this.subpoint = time.subpoint;
		this.isInfinite = time.isInfinite;
	}

	public boolean isInfinite() {
		return isInfinite;
	}

	public static PointInTime getInfinityTime() {
		if (infinityValue == null) {
			synchronized (PointInTime.class) {
				if (infinityValue == null) {
					infinityValue = new PointInTime();
				}
			}
		}
		return infinityValue;
	}

	public void setMainPoint(long point) {
		this.point = point;
		this.isInfinite = false;
	}

	public void setInfinite() {
		this.isInfinite = true;
	}

	public void setSubpoint(long subpoint) {
		this.subpoint = subpoint;
	}

	public void setPoint(long mainPoint, long subpoint) {
		this.point = mainPoint;
		this.subpoint = subpoint;
		this.isInfinite = false;
	}

	public long getMainPoint() {
		return this.point;
	}

	public long getSubpoint() {
		return this.subpoint;
	}

	public boolean before(PointInTime time) {
		return before(this, time);
	}

	public boolean beforeOrEquals(PointInTime time) {
		return before(this, time) || equals(this, time);
	}

	public boolean equals(PointInTime time) {
		return equals(this, time);
	}

	public boolean after(PointInTime time) {
		return before(time, this);
	}

	public boolean afterOrEquals(PointInTime time) {
		return before(time, this) || equals(this, time);
	}

	public static PointInTime currentPointInTime() {
		return new PointInTime(System.currentTimeMillis(), 0);
	}

	public static boolean before(PointInTime left, PointInTime right) {
		// Achtung: Vorher die Behandlung von unendlich testen
		// Wenn der linke Punkt unendlich ist, kann der rechte nicht davor sein
		if (left.isInfinite()) {
			return false;
		}
		// Wenn der rechte Punkt unendlich ist und der linke nicht (s.o.) dann
		// ist
		// er auf jeden Fall davor :-)
		if (right.isInfinite()) {
			return true;
		}

		// Ansonsten ganz normal
		return left.point < right.point
				|| (left.point == right.point && left.subpoint < right.subpoint);
	}

	public static boolean equals(PointInTime left, PointInTime right) {
		// Entweder gleich in den Werten oder unendlich
		if (left.isInfinite) {
			if (right.isInfinite) {
				return true;
			}
			return false;
		} else {
			if (right.isInfinite) {
				return false;
			}
			return (left.point == right.point && left.subpoint == right.subpoint);
		}
	}

	public static PointInTime min(PointInTime left, PointInTime right) {
		if (left != null && right != null) {
			if (before(left, right)) {
				return left;
			} else {
				return right;
			}
		}
		if (left != null) {
			return left;
		}
		if (right != null) {
			return right;
		}
		return null;
	}

	public static PointInTime max(PointInTime left, PointInTime right) {
		if (left != null && right != null) {
			if (before(left, right)) {
				return right;
			} else {
				return left;
			}
		}
		if (left != null) {
			return left;
		}
		if (right != null) {
			return right;
		}
		return null;
	}

	public int compareTo(PointInTime toCompare) {
		if (equals(toCompare)) {
			return 0;
		} else {
			if (before(this, toCompare)) {
				return -1;
			}
			return 1;
		}
	}

	@Override
	public String toString() {
		if (isInfinite()) {
			return "\u221E";
		} else {
			return getMainPoint() + ":" + getSubpoint();
		}
	}

	public String toString(PointInTime baseTime) {
		if (isInfinite()) {
			return "\u221E";
		} else {
			return (getMainPoint() - baseTime.getMainPoint()) + ":"
					+ (getSubpoint() - baseTime.getSubpoint());
		}
	}

	@Override
	public PointInTime clone() {
		return new PointInTime(this);

	}

	public PointInTime minus(PointInTime time) {
		if (isInfinite)
			return this;
		return new PointInTime(this.point - time.point, this.subpoint
				- time.subpoint);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (isInfinite ? 1231 : 1237);
		result = PRIME * result + (int) (point ^ (point >>> 32));
		result = PRIME * result + (int) (subpoint ^ (subpoint >>> 32));
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
		final PointInTime other = (PointInTime) obj;
		return PointInTime.equals(this, other);
	}

	public PointInTime sum(long point, int subpoint) {
		// TODO eigentlich auch nicht so ganz richtig
		// 5.5 + 4.6 sind ja auch nicht 9.11 sondern 10.1
		// Aber nur dann wenn die Punkte eine Beziehung zueinander haben ... (dazu muesste man dann definieren
		// das n subpoints ein Punkt sind ... aber wozu? Subpoint sind nur zum Unterscheiden "fast" zeitgleicher 
		// Punkte ...)
		return new PointInTime(this.point + point, this.subpoint + subpoint);
	}

	public static PointInTime getZeroTime() {
		return zeroTime.clone();
	}

}
