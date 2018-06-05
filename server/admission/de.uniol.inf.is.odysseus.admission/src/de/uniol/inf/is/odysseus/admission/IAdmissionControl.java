package de.uniol.inf.is.odysseus.admission;

public interface IAdmissionControl {
	public <E extends IAdmissionEvent> void processEventAsync(E event);
	public <E extends IAdmissionEvent> void processEventDelayedAsync(E event, int delayMillis);
}
