package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

/**
 * Interface to a DynaQuest event history
 * @author Jonas Jacobi
 */
public interface IPOMetadatarepository {
	
	/**
	 * Get all events in the event history for a specific planoperator
	 * @param poGuid unique identifier of the planoperator ({@link PlanOperator#getGuid()})
	 * @return all events of the planoperator sorted chronologically beginning with the oldest 
	 * @throws Exception gets thrown if events could not be retrieved
	 */
	public List<POEventData> getEvents(String poGuid) throws Exception;
	
	/**
	 * Get all plans in the event history
	 * @return all plans in the event history
	 */
	public List<PlanOperator> getPlans();
	
	/**
	 * Get all plans in the event history which were
	 * executed after a specific time.
	 * @param afterTimestamp a timestamp in milliseconds since 1.1.1970
	 * @return all plans in the event history which have been
	 * executed after afterTimestamp
	 */
	public List<PlanOperator> getPlans(long afterTimestamp);
}
