package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for distributors of {@link SLAViolationEvent}. Required to decouple
 * event generation and event handling, so no processing time of a selected 
 * partial plan is wasted by event lsisteners.
 * 
 * @author Thomas Vogelgesang
 *
 */
public interface ISLAViolationEventDistributor {
	
	/**
	 * adds the given event to the event queue 
	 * @param event the event to buffer
	 */
	public void queueSLAViolationEvent(SLAViolationEvent event);

}
