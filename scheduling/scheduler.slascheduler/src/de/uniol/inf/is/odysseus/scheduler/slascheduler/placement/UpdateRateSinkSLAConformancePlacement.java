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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.billingmodel.physicaloperator.TupleCostCalculationPipe;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
//import de.uniol.inf.is.odysseus.intervalapproach.AssureHeartbeatPO;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLaConformance;

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
	public List<ISubscribable<?, ?>> placeSLAConformance(IPhysicalQuery query,
			ISLAConformance conformance) {
		calculateTupleCosts = false;
		// it is expected that there is only one query per partial plan!
		List<ISubscribable<?, ?>> subscribables = new ArrayList<>();
		// get sinks of query
		List<IPhysicalOperator> roots = query.getRoots();
		for (IPhysicalOperator root : roots) {
			if (root.isSource()) {
				ISubscribable subscribable;
				if (calculateTupleCosts) {
					TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
					subscribable = costCalc;
					((IOwnedOperator) conformance).addOwner(root.getOwner());
					subscribable.connectSink(conformance, 0, 0,
							root.getOutputSchema());

					subscribable = (ISubscribable) root;
					((IOwnedOperator) costCalc).addOwner(root.getOwner());
					subscribable.connectSink(costCalc, 0, 0,
							root.getOutputSchema());
				} else {
					subscribable = (ISubscribable) root;
					((IOwnedOperator) conformance).addOwner(root.getOwner());
					subscribable.connectSink(conformance, 0, 0,
							root.getOutputSchema());
				}
				subscribables.add(subscribable);
			} else if (root.isSink() && !root.isSource()) {
				// kann eine Senke mehrere Vorgaenger haben??? yes she can!
				for (PhysicalSubscription<? extends ISource<?>> s : ((ISink<?>) root)
						.getSubscribedToSource()) {
					ISource source = s.getTarget();
					ISubscribable subscribable;
					if (calculateTupleCosts) { //add AssureHeartBeat
//						AssureHeartbeatPO<?> heartbeat = new AssureHeartbeat();
						TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
						subscribable = costCalc;
						((IOwnedOperator) conformance).addOwner(source.getOwner());
						subscribable.connectSink(conformance, 0, 0,
								source.getOutputSchema());
						
						subscribable = (ISubscribable) source;
						((IOwnedOperator) costCalc).addOwner(source.getOwner());
						subscribable.connectSink(costCalc, 0, 0,
								source.getOutputSchema());
					} else {
						subscribable = (ISubscribable) source;
						((IOwnedOperator) conformance).addOwner(source.getOwner());
//						((ISink)conformance).open(source.getOwner().get(0));
						subscribable.connectSink(conformance, 0, 0,
								source.getOutputSchema());
					}
					subscribables.add(subscribable);
				}
			}
		}
		return subscribables;
		
//		throw new RuntimeException(
//				"Cannot connect SLA conformance operator to query root: "
//						+ root);
	}

	/**
	 * removes the given sla conformance operator from the related partial plan
	 * by disconnecting from the root operator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void removeSLAConformance(List<ISubscribable<?, ?>> connectionPoints,
			ISLAConformance conformance) {
		for (ISubscribable connectionPoint : connectionPoints) {
			connectionPoint.disconnectSink(conformance, 0, 0,
					((AbstractSLaConformance<?>) conformance).getOutputSchema());
		}
	}

}
