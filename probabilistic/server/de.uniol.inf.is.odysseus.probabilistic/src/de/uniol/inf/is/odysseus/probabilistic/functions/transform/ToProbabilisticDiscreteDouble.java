/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.functions.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.util.Pair;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateEnumeratedDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToProbabilisticDiscreteDouble extends AbstractProbabilisticFunction<IMultivariateDistribution> {

    /**
     * 
     */
    private static final long serialVersionUID = 1715965302932232569L;

    public ToProbabilisticDiscreteDouble() {
        super("toProbabilisticDiscreteDouble", 2, ToProbabilisticDiscreteDouble.ACC_TYPES, SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final IMultivariateDistribution getValue() {
        final double[][] singletons = (double[][]) this.getInputValue(0);
        final double[] probabilities = ((double[][]) this.getInputValue(1))[0];
        Objects.requireNonNull(singletons);
        Objects.requireNonNull(probabilities);
        Preconditions.checkArgument(singletons.length > 0);
        Preconditions.checkArgument(singletons.length == probabilities.length);
        final List<Pair<Double, IMultivariateDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateDistribution>>();
        final IMultivariateDistribution component = new MultivariateEnumeratedDistribution(singletons, probabilities);
        mvns.add(new Pair<Double, IMultivariateDistribution>(1.0, component));

        final MultivariateMixtureDistribution result = new MultivariateMixtureDistribution(mvns);
        final Interval[] support = new Interval[result.getSupport().length];
        for (int i = 0; i < result.getSupport().length; i++) {
            support[i] = Interval.MAX;
        }
        result.setSupport(support);
        return result;
    }

    /*
     * 
     * 
     * 
     * /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE },
            { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE } };

}
