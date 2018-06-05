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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;

/**
 * Starvation freedom function based on the size of the buffers of the related
 * partial plan. the bigger the queue size, the higher the cost calculated by
 * starvation freedom function.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class QueueSizeSF implements IStarvationFreedom {
	/**
	 * list of buffers of the related partial plan
	 */
	private List<IBuffer<?>> buffers;

	/**
	 * creates a new queue size-based starvation freedom function for the given
	 * partial plan
	 * 
	 * @param plan
	 *            the partial plan
	 */
	public QueueSizeSF(IPhysicalQuery query) {
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
		double sf = this.queueSize() * decay;
		return sf * sf;
	}

	/**
	 * @return returns the total size of all queues in the related partial plan
	 */
	private int queueSize() {
		int size = 0;
		for (IBuffer<?> buffer : this.buffers) {
			size += buffer.size();
		}
		return size;
	}

}
