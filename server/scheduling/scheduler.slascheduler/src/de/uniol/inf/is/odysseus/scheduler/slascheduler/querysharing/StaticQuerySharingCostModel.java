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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTIPO;

/**
 * Simple standard cost model
 * @author Thomas Vogelgesang
 *
 */
public class StaticQuerySharingCostModel implements IQuerySharingCostModel {

	/**
	 * the returned costs are statically defined for the type of the given
	 * operator
	 */
	@Override
	public double getOperatorCost(IPhysicalOperator op) {
		if (op instanceof JoinTIPO<?, ?> || op instanceof LeftJoinTIPO<?, ?>) {
			// joins are expected to be expensive
			return 2.0;
		} else if (op instanceof IBuffer<?>) {
			// buffers do no action so they are expected to be very cheap
			return 0.0;
		} else {
			// other operators are axpected to have moderate costs
			return 1.0;
		}
	}

}
