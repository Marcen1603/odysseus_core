/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.AbstractDynamicScheduling;

/**
 * Schedules only <code>IIterableSource</code>s. It will always
 * schedule the source (mostly buffers) containing the most elements.
 * @author Jonas Jacobi, Marco Grawunder
 */

public class BiggestQueueScheduling extends AbstractDynamicScheduling {
	
	public BiggestQueueScheduling(IPhysicalQuery plan) {
		super(plan);
	}

	@Override
	public IIterableSource<?> nextSource() {
		int maxSize = 0; // Treat only Sources with at least one Element
		IIterableSource<?> currentSource = null;
		for (IIterableSource<?> curSource : operators) {
			int curSize = ((IBuffer<?>) curSource).size();
			if (curSize > maxSize) {
				maxSize = curSize;
				currentSource = curSource;
			}
		}
		return currentSource;
	}	
	
	@Override
	public void applyChangedPlan() {
		// Nothing to do
	}

}
