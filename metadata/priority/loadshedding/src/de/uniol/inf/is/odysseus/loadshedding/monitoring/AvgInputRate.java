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
package de.uniol.inf.is.odysseus.loadshedding.monitoring;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.loadshedding.LoadManager;

public class AvgInputRate implements
		IPOEventListener {

	public static final double UNKOWN = -1;
	private LoadManager target;

	public AvgInputRate(LoadManager target) {
		this.target = target;
	}

	private double streams = 0;
	private double read = 0;

	public void addInputStream(IPhysicalOperator source) {
		streams++;
	}

	public void removeInputStream(IPhysicalOperator source) {
		streams--;
	}

	public Double getAvgInputRate() {
		if (streams == 0) {
			return UNKOWN;
		}
        double result = read/streams;
        return result;
	}

	@Override
	public void eventOccured(IEvent<?,?> poEvent, long eventNanoTime) {
		read++;
		target.updateLoadSheddingState();
	}
	
	public void reset() {
		read = 0;
	}


}
