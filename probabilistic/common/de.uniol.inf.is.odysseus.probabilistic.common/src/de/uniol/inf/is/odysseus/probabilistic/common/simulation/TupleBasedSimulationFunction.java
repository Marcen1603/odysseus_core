package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

abstract public class TupleBasedSimulationFunction<H extends Tuple<M>, M extends ITimeInterval>
		implements ISimulationFunction<H, Double> {

}
