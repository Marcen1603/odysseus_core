package mg.dynaquest.queryexecution.po.streaming.object;

public class PointInTime implements Comparable<PointInTime> {

	long point;

	long subpoint;

	boolean infinity = false;

	static volatile PointInTime infinityValue = null;

	public PointInTime(long point, long subpoint) {
		this.point = point;
		this.subpoint = subpoint;
	}

	public PointInTime() {
		infinity = true;
	}

	public boolean isInfinite() {
		return infinity;
	}

	static PointInTime getInfinityTime() {
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
		this.infinity = false;
	}
	
	public void setInfinite() {
		this.infinity = true;
	}
	
	public void setSubpoint(long subpoint){
		this.subpoint = subpoint;
	}
	
	public void setPoint(long mainPoint, long subpoint) {
		this.point = mainPoint;
		this.subpoint = subpoint;
		this.infinity = false;
	}
	
	public long getMainPoint(){
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
		return (left.point == right.point && left.subpoint == right.subpoint)
				|| (left.infinity == right.infinity);
	}

	public static PointInTime min(PointInTime left, PointInTime right) {
		if (before(left, right)) {
			return left;
		} else {
			return right;
		}
	}

	public static PointInTime max(PointInTime left, PointInTime right) {
		if (before(left, right)) {
			return right;
		} else {
			return left;
		}
	}


	public int compareTo(PointInTime toCompare) {
		if (equals(toCompare)) {
			return 0;
		} else {
			if (before(toCompare, this)) {
				return -1;
			}
			return 1;
		}
	}
	
	public String toString() {
		if (isInfinite()) {
			return "\u221E";
		} else {
			return getMainPoint() + ":" + getSubpoint();
		}
	}
}
