/*
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.datatype.interval.datatype;

import java.io.Serializable;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalDouble implements Serializable, Cloneable, Comparable<IntervalDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5098617654305826274L;
	private final double inf;
	private final double sup;

	public IntervalDouble(final double inf, final double sup) {
		this.inf = inf;
		this.sup = sup;
	}

	public double inf() {
		return this.inf;
	}

	public double sup() {
		return this.sup;
	}

	public boolean isEmpty() {
		return !(this.inf <= this.sup);
	}

	public boolean contains(final double value) {
		return ((value >= this.inf) && (value <= this.sup));
	}

	public boolean contains(final IntervalDouble other) {
		if (other.isEmpty()) {
			return true;
		}
		if (this.isEmpty()) {
			return false;
		}
		return ((other.inf >= this.inf) && (other.sup <= this.sup));
	}

	@Override
	public int compareTo(final IntervalDouble other) {
		return Double.valueOf(this.inf).compareTo(Double.valueOf(other.inf))
				+ Double.valueOf(this.sup).compareTo(Double.valueOf(other.sup));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.inf);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.sup);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final IntervalDouble other = (IntervalDouble) obj;
		if (Double.doubleToLongBits(this.inf) != Double
				.doubleToLongBits(other.inf)) {
			return false;
		}
		if (Double.doubleToLongBits(this.sup) != Double
				.doubleToLongBits(other.sup)) {
			return false;
		}
		return true;
	}

	@Override
	public IntervalDouble clone() {
		return new IntervalDouble(this.inf, this.sup);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[" + this.inf + "," + this.sup + "]";
	}
}
