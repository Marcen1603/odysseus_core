package de.uniol.inf.is.odysseus.finance.simulation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.TupleBasedSimulationFunction;

/**
 * 
 * A simple simulation function which returns the same tuple as input, appended
 * by a a attribute of the historical tuple.
 * 
 * @author Christoph Schröer
 *
 */
public class ReturnSimulationFunction extends TupleBasedSimulationFunction<Tuple<ITimeInterval>, ITimeInterval> {

	/**
	 * Index in the tuple, which should be append to output tuple
	 */
	private int returnIndex;

	public ReturnSimulationFunction(int returnIndex) {
		super();
		this.returnIndex = returnIndex;
	}

	@Override
	public Double getValue(Tuple<ITimeInterval> historicalValue) {
		Double value = historicalValue.getAttribute(this.returnIndex);
		return value;
	}
}
