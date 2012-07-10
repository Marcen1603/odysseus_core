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
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class ViewSchema<T> {

	final private SDFSchema outputSchema;
	final private SDFMetaAttributeList metadataSchema;
	final private int port;
	
	private List<IViewableAttribute> viewableAttributes = new ArrayList<IViewableAttribute>();

	private List<IViewableAttribute> choosenAttributes = new ArrayList<IViewableAttribute>();

	public ViewSchema(SDFSchema outputSchema, SDFMetaAttributeList metaSchema, int port) {
		this.outputSchema = outputSchema;
		this.metadataSchema = metaSchema;
		this.port = port;

		init();
	}

	private void init() {
		int index = 0;
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute attribute = new ViewableSDFAttribute(a, outputSchema.getURI(), index, port);
			if (isAllowedDataType(attribute.getSDFDatatype())) {				
					viewableAttributes.add(attribute);		
			}
			index++;
		}
		// add all (except of currently timestamps) to the list of pre-chosen attributes
		this.choosenAttributes = new ArrayList<IViewableAttribute>();
		for(IViewableAttribute a : this.viewableAttributes){
			if(chooseAsInitialAttribute(a.getSDFDatatype())){
				this.choosenAttributes.add(a);
			}
		}

		for (SDFMetaAttribute m : this.metadataSchema) {
			for (Method method : m.getMetaAttributeClass().getMethods()) {
				if (!method.getName().endsWith("hashCode")) {
					IViewableAttribute attribute = new ViewableMetaAttribute(m, method, port);
					if (method.getParameterTypes().length == 0 && isAllowedDataType(attribute.getSDFDatatype())) {
						viewableAttributes.add(new ViewableMetaAttribute(m, method, port));
					}
				}
			}
		}
		
		

	}

	private boolean chooseAsInitialAttribute(SDFDatatype sdfDatatype) {
		if(sdfDatatype.equals(SDFDatatype.TIMESTAMP)){
			return false;
		}
		if(sdfDatatype.equals(SDFDatatype.START_TIMESTAMP)){
			return false;
		}
		if(sdfDatatype.equals(SDFDatatype.END_TIMESTAMP)){
			return false;
		}
		return true;
	}

	private static boolean isAllowedDataType(SDFDatatype sdfDatatype) {
		return ViewableDatatypeRegistry.getInstance().isAllowedDataType(sdfDatatype);
	}

	@SuppressWarnings("unchecked")
	public List<T> convertToViewableFormat(Tuple<? extends IMetaAttribute> tuple) {
		List<T> values = new ArrayList<T>();
		for (int index = 0; index < this.viewableAttributes.size(); index++) {
			IViewableAttribute viewable = this.viewableAttributes.get(index);			
			Object value = viewable.evaluate(tuple);
			IViewableDatatype<?> converter = ViewableDatatypeRegistry.getInstance().getConverter(viewable.getSDFDatatype());			
			values.add((T)converter.convertToValue(value));
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
		return choosenAttributes;
	}

	public void setChoosenAttributes(List<IViewableAttribute> choosenAttributes) {
		this.choosenAttributes = choosenAttributes;
	}

	public List<IViewableAttribute> getViewableAttributes() {
		return viewableAttributes;
	}

}
