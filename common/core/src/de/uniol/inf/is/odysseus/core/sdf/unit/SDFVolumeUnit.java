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

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;

public class SDFVolumeUnit extends SDFUnit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7812373795870581513L;

	protected SDFVolumeUnit(String URI) {
		super(URI);
	}
	
	public SDFVolumeUnit(SDFVolumeUnit sdfVolumeUnit) {
		super(sdfVolumeUnit);
	}

	@Override
	public SDFElement clone() {
		return new SDFVolumeUnit(this);
	}
}