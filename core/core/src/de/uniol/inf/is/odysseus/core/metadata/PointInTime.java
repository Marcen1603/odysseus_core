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

/**
 * 
 * This immutable class represents a simple point in time and that can be
 * infinite
 * 
 * @author Jonas Jacobi, Marco Grawunder
 * 
 */
public class PointInTime implements Comparable<PointInTime>, Cloneable,
		Serializable {

	private static final String INFINITY_SYMBOL = "oo";

	private static final long serialVersionUID = -350811211489411617L;

	/**
	 * A representation of this time
	 */
	final long point;

	/**
	 * A marker if this point has not time, but is infinite
	 */
	final boolean isInfinite;

	/**
	 * Constant for infinityTime
	 */
	static final PointInTime INFINITY = new PointInTime();

	/**
	 * Constant for zero time
	 */
	private static final PointInTime ZERO = new PointInTime(0);

	/**
	 * Create a point in time from a long value
	 * 
	 * @param point
	 */
	public PointInTime(long point) {
		this.point = point;
		this.isInfinite = false;
	}

	/**
	 * Create a point in time from a number that can be cast to a long value
	 * 
	 * @param point
	 */
	public PointInTime(Number point) {
		this.point = point.longValue();
		this.isInfinite = false;
	}

	private PointInTime() {
		isInfinite = true;
		point = -1;
	}

	// public PointInTime(PointInTime time) {
	// this.point = time.point;
	// this.isInfinite = time.isInfinite;
	// }

	public boolean isInfinite() {
		return isInfinite;
	}

	public static PointInTime getInfinityTime() {
		return INFINITY;
	}

	// public void setMainPoint(long point) {
	// this.point = point;
	// this.isInfinite = false;
	// }
	//
	// public void setInfinite() {
	// this.isInfinite = true;
	// }
	//
	//
	// public void setPoint(long mainPoint) {
	// this.point = mainPoint;
	// this.isInfinite = false;
	// }

	public long getMainPoint() {
		return this.point;
	}

	public boolean before(PointInTime time) {
		return before(this, time);
	}

	public boolean beforeOrEquals(PointInTime time) {
		return beforeOrEquals(this, time);
	}

	public boolean equals(PointInTime time) {
		return equals(this, time);
	}

	public boolean after(PointInTime time) {
		return before(time, this);
	}

	public boolean afterOrEquals(PointInTime time) {
		return beforeOrEquals(this, this);
	}

	public static PointInTime currentPointInTime() {
		return new PointInTime(System.currentTimeMillis());
	}

	public static boolean before(PointInTime left, PointInTime right) {
		boolean before;
		// Achtung: Vorher die Behandlung von unendlich testen
		// Wenn der linke Punkt unendlich ist, kann der rechte nicht davor sein
		if (left.isInfinite()) {
			before = false;
			// Wenn der rechte Punkt unendlich ist und der linke nicht (s.o.)
			// dann ist er auf jeden Fall davor :-)
		} else if (right.isInfinite()) {
			before = true;
		} else {
			// Ansonsten ganz normal
			before = left.point < right.point;
		}
		return before;
	}

	public static boolean beforeOrEquals(PointInTime left, PointInTime right) {
		boolean before;
		// Achtung: Vorher die Behandlung von unendlich testen
		// Wenn der linke Punkt unendlich ist, kann der rechte nicht davor sein
		if (left.isInfinite()) {
			before = false;
			// Wenn der rechte Punkt unendlich ist und der linke nicht (s.o.)
			// dann ist er auf jeden Fall davor :-)
		} else if (right.isInfinite()) {
			before = true;
		} else {
			// Ansonsten ganz normal
			before = left.point <= right.point;
		}
		return before;
	}
	
	public static boolean equals(PointInTime left, PointInTime right) {
		// Entweder gleich in den Werten oder unendlich
		if (left.isInfinite) {
			if (right.isInfinite) {
				return true;
			}
			return false;
		}
		if (right.isInfinite) {
			return false;
		}
		return (left.point == right.point);
	}

	public static PointInTime min(PointInTime left, PointInTime right) {
		PointInTime min = null;
		if (left != null && right != null) {
			if (before(left, right)) {
				min = left;
			} else {
				min = right;
			}
		} else if (left != null) {
			min = left;
		} else if (right != null) {
			min = right;
		}
		return min;
	}

	public static PointInTime max(PointInTime left, PointInTime right) {
		PointInTime max = null;
		if (left != null && right != null) {
			if (before(left, right)) {
				max = right;
			} else {
				max = left;
			}
		} else if (left != null) {
			max = left;
		} else if (right != null) {
			max = right;
		}
		return max;
	}

	@Override
	public int compareTo(PointInTime toCompare) {
		int ret = 1;
		if (equals(toCompare)) {
			ret = 0;
		} else if (before(this, toCompare)) {
			ret = -1;
		}
		return ret;
	}

	@Override
	public String toString() {
		if (isInfinite()) {
			return INFINITY_SYMBOL;
		}
		return "" + getMainPoint();
	}

	public String toString(PointInTime baseTime) {
		if (isInfinite()) {
			return INFINITY_SYMBOL;
		}
		return "" + (getMainPoint() - baseTime.getMainPoint());
	}

	@Override
	public PointInTime clone() {
		// PointInTime is immutable
		return this;
	}

	public PointInTime minus(PointInTime time) {
		if (isInfinite)
			return this;
		return new PointInTime(this.point - time.point);
	}

	public PointInTime plus(PointInTime time) {
		if (isInfinite)
			return this;
		return new PointInTime(this.point + time.point);
	}
	
	public static PointInTime plus(PointInTime time, long toAdd) {
		return new PointInTime(time.point+toAdd);
	}


	public PointInTime plus(int time) {
		if (isInfinite)
			return this;
		return new PointInTime(this.point + time);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (isInfinite ? 1231 : 1237);
		result = PRIME * result + (int) (point ^ (point >>> 32));
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

	public PointInTime sum(long point) {
		return new PointInTime(this.point + point);
	}

	public static PointInTime getZeroTime() {
		return ZERO.clone();
	}


}
