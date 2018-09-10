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
package de.uniol.inf.is.odysseus.probabilistic.functions.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;

/**
 * Implementation of the Kullback-Leibler divergence for multivariate normal
 * distribution, see {@link https
 * ://en.wikipedia.org/wiki/Kullback%E2%80%93Leibler_divergence}
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractKullbackLeiblerDivergenceFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 1829503825183042744L;

    public AbstractKullbackLeiblerDivergenceFunction(final SDFDatatype[][] accTypes) {
        super("kl", 2, accTypes, SDFDatatype.DOUBLE);
    }

    /**
     * 
     * @param a
     *            The normal distribution mixture
     * @param b
     *            The normal distribution mixture
     * @return The Kullback-Leibler divergence
     */
    protected final double getValueInternal(final MultivariateMixtureDistribution a, final MultivariateMixtureDistribution b) {
        // FIXME Only for 1 component mixtures. How to do it with multiple
        // mixture components?
        double divergence = 0.0;
        final int dimension = a.getDimension();
        for (final Pair<Double, IMultivariateDistribution> aEntry : a.getComponents()) {
            final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMean());
            final RealMatrix aCovariance = new Array2DRowRealMatrix(aEntry.getValue().getVariance());
            final EigenDecomposition aEigenDecomposition = new EigenDecomposition(aCovariance);
            final RealMatrix inverse2 = aEigenDecomposition.getSolver().getInverse();
            final double aDeterminant = aEigenDecomposition.getDeterminant();

            for (final Pair<Double, IMultivariateDistribution> bEntry : b.getComponents()) {
                final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMean());
                final RealMatrix bCovariance = new Array2DRowRealMatrix(bEntry.getValue().getVariance());
                final EigenDecomposition bEigenDecomposition = new EigenDecomposition(bCovariance);
                final double bDeterminant = bEigenDecomposition.getDeterminant();

                divergence += aEntry.getKey()
                        * bEntry.getKey()
                        * (0.5 * ((inverse2.multiply(aCovariance).getTrace() + bMean.subtract(aMean).transpose().multiply(inverse2).multiply(bMean.subtract(aMean)).getData()[0][0]) - dimension - FastMath
                                .log(aDeterminant / bDeterminant)));
            }
        }
        return divergence;
    }
}
