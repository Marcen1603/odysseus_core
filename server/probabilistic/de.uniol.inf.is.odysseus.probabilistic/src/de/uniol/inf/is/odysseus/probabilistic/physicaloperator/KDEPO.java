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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.kde.BandwidthSelectionRule;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class KDEPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** The logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(KDEPO.class);
    /** The sweep area to hold the data. */
    private final ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> area;
    /** The attribute positions. */
    private final int[] attributes;
    /** partial parameter to estimate the covariance matrix. */
    private final Parameter[][] covarianceParameter;
    /** The covariance matrix. */
    private final double[][] covariance;

    /**
 * 
 */
    public KDEPO(final int[] attributes, ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> area) {
        this.attributes = attributes;
        this.covariance = new double[this.attributes.length][attributes.length];
        this.covarianceParameter = new Parameter[this.attributes.length][attributes.length];
        this.area = area;
        for (int i = 0; i < this.attributes.length; i++) {
            for (int j = 0; j < this.attributes.length; j++) {
                this.covarianceParameter[i][j] = new Parameter();
            }
        }
    }

    /**
     * @param po
     */
    public KDEPO(final KDEPO<T> po) {
        super(po);
        this.attributes = po.attributes.clone();
        this.covariance = po.covariance.clone();
        this.covarianceParameter = new Parameter[this.attributes.length][this.attributes.length];
        this.area = po.area.clone();
        for (int i = 0; i < this.attributes.length; i++) {
            for (int j = 0; j < this.attributes.length; j++) {
                this.covarianceParameter[i][j] = new Parameter();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_next(final ProbabilisticTuple<T> object, final int port) {
        final MultivariateMixtureDistribution[] distributions = object.getDistributions();
        final ProbabilisticTuple<T> outputVal = object.clone();
        final Tuple<T> restrictedObject = object.restrict(this.attributes, true);
        synchronized (this.area) {
            final Iterator<Tuple<? extends ITimeInterval>> iter = this.area.extractElementsBefore(restrictedObject.getMetadata().getStart());
            while (iter.hasNext()) {
                final Tuple<? extends ITimeInterval> attr = iter.next();
                for (int i = 0; i < this.attributes.length; i++) {
                    for (int j = 0; j < this.attributes.length; j++) {
                        this.covarianceParameter[i][j].subtract(((Number) attr.getAttributes()[i]).doubleValue(), ((Number) attr.getAttributes()[j]).doubleValue());
                    }
                }
            }
        }

        synchronized (this.area) {
            this.area.insert(restrictedObject);
            for (int i = 0; i < this.attributes.length; i++) {
                for (int j = 0; j < this.attributes.length; j++) {
                    this.covarianceParameter[i][j].add(((Number) restrictedObject.getAttributes()[i]).doubleValue(), ((Number) restrictedObject.getAttributes()[j]).doubleValue());
                }
            }
        }
        // Estimate covariance matrix
        final double factor = BandwidthSelectionRule.scott(this.area.size(), this.attributes.length);
        for (int i = 0; i < this.attributes.length; i++) {
            for (int j = 0; j < this.attributes.length; j++) {
                this.covariance[i][j] = this.covarianceParameter[i][j].covariance() * FastMath.pow(factor, 2.0);
            }
        }
        // Create KDE components
        final List<Pair<Double, IMultivariateDistribution>> components = new ArrayList<>(this.area.size());
        for (final Tuple<?> t : this.area) {
            final double[] data = new double[this.attributes.length];
            for (int j = 0; j < this.attributes.length; j++) {
                data[j] = ((Number) t.getAttributes()[j]).doubleValue();
            }
            final IMultivariateDistribution component = new MultivariateNormalDistribution(data, this.covariance);

            components.add(new Pair<>(new Double(1.0 / this.area.size()), component));
        }

        final MultivariateMixtureDistribution distribution = new MultivariateMixtureDistribution(components);
        distribution.setAttributes(this.attributes);
        final MultivariateMixtureDistribution[] outputValDistributions = new MultivariateMixtureDistribution[distributions.length + 1];
        for (final int attribute : this.attributes) {
            outputVal.setAttribute(attribute, new ProbabilisticDouble(distributions.length));
        }
        // Copy the old distribution to the new tuple
        System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
        // And append the new distribution to tThe end of the array
        outputValDistributions[distributions.length] = distribution;
        outputVal.setDistributions(outputValDistributions);
        // KTHXBYE
        this.transfer(outputVal);
    }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
    
    private static class Parameter {

        private double sumA;
        private double sumB;
        private double crossproductSum;
        private double count;

        /**
         * 
         */
        public Parameter() {
        }

        public void add(final double a, final double b) {
            this.sumA += a;
            this.sumB += b;
            this.crossproductSum += a * b;
            this.count++;
        }

        public void subtract(final double a, final double b) {
            this.sumA -= a;
            this.sumB -= b;
            this.crossproductSum -= a * b;
            this.count--;
        }

        public double covariance() {
            return (this.crossproductSum - ((this.sumA * this.sumB) / this.count)) / (this.count - 1);

        }
    }

}
