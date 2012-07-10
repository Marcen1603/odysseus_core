/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.ac;

import java.util.List;

/**
 * Schnittstelle, welche Algorithmen zur Auswahl eines Vorschlags zur
 * Überlastauflösung aus einer Menge von Vorschlägen repräsentiert. Die aktuelle
 * Implementierung der Schnittstelle lässt sich über den entsprechenden Service
 * ermitteln.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IAdmissionReaction {

	/**
	 * Wählt aus einer Menge von Vorschlägen einen Vorschlag aus.
	 * 
	 * @param possibilities
	 *            Menge an Vorschlägen
	 * @return Gewählter Vorschlag
	 */
	public IPossibleExecution react(List<IPossibleExecution> possibilities);

}
