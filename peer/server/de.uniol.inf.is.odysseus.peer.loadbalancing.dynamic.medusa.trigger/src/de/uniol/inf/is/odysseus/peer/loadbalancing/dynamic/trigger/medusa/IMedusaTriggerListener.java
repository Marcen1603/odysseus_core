package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.medusa;

/**
 * Listener for Medusa Trigger
 * @author Carsten Cordes
 *
 */
public interface IMedusaTriggerListener {

	/**
	 * Called when Trigger triggeres.
	 */
	public void loadBalancingTriggered();
}
