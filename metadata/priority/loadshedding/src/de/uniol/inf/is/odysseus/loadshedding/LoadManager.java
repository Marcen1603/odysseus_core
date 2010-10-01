package de.uniol.inf.is.odysseus.loadshedding;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.loadshedding.monitoring.AvgInputRate;
import de.uniol.inf.is.odysseus.loadshedding.strategy.SimpleLoadSheddingStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public class LoadManager {
	
	private static LoadManager instance = null;
	
	private List<Double> capacities = new ArrayList<Double>();
	
	private ArrayList<AvgInputRate> avgInputs = new ArrayList<AvgInputRate>();
	
	private List<DirectLoadSheddingBuffer<?>> shedders = new ArrayList<DirectLoadSheddingBuffer<?>>();

	private boolean isActive;
	
	private ILoadSheddingStrategy strategy = null;
	
	private LoadManager(ILoadSheddingStrategy strategy) {
		if(strategy == null) {
			this.strategy = new SimpleLoadSheddingStrategy();
		} else {
			this.strategy = strategy;
		}
	}
	
	/**
	 * Returns a load manager using the singleton pattern
	 * @param strategy depending load shedding strategy or null for SimpleLoadSheddingStrategy
	 * @return
	 */
	public static LoadManager getInstance(ILoadSheddingStrategy strategy) {
		if(instance == null) {
			instance = new LoadManager(strategy);
		}
		return instance;
	}
	
	
	public void addCapacities(IPartialPlan plan) {
		
		AvgInputRate avgInput = new AvgInputRate(this);
		
		for(IPhysicalOperator each : plan.getRoots()) {
			if(each.isSink()){
				List<ISource<?>> leafs = new ArrayList<ISource<?>>();
				findLeafs((ISink<?>)each, leafs);
				
				for(ISource<?> leaf : leafs) {
					avgInput.addInputStream(leaf);
					leaf.subscribe(avgInput, POEventType.PushInit);
				}
			}
		}
		avgInputs.add(avgInput);
		capacities.add(strategy.calcCapacity(plan));
	}
	
	public synchronized void updateLoadSheddingState() {
		/*
		 * Wenn state > 1, dann Load Shedding aktivieren
		 */
		double state = 0;
		
		for(int i=0; i < avgInputs.size(); i++) {
			double avgInputRate = avgInputs.get(i).getAvgInputRate();
			
			double capacity = capacities.get(i);
			if(capacity == 0) {
				return;
			}
			
			state += (avgInputRate/capacity);
			avgInputs.get(i).reset();
		}
		
		if(state > 1.0) {
			activateLoadShedding(state);
		} else {
			deactivateLoadShedding();
		}
		
	}
	
	private void activateLoadShedding(double state) {
		isActive = true;
		double percentToRemove = state-1.0;
		
		strategy.activateLoadShedding(percentToRemove,shedders);
	}
	
	private void deactivateLoadShedding() {
		if(isActive) {
			isActive = false;
			strategy.deactivateLoadShedding(shedders);
		}
	}
	


	/**
	 * 
	 * @param sink
	 * @param leafs
	 * @deprecated Will not work with cycles
	 */
	@Deprecated
	private void findLeafs(ISink<?> sink, List<ISource<?>> leafs) {
		if (sink.getSubscribedToSource() == null
				|| sink.getSubscribedToSource().size() == 0) {
			leafs.add((ISource<?>) sink);
		} else {
			for (PhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedToSource()) {
				if (sub.getTarget() instanceof ISink<?>) {
					findLeafs((ISink<?>) sub.getTarget(), leafs);
				} else {
					leafs.add(sub.getTarget());
				}
			}
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void addLoadShedder(DirectLoadSheddingBuffer shedder) {
		this.shedders.add(shedder);
	}
	
}
