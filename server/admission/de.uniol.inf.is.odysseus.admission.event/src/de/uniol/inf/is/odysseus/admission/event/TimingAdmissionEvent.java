package de.uniol.inf.is.odysseus.admission.event;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class TimingAdmissionEvent implements IAdmissionEvent {

	private final long timeIntervalMillis;
	private final long totalTimeMillis;
	
	public TimingAdmissionEvent( long timeIntervalMillis, long totalTimeMillis ) {
		Preconditions.checkArgument(timeIntervalMillis > 0, "TimeINtervalMillis must be positive!");
		Preconditions.checkArgument(totalTimeMillis > 0, "TotalTimeMillis must be positive");
		
		this.timeIntervalMillis = timeIntervalMillis;
		this.totalTimeMillis = totalTimeMillis;
	}
	
	public long getTimeIntervalMillis() {
		return timeIntervalMillis;
	}
	
	public long getTotalTimeMillis() {
		return totalTimeMillis;
	}
}
