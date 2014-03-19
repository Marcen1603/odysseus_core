/**
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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateRealDistribution;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class AvgPartialAggregate<T> extends AbstractPartialAggregate<T> {
    /** The sum value of the aggregate. */
    private ExtendedMixtureMultivariateRealDistribution sum;
    /** The count value of the aggregate. */
    private int count = 0;
    /** The result data type. */
    private final String datatype;

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param distribution
     *            The distribution
     * @param datatype
     *            The result datatype
     */
    public AvgPartialAggregate(final ExtendedMixtureMultivariateRealDistribution distribution, final String datatype) {
        this.sum = distribution;
        this.count = 1;
        this.datatype = datatype;
    }

    /**
     * Copy constructor.
     * 
     * @param avgPartialAggregate
     *            The object to copy from
     */
    public AvgPartialAggregate(final AvgPartialAggregate<T> avgPartialAggregate) {
        this.sum = avgPartialAggregate.sum;
        this.count = avgPartialAggregate.count;
        this.datatype = avgPartialAggregate.datatype;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return the sum
     */
    public final ExtendedMixtureMultivariateRealDistribution getSum() {
        return this.sum;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return the count
     */
    public final int getCount() {
        return this.count;
    }

    /**
     * Gets the current average.
     * 
     * @return The average.
     */
    public final ExtendedMixtureMultivariateRealDistribution getAvg() {
        final List<Pair<Double, IMultivariateRealDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateRealDistribution>>();
        for (final Pair<Double, IMultivariateRealDistribution> entry : ((ExtendedMixtureMultivariateRealDistribution) this.sum).getComponents()) {
            final IMultivariateRealDistribution normalDistribution = entry.getValue();
            final Double weight = entry.getKey();
            final double[] means = normalDistribution.getMean();
            for (int i = 0; i < means.length; i++) {
                means[i] /= this.count;
            }
            final RealMatrix covariances = new Array2DRowRealMatrix(normalDistribution.getVariance()).scalarMultiply(1.0 / (this.count * this.count));
            final IMultivariateRealDistribution component = new ExtendedMultivariateNormalDistribution(means, covariances.getData());
            mvns.add(new Pair<Double, IMultivariateRealDistribution>(weight, component));
        }
        final ExtendedMixtureMultivariateRealDistribution result = new ExtendedMixtureMultivariateRealDistribution(mvns);
        final Interval[] support = new Interval[result.getSupport().length];
        for (int i = 0; i < result.getSupport().length; i++) {
            support[i] = result.getSupport(i).div(this.count);
        }
        result.setSupport(support);
        result.setAttributes(sum.getAttributes());
        result.setScale(sum.getScale());
        return result;
    }

    /**
     * Add the given distribution to the aggregate.
     * 
     * @param value
     *            The value to add
     */
    public final void add(final ExtendedMixtureMultivariateRealDistribution value) {
        final List<Pair<Double, IMultivariateRealDistribution>> mixtures = new ArrayList<Pair<Double, IMultivariateRealDistribution>>();
        for (final Pair<Double, IMultivariateRealDistribution> sumEntry : ((ExtendedMixtureMultivariateRealDistribution) this.sum).getComponents()) {
            final RealMatrix sumMean = MatrixUtils.createColumnRealMatrix(sumEntry.getValue().getMean());
            final RealMatrix sumCovarianceMatrix = new Array2DRowRealMatrix(sumEntry.getValue().getVariance());

            for (final Pair<Double, IMultivariateRealDistribution> entry : ((ExtendedMixtureMultivariateRealDistribution) value).getComponents()) {
                final RealMatrix mean = MatrixUtils.createColumnRealMatrix(entry.getValue().getMean());
                final RealMatrix covarianceMatrix = new Array2DRowRealMatrix(entry.getValue().getVariance());

                final IMultivariateRealDistribution distribution = new ExtendedMultivariateNormalDistribution(sumMean.add(mean).getColumn(0), sumCovarianceMatrix.add(covarianceMatrix).getData());
                mixtures.add(new Pair<Double, IMultivariateRealDistribution>(sumEntry.getKey() * entry.getKey(), distribution));
            }
        }

        final ExtendedMixtureMultivariateRealDistribution result = new ExtendedMixtureMultivariateRealDistribution(mixtures);
        final Interval[] support = new Interval[this.sum.getSupport().length];
        for (int i = 0; i < this.sum.getSupport().length; i++) {
            support[i] = this.sum.getSupport(i).add(value.getSupport(i));
        }
        result.setSupport(support);
        result.setScale(this.sum.getScale() * value.getScale());
        this.count++;
        this.sum = result;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("AvgPartialAggregate (").append(this.hashCode()).append(")").append(this.sum).append(this.count);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final AvgPartialAggregate<T> clone() {
        return new AvgPartialAggregate<T>(this);
    }
}
