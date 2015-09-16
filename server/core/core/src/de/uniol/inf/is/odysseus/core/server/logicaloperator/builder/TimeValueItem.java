package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.concurrent.TimeUnit;

public class TimeValueItem {
	
	private final long time;
	private final TimeUnit unit;
	
	public TimeValueItem(long time, TimeUnit unit){
		this.time = time;
		if (unit != null){
			this.unit = unit;
		}else{
			this.unit = TimeUnit.MILLISECONDS;
		}
	}
	
	public TimeUnit getUnit() {
		return unit;
	}
	
	public long getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return time+" "+unit;
	}
}
