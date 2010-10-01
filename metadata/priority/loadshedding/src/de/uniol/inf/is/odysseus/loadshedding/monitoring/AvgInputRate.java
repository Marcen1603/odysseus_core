package de.uniol.inf.is.odysseus.loadshedding.monitoring;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;

public class AvgInputRate implements
		IPOEventListener {

	public static final double UNKOWN = -1;
	private LoadManager target;

	public AvgInputRate(LoadManager target) {
		this.target = target;
	}

	private double streams = 0;
	private double read = 0;

	public void addInputStream(IPhysicalOperator source) {
		streams++;
	}

	public void removeInputStream(IPhysicalOperator source) {
		streams--;
	}

	public Double getAvgInputRate() {
		if (streams == 0) {
			return UNKOWN;
		} else {
			double result = read/streams;
			return result;
		}
	}

	@Override
	public void eventOccured(IEvent<?,?> poEvent) {
		read++;
		target.updateLoadSheddingState();
	}
	
	public void reset() {
		read = 0;
	}


}
