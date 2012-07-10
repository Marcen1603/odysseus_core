/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen
 * Created at: 24.04.2012
 */
public class BooleanDatatypeProvider extends AbstractViewableDatatype<Double> {	
	
	public BooleanDatatypeProvider() {
		super.addProvidedSDFDatatype(SDFDatatype.BOOLEAN);			
		super.addProvidedClass(Boolean.class);
		super.addProvidedClass(boolean.class);
	}
	
	@Override
	public Double convertToValue(Object value) {		
		Boolean num = (Boolean)value;
		if(num.booleanValue()){
			return 1.0;
		}else{
			return 0.0;
		}		
	}

}
