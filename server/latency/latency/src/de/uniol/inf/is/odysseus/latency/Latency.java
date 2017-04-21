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
package de.uniol.inf.is.odysseus.latency;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

final public class Latency extends AbstractBaseMetaAttribute implements ILatency {

	public static final String LATENCY = "Latency";

	public static final int LATENCY_ATTRIBUTE_INDEX = 3;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ILatency.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute(LATENCY, "minlstart",
				SDFDatatype.LONG));
		attributes.add(new SDFAttribute(LATENCY, "maxlstart",
				SDFDatatype.LONG));
		attributes.add(new SDFAttribute(LATENCY, "lend", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(LATENCY, "latency", SDFDatatype.LONG));
		schema.add(SDFSchemaFactory.createNewMetaSchema(LATENCY, Tuple.class,
				attributes, ILatency.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	private static final long serialVersionUID = -3355802503979937479L;
	private long minlstart;
	private long maxlstart;
	private long lend;

	public Latency() {
		this.minlstart = System.nanoTime();
		this.maxlstart = minlstart;
	}

	public Latency(Latency copy) {
		this.lend = copy.lend;
		this.minlstart = copy.minlstart;
		this.maxlstart = copy.maxlstart;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(4, false);
		t.setAttribute(0, minlstart);
		t.setAttribute(1, maxlstart);
		t.setAttribute(2, lend);
		t.setAttribute(3, getLatency());
		values.add(t);
	}

	@Override
	public void writeValue(Tuple<?> value) {
		minlstart = value.getAttribute(0);
		maxlstart= value.getAttribute(1);
		lend = value.getAttribute(2);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K)(Long)minlstart;
		case 1:
			return (K)(Long)maxlstart;
		case 2:
			return (K)(Long)lend;
		case 3:
			return (K)(Long)getLatency();
		}
		return null;
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new LatencyMergeFunction();
	}

	@Override
	public long getLatency() {
		return this.lend - this.minlstart;
	}

	@Override
	public long getMaxLatency() {
		return this.lend - this.maxlstart;
	}

	@Override
	public long getLatencyEnd() {
		return this.lend;
	}

	@Override
	public long getLatencyStart() {
		return this.minlstart;
	}

	@Override
	public long getMaxLatencyStart() {
		return this.maxlstart;
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.lend = timestamp;
	}

	@Override
	public void setMinLatencyStart(long timestamp) {
		this.minlstart = timestamp;
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		this.maxlstart = timestamp;
	}

	@Override
	public ILatency clone() {
		return new Latency(this);
	}

	@Override
	public String toString() {
		return "[(max="
				+ this.maxlstart
				+ ")"
				+ this.minlstart
				+ ", "
				+ this.lend
				+ "["
				+ (this.lend > this.minlstart ? (this.lend - this.minlstart)
						: "oo");
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return LATENCY;
	}
}
