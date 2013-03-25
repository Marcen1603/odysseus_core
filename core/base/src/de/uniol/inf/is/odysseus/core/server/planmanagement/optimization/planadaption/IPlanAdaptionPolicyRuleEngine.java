/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

/**
 * Interface providing functionality for policy rules for the plan adaption.
 * Such as when do we want to adapt and how long should we wait after an adaption.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionPolicyRuleEngine {
	

	/**
	 * @return duration of a migration/adaption block
	 */
	public long getBlockedTime();
	
	public void fireAdaptionStartEvent();
		
	/**
	 * Stops the rule engine from sending adaption events.
	 */
	public void start();
	
	/**
	 * Resumes sending adaption events after the blocked time.
	 */
	public void stop();
	
	public void register(IPlanAdaptionPolicyListener listener);
	
	public void unregister(IPlanAdaptionPolicyListener listener);
}
