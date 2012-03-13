/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

/**
 * Special BenchmarkPO, needed to use prios and selectivity
 * 
 * @author Jonas Jacobi
 * 
 * @param <T>
 */
public class PriorityBenchmarkPO<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BenchmarkPO<T> {

	double[] oldVals = new double[128];

	public PriorityBenchmarkPO(int processingTime,
			double selectivity) {
		super(processingTime, selectivity);
		for (int i = 0; i < 128; ++i) {
			oldVals[i] = this.selectivity;
		}
	}

	@Override
	protected double getOldVal(T element) {
		byte curP = element.getMetadata().getPriority();
		return oldVals[curP];
	}
	
	@Override
	protected void setOldVal(T element, double d) {
		byte curP = element.getMetadata().getPriority();
		oldVals[curP] = d;
	}
}
