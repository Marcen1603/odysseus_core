package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class ElementWindowAOEstimator extends StandardLogicalOperatorEstimator<ElementWindowAO> {

	@Override
	protected Class<? extends ElementWindowAO> getOperatorClass() {
		return ElementWindowAO.class;
	}
	
	@Override
	public double getWindowSize() {

		double inputDataRate = getInputDataRate();
		
		long windowAdvanceElements = getOperator().getWindowAdvanceE();
		long windowSizeElements = getOperator().getWindowSizeE();
		
		//Calculations taken from WindowAO Estimator
		if (windowAdvanceElements == 1) {
				return windowSizeElements / (inputDataRate / 1000.0);
		}
	
		return windowSizeElements / ( 2.0 * (inputDataRate / 1000.0));
	
	}
	

	private double getInputDataRate() {
		for (ILogicalOperator operator : getPrevCostMap().keySet()) {
			return getPrevCostMap().get(operator).getDatarate();
		}

		throw new RuntimeException("ElementWindowAO has no input operators!");
	}
}
