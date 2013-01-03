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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalProbabilistic extends TimeInterval implements
		IProbabilistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9030157268224460919L;
	private final IProbabilistic probabilistic;

	public IntervalProbabilistic() {
		super();
		this.probabilistic = new Probabilistic();
	}

	public IntervalProbabilistic(
			final IntervalProbabilistic intervalProbabilistic) {
		super(intervalProbabilistic);
		this.probabilistic = intervalProbabilistic.probabilistic.clone();
	}

	@Override
	public IntervalProbabilistic clone() {
		return new IntervalProbabilistic(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " | " + " p=" + this.probabilistic
				+ ")";
	}

	@Override
	public String csvToString() {
		return super.csvToString() + ";" + this.probabilistic.csvToString();
	}

	@Override
	public String getCSVHeader() {
		return super.getCSVHeader() + ";" + this.probabilistic.getCSVHeader();
	}

	@Override
	public double getExistence() {
		return this.probabilistic.getExistence();
	}

	@Override
	public void setExistence(final double existence) {
		this.probabilistic.setExistence(existence);
	}

}
