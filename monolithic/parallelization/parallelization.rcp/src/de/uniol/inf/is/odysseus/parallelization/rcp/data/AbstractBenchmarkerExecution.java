/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBenchmarkerExecution implements IBenchmarkerExecution{

	protected List<Long> executionTimes = new ArrayList<Long>();
	
	public abstract String toString();
	
	@Override
	public long getAverageExecutionTime(long maximumExecutionTime) {
		long sumExecutionTime = 0l;
		
		for (Long executionTime : executionTimes) {
			if (executionTime == -1l){
				return -1l;
			} else if (executionTime == 0l){
				return 0l;
			}  else if (executionTime >= maximumExecutionTime){
				return maximumExecutionTime;
			} else {
				sumExecutionTime += executionTime;				
			}
		}
		return sumExecutionTime / executionTimes.size();
	}

	@Override
	public void addExecutionTime(long executionTime) {
		this.executionTimes.add(executionTime);
	}
}
