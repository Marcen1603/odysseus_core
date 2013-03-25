/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

/**
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionPolicyListener {

	public void adaptionEventFired(IPlanAdaptionPolicyRuleEngine sender);
}
