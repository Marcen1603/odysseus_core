package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.finance.simulation.VarianceIntegratedSimulationFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.HistoricalSimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulationFunction;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class VarianceIntegratedHistoricalSimulationEstimator extends HistoricalSimulationEstimator
		implements IHasEstimationData<Tuple<ITimeInterval>> {

	public static final String NAME = "VarianceIntegratedHistoricalSimulationEstimator";

	private int returnIndex;

	private int varianceIndex;

	public VarianceIntegratedHistoricalSimulationEstimator(int returnIndex, int varianceIndex) {
		super();
		this.returnIndex = returnIndex;
		this.varianceIndex = varianceIndex;
	}

	@Override
	public void estimateModel() {

		ISimulation<Tuple<ITimeInterval>, Double> numericalSimulator = new HistoricalSimulation(
				this.historicalData);

		// get newest estimated variance
		Tuple<ITimeInterval> newestTuple = this.historicalData.get(this.historicalData.size() - 1);

		if (newestTuple != null) {
			double varianceT1 = newestTuple.getAttribute(this.varianceIndex);

			ISimulationFunction<Tuple<ITimeInterval>, Double> simulationFunction = new VarianceIntegratedSimulationFunction(
					this.returnIndex, this.varianceIndex, varianceT1);
			numericalSimulator.setSimulationFunction(simulationFunction);
			numericalSimulator.simulate();

			RealDistribution distribution = numericalSimulator.getEnumeratedDistribution();

			this.varForecaster.setDistribution(distribution);

		} else {
			this.isEstimated = true;
		}

	}
}
