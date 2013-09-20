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

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLaConformance;

/**
 * Placement strategy for UpdateRateSource based sla conformance operators
 * 
 * @author Lena Eylert
 * 
 */
public class UpdateRateSourceSLAConformancePlacement implements ISLAConformancePlacement {

	private List<Integer> conformancePlacedForQuery = new ArrayList<>();
	
	/**
	 * places the given sla conformance operator at the sources of the given
	 * partial plan, assuming that one partial plan consists only of one query
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ISubscribable<?, ?>> placeSLAConformance(IPhysicalQuery query,
			ISLAConformance conformance) {
		
		List<ISubscribable<?, ?>> subscribables = new ArrayList<>();
		if (!conformancePlacedForQuery.contains(query.getID())) {

			List<IPhysicalOperator> sources = query.getLeafSources();
			for (IPhysicalOperator source : sources) {
				if (source.isSource()) {
					ISubscribable subscribable = (ISubscribable) source;
//					((IOwnedOperator) conformance).addOwner(source.getOwner());
					subscribable.connectSink(conformance, 0, 0,
							source.getOutputSchema());
					
					List<IPhysicalOperator> list = new ArrayList<>(query.getRoots());
					list.add((IPhysicalOperator)conformance);
					query.setRoots(list);
					subscribables.add(subscribable);
				} else {
					throw new RuntimeException(
							"Cannot connect SLA conformance operator to query source: "
									+ source);
				}
			}
			
			conformancePlacedForQuery.add(query.getID());
		}
		
		return subscribables;
		
//		IPhysicalOperator source = query.getLeafSources().get(0);
//		if (source.isSource()) {
//			ISubscribable subscribable = (ISubscribable) source;
//			subscribable.connectSink(conformance, 0, 0, source.getOutputSchema());
//
//			return subscribable;
//		}
//		throw new RuntimeException(
//				"Cannot connect SLA conformance operator to query root: " + source);
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public void removeSLAConformance(List<ISubscribable<?, ?>> connectionPoints,
			ISLAConformance conformance) {
		for (ISubscribable connectionPoint : connectionPoints) {
			connectionPoint.disconnectSink(conformance, 0, 0,
					((AbstractSLaConformance<?>) conformance).getOutputSchema());
		}
	}

}
