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

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TimeIntervalProbabilistic extends TimeInterval implements
		ITimeIntervalProbabilistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9030157268224460919L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] {
			ITimeInterval.class, IProbabilistic.class,
			ITimeIntervalProbabilistic.class };
	private final IProbabilistic probabilistic;

	public TimeIntervalProbabilistic() {
		super();
		this.probabilistic = new Probabilistic();
	}

	public TimeIntervalProbabilistic(
			final TimeIntervalProbabilistic intervalProbabilistic) {
		super(intervalProbabilistic);
		this.probabilistic = intervalProbabilistic.probabilistic.clone();
	}

	@Override
	public TimeIntervalProbabilistic clone() {
		return new TimeIntervalProbabilistic(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " | " + " p=" + this.probabilistic
				+ ")";
	}

	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + this.probabilistic.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + this.probabilistic.getCSVHeader(delimiter);
	}

	@Override
	public double getExistence() {
		return this.probabilistic.getExistence();
	}

	@Override
	public void setExistence(final double existence) {
		this.probabilistic.setExistence(existence);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
}
