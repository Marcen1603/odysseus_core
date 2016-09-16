package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * Factory to create Imputation(@see IImputation)-Objects.
 * 
 * @author Christoph Schröer
 *
 */
public class ImputationFactory<M extends ITimeInterval> {

	public IImputation<ITimeInterval> createImputation(String imputationStrategy, TimeValueItem imputationWindowSize,
			Map<String, String> optionsMap) {

		if (imputationStrategy.equalsIgnoreCase("LastValueCarriedForward")) {
			return new LastValueCarriedForwardImputation(imputationWindowSize);
		}

		throw new IllegalArgumentException("Imputation strategy does not exist.");
	}
}
