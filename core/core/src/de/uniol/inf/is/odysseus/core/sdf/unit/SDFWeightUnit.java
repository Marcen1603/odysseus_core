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
package de.uniol.inf.is.odysseus.core.sdf.unit;

import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;

public class SDFWeightUnit extends SDFUnit {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3510914270746883370L;

	protected SDFWeightUnit(String URI) {
		super(URI);
	}
	
	public SDFWeightUnit(SDFWeightUnit sdfWeightUnit) {
		super(sdfWeightUnit);
	}

	@Override
    public SDFWeightUnit clone(){
		return new SDFWeightUnit(this);
	}
	
}