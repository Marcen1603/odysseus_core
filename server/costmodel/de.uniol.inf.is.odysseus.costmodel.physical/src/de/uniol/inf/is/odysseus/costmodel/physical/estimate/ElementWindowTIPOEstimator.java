package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;

@SuppressWarnings("rawtypes")
public class ElementWindowTIPOEstimator extends StandardPhysicalOperatorEstimator<SlidingElementWindowTIPO> {

	@Override
	protected Class<? extends SlidingElementWindowTIPO> getOperatorClass() {
		return SlidingElementWindowTIPO.class;
	}

	@Override
	public double getWindowSize() {

		double inputDataRate = getInputDataRate();

		long windowAdvanceElements = getOperator().getWindowAdvanceMillis();
		long windowSizeElements = getOperator().getWindowSizeMillis();

		if (windowAdvanceElements == 1) {
			return windowSizeElements / (inputDataRate / 1000.0);
		}

		return windowSizeElements / ( 2.0 * (inputDataRate / 1000.0));
	}

	private double getInputDataRate() {
		for (IPhysicalOperator operator : getPrevCostMap().keySet()) {
			return getPrevCostMap().get(operator).getDatarate();
		}

		throw new RuntimeException("WindowTIPO has no input operators!");
	}
}
