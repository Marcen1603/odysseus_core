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
package de.uniol.inf.is.odysseus.cep.metamodel;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Objekte dieser Klasse stellen das Ausgabeschema des EPA dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class OutputScheme implements Serializable{

	private static final long serialVersionUID = 7002822862639034364L;
	private LinkedList<IOutputSchemeEntry> entries;

	/**
	 * Erzeugt ein neues leeres Ausgabeschema
	 */
	public OutputScheme() {
		this.entries = new LinkedList<IOutputSchemeEntry>();
	}

	/**
	 * Erzeugt ein neues Ausgabeschema aus einer Liste von
	 * Ausgabeschema-Einträgen
	 * 
	 * @param entries
	 *            Nicht leere Liste von Ausgabeschema-Einträgen, die die
	 *            Ausgaben des komplexen Events beschreiben. Darf nicht null
	 *            sein.
	 */
	public OutputScheme(LinkedList<IOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Liefert eine Liste von Ausdrücken, die die Werte der Ausgabe definieren.
	 * 
	 * @return Liste von Ausdrücken, die die auszugebenden Werte definieren.
	 */
	public LinkedList<IOutputSchemeEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(LinkedList<IOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	public String toString(String indent) {
		String str = indent + "OutputScheme: " + this.hashCode();
		indent += "  ";
		for (IOutputSchemeEntry entry : this.entries) {
			str += entry.toString();
		}
		return str;
	}

	public void append(IOutputSchemeEntry e) {
		entries.add(e);
	}
	
	@Override
	public String toString() {
		return toString(" ");
	}

}
