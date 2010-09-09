package de.uniol.inf.is.odysseus.loadshedding.monitoring;

import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;

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
	public void poEventOccured(POEvent poEvent) {
		read++;
		target.updateLoadSheddingState();
	}
	
	public void reset() {
		read = 0;
	}


}
