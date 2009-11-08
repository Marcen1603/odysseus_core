package de.uniol.inf.is.odysseus.loadshedding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.loadshedding.monitoring.AvgInputRate;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class LoadManager {
	
	private static LoadManager instance = null;
	
	private List<Double> capacities = new ArrayList<Double>();
	
	private ArrayList<AvgInputRate> avgInputs = new ArrayList<AvgInputRate>();

	private boolean isActive;
	
	private LoadManager() {
	}
	
	public static LoadManager getInstance() {
		if(instance == null) {
			instance = new LoadManager();
		}
		
		return instance;
	}
	
	
	public void addCapacities(IPartialPlan plan) {
		
		AvgInputRate avgInput = new AvgInputRate(this);
		
		for(ISink<?> each : plan.getRoots()) {
			List<ISource<?>> leafs = new ArrayList<ISource<?>>();
			findLeafs(each, leafs);
			
			for(ISource<?> leaf : leafs) {
				avgInput.addInputStream(leaf);
				leaf.subscribe(avgInput, POEventType.PushInit);
			}
		}
		avgInputs.add(avgInput);
		capacities.add(calcCapacity(plan));
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
		
		/*
		 * Sind die zu verwerfenden Daten mehr als oder gleich 100%, dann
		 * nutzt auch Load Shedding nichts mehr.
		 */
		if(percentToRemove >= 1) {
			return;
		} else {
			//TODO: Load Shedder aktivieren
		}
	}
	
	private void deactivateLoadShedding() {
		if(isActive) {
			isActive = false;
			//TODO: Load Shedder deaktivieren
		}
	}
	
	private double calcCapacity(IPartialPlan plan) {
		// TODO Selektivitaet momentan immer als 1 angesetzt. Muss noch (wie Anzahl gelesener Elemente)
		// ueberwacht werden
		
		double processingSteps = 0;
		
		for(int i=0; i < plan.getIterableSource().size(); i++) {
			processingSteps += 1;
		}
		
		double result = 1/processingSteps;
		
		return 1/result;
	}

	
	private void findLeafs(ISink<?> sink, List<ISource<?>> leafs) {
		if (sink.getSubscribedTo() == null
				|| sink.getSubscribedTo().size() == 0) {
			leafs.add((ISource<?>) sink);
		} else {
			for (PhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedTo()) {
				if (sub.getTarget() instanceof ISink<?>) {
					findLeafs((ISink<?>) sub.getTarget(), leafs);
				} else {
					leafs.add(sub.getTarget());
				}
			}
		}
	}	
	
}
