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
package de.offis.chart.charts.datatype;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;

public class ProbViewSchema<T> extends ViewSchema<T>{

	public ProbViewSchema(SDFSchema outputSchema, SDFMetaAttributeList metaSchema, int port) {
		super(outputSchema, metaSchema, port);
	}

	@Override
	protected void init() {
		int index = 0;
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute attribute;
			
			if(a.getDatatype().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)){
				attribute = new ProbViewableSDFAttribute(a, outputSchema.getURI(), index, port);				
			} else {
				attribute = new ViewableSDFAttribute(a, outputSchema.getURI(), index, port);				
			}
			
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
}
