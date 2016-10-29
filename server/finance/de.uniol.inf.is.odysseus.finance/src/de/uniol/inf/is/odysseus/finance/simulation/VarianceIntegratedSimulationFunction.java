package de.uniol.inf.is.odysseus.finance.simulation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.TupleBasedSimulationFunction;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class VarianceIntegratedSimulationFunction
		extends TupleBasedSimulationFunction<Tuple<ITimeInterval>, ITimeInterval> {

	private int returnIndex;

	private int varianceIndex;

	/**
	 * the variance estimation for t+1
	 */
	private double varianceT1;

	public VarianceIntegratedSimulationFunction(int returnIndex, int varianceIndex, double varianceT1) {
		super();
		this.returnIndex = returnIndex;
		this.varianceIndex = varianceIndex;
		this.varianceT1 = varianceT1;

	}

	@Override
	public Double getValue(Tuple<ITimeInterval> historicalValue) {

		double historicalReturn = historicalValue.getAttribute(this.returnIndex);
		double historicalVariance = historicalValue.getAttribute(this.varianceIndex);

		double simulatedValue = historicalReturn * (this.varianceT1 / historicalVariance);

		return simulatedValue;
	}
}
