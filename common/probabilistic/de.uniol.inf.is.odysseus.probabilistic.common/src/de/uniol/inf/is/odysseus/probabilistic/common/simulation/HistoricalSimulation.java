package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * This class represents and implements a historical simulation.
 * 
 * @author Christoph Schröer
 *
 */
public class HistoricalSimulation extends TupleBasedSimulation<Tuple<ITimeInterval>, ITimeInterval> {

	private List<Tuple<ITimeInterval>> historicalData;

	public HistoricalSimulation(List<Tuple<ITimeInterval>> historicalData) {
		super();
		this.historicalData = historicalData;
	}

	@Override
	public void simulate() {
		this.simulatedData = new ArrayList<>();

		this.historicalData.sort(new Comparator<Tuple<ITimeInterval>>() {

			@Override
			public int compare(Tuple<ITimeInterval> o1, Tuple<ITimeInterval> o2) {
				// sorting by timestamp

				ITimeInterval o1TimeInterval = o1.getMetadata();
				ITimeInterval o2TimeInterval = o2.getMetadata();

				return o1TimeInterval.compareTo(o2TimeInterval);

			}
		});

		for (Tuple<ITimeInterval> historicalValue : this.historicalData) {
			Double simulatedTuple = this.simulationFunction.getValue(historicalValue);
			this.simulatedData.add(simulatedTuple);
		}

	}

}
