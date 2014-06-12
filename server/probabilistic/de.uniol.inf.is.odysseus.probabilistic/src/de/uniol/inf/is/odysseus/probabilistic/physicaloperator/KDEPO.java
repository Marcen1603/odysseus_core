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
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.kde.BandwidthSelectionRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class KDEPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** The logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(KDEPO.class);
    /** The sweep area to hold the data. */
    private final DefaultTISweepArea<Tuple<? extends ITimeInterval>> area;
    /** The attribute positions. */
    private final int[] attributes;

    /**
 * 
 */
    public KDEPO(final int[] attributes) {
        this.attributes = attributes;
        this.area = new DefaultTISweepArea<Tuple<? extends ITimeInterval>>();
    }

    /**
     * @param po
     */
    public KDEPO(final KDEPO<T> po) {
        super(po);
        this.attributes = po.attributes.clone();
        this.area = po.area.clone();
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
            this.area.purgeElements(restrictedObject, Order.LeftRight);
        }

        synchronized (this.area) {
            this.area.insert(restrictedObject);
        }
        final double[][] data = new double[this.area.size()][this.attributes.length];
        int i = 0;
        for (final Tuple<?> t : this.area) {
            for (int j = 0; j < this.attributes.length; j++) {
                data[i][j] = ((Number) t.getAttributes()[j]).doubleValue();
            }
            i++;
        }
        // Estimate covariance matrix
        final double factor = BandwidthSelectionRule.scott(this.area.size(), this.attributes.length);
        final RealMatrix dataCovarianceMatrix = new Covariance(data, false).getCovarianceMatrix();
        final RealMatrix covariance = dataCovarianceMatrix.scalarMultiply(FastMath.pow(factor, 2.0));

        final List<Pair<Double, IMultivariateDistribution>> components = new ArrayList<>(data.length);
        for (final double[] d : data) {
            final IMultivariateDistribution component = new MultivariateNormalDistribution(d, covariance.getData());
            components.add(new Pair<>(new Double(1.0 / data.length), component));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
        return new KDEPO<T>(this);
    }

}
