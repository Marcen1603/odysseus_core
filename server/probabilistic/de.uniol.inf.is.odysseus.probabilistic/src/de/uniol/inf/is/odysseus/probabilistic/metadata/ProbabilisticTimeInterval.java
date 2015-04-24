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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
final public class ProbabilisticTimeInterval extends AbstractMetaAttribute
		implements IProbabilisticTimeInterval {

	private static final long serialVersionUID = -9030157268224460919L;
	/** The classes. */
	@SuppressWarnings("unchecked")
	public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ITimeInterval.class, IProbabilistic.class };

	private final ITimeInterval timeInterval;
	/** The tuple probability. */
	private final IProbabilistic probabilistic;

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(
			CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Probabilistic.schema);
	}

	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}

	/**
	 * 
	 * Default constructor.
	 */
	public ProbabilisticTimeInterval() {
		timeInterval = new TimeInterval();
		probabilistic = new Probabilistic();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param copy
	 *            The object to copy from
	 */
	public ProbabilisticTimeInterval(final ProbabilisticTimeInterval copy) {
		timeInterval = copy.timeInterval.clone();
		probabilistic = copy.probabilistic.clone();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public ProbabilisticTimeInterval clone() {
		return new ProbabilisticTimeInterval(this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "ProbabilisticTimeInterval";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		probabilistic.retrieveValues(values);
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return probabilistic.getValue(0, index);
		}
		return null;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "i=" + timeInterval.toString() + " | prob=" + this.probabilistic;
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "i=" + timeInterval.toString(baseTime) + " | prob="
				+ this.probabilistic;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter()
				+ this.probabilistic.csvToString(options);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getCSVHeader(final char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter
				+ this.probabilistic.getCSVHeader(delimiter);
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for timeInterval
	// ------------------------------------------------------------------------------
	
	@Override
	public PointInTime getStart() {
		return timeInterval.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return timeInterval.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		timeInterval.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		timeInterval.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		timeInterval.setStartAndEnd(start, end);
	}

	@Override
	public int compareTo(ITimeInterval o) {
		return timeInterval.compareTo(o);
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for Probabilistic
	// ------------------------------------------------------------------------------

	public double getExistence() {
		return probabilistic.getExistence();
	}

	public void setExistence(double existence) {
		probabilistic.setExistence(existence);
	}
	
	

}
