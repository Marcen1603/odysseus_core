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
package de.uniol.inf.is.odysseus.cep.metamodel;

import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

/**
 * Eine Instanz diser Klasse stellt einen Eintrag im Ausgabeschema dar.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
abstract public class AbstractOutputSchemeEntry implements IOutputSchemeEntry{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1475755508894213201L;
	/**
	 * textuelle Darstellung des Ausdrucks
	 */
	private String label;

	public AbstractOutputSchemeEntry() {
	}

	/**
	 * Erzeugt einen Eintrag für das Ausgabeschema aus einer textuellen
	 * Beschreibung des Ausdrucks.
	 * 
	 * @param label
	 *            textuelle Darstellung des Ausdrucks. Nicht null.
	 */
	public AbstractOutputSchemeEntry(String label) {
		setLabel(label);
	}

	/**
	 * Setzt die textuelle Darstellung des Ausgabeschema-Eintrags und erzeugt
	 * daraus automatisch einen Ausdrucksbaum.
	 * 
	 * @param label De textuelle darstellung des Ausgabeschema-Eintrags.
	 * @throws UndefinedExpressionLabelException Falls das Label null oder leer ist.
	 */
	@Override
	public void setLabel(String label) throws UndefinedExpressionLabelException {
		if (label == null) {
			throw new UndefinedExpressionLabelException();
		} else if (label.isEmpty()) {
			throw new UndefinedExpressionLabelException();
		} 	
		this.label = label;
	}

	/**
	 * Gibt das Label des Ausgabeschema-Eintrags zurück.
	 * 
	 * @return Das Label des Eintrags.
	 */
	@Override
	public String getLabel() {
		return this.label;
	}


}
