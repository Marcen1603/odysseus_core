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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.sf;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;

/**
 * Starvation freedom function based on the timestamp of the oldest element
 * buffered in a partial plan. The more the oldest element is waiting, the
 * higher the cost calculated by starvation freedom function.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class ElementTimeStampSF implements IStarvationFreedom {
	/**
	 * list of buffers from the partial plan
	 */
	private List<IBuffer<?>> buffers;

	/**
	 * creates a new element timestamp-based starvation freedom function for the
	 * given plan
	 * 
	 * @param plan
	 *            the plan
	 */
	public ElementTimeStampSF(IPhysicalQuery query) {
		super();
		this.buffers = new ArrayList<IBuffer<?>>();
		for (IPhysicalOperator po : query.getAllOperators()) {
			if (po instanceof IBuffer<?>) {
				IBuffer<?> buffer = (IBuffer<?>) po;
				this.buffers.add(buffer);
			}
		}
	}

	/**
	 * quadratic function for starvation freedom
	 */
	@Override
	public double sf(double decay) {
		// use quadratic function
		double sf = decay * this.longestTupleWaitingTime();
		return sf * sf;
	}

	/**
	 * calculates the waiting time of the oldest buffered element
	 * 
	 * @return the waiting time of the oldest buffered element
	 */
	private long longestTupleWaitingTime() {
		long oldestTS = Long.MAX_VALUE;

		// find element with oldest ts
		for (IBuffer<?> buffer : this.buffers) {
			Object head = buffer.peek();
			if (head instanceof IStreamObject<?>) {
				IStreamObject<?> metaContainer = (IStreamObject<?>) head;
				IMetaAttribute metaData = metaContainer.getMetadata();
				if (metaData instanceof ILatency) {
					ILatency latency = (ILatency) metaData;
					long startTS = latency.getLatencyStart();
					if (startTS < oldestTS) {
						oldestTS = startTS;
					}
				} else {
					throw new RuntimeException("Latency missing");
				}
			}
		}

		// avoid returning negative delta
		long delta = System.nanoTime() - oldestTS;
		return (delta < 0) ? 0 : delta;
	}

}
