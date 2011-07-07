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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.AuroraMinCost;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.AuroraMinLatency;


public class AuroraFactory extends AbstractSchedulingFactory {

	static public enum AuroraStrategy{MIN_LATENCY, MIN_COST, MIN_MEM}

	private AuroraStrategy strategy;;
	private boolean useIter;
	
	public AuroraStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AuroraStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isUseIter() {
		return useIter;
	}

	public void setUseIter(boolean useIter) {
		this.useIter = useIter;
	}

	public AuroraFactory(){
		this.strategy = AuroraStrategy.MIN_LATENCY;
		this.useIter = true;
	}
	
	@Override	
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary<?,?> properties = context.getProperties();
		this.strategy = AuroraStrategy.valueOf((String)properties.get("aurora.strategy"));
		this.useIter = (Boolean)properties.get("execlist.useIter");
	}
	
	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		switch(strategy){
		case MIN_LATENCY:
			return new AuroraMinLatency(plan);
		case MIN_COST:
			return new AuroraMinCost(plan);
		case MIN_MEM:
			//return new AuroraMinMemory(plan);
			throw new RuntimeException("Not implemented");
		}
		
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" strategy="+strategy;
	}

}
