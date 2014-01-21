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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;

public class SDFDatatypeConstraint extends SDFElement {
	
	final static public String BASE_TIME_UNIT = "basetimeunit"; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2738703926500426135L;
	final private Object value;
	
	public SDFDatatypeConstraint(String URI, Object value) {
		super(URI);
		this.value = value;
	}

	public SDFDatatypeConstraint(SDFDatatypeConstraint sdfDatatypeConstraint) {
		super(sdfDatatypeConstraint);
		this.value = sdfDatatypeConstraint.value;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public SDFElement clone() {
		return new SDFDatatypeConstraint(this);
	}
	
}