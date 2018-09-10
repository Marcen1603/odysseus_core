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
package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

final public class IntervalPriority extends AbstractCombinedMetaAttribute implements ITimeInterval, IPriority {

	private static final long serialVersionUID = -313473603482217362L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{
		ITimeInterval.class, IPriority.class
	};

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}


	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Priority.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private IPriority priority;


	public IntervalPriority() {
		timeInterval = new TimeInterval();
		priority = new Priority();
	}

	public IntervalPriority(IntervalPriority other){
		this.timeInterval = (ITimeInterval) other.timeInterval.clone();
		this.priority = (IPriority) other.priority.clone();
	}

	@Override
	public IntervalPriority clone() {
		return new IntervalPriority(this);
	}

	@Override
	public String getName() {
		return "IntervalPriority";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------


	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		priority.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> value) {
		timeInterval.writeValue(value.get(0));
		priority.writeValue(value.get(1));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(priority.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return priority.getValue(0, index);
		}
		return null;
	}


	@Override
	public String toString() {
		return "i="+timeInterval.toString()+" p= "+this.priority;
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
	// Delegates for Priority
	// ------------------------------------------------------------------------------


	@Override
	public final byte getPriority() {
		return priority.getPriority();
	}

	@Override
	public void setPriority(byte priority) {
		this.priority.setPriority(priority);
	}






}
