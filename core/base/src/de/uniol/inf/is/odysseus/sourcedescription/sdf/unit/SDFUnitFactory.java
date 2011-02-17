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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.unit;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFUnits;

public class SDFUnitFactory {
	// Eine Cache-Struktur, dass Datentypen-Objekte nicht mehrfach
	// erzeugt werden
	static HashMap<String, SDFUnit>  createdTypes = new HashMap<String, SDFUnit>();

	private SDFUnitFactory() {
	}

	static public SDFUnit getUnit(String unitURI, String unitGroupURI) {
		// Erst mal testen, ob es das Objekt schon gibt
		if (createdTypes.containsKey(unitURI)) {
			return createdTypes.get(unitURI);
		}
		SDFUnit unit = null;
		// Kleiner Hack um nicht zu viele geschachtelte if then else zu haben
		while (true) {
			if (unitGroupURI.equals(SDFUnits.SDFCurrencyUnit)) {
				unit = new SDFCurrencyUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFWeightUnit)) {
				unit = new SDFWeightUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFDistanceUnit)) {
				unit = new SDFDistanceUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFAreaUnit)) {
				unit = new SDFAreaUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFVolumeUnit)) {
				unit = new SDFVolumeUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFTemperatureUnit)) {
				unit = new SDFTemperatureUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFTimeUnit)) {
				unit = new SDFTimeUnit(unitURI);
				break;
			}
			if (unitGroupURI.equals(SDFUnits.SDFSpeedUnit)) {
				unit = new SDFSpeedUnit(unitURI);
				break;
			}
			// Hier noch eine Exception draus machen
			System.err.println("Ungueltige Einheitengruppe");
			break;
		}

		if (unit != null) {
			createdTypes.put(unitURI, unit);
		}
		return unit;
	}
}