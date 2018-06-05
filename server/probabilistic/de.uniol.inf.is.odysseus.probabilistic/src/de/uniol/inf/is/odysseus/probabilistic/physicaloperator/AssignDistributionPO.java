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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateEnumeratedDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class AssignDistributionPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, ProbabilisticTuple<T>> {
    /** The attribute positions. */
    private final int[] attributes;
    /** The variance. */
    private final int variance;
    private final boolean continuous;

    /**
     * Creates a new Sample operator.
     * 
     * @param attributes
     *            The attribute positions
     * @param variance
     *            The variance attribute position for the distribution
     */
    public AssignDistributionPO(final int[] attributes, final int variance, final boolean continuous) {
        this.attributes = attributes;
        this.variance = variance;
        this.continuous = continuous;
    }

    /**
     * Clone constructor.
     * 
     * @param clone
     *            The copy
     */
    public AssignDistributionPO(final AssignDistributionPO<T> clone) {
        super(clone);
        this.attributes = clone.attributes.clone();
        this.variance = clone.variance;
        this.continuous = clone.continuous;
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
        final MultivariateMixtureDistribution[] distributions = outputVal.getDistributions();
        final List<Pair<Double, IMultivariateDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateDistribution>>();
        final double[] means = new double[this.attributes.length];
        for (int i = 0; i < this.attributes.length; i++) {
            means[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
        }

        if (this.continuous) {
            final double[][] variances = (double[][]) object.getAttribute(this.variance);

            final IMultivariateDistribution component = new MultivariateNormalDistribution(means, variances);
            mvns.add(new Pair<Double, IMultivariateDistribution>(1.0, component));
        }
        else {
            final double probability = object.getAttribute(this.variance);

            final IMultivariateDistribution component = new MultivariateEnumeratedDistribution(means, probability);
            mvns.add(new Pair<Double, IMultivariateDistribution>(1.0, component));
        }
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(mvns);
        mixture.setAttributes(this.attributes);

        final MultivariateMixtureDistribution[] outputValDistributions = new MultivariateMixtureDistribution[distributions.length + 1];

        for (final int attribute : this.attributes) {
            outputVal.setAttribute(attribute, new ProbabilisticDouble(distributions.length));
        }
        // Copy the old distribution to the new tuple
        System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
        // And append the new distribution to the end of the array
        outputValDistributions[distributions.length] = mixture;
        outputVal.setDistributions(outputValDistributions);

        // KTHXBYE
        this.transfer(outputVal);
    }
    
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}


}
