/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.offis.chart.charts.datatype;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
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

	private boolean isProbalisticDatatype(SDFDatatype type){		
		return type instanceof SDFProbabilisticDatatype;
	}
	
	@Override
	protected void init() {
		int index = 0;
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute attribute;
			
			if(isProbalisticDatatype(a.getDatatype())){
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
