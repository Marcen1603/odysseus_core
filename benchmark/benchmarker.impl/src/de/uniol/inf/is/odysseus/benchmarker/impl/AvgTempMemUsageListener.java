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

import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;

/**
 * Collects memory usage for a given operator using SweepAreas.
 * 
 * @author Jan Steinke
 * 
 */
public class AvgTempMemUsageListener implements IPOEventListener {

	private DescriptiveStatistics stats = new DescriptiveStatistics();

	public DescriptiveStatistics getStats() {
		return stats;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	@Override
	public synchronized void eventOccured(IEvent<?,?> event) {
		POEvent poEvent = (POEvent) event;
		long tmp = 0;

		IPhysicalOperator sourceOp = poEvent.getSender();
		if (sourceOp instanceof JoinTIPO) {
			final ISweepArea[] areas = ((JoinTIPO) sourceOp).getAreas();

			for (ISweepArea each : areas) {
				tmp += each.size();
			}
		} else if (sourceOp instanceof IBuffer) {
			tmp = ((IBuffer) sourceOp).size();
		}
		try {
			stats.addValue(tmp);
		} catch (Exception e) {
		}
	}

}
