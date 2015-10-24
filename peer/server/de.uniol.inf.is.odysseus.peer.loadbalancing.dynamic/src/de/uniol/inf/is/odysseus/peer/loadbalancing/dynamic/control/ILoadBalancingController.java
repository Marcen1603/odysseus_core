package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.control;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingControllerListener;

/**
 * Provides Methods to control Load Balancing Processes from user level.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingController {
	
	/**
	 * Set Load balancing Strategy to use. Depending on Strategy, this overrides other settings.
	 * @param strategyName Name of Strategy to use for Load balancing
	 * @return true if strategy is found.
	 */
	public boolean setLoadBalancingStrategy(String strategyName);
	
	/**
	 * Checks if Load Balancing is already running
	 * @return true if a Load Balancing Strategy is active.
	 */
	public boolean isLoadBalancingRunning();
	
	/**
	 * Gets currenty selected Strategy.
	 * @return Name of currently selected Strategy
	 */
	public String getSelectedLoadBalancingStrategy();
	
	/**
	 * Stop Dynamic Load Balancing
	 */
	public void stopLoadBalancing();
	
	/**
	 * Start Dynamic Load Balancing
	 */
	public void startLoadBalancing();
	
	/**
	 * Returns list of available Load Balancing Strategies.
	 * @return List of Strategies that are available for dynamic load Balancing
	 */
	public Set<String> getAvailableStrategies();
	
	/**
	 * Forces a redistribution of Load with currently selected Load Balancing Strategy 
	 * (for debug purpose only)
	 */
	public void forceLoadBalancing();
	
	/**
	 * Adds Listener that is notified when Strategy is changed or Load Balancing is activated.
	 * @param listener Listener to add.
	 */
	public void addControllerListener(ILoadBalancingControllerListener listener);
	
	/**
	 * Removes Listener that is notified when Strategy is changed or Load Balancing is activated.
	 * @param listener Listener to remove.
	 */
	public void removeControllerListener(ILoadBalancingControllerListener listener);
}
