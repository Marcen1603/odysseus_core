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
package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.io.Serializable;
import java.util.Comparator;

public class CurrentPlanPriorityComperator implements Comparator<IScheduling>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(IScheduling p1, IScheduling p2) {
		long p1Priority = p1.getPlan().getCurrentPriority();
		long p2priority = p2.getPlan().getCurrentPriority();
		if (p1Priority < p2priority){
			return 1;
		}
		if (p1Priority > p2priority){
			return -1;
		}
		return 0;
	}

}
