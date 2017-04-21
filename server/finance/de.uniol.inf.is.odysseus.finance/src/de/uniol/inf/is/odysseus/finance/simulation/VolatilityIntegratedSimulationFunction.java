package de.uniol.inf.is.odysseus.finance.simulation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.TupleBasedSimulationFunction;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class VolatilityIntegratedSimulationFunction
		extends TupleBasedSimulationFunction<Tuple<ITimeInterval>, ITimeInterval> {

	private int returnIndex;

	private int volatilityIndex;

	/**
	 * the variance estimation for t+1
	 */
	private double volatilityT1;

	public VolatilityIntegratedSimulationFunction(int returnIndex, int volatilityIndex, double volatilityT1) {
		super();
		this.returnIndex = returnIndex;
		this.volatilityIndex = volatilityIndex;
		this.volatilityT1 = volatilityT1;
	}

	@Override
	public Double getValue(Tuple<ITimeInterval> historicalValue) {

		double historicalReturn = historicalValue.getAttribute(this.returnIndex);
		double historicalVolatility = historicalValue.getAttribute(this.volatilityIndex);

		double simulatedValue = historicalReturn * (this.volatilityT1 / historicalVolatility);

		return simulatedValue;
	}
}
