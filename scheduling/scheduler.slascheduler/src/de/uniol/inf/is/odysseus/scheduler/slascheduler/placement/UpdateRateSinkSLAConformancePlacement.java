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
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.billingmodel.physicaloperator.TupleCostCalculationPipe;
import de.uniol.inf.is.odysseus.billingmodel.physicaloperator.TupleCostCalculationPipe.TupleCostCalculationType;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.intervalapproach.AssureHeartbeatPO;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.Helper;
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

	/**
	 * places the given sla conformance operator at the root of the given
	 * partial plan, assuming that one partial plan consists only of one query
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ISubscribable<?, ?> placeSLAConformance(IPhysicalQuery query, IPhysicalOperator root,
			ISLAConformance conformance) {
		// it is expected that there is only one query per partial plan!
		
		IPhysicalOperator op;
		if (root.isSource()) { // Source or Pipe
			op = root;
		} else if (root.isSink() && !root.isSource()) { // Sink
			PhysicalSubscription<? extends ISource<?>> s = ((ISink<?>) root).getSubscribedToSource().iterator().next();
			op = s.getTarget();
		} else
			throw new RuntimeException("Cannot connect SLA conformance operator to query root: " + root);
		
		ArrayList<IPhysicalOperator> operatorsToAdd = new ArrayList<IPhysicalOperator>();
		ISubscribable subscribable;
		if (Helper.useBillingModel()) {
			if (((IOwnedOperator)conformance).getOwner().size() == 0)
				((IOwnedOperator)conformance).addOwner(root.getOwner());
			
			TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe(TupleCostCalculationType.OUTGOING_TUPLES);
			if (((IOwnedOperator)costCalc).getOwner().size() == 0)
				((IOwnedOperator)costCalc).addOwner(root.getOwner());
			
			AssureHeartbeatPO<?> heartbeat = new AssureHeartbeatPO<>(true);
			heartbeat.setSendAlwaysHeartbeat(true);
			heartbeat.setAllowOutOfOrder(true);
			heartbeat.setRealTimeDelay(500, TimeUnit.MILLISECONDS);
			if (((IOwnedOperator)heartbeat).getOwner().size() == 0)
				((IOwnedOperator)heartbeat).addOwner(root.getOwner());

//			subscribable = heartbeat;
//			subscribable.connectSink(costCalc, 0, 0, op.getOutputSchema());
//			subscribable.connectSink(conformance, 0, 0, op.getOutputSchema());
//
//			subscribable = (ISubscribable) op;
//			subscribable.connectSink(heartbeat, 0, 0, op.getOutputSchema());
//			
//			operatorsToAdd.add(costCalc);
//			operatorsToAdd.add((IPhysicalOperator) conformance);
			
			subscribable = costCalc;
			subscribable.connectSink(conformance, 0, 0, op.getOutputSchema());
			
//			AssureHeartbeatPO<?> heartbeat = new AssureHeartbeatPO<>(true);
//			heartbeat.setSendAlwaysHeartbeat(true);
//			heartbeat.setAllowOutOfOrder(true);
//			heartbeat.setRealTimeDelay(500, TimeUnit.MILLISECONDS);
//			if (((IOwnedOperator)heartbeat).getOwner().size() == 0)
//				((IOwnedOperator)heartbeat).addOwner(root.getOwner());
			subscribable = heartbeat;
			subscribable.connectSink(costCalc, 0, 0, op.getOutputSchema());

			subscribable = (ISubscribable) op;
			subscribable.connectSink(heartbeat, 0, 0, op.getOutputSchema());
			
			operatorsToAdd.add((IPhysicalOperator) conformance);
		} else {
			if (((IOwnedOperator)conformance).getOwner().size() == 0)
				((IOwnedOperator)conformance).addOwner(root.getOwner());
			AssureHeartbeatPO<?> heartbeat = new AssureHeartbeatPO<>(true);
			heartbeat.setSendAlwaysHeartbeat(true);
			heartbeat.setAllowOutOfOrder(true);
			heartbeat.setRealTimeDelay(500, TimeUnit.MILLISECONDS);
			if (((IOwnedOperator)heartbeat).getOwner().size() == 0)
				((IOwnedOperator)heartbeat).addOwner(root.getOwner());
			subscribable = heartbeat;
			subscribable.connectSink(conformance, 0, 0, op.getOutputSchema());
			
			subscribable = (ISubscribable) op;
			subscribable.connectSink(heartbeat, 0, 0, op.getOutputSchema());
			
			operatorsToAdd.add((IPhysicalOperator) conformance);
		}
//		query.replaceRoot(root, (IPhysicalOperator)conformance);
		
		ArrayList<IPhysicalOperator> list = new ArrayList<IPhysicalOperator>(query.getRoots());
		list.addAll(operatorsToAdd);
		query.setRoots(list);
		
		return subscribable;
			

//		if (root.isSource()) { // Source or Pipe
//			ISubscribable subscribable;
//			if (calculateTupleCosts) {
//				TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
//				subscribable = costCalc;
//				subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
//
//				subscribable = (ISubscribable) root;
//				subscribable.connectSink(costCalc, 0, 0, root.getOutputSchema());
//			} else {
//				subscribable = (ISubscribable) root;
//				subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
//			}
//			return subscribable;
//		} else if (root.isSink() && !root.isSource()) { // Sink
//			// assumption: a sink has just one inputstream
//			PhysicalSubscription<? extends ISource<?>> s = ((ISink<?>) root).getSubscribedToSource().iterator().next();
//			ISource source = s.getTarget();
//			ISubscribable subscribable;
//			if (calculateTupleCosts) {
//				TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
//				subscribable = costCalc;
//				subscribable.connectSink(conformance, 0, 0, source.getOutputSchema());
//				
//				AssureHeartbeatPO<?> heartbeat = new AssureHeartbeatPO<>(true);
//				heartbeat.setSendAlwaysHeartbeat(true);
//				subscribable = heartbeat;
//				subscribable.connectSink(costCalc, 0, 0, source.getOutputSchema());
//
//				subscribable = (ISubscribable) source;
//				subscribable.connectSink(heartbeat, 0, 0, source.getOutputSchema());
//			} else {
//				subscribable = (ISubscribable) source;
//				subscribable.connectSink(conformance, 0, 0, source.getOutputSchema());
//			}
//			return subscribable;
			
			/*for (PhysicalSubscription<? extends ISource<?>> s : ((ISink<?>) root).getSubscribedToSource()) {
				ISource source = s.getTarget();
				ISubscribable subscribable;
				if (calculateTupleCosts) { // add AssureHeartBeat
					// AssureHeartbeatPO<?> heartbeat = new
					// AssureHeartbeat();
					TupleCostCalculationPipe<?> costCalc = new TupleCostCalculationPipe();
					subscribable = costCalc;
					subscribable.connectSink(conformance, 0, 0, source.getOutputSchema());

					subscribable = (ISubscribable) source;
					subscribable.connectSink(costCalc, 0, 0, source.getOutputSchema());
				} else {
					subscribable = (ISubscribable) source;
					subscribable.connectSink(conformance, 0, 0, source.getOutputSchema());
				}
				subscribables.add(subscribable);
			}
			return subscribables;*/
//		} else
//			throw new RuntimeException("Cannot connect SLA conformance operator to query root: " + root);
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
