package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * Factory to create Imputation(@see IImputation)-Objects.
 * 
 * The created imputation-stretegies base on tuples and time windows.
 * 
 * @author Christoph Schr�er
 *
 */
public class TupleBasedImputationFactory {

	public IImputation<Tuple<ITimeInterval>, ITimeInterval> createImputation(String imputationStrategy,
			Map<String, String> optionsMap) {

		if (imputationStrategy.equalsIgnoreCase("LastValueCarriedForward")) {
			return new LastValueCarriedForwardImputation();
		}

		throw new IllegalArgumentException("Imputation strategy does not exist.");
	}
}
