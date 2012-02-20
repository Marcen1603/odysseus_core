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
package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPhysicalPlan;

/**
 * ReoptimizeTimer is a reoptimze rule. After a defined time an reoptimize
 * request is send.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
final public class ReoptimizeTimer extends AbstractPlanReoptimizeRule implements
		Runnable {

	private long period;
	private Thread thread;

	/**
	 * Creates a new time rule with a specific time period.
	 * 
	 * @param period
	 *            Period which defines after which time a reoptimization is
	 *            fired.
	 */
	public ReoptimizeTimer(long time) {
		this.period = time;
		this.reoptimizable = new ArrayList<IPhysicalPlan>();
		this.thread = new Thread(this);
		this.thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(this.period);
				fireReoptimizeEvent();
			}
		} catch (InterruptedException e) {
			//end
		}
	}

	@Override
	public void deinitialize() {
		this.thread.interrupt();
	}
}
