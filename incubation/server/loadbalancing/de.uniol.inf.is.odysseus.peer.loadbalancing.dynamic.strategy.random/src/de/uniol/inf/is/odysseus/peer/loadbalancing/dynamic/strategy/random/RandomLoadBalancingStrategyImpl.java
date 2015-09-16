package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.strategy.random;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.AbstractFiveStepStrategy;

public class RandomLoadBalancingStrategyImpl extends AbstractFiveStepStrategy implements ILoadBalancingStrategy {
private final String STRATEGY_NAME = "Random";
	
	private final String TRIGGER_TO_USE = "Monitoring";
	private final String ALLOCATOR_TO_USE = "Random";
	private final String COMMUNICATORCHOOSER_TO_USE = "OdyLoad";
	private final String TRANSMISSION_HANDLER_TO_USE = "OdyLoad";
	
	private final String SELECTOR = "Random";
	
	
	@Override
	public String getName() {
		return STRATEGY_NAME;
	}
	
	@Override
	public void startMonitoring() throws LoadBalancingException {
		//Configure Basic Strategy.
		setAllocator(ALLOCATOR_TO_USE);
		setTrigger(TRIGGER_TO_USE);
		setTransmissionHandler(TRANSMISSION_HANDLER_TO_USE);
		setSelector(SELECTOR);
		setCommunicatorChooser(COMMUNICATORCHOOSER_TO_USE);
		
		//Start Monitoring.
		super.startMonitoring();
	}
}
