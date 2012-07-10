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
package de.uniol.inf.is.odysseus.core.server.sla.penalty;

import de.uniol.inf.is.odysseus.core.server.sla.Penalty;
import de.uniol.inf.is.odysseus.core.server.sla.ServiceLevel;

/**
 * abstract super class for all penalties.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class AbstractPenalty implements Penalty {
	/**
	 * reference to the service level that contains the penalty
	 */
	private ServiceLevel serviceLevel;

	/**
	 * creates a new Penalty object
	 */
	public AbstractPenalty() {
		super();
	}

	/**
	 * sets the containing service level of the penalty
	 * 
	 * @param serviceLevel
	 *            the containing service level
	 */
	public void setServiceLevel(ServiceLevel serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	/**
	 * 
	 * @return the containing service level of the penalty
	 */
	public ServiceLevel getServiceLevel() {
		return serviceLevel;
	}

}
