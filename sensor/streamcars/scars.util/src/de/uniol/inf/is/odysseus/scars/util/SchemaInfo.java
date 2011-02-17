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
package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Einfache Klasse, welche alle Informationen während einer Iteration durch
 * Schemata zusammenfasst. Sie dient als Iterationsvariable für den
 * SchemaIterator. Sie wird nach jedem Durchlauf neu erstellt. Änderungen
 * innerhalb dieser Klasse haben keinen Einfluss auf die Iteration selbst.
 * <p>
 * Nutzern ist es nicht gestattet, eigene Instanzen von dieser Klasse zu
 * erstellen.
 * 
 * @author Timo Michelsen.
 * 
 */
public class SchemaInfo {

	/**
	 * Das SDFAttribute, worauf die Iteration gerade zeigt.
	 */
	public SDFAttribute attribute = null;

	/**
	 * Das SchemaIndexPath zum aktuellen SDFAttribut, worauf die Iteration
	 * zeigt.
	 */
	public SchemaIndexPath schemaIndexPath = null;

	/**
	 * Aktuelle Iterationstiefe relativ zum Startpunkt.
	 */
	public int level = -1;

	/**
	 * <code>true</code>, wenn im aktuellen SchemaIndexPath mindestens ein
	 * Listenattribut vorkommt, sonst <code>false</code>.
	 */
	public boolean isInList = false;

	// Nutzer davon abhalten, eigene Instanzen davon
	// zu erzeugen
	SchemaInfo() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(attribute).append(", SchemaIndexPath=");
		sb.append(schemaIndexPath).append(", Level=").append(level);
		sb.append(", IsInList=").append(isInList).append("}");
		return sb.toString();
	}
}
