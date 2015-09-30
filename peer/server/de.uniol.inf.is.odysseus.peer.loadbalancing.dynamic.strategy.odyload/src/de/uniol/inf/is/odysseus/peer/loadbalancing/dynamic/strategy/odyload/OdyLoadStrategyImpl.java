package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.strategy.odyload;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.AbstractFiveStepStrategy;

public class OdyLoadStrategyImpl extends AbstractFiveStepStrategy implements ILoadBalancingStrategy {

	private final String STRATEGY_NAME = "OdyLoad";
	
	private final String TRIGGER_TO_USE = "Monitoring";
	private final String ALLOCATOR_TO_USE = "OdyLoad";
	private final String COMMUNICATORCHOOSER_TO_USE = "OdyLoad";
	private final String TRANSMISSION_HANDLER_TO_USE = "OdyLoad";
	
	private final String FIRST_SELECTOR = "Greedy";
	private final String SECOND_SELECTOR = "SimulatedAnnealing";
	
	
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
		List<String> selectorsToUse = Lists.newArrayList();
		selectorsToUse.add(FIRST_SELECTOR);
		selectorsToUse.add(SECOND_SELECTOR);
		setSelectors(selectorsToUse);
		setCommunicatorChooser(COMMUNICATORCHOOSER_TO_USE);
		
		//Start Monitoring.
		super.startMonitoring();
	}
	

}
