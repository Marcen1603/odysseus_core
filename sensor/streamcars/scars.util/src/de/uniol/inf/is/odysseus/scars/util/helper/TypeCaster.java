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
package de.uniol.inf.is.odysseus.scars.util.helper;

/**
 * Kleine Hilfsklasse, die besonders für die Konvertierung von numerischen
 * Werten verwendet wird. Es stellte sich das Problem, dass durch Berechnungen
 * mit Zahlen aus Attributen die Zahlen implizit ihren Typ ändern können (z.B.
 * int -> float). Würden diese ungeprüft wieder in das Attribut
 * zurückgeschrieben werden, so würde das Attribut einen neuen Typ bekommen,
 * aber das entsprechende Schema nichts davon wissen. Die statische Methode
 * <code>cast</code> dieser Klasse löst dieses Problem, indem es ein Objekt in
 * den Typ eines anderen Objektes umwandelt.
 * <p>
 * Von dieser Klasse kann keine Instanz erzeugt werden, sie besitzt
 * ausschließlich statische Methoden.
 * 
 * @author Timo Michelsen
 * 
 */
public class TypeCaster {

	private TypeCaster() {

	}

	/**
	 * Wandelt das in <code>obj</code> gegebene Objekt in die Klasse des in
	 * <code>toClass</code> angegebenen Objektes um. Letzteres wird nicht
	 * verändert. Dabei werden nur numerische Typen unterstützt (double, float,
	 * int und long).
	 * 
	 * @param obj
	 *            Objekt, welches umgewandelt werden soll
	 * @param toClass
	 *            Objekt, dessen Klasse die Zielklasse der Umwandlung ist.
	 * @return Das umgewandelte Objekt
	 */
	public static Object cast(Object obj, Object toClass) {
		if (obj instanceof Double) {
			Double castedValue = (Double) obj;
			if (toClass instanceof Float) {
				return new Float(castedValue);
			} else if (toClass instanceof Integer) {
				return new Integer(castedValue.toString());
			} else if (toClass instanceof Long) {
				return new Long(castedValue.toString());
			} else {
				// muss wohl ein double sein
				return castedValue;
			}
		} else if (obj instanceof Float) {
			Float castedValue = (Float) obj;
			if (toClass instanceof Double) {
				return new Double(castedValue);
			} else if (toClass instanceof Integer) {
				return new Integer(castedValue.toString());
			} else if (toClass instanceof Long) {
				return new Long(castedValue.toString());
			} else {
				// muss wohl ein float sein
				return castedValue;
			}
		} else if (obj instanceof Long) {
			Long castedValue = (Long) obj;
			if (toClass instanceof Double) {
				return new Double(castedValue);
			} else if (toClass instanceof Integer) {
				return new Integer(castedValue.toString());
			} else if (toClass instanceof Float) {
				return new Float(castedValue.toString());
			} else {
				// muss wohl ein long sein
				return castedValue;
			}
		}

		return obj;
	}
}
