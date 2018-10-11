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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

public class ViewSchema<T> {

	protected final SDFSchema outputSchema;
	protected final SDFMetaAttributeList metadataSchema;
	protected final int port;
	protected final TimeUnit timeUnit;

	final protected List<IViewableAttribute> viewableAttributes = new ArrayList<IViewableAttribute>();

	final protected List<IViewableAttribute> choosenAttributes = new ArrayList<IViewableAttribute>();

	final protected List<IViewableAttribute> groupByAttributes = new ArrayList<IViewableAttribute>();
	private int[] groupRestrictList;

	public ViewSchema(SDFSchema outputSchema, SDFMetaAttributeList metaSchema,
			int port) {
		this.outputSchema = outputSchema;
		this.metadataSchema = metaSchema;
		this.port = port;
		this.timeUnit = outputSchema.determineTimeUnit();

		init(Lists.<String> newArrayList(), Lists.<String> newArrayList());
	}

	public ViewSchema(SDFSchema outputSchema, SDFMetaAttributeList metaSchema,
			int port, List<String> preChoosenAttributes,
			List<String> preGroupingAttributes) {
		// Preconditions.checkNotNull(preChoosenAttributes,"List of previous chosen attributes must not be null!");
		// Preconditions.checkNotNull(preGroupingAttributes,"List of grouping attributes must not be null!");

		this.outputSchema = outputSchema;
		this.metadataSchema = metaSchema;
		this.port = port;
		this.timeUnit = outputSchema.determineTimeUnit();

		init(preChoosenAttributes, preGroupingAttributes);
	}

	protected void init(List<String> preChoosenAttributes,
			List<String> preGroupingAttributes) {
		int index = 0;
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute attribute = new ViewableSDFAttribute(a,
					outputSchema.getURI(), index, port);
			if (isAllowedDataType(attribute.getSDFDatatype())) {
				viewableAttributes.add(attribute);
			}
			index++;
		}

		this.choosenAttributes.clear();
		for (IViewableAttribute a : this.viewableAttributes) {
			if (!preChoosenAttributes.isEmpty()) {
				if (preChoosenAttributes.contains(a.getName())
						&& chooseAsInitialAttribute(a.getSDFDatatype())) {
					this.choosenAttributes.add(a);
				}
			} else if (chooseAsInitialAttribute(a.getSDFDatatype())) {
				this.choosenAttributes.add(a);
			}
		}

		List<IViewableAttribute> groupByAttribs = new ArrayList<>();
		if (!preGroupingAttributes.isEmpty()) {
			for (IViewableAttribute a : this.viewableAttributes) {
				if (preGroupingAttributes.contains(a.getName())){
					groupByAttribs.add(a);
				}
			}
		}
		setGroupByAttributes(groupByAttribs);
		

		for (SDFMetaAttribute m : this.metadataSchema) {
			for (Method method : m.getMetaAttributeClass().getMethods()) {
				if (!method.getName().endsWith("hashCode")) {
					IViewableAttribute attribute = new ViewableMetaAttribute(m,
							method, port);
					if (method.getParameterTypes().length == 0
							&& isAllowedDataType(attribute.getSDFDatatype())) {
						viewableAttributes.add(new ViewableMetaAttribute(m,
								method, port));
					}
				}
			}
		}
	}

	protected boolean chooseAsInitialAttribute(SDFDatatype sdfDatatype) {
		if (sdfDatatype.equals(SDFDatatype.TIMESTAMP)) {
			return false;
		}
		if (sdfDatatype.equals(SDFDatatype.START_TIMESTAMP)) {
			return false;
		}
		if (sdfDatatype.equals(SDFDatatype.END_TIMESTAMP)) {
			return false;
		}
		return true;
	}

	protected static boolean isAllowedDataType(SDFDatatype sdfDatatype) {
		return ViewableDatatypeRegistry.getInstance().isAllowedDataType(
				sdfDatatype);
	}

	@SuppressWarnings("unchecked")
	public List<T> convertToViewableFormat(Tuple<? extends IMetaAttribute> tuple) {
		List<T> values = new ArrayList<T>();
		for (int index = 0; index < this.viewableAttributes.size(); index++) {
			IViewableAttribute viewable = this.viewableAttributes.get(index);
			Object value = viewable.evaluate(tuple);
			IViewableDatatype<?> converter = ViewableDatatypeRegistry
					.getInstance().getConverter(viewable.getSDFDatatype());
			values.add((T) converter.convertToValue(value));
		}
		return values;
	}

	public List<T> convertToChoosenFormat(List<T> objects) {
		List<T> restricted = new ArrayList<T>();

		for (IViewableAttribute viewable : this.choosenAttributes) {
			int index = this.viewableAttributes.indexOf(viewable);
			if (index >= 0) {
				T value = objects.get(index);
				restricted.add(value);
			}
		}
		return restricted;
	}

	public List<IViewableAttribute> getChoosenAttributes() {
		return Collections.unmodifiableList(choosenAttributes);
	}

	public void setChoosenAttributes(List<IViewableAttribute> choosenAttributes) {
		this.choosenAttributes.clear();
		this.choosenAttributes.addAll(choosenAttributes);
	}

	public void setGroupByAttributes(List<IViewableAttribute> groupByAttributes) {
		this.groupByAttributes.clear();
		this.groupByAttributes.addAll(groupByAttributes);

		List<SDFAttribute> groupByAttribs = new ArrayList<>();
		for (IViewableAttribute a : groupByAttributes) {
			SDFAttribute attr = new SDFAttribute(a.getTypeName(),
					a.getAttributeName(), null, null, null, null);
			groupByAttribs.add(attr);
		}

		SDFSchema groupBySchema = SDFSchemaFactory.createNewTupleSchema("", groupByAttribs);
		this.groupRestrictList = SDFSchema.calcRestrictList(outputSchema,
				groupBySchema);
	}

	public List<IViewableAttribute> getGroupByAttributes() {
		return Collections.unmodifiableList(groupByAttributes);
	}

	public List<IViewableAttribute> getViewableAttributes() {
		return viewableAttributes;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public TimeUnit getTimeUnit(TimeUnit defaultUnit) {
		if (timeUnit == null) {
			return defaultUnit;
		} 
		return timeUnit;
		
	}

	public int[] getGroupRestrictList() {
		return groupRestrictList;
	}

}
