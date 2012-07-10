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

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * {@link IPossibleExecution} stellt ein Vorschlag zur Kompensation einer
 * erkannten Überlastung dar. Es beinhaltet die Information, welche Anfragen
 * gestoppt werden sollten und welche Anfragen weiterhin laufen können.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPossibleExecution {

	/**
	 * Liefert alle Anfragen, die ausgeführt werden können.
	 * 
	 * @return Anfragen, die ausgeführt werden können.
	 */
	public Collection<IPhysicalQuery> getRunningQueries();

	/**
	 * Liefert alle Anfragen, die zur Überlastkompensation gestoppt
	 * werden sollten.
	 * 
	 * @return Liste von zu stoppenden Anfragen
	 */
	public Collection<IPhysicalQuery> getStoppingQueries();

	/**
	 * Liefert die Kostenschätzung für den Fall, dass dieser Vorschlag
	 * umgesetzt werden würde.
	 * 
	 * @return Kostenschätzung im Falle einer Umsetzung des Vorschlags.
	 */
	public ICost getCostEstimation();

}
