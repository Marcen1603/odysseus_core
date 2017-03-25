package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * 
 * @author Christoph Schröer
 *
 */
abstract public class HistoricalSimulationEstimator extends NumericalEstimator
		implements IHasEstimationData<Tuple<ITimeInterval>> {

	protected List<Tuple<ITimeInterval>> historicalData;

	public HistoricalSimulationEstimator() {
		super();
		this.historicalData = new ArrayList<>();
	}

	@Override
	public void addEstimationData(Tuple<ITimeInterval> data) {
		this.historicalData.add(data);

	}

	@Override
	public void removeEstimationData(Tuple<ITimeInterval> data) {
		this.historicalData.remove(data);
	}

	@Override
	public void clear() {
		this.historicalData.clear();
	}
}
