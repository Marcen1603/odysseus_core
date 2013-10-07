/**
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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimeIntervalProbabilistic extends TimeInterval implements ITimeIntervalProbabilistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9030157268224460919L;
	/** The classes. */
	@SuppressWarnings("unchecked")
	public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, IProbabilistic.class, ITimeIntervalProbabilistic.class };
	/** The tuple probability. */
	private final IProbabilistic probabilistic;

	/**
	 * Default constructor.
	 */
	public TimeIntervalProbabilistic() {
		super();
		this.probabilistic = new Probabilistic();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param intervalProbabilistic
	 *            The object to copy from
	 */
	public TimeIntervalProbabilistic(final TimeIntervalProbabilistic intervalProbabilistic) {
		super(intervalProbabilistic);
		this.probabilistic = intervalProbabilistic.probabilistic.clone();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.TimeInterval#clone()
	 */
	@Override
	public TimeIntervalProbabilistic clone() {
		return new TimeIntervalProbabilistic(this);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.TimeInterval#toString()
	 */
	@Override
	public String toString() {
		return "( i= " + super.toString() + " | " + " p=" + this.probabilistic + ")";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.TimeInterval#csvToString(char, java.lang.Character, java.text.NumberFormat, java.text.NumberFormat, boolean)
	 */
	@Override
	public String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
		return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + this.probabilistic.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.TimeInterval#getCSVHeader(char)
	 */
	@Override
	public String getCSVHeader(final char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + this.probabilistic.getCSVHeader(delimiter);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic#getExistence()
	 */
	@Override
	public final double getExistence() {
		return this.probabilistic.getExistence();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic#setExistence(double)
	 */
	@Override
	public final void setExistence(final double existence) {
		this.probabilistic.setExistence(existence);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.TimeInterval#getClasses()
	 */
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return TimeIntervalProbabilistic.CLASSES;
	}
}
