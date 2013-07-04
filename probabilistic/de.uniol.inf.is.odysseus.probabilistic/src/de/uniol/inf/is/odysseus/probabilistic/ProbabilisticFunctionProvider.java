/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.probabilistic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousEqualsOperatorVector;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousGreaterEqualsOperatorVector;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousGreaterOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousGreaterOperatorVector;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousSmallerEqualsOperatorVector;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousSmallerOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare.ProbabilisticContinuousSmallerOperatorVector;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticGreaterThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare.ProbabilisticSmallerThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticDivisionNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticDivisionNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticDivisionOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMinusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMultiplicationNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMultiplicationNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticMultiplicationOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPlusOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticPowerOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.ProbabilisticSQRTFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ProbabilisticDoubleToByteFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ProbabilisticDoubleToFloatFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ProbabilisticDoubleToIntegerFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ProbabilisticDoubleToLongFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ProbabilisticDoubleToShortFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform.ToProbabilisticDoubleFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.ProbabilisticIntegrateFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.ProbabilisticIntegrateMultivariateFunction;

/**
 * Function provider for probabilistic functions.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticFunctionProvider implements IFunctionProvider {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticFunctionProvider.class);

	/**
	 * Default constructor.
	 */
	public ProbabilisticFunctionProvider() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider#getFunctions()
	 */
	@Override
	public final List<IFunction<?>> getFunctions() {
		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		try {
			/** Boolean functions for discrete probabilistic values */
//			functions.add(new ProbabilisticAndOperator());
//			functions.add(new ProbabilisticOrOperator());
//			functions.add(new ProbabilisticNotOperator());

			functions.add(new ProbabilisticEqualsOperator());
			functions.add(new ProbabilisticSmallerEqualsOperator());
			functions.add(new ProbabilisticSmallerThanOperator());
			functions.add(new ProbabilisticGreaterEqualsOperator());
			functions.add(new ProbabilisticGreaterThanOperator());

			/** Arithmetic functions for discrete probabilistic values */
			functions.add(new ProbabilisticMinusOperator());
			functions.add(new ProbabilisticMinusNumberRHSOperator());
			functions.add(new ProbabilisticMinusNumberLHSOperator());
			functions.add(new ProbabilisticPlusOperator());
			functions.add(new ProbabilisticPlusNumberRHSOperator());
			functions.add(new ProbabilisticPlusNumberLHSOperator());
			functions.add(new ProbabilisticMultiplicationOperator());
			functions.add(new ProbabilisticMultiplicationNumberRHSOperator());
			functions.add(new ProbabilisticMultiplicationNumberLHSOperator());
			functions.add(new ProbabilisticDivisionOperator());
			functions.add(new ProbabilisticDivisionNumberRHSOperator());
			functions.add(new ProbabilisticDivisionNumberLHSOperator());
			functions.add(new ProbabilisticPowerOperator());
			functions.add(new ProbabilisticSQRTFunction());

			/** Convert functions */
			functions.add(new ProbabilisticDoubleToByteFunction());
			functions.add(new ProbabilisticDoubleToShortFunction());
			functions.add(new ProbabilisticDoubleToIntegerFunction());
			functions.add(new ProbabilisticDoubleToLongFunction());
			functions.add(new ProbabilisticDoubleToFloatFunction());
			functions.add(new ToProbabilisticDoubleFunction());

			/** Boolean functions for continuous probabilistic values */
			functions.add(new ProbabilisticIntegrateMultivariateFunction());
			functions.add(new ProbabilisticIntegrateFunction());

			functions.add(new ProbabilisticContinuousSmallerEqualsOperator());
			functions.add(new ProbabilisticContinuousSmallerEqualsOperatorVector());
			functions.add(new ProbabilisticContinuousSmallerOperator());
			functions.add(new ProbabilisticContinuousSmallerOperatorVector());
			functions.add(new ProbabilisticContinuousGreaterEqualsOperator());
			functions.add(new ProbabilisticContinuousGreaterEqualsOperatorVector());
			functions.add(new ProbabilisticContinuousGreaterOperator());
			functions.add(new ProbabilisticContinuousGreaterOperatorVector());
			functions.add(new ProbabilisticContinuousEqualsOperator());
			functions.add(new ProbabilisticContinuousEqualsOperatorVector());
			// ProbabilisticFunctionProvider.LOG.info(String.format(
			// "Register functions: %s", functions));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return functions;
	}
}
