package de.uniol.inf.is.odysseus.probabilistic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticContinuousGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticContinuousSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticDivisionOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticGreaterThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticIntegrate;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticMultiplicationOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.function.ProbabilisticSmallerThanOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticFunctionProvider implements IFunctionProvider {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ProbabilisticFunctionProvider.class);

	public ProbabilisticFunctionProvider() {

	}

	@Override
	public List<IFunction<?>> getFunctions() {

		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		functions.add(new ProbabilisticSmallerEqualsOperator());
		functions.add(new ProbabilisticSmallerThanOperator());
		functions.add(new ProbabilisticGreaterEqualsOperator());
		functions.add(new ProbabilisticGreaterThanOperator());

		functions.add(new ProbabilisticMinusOperator());
		functions.add(new ProbabilisticPlusOperator());
		functions.add(new ProbabilisticMultiplicationOperator());
		functions.add(new ProbabilisticDivisionOperator());

		functions.add(new ProbabilisticIntegrate());

		functions.add(new ProbabilisticContinuousSmallerEqualsOperator());
		functions.add(new ProbabilisticContinuousGreaterEqualsOperator());
		// ProbabilisticFunctionProvider.LOG.info(String.format(
		// "Register functions: %s", functions));
		return functions;
	}
}
