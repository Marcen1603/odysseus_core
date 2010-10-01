package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

/**
 * Special BenchmarkPO, needed to use prios and selectivity
 * 
 * @author Jonas Jacobi
 * 
 * @param <T>
 */
public class PriorityBenchmarkPO<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BenchmarkPO<T> {

	double[] oldVals = new double[128];

	public PriorityBenchmarkPO(int processingTime,
			double selectivity) {
		super(processingTime, selectivity);
		for (int i = 0; i < 128; ++i) {
			oldVals[i] = this.selectivity;
		}
	};

	@Override
	protected double getOldVal(T element) {
		byte curP = element.getMetadata().getPriority();
		return oldVals[curP];
	};
	
	@Override
	protected void setOldVal(T element, double d) {
		byte curP = element.getMetadata().getPriority();
		oldVals[curP] = d;
	};
}
