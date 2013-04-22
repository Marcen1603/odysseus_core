/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.equivalentoutput.interval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;

/**
 * Merges the intervals of all value-equal tuples.
 * 
 * @author Merlin Wasmann
 *
 */
public class IntervalMerge {

	/**
	 * Merges the intervals of all tuples.
	 * 
	 * @param input0
	 * @param input1
	 * @return
	 */
	public static List<Tuple> mergeIntervals(List<Tuple> input0) {
		// find tuples with equal values.
		Map<Tuple, List<Pair<Long, Long>>> map = getIntervalMapping(input0);
		
		List<Tuple> merged = mergeIntervals(map);
		
		return merged;
	}
	
	private static List<Tuple> mergeIntervals(Map<Tuple, List<Pair<Long, Long>>> intervalMapping) {
		List<Tuple> merged = new ArrayList<Tuple>();
		
		for(Tuple tuple : intervalMapping.keySet()) {
			if(intervalMapping.get(tuple).size() == 1) {
				// no merge necessary
				merged.add(tuple);
			} else {
				Pair<Long, Long> newInterval = intersectTimeInterval(intervalMapping.get(tuple));
				if(newInterval == null) {
					System.err.println("The intervals for " + tuple.hashCode() + " do not intersect");
					continue;
				}
				merged.add(new Tuple(tuple.getAttributes(), newInterval.getLeft(), newInterval.getRight()));
			}
		}
		
		return merged;
	}

	private static Pair<Long, Long> intersectTimeInterval(List<Pair<Long, Long>> intervals) {
		long start = intervals.get(0).getLeft();
		long end = intervals.get(0).getRight();
		for(int i = 1; i < intervals.size() - 1; i++) {
			start = (start > intervals.get(i).getLeft() ? start : intervals.get(i).getLeft());
			end = (end < intervals.get(i).getRight() ? end : intervals.get(i).getRight());
		}
		if(start < end) {
			return new ImmutablePair<Long, Long>(start, end);
		} else {
			return null;
		}
	}
	
	private static HashMap<Tuple, List<Pair<Long, Long>>> getIntervalMapping(List<Tuple> input) {
		HashMap<Tuple, List<Pair<Long, Long>>> map = new HashMap<Tuple, List<Pair<Long, Long>>>();
		
		for(Tuple tuple : input) {
			if(!map.containsKey(tuple)) {
				List<Pair<Long, Long>> intervals = new ArrayList<Pair<Long, Long>>();
				intervals.add(new ImmutablePair<Long, Long>(tuple.getStartTimestamp(), tuple.getEndTimestamp()));
				map.put(tuple, intervals);
			} else {
				map.get(tuple).add(new ImmutablePair<Long, Long>(tuple.getStartTimestamp(), tuple.getEndTimestamp()));
			}
		}
		
		return map;
	}
	
	
}
