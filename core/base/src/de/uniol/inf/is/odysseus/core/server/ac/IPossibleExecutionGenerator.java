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
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Schnittstelle für alle Algorithmen, die aus einer Menge von Anfragen (mit
 * Kostenschätzungen) Vorschläge zur Überlastkompensation generieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPossibleExecutionGenerator {

	/**
	 * Generiert eine Liste von möglichen Vorschlägen, um eine Überlastung
	 * aufzuheben. Ist keine Überlastung festgestellt worden, sollte in der
	 * Implementierung genau ein Vorschlag - alle Anfragen auszuführen -
	 * zurückgegeben werden.
	 * 
	 * @param sender
	 *            {@link IAdmissionControl}, welches die Generierung von
	 *            Vorschlägen auslöst.
	 * @param queryCosts
	 *            Aktuelle Zuordnung Anfrage - Kostenschätzung
	 * @param maxCost
	 *            Maximal zulässige Kosten
	 * @return Liste an möglichen Vorschlägen zur Überlastkompensation.
	 */
	public List<IPossibleExecution> getPossibleExecutions(IAdmissionControl sender, Map<IPhysicalQuery, ICost> queryCosts, ICost maxCost);
}
