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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class AssignDistributionPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, ProbabilisticTuple<T>> {
    /** The attribute positions. */
    private final int[] attributes;
    /** The variance. */
    private final int variance;

    /**
     * Creates a new Sample operator.
     * 
     * @param attributes
     *            The attribute positions
     * @param variance
     *            The variance attribute position for the distribution
     */
    public AssignDistributionPO(final int[] attributes, final int variance) {
        this.attributes = attributes;
        this.variance = variance;
    }

    /**
     * Clone constructor.
     * 
     * @param distributionPO
     *            The copy
     */
    public AssignDistributionPO(final AssignDistributionPO<T> distributionPO) {
        super(distributionPO);
        this.attributes = distributionPO.attributes.clone();
        this.variance = distributionPO.variance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * getOutputMode()
     */
    @Override
    public final OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
     * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
     */
    @Override
    protected final void process_next(final Tuple<T> object, final int port) {
        final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(object);
        final ExtendedMixtureMultivariateRealDistribution[] distributions = outputVal.getDistributions();

        final double[] means = new double[this.attributes.length];
        for (int i = 0; i < this.attributes.length; i++) {
            means[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
        }

        final double[][] variances = (double[][]) object.getAttribute(this.variance);

        final List<Pair<Double, IMultivariateRealDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateRealDistribution>>();
        final IMultivariateRealDistribution component = new ExtendedMultivariateNormalDistribution(means, variances);
        mvns.add(new Pair<Double, IMultivariateRealDistribution>(1.0, component));

        final ExtendedMixtureMultivariateRealDistribution mixture = new ExtendedMixtureMultivariateRealDistribution(mvns);
        mixture.setAttributes(this.attributes);

        final ExtendedMixtureMultivariateRealDistribution[] outputValDistributions = new ExtendedMixtureMultivariateRealDistribution[distributions.length + 1];

        for (final int attribute : this.attributes) {
            outputVal.setAttribute(attribute, new ProbabilisticContinuousDouble(distributions.length));
        }
        // Copy the old distribution to the new tuple
        System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
        // And append the new distribution to the end of the array
        outputValDistributions[distributions.length] = mixture;
        outputVal.setDistributions(outputValDistributions);

        // KTHXBYE
        this.transfer(outputVal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
     * ()
     */
    @Override
    public final AbstractPipe<Tuple<T>, ProbabilisticTuple<T>> clone() {
        return new AssignDistributionPO<T>(this);
    }

}
