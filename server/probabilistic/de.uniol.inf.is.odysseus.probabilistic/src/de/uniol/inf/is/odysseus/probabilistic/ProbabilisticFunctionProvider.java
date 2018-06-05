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

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.functions.bool.ProbabilisticAndOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.bool.ProbabilisticNotOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.bool.ProbabilisticOrOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsProbabilisticNumberOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsVectorOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticEqualsVectorOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsProbabilisticNumberOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsVectorOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterEqualsVectorOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterProbabilisticNumberOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterVectorOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticGreaterVectorOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsProbabilisticNumberOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsVectorOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerEqualsVectorOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerProbabilisticNumberOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerVectorOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.compare.ProbabilisticSmallerVectorOperatorInverse;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.As2DVectorFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.As3DVectorFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.BhattacharyyaDistanceFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.BhattacharyyaDistanceFunctionVector;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.EuclideanDistanceFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.EuclideanDistanceFunctionVector;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.KullbackLeiblerDivergenceFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.KullbackLeiblerDivergenceFunctionVector;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.LogLikelihoodFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.MahalanobisDistanceFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.MahalanobisDistanceFunctionVector;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticAddNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticAddNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticAddOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticDivideNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticDivideNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticDivideOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticIntegrateFunction;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticMultiplyNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticMultiplyNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticMultiplyOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticSubtractNumberLHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticSubtractNumberRHSOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.math.ProbabilisticSubtractOperator;
import de.uniol.inf.is.odysseus.probabilistic.functions.transform.ToProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.transform.ToProbabilisticDiscreteDouble;

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
     * @see de.uniol.inf.is.odysseus.mep.IFunctionProvider#getFunctions()
     */
    @Override
    public final List<IMepFunction<?>> getFunctions() {
        final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
        try {
            /** Boolean functions for probabilistic values */
            functions.add(new ProbabilisticAndOperator());
            functions.add(new ProbabilisticOrOperator());
            functions.add(new ProbabilisticNotOperator());

            /** Arithmetic functions for probabilistic values */
            functions.add(new ProbabilisticSubtractOperator());
            functions.add(new ProbabilisticSubtractNumberRHSOperator());
            functions.add(new ProbabilisticSubtractNumberLHSOperator());
            functions.add(new ProbabilisticAddOperator());
            functions.add(new ProbabilisticAddNumberRHSOperator());
            functions.add(new ProbabilisticAddNumberLHSOperator());
            functions.add(new ProbabilisticMultiplyOperator());
            functions.add(new ProbabilisticMultiplyNumberRHSOperator());
            functions.add(new ProbabilisticMultiplyNumberLHSOperator());
            functions.add(new ProbabilisticDivideOperator());
            functions.add(new ProbabilisticDivideNumberRHSOperator());
            functions.add(new ProbabilisticDivideNumberLHSOperator());
            // functions.add(new ProbabilisticPowerOperator());
            // functions.add(new ProbabilisticSQRTFunction());
            // functions.add(new ProbabilisticMinFunction());
            // functions.add(new ProbabilisticMinNumberRHSFunction());
            // functions.add(new ProbabilisticMinNumberLHSFunction());
            // functions.add(new ProbabilisticMaxFunction());
            // functions.add(new ProbabilisticMaxNumberRHSFunction());
            // functions.add(new ProbabilisticMaxNumberLHSFunction());

            /** Compare operators. */
            functions.add(new ProbabilisticSmallerEqualsOperator());
            functions.add(new ProbabilisticSmallerEqualsOperatorInverse());

            functions.add(new ProbabilisticSmallerEqualsVectorOperator());
            functions.add(new ProbabilisticSmallerEqualsVectorOperatorInverse());

            functions.add(new ProbabilisticSmallerEqualsProbabilisticNumberOperator());

            functions.add(new ProbabilisticSmallerOperator());
            functions.add(new ProbabilisticSmallerOperatorInverse());

            functions.add(new ProbabilisticSmallerVectorOperator());
            functions.add(new ProbabilisticSmallerVectorOperatorInverse());

            functions.add(new ProbabilisticSmallerProbabilisticNumberOperator());

            functions.add(new ProbabilisticGreaterEqualsOperator());
            functions.add(new ProbabilisticGreaterEqualsOperatorInverse());

            functions.add(new ProbabilisticGreaterEqualsVectorOperator());
            functions.add(new ProbabilisticGreaterEqualsVectorOperatorInverse());

            functions.add(new  ProbabilisticGreaterEqualsProbabilisticNumberOperator());

            functions.add(new ProbabilisticGreaterOperator());
            functions.add(new ProbabilisticGreaterOperatorInverse());

            functions.add(new ProbabilisticGreaterVectorOperator());
            functions.add(new ProbabilisticGreaterVectorOperatorInverse());

            functions.add(new ProbabilisticGreaterProbabilisticNumberOperator());

            functions.add(new ProbabilisticEqualsOperator());
            functions.add(new ProbabilisticEqualsOperatorInverse());

            functions.add(new ProbabilisticEqualsVectorOperator());
            functions.add(new ProbabilisticEqualsVectorOperatorInverse());

            functions.add(new ProbabilisticEqualsProbabilisticNumberOperator());

            /** Convert functions */
            functions.add(new ToProbabilisticContinuousDouble());
            functions.add(new ToProbabilisticDiscreteDouble());

            /** Additional functions for probabilistic value */
            functions.add(new BhattacharyyaDistanceFunctionVector());
            functions.add(new BhattacharyyaDistanceFunction());
            functions.add(new MahalanobisDistanceFunctionVector());
            functions.add(new MahalanobisDistanceFunction());

            functions.add(new KullbackLeiblerDivergenceFunctionVector());
            functions.add(new KullbackLeiblerDivergenceFunction());

            functions.add(new LogLikelihoodFunction());

            functions.add(new As2DVectorFunction());
            functions.add(new As3DVectorFunction());

            functions.add(new EuclideanDistanceFunction());
            functions.add(new EuclideanDistanceFunctionVector());

            functions.add(new ProbabilisticIntegrateFunction());
            // ProbabilisticFunctionProvider.LOG.info(String.format(
            // "Register functions: %s", functions));

            //functions.add(new TimelinessFunction());
        }
        catch (final Exception e) {
            ProbabilisticFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }
}
