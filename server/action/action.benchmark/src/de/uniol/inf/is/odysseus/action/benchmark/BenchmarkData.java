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
package de.uniol.inf.is.odysseus.action.benchmark;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkData {
	private long creationTime;
	private Map<String, Long> outputTimes;
	
	public BenchmarkData(String identifier){
		this.creationTime = System.currentTimeMillis();
		this.outputTimes = new HashMap<String, Long>();
	}
	
	
	public BenchmarkData(long creationTime) {
		this.creationTime = creationTime;
		this.outputTimes = new HashMap<String, Long>();
	}

	public void addOutputTime(String id){
		this.outputTimes.put(id, System.currentTimeMillis());
	}
	
	public void addOutputTime(String id, long timestamp){
		this.outputTimes.put(id, timestamp);
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public Map<String, Long> getOutputTimes() {
		return outputTimes;
	}
	
	


}
