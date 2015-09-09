package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;

@SuppressWarnings("rawtypes")
public class ElementWindowTIPOEstimator extends StandardPhysicalOperatorEstimator<SlidingElementWindowTIPO> {


	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ElementWindowTIPOEstimator.class);
	
	@Override
	protected Class<? extends SlidingElementWindowTIPO> getOperatorClass() {
		return SlidingElementWindowTIPO.class;
	}

	@Override
	public double getWindowSize() {
		//TODO I guess this should calculate Window Size per Second, but imho this is inconsistent and leads to strage results.
		
		//double inputDataRate = getInputDataRate();

		
		//long windowAdvanceElements = getOperator().getWindowAdvanceMillis();
		long windowSizeElements = getOperator().getWindowSizeMillis();

		/*
		if (windowAdvanceElements == 1) {
			return windowSizeElements / (inputDataRate / 1000.0);
		}
		*/
		
		//return windowSizeElements / ( 2.0 * (inputDataRate / 1000.0));
		
		return windowSizeElements;
	}

	@SuppressWarnings("unused")
	private double getInputDataRate() {
		for (IPhysicalOperator operator : getPrevCostMap().keySet()) {

			if(getPrevCostMap().get(operator).getDatarate()==0.0) {
				LOG.warn("Operator {} had Datarate of 0.0. This will lead to infinite Window Size and is probably not correct.",operator.getName());
			}
			return getPrevCostMap().get(operator).getDatarate();
		}

		throw new RuntimeException("WindowTIPO has no input operators!");
	}
	
	@Override
	public double getMemory() {
		return getOperator().getWindowSizeMillis() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
	}
}
