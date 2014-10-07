package de.uniol.inf.is.odysseus.latency;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.latency.physicaloperator.CalcLatencyPO;

public class LatencyValuesContainer {

	private static Map<CalcLatencyPO<?>, Long> sumMap = new HashMap<CalcLatencyPO<?>, Long>();
	private static Map<CalcLatencyPO<?>, Long> countMap = new HashMap<CalcLatencyPO<?>, Long>();
	private static long countSum;
	
	public synchronized static void add( long value, CalcLatencyPO<?> op ) {
		if( !sumMap.containsKey(op)) {
			sumMap.put(op, value);
			countMap.put(op, 1L);
		}  else {
			sumMap.put(op, sumMap.get(op) + value);
			countMap.put(op, countMap.get(op) + 1);
		}
		countSum++;
	}
	
	public synchronized static long popCount() {
		long ret = countSum;
		countSum = 0;
		return ret;
	}
	
	public synchronized static long popAverage() {
		if( sumMap.isEmpty() ) {
			return 0;
		}
		
		long avg = 0;
		for( CalcLatencyPO<?> op : sumMap.keySet() ) {
			long sum = sumMap.get(op);
			long count = countMap.get(op);
			
			avg += (sum / count);
		}
		
		long ret = avg / sumMap.size();
		sumMap.clear();
		countMap.clear();
		
		return ret;
	}
}
