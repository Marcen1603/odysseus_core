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

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class SumPartialAggregate<T> extends AbstractPartialAggregate<T> {
    /** The value of the aggregate. */
    private NormalDistributionMixture sum;
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
    public SumPartialAggregate(final NormalDistributionMixture distribution, final String datatype) {
        this.sum = distribution;
        this.datatype = datatype;
    }

    /**
     * Copy constructor.
     * 
     * @param sumPartialAggregate
     *            The object to copy from
     */
    public SumPartialAggregate(final SumPartialAggregate<T> sumPartialAggregate) {
        this.sum = sumPartialAggregate.sum;
        this.datatype = sumPartialAggregate.datatype;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return the sum
     */
    public final NormalDistributionMixture getSum() {
        return this.sum;
    }

    /**
     * Add the given distribution to the aggregate.
     * 
     * @param value
     *            The value to add
     */
    public final void add(final NormalDistributionMixture value) {
        final List<Pair<Double, MultivariateNormalDistribution>> mixtures = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
        for (final Pair<Double, MultivariateNormalDistribution> sumEntry : this.sum.getMixtures().getComponents()) {
            final RealMatrix sumMean = MatrixUtils.createColumnRealMatrix(sumEntry.getValue().getMeans());
            final RealMatrix sumCovarianceMatrix = sumEntry.getValue().getCovariances();

            for (final Pair<Double, MultivariateNormalDistribution> entry : value.getMixtures().getComponents()) {
                final RealMatrix mean = MatrixUtils.createColumnRealMatrix(entry.getValue().getMeans());
                final RealMatrix covarianceMatrix = entry.getValue().getCovariances();

                final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(sumMean.add(mean).getColumn(0), sumCovarianceMatrix.add(covarianceMatrix).getData());
                mixtures.add(new Pair<Double, MultivariateNormalDistribution>(sumEntry.getKey() * entry.getKey(), distribution));
            }
        }

        final NormalDistributionMixture result = new NormalDistributionMixture(mixtures);
        final Interval[] support = new Interval[this.sum.getSupport().length];
        for (int i = 0; i < this.sum.getSupport().length; i++) {
            support[i] = this.sum.getSupport(i).add(value.getSupport(i));
        }
        result.setSupport(support);
        result.setScale(this.sum.getScale() * value.getScale());
        this.sum = result;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("SumPartialAggregate (").append(this.hashCode()).append(")").append(this.sum);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final SumPartialAggregate<T> clone() {
        return new SumPartialAggregate<T>(this);
    }
}
