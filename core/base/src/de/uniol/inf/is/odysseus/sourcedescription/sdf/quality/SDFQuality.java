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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * Alle Qualitaetseigenschaften sind Instanzen dieser Klasse Eine Qualitaetseigenschaft ist dabei ein Attribut mit dem speziellen Datentyp Quality
 */

public class SDFQuality extends SDFEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 272535927319723816L;
	// Kein Datentyp
	static SDFDatatype dt = null;

	public SDFQuality(String URI) {
		super(URI);
		super.setDatatype(dt);
	}

	@Override
	public void setDatatype(SDFDatatype datatype) {
		// Ignorieren. Der Datentyp darf nicht veraendert werden
	}

}