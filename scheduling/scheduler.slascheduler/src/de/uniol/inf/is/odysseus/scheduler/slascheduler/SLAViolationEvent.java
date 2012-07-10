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
package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Event, marking the violation of an sla
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAViolationEvent {
	/**
	 * the query, the violated sla was defined for
	 */
	private IPhysicalQuery query;
	/**
	 * the costs caused by the violation due to predefined penalties
	 */
	private double cost;
	/**
	 * index of the hold service level
	 */
	private int serviceLevel;
	/**
	 * measured conformnace to sla
	 */
	private double conformance;

	/**
	 * creates a new sla violation event
	 * 
	 * @param query
	 *            the query, the violated sla was defined for
	 * @param cost
	 *            the costs caused by the violation
	 * @param serviceLevel
	 *            index of the hold service level
	 * @param conformance
	 *            measured conformnace to sla
	 */
	public SLAViolationEvent(IPhysicalQuery query, double cost, int serviceLevel,
			double conformance) {
		this.query = query;
		this.cost = cost;
		this.serviceLevel = serviceLevel;
		this.conformance = conformance;
	}

	/**
	 * @return the query, the violated sla was defined for
	 */
	public IPhysicalQuery getQuery() {
		return query;
	}

	/**
	 * @return the costs caused by the sla violation
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return the hold service level of the sla
	 */
	public int getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * @return the measured conformance to sla
	 */
	public double getConformance() {
		return conformance;
	}

}
