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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.HashMap;

public class SDFDatatypeFactory {
	// Eine Cache-Struktur, dass Datentypen-Objekte nicht mehrfach
	// erzeugt werden
	static HashMap<String, SDFDatatype> createdTypes = new HashMap<String, SDFDatatype>();

	private SDFDatatypeFactory() {
	}

	static public SDFDatatype getDatatype(String URI) {
		// Erst mal testen, ob es das Objekt schon gibt
		if (createdTypes.containsKey(URI)) {
			return (SDFDatatype) createdTypes.get(URI);
		}
		SDFDatatype dt = new SDFDatatype(URI);
		createdTypes.put(URI, dt);
		return dt;
	}
}