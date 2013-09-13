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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.placement;

//import de.uniol.inf.is.odysseus.billingmodel.physicaloperator.TupleCostCalculationPipe;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLaConformance;

//import de.uniol.inf.is.odysseus.billingmodel.physicaloperator.TupleCostCalculationPipe;

/**
 * Placement strategy for UpdateRateSink based sla conformance  operators
 * 
 * @author Lena Eylert
 * 
 */
public class UpdateRateSinkSLAConformancePlacement implements
		ISLAConformancePlacement {

	private boolean calculateTupleCosts;

	/**
	 * places the given sla conformance operator at the root of the given
	 * partial plan, assuming that one partial plan consists only of one query
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ISubscribable<?, ?> placeSLAConformance(IPhysicalQuery query,
			ISLAConformance conformance) {
		calculateTupleCosts = false;
		// it is expected that there is only one query per partial plan!
		// get sinks of query
		IPhysicalOperator root = query.getRoots().get(0);
		if (root.isSource()) {
			ISubscribable subscribable;
//			if (calculateTupleCosts) {
//				TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
//				subscribable = costCalc;
//				subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
//
//				subscribable = (ISubscribable) root;
//				subscribable.connectSink(costCalc, 0, 0, root.getOutputSchema());
//			} else {
				subscribable = (ISubscribable) root;
				subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
//			}

			return subscribable;
		}
		throw new RuntimeException(
				"Cannot connect SLA conformance operator to query root: "
						+ root);
	}

	/**
	 * removes the given sla conformance operator from the related partial plan
	 * by disconnecting from the root operator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void removeSLAConformance(ISubscribable connectionPoint,
			ISLAConformance conformance) {
		connectionPoint.disconnectSink(conformance, 0, 0,
				((AbstractSLaConformance<?>) conformance).getOutputSchema());
	}

}
