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
package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class Priority extends AbstractMetaAttribute implements IPriority {

	private static final long serialVersionUID = 1837720176871400611L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IPriority.class };

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(classes.length);
	static{
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Priority", "priority", SDFDatatype.BYTE, null));
		schema.add(SDFSchemaFactory.createNewSchema("Priority", Tuple.class, attributes));
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	byte prio;

	public Priority() {
		this.prio = 0;
	}

	private Priority(Priority original) {
		this.prio = original.prio;
	}

	@Override
	public void fillValueList(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(1,false);
		t.setAttribute(0, prio);
		values.add(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (Byte) prio;
		}
		return null;
	}
	
	@Override
	public byte getPriority() {
		return this.prio;
	}

	@Override
	public void setPriority(byte prio) {
		this.prio = prio;
	}

	@Override
	public IPriority clone() {
		return new Priority(this);
	}

	@Override
	public String toString() {
		return "" + prio;
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		
		if (options.hasNumberFormatter()) {
			return options.getNumberFormatter().format(prio);
		} else {
			return prio + "";
		}
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return "Priority";
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return "Priority";
	}
}
