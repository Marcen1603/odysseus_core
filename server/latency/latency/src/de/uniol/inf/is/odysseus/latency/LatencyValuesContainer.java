package de.uniol.inf.is.odysseus.latency;

public class LatencyValuesContainer {

	private static long sum;
	private static int count;
	
	public synchronized static void add( long value ) {
		sum += value;
		count++;
	}
	
	public synchronized static long popAverage() {
		if( count == 0 ) {
			return 0;
		}
		
		long ret = sum / count;
		sum = 0;
		count = 0;
		
		return ret;
	}
}
