package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class WindowAOEstimator extends StandardLogicalOperatorEstimator<WindowAO> {

	@Override
	protected Class<? extends WindowAO> getOperatorClass() {
		return WindowAO.class;
	}
	
	@Override
	public double getWindowSize() {

		double inputDataRate = getInputDataRate();

		WindowType windowType = getOperator().getWindowType();
		if( windowType.equals(WindowType.TUPLE) ) {
			long windowAdvanceElements = getOperator().getWindowAdvanceMillis();
			long windowSizeElements = getOperator().getWindowSizeMillis();
			
			if (windowAdvanceElements == 1) {
				return windowSizeElements / (inputDataRate / 1000.0);
			}
	
			return windowSizeElements / ( 2.0 * (inputDataRate / 1000.0));
		}
		
		if( windowType.equals(WindowType.TIME)) {
			long windowAdvanceMillis = getOperator().getWindowAdvanceMillis();
			long windowSizeMillis = getOperator().getWindowSizeMillis();
			
			if( windowAdvanceMillis <= 1 ) {
				return windowSizeMillis;
			}
			
			return windowSizeMillis / 2;
			
		}
		
		return super.getWindowSize();
	}
	

	private double getInputDataRate() {
		for (ILogicalOperator operator : getPrevCostMap().keySet()) {
			return getPrevCostMap().get(operator).getDatarate();
		}

		throw new RuntimeException("WindowTIPO has no input operators!");
	}
}
