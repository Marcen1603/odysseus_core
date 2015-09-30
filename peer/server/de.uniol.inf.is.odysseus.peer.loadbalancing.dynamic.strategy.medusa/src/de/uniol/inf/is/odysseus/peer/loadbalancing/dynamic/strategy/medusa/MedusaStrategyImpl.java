package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.strategy.medusa;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.AbstractFiveStepStrategy;

public class MedusaStrategyImpl extends AbstractFiveStepStrategy implements ILoadBalancingStrategy {

	
	private static final String STRATEGY_NAME = "Medusa";
	
	private static final String TRIGGER_NAME = "Medusa";
	private static final String SELECTOR_NAME = "Medusa";
	private static final String ALLOCATOR_NAME = "Medusa";
	private static final String COMMUNICATOR_CHOOSER_NAME = "OdyLoad";
	private static final String TRANSMISSION_HANDLER_NAME = "OdyLoad";
	
	
	
	
	
	
	
	@Override
	public String getName() {
		return STRATEGY_NAME;
	}


	@Override
	public void startMonitoring() throws LoadBalancingException {
		setTrigger(TRIGGER_NAME);
		setSelector(SELECTOR_NAME);
		setAllocator(ALLOCATOR_NAME);
		setCommunicatorChooser(COMMUNICATOR_CHOOSER_NAME);
		setTransmissionHandler(TRANSMISSION_HANDLER_NAME);
		super.startMonitoring();
	}

}
