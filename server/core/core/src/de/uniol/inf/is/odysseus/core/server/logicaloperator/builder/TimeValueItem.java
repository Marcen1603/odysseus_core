package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.concurrent.TimeUnit;

public class TimeValueItem {
	
	private final long time;
	private final TimeUnit unit;
	
	public TimeValueItem(long time, TimeUnit unit){
		this.time = time;
		this.unit = unit;
	}
	
	public TimeUnit getUnit() {
		return unit;
	}
	
	public long getTime() {
		return time;
	}
}
