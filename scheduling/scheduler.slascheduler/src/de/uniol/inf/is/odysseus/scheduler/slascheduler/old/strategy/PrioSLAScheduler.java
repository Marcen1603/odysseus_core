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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

// This class is only for testing purposes to compare
// sla based scheduling with priority based scheduling
// do not use this class in real szenarios ;-)

public class PrioSLAScheduler extends AbstractTimebasedSLAScheduler{

	
	final private int minPrio;
	final Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	
	public PrioSLAScheduler(int minPrio) {
		super(PrioCalcMethod.MAX);
		this.minPrio = minPrio;
	}
	
	public PrioSLAScheduler(PrioSLAScheduler other){
		super(other);
		this.minPrio = other.minPrio;
	}

	public PrioSLAScheduler(PrioCalcMethod method) {
		super(method);
		this.minPrio = 0;
	}

	@Override
	public IScheduling nextPlan() {
		IScheduling plan = super.nextPlan();
		if (plan != null) return plan;
		long maxPrio = getMaxPrio();
		return initLastRunAndReturn(maxPrio);
		
	}
	
	protected IScheduling updatePriority(IScheduling current) {
		long currentPriority = current.getPlan().getCurrentPriority();
		long newPrio = currentPriority - 1;
		if (newPrio <= minPrio) {
			newPrio = current.getPlan().getBasePriority();
		}
		if (newPrio != currentPriority) {
			current.getPlan().setCurrentPriority(newPrio);
		}
		return current;
	}
	
	@Override
	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		drainHistory(toSchedule);
		return updatePriority(super.updateMetaAndReturnPlan(toSchedule));
	}

	@Override
	public PrioSLAScheduler clone() {
		return new PrioSLAScheduler(this);
	}

}
