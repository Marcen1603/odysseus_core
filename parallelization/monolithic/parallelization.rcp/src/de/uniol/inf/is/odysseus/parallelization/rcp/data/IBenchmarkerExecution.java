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

/**
 * This interface defines executions for benchmarker. the executions are
 * calculated from the configuration the user has defined in the benchmarker UI.
 * Possible executions are for inter or intra operator parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IBenchmarkerExecution {

	/**
	 * returns the odysseus script keywords for this execution
	 * 
	 * @return odysseus script keywords
	 */
	String getOdysseusScript();

	/**
	 * calculates the median of execution times for an execution which is executed
	 * multiple times to get better results
	 * 
	 * @return median of execution time
	 */
	long getMedianExecutionTime();

	/**
	 * adds an execution time to this execution. 
	 * 
	 * @param executionTime
	 */
	void addExecutionTime(long executionTime);
}
