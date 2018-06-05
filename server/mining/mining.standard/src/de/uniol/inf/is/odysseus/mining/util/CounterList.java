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
package de.uniol.inf.is.odysseus.mining.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Dennis Geesen
 * 
 */
public class CounterList<T> {

	private Map<T, Integer> counts = new HashMap<T, Integer>();
	private int totalCount = 0;

	public void count(T object) {
		if (counts.containsKey(object)) {
			int val = counts.get(object);
			val = val + 1;
			counts.put(object, val);
		} else {
			counts.put(object, 1);
		}
		totalCount++;
	}

	public int getTotalCount() {
		return this.totalCount;
	}
	
	public T getMostFrequent(){
		int max = 0;
		T maxT = null;
		for(Entry<T, Integer> e : this.counts.entrySet()){
			int currentCount = e.getValue().intValue();
			if(currentCount>=max){
				max = currentCount;
				maxT = e.getKey();
			}
		}
		
		return maxT;
	}

	public int getCount(T object) {
		if (counts.containsKey(object)) {
			return counts.get(object);
		} else {
			return 0;
		}
	}

}
