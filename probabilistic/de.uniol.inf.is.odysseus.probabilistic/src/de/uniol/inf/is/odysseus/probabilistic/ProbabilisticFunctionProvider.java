package de.uniol.inf.is.odysseus.probabilistic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticIntegrate;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticFunctionProvider implements IFunctionProvider {
	private static final Logger LOG = LoggerFactory
			.getLogger(ProbabilisticFunctionProvider.class);

	public ProbabilisticFunctionProvider() {

	}

	@Override
	public List<IFunction<?>> getFunctions() {

		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		functions.add(new ProbabilisticIntegrate());
		ProbabilisticFunctionProvider.LOG.info(String.format(
				"Register functions: %s", functions));
		return functions;
	}
}
