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

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Interface for placing/removing sla conformance operators in/from partial
 * plans
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface ISLAConformancePlacement {

	/**
	 * places the given conformance in the given partial plan
	 * 
	 * @param plan
	 *            the partial plan
	 * @param conformance
	 *            the sla conformance operator
	 * @return the operator from the given plan, where the sla conformance
	 *         operator is appended to
	 */
	public ISubscribable<?, ?> placeSLAConformance(IPhysicalQuery query,
			ISLAConformance conformance);

	/**
	 * removes the given sla conformance operator from the given physical
	 * operator it is connected to
	 * 
	 * @param connectionPoint
	 *            the physical operator the sla conformance operator is
	 *            connected to
	 * @param conformance the sla conformance operator to remove
	 */
	public void removeSLAConformance(ISubscribable<?, ?> connectionPoint,
			ISLAConformance conformance);

}
