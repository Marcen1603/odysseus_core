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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class ViewableSDFAttribute extends AbstractViewableAttribute{

	final private SDFAttribute attribute;
	final private String typeName;
	final private int index;
	
	
	public ViewableSDFAttribute(SDFAttribute attribute, String typeName, int index, int port){
		super(port);
		this.attribute = attribute;
		this.index = index;
		this.typeName = typeName;
	}

	@Override
	public String getName() {
		return typeName+"."+attribute.getAttributeName()+"("+getPort()+")";
	}

	@Override
	public SDFDatatype getSDFDatatype() {		
		return attribute.getDatatype();
	}

	@Override
	public Object evaluate(Tuple<? extends IMetaAttribute> tuple) {
		return tuple.getAttribute(this.index);		
	}
	
}
