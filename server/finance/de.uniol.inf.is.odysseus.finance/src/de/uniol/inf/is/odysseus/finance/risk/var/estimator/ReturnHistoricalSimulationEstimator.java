package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import java.util.List;

import org.apache.commons.math3.distribution.RealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.finance.risk.var.model.HistoricalSimulationVaRForecaster;
import de.uniol.inf.is.odysseus.finance.simulation.ReturnSimulationFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.HistoricalSimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulationFunction;

/**
 * 
 * Estimator, which configures a historical simulation with
 * {@link ReturnSimulationFunction}
 * 
 * @author Christoph Schröer
 *
 */
public class ReturnHistoricalSimulationEstimator extends HistoricalSimulationEstimator {

	protected static Logger logger = LoggerFactory.getLogger(ReturnHistoricalSimulationEstimator.class);

	public static final String NAME = "ReturnHistoricalSimulationEstimator";

	private int returnIndex;

	public ReturnHistoricalSimulationEstimator(int returnIndex) {
		super();
		this.returnIndex = returnIndex;
	}

	@Override
	public void estimateModel() {

		ISimulation<Tuple<ITimeInterval>, Double> numericalSimulator = new HistoricalSimulation(this.historicalData);

		ISimulationFunction<Tuple<ITimeInterval>, Double> simulationFunction = new ReturnSimulationFunction(
				this.returnIndex);
		numericalSimulator.setSimulationFunction(simulationFunction);
		numericalSimulator.simulate();

		// get the distribution
		RealDistribution distribution = numericalSimulator.getEnumeratedDistribution();

		// Adding the simulated values to forecaster
		List<Double> sample = numericalSimulator.getSimulatedData();

		//
		HistoricalSimulationVaRForecaster histVarForecaster = new HistoricalSimulationVaRForecaster();
		histVarForecaster.setSample(sample);
		this.varForecaster = histVarForecaster;
		this.varForecaster.setDistribution(distribution);
		this.isEstimated = true;

	}

	@Override
	public String toString() {
		return ReturnHistoricalSimulationEstimator.NAME;
	}
}
