package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for listining on {@link SLAViolationEvent}
 * @author Thomas Vogelgesang
 *
 */
public interface ISLAViolationEventListener {

	/**
	 * handles the given event
	 * @param event the event to handle
	 */
	public void slaViolated(SLAViolationEvent event);
	
}
