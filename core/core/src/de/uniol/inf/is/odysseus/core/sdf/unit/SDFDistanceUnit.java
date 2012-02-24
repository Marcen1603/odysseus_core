/** Copyright [2011] [The Odysseus Team]
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

public class SDFDistanceUnit extends SDFUnit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493747261498781231L;

	protected SDFDistanceUnit(String URI) {
		super(URI);
	}
	
	public SDFDistanceUnit(SDFDistanceUnit sdfDistanceUnit) {
		super(sdfDistanceUnit);
	}

	@Override
	public SDFElement clone() {
		return new SDFDistanceUnit(this);
	}
}