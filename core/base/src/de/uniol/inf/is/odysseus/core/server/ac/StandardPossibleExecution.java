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
 * Repräsentiert die Standardimplementierung einer {@link IPossibleExecution}.
 * Es beinhaltet die Information, welche Anfragen gestoppt werden sollten und
 * welche Anfragen weiterhin laufen können.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardPossibleExecution implements IPossibleExecution {

	private final Collection<IPhysicalQuery> runningQueries;
	private final Collection<IPhysicalQuery> stoppingQueries;
	private final ICost costEstimation;

	/**
	 * Konstruktor. Erstellt eine neue {@link StandardPossibleExecution}-Instanz mit
	 * gegebenen laufenden und stoppenden Anfragen sowie der dazugehörigen Kostenschätzung.
	 *  
	 * @param runningQueries Liste der laufenden Anfragen
	 * @param stoppingQueries Liste der zu stoppenden Anfragen
	 * @param costEstimation Kostenschätzung im Falle der Ausführung des Vorschlags.
	 */
	public StandardPossibleExecution(Collection<IPhysicalQuery> runningQueries, Collection<IPhysicalQuery> stoppingQueries, ICost costEstimation) {
		this.runningQueries = runningQueries;
		this.stoppingQueries = stoppingQueries;
		this.costEstimation = costEstimation;
	}

	@Override
	public Collection<IPhysicalQuery> getRunningQueries() {
		return runningQueries;
	}

	@Override
	public Collection<IPhysicalQuery> getStoppingQueries() {
		return stoppingQueries;
	}

	@Override
	public ICost getCostEstimation() {
		return costEstimation;
	}

}
