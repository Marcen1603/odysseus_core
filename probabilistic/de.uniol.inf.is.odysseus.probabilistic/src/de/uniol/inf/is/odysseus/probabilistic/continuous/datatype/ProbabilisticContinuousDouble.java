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

package de.uniol.inf.is.odysseus.probabilistic.continuous.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticContinuousDouble implements Serializable, IClone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 537104992550497486L;
	private int distribution;

	public ProbabilisticContinuousDouble(final int distribution) {
		this.distribution = distribution;
	}

	public ProbabilisticContinuousDouble(final ProbabilisticContinuousDouble probabilisticContinuousDouble) {
		this.distribution = probabilisticContinuousDouble.distribution;
	}

	public int getDistribution() {
		return this.distribution;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.distribution);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public ProbabilisticContinuousDouble clone() {
		return new ProbabilisticContinuousDouble(this);
	}

	public void setDistribution(final int distribution) {
		this.distribution = distribution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.distribution;
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

		if (this.getClass() == obj.getClass()) {
			final ProbabilisticContinuousDouble other = (ProbabilisticContinuousDouble) obj;
			return this.distribution == other.distribution;
		} else {
			if (obj.getClass() == Double.class) {
				return true;
			}
		}
		return false;
	}

}
