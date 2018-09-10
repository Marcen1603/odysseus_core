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

package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Physical operator for Expectation Maximization (EM) classifier.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class EMPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(EMPO.class);
    /** The sweep area to hold the data. */
    private final ITimeIntervalSweepArea<ProbabilisticTuple<? extends ITimeInterval>> area;
    /** The attribute positions. */
    private final int[] attributes;

    /**
     * Creates a new EM operator.
     * 
     * @param attributes
     *            The attribute positions
     * @param mixtures
     *            The number of mixtures
     * @param iterations
     *            The maximum number of iterations allowed per fitting process
     * @param threshold
     *            The convergence threshold for fitting
     * @param incremental
     *            Flag indicating incremental fitting
     * @param predicate
     *            The predicate for model fitting
     */
    @SuppressWarnings("unchecked")
    public EMPO(final int[] attributes, final int mixtures, final int iterations, final double threshold, final boolean incremental, @SuppressWarnings("rawtypes") final IPredicate predicate) {
        this.attributes = attributes;
        this.area = new BatchEMTISweepArea(attributes, mixtures, iterations, threshold, incremental, predicate);
    }

    /**
     * Clone constructor.
     * 
     * @param emPO
     *            The copy
     */
    public EMPO(final EMPO<T> emPO) {
        super(emPO);
        this.attributes = emPO.attributes.clone();
        this.area = emPO.area.clone();
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
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        final MultivariateMixtureDistribution[] distributions = object.getDistributions();
        final ProbabilisticTuple<T> outputVal = object.clone();
        // Purge old elements out of the sweep area.
        synchronized (this.area) {
            this.area.purgeElements(object, Order.LeftRight);
        }
        try {
            // Insert the new element into the sweep area.
            // Expectation-step and Maximization-step will be done during
            // insert.
            synchronized (this.area) {
                this.area.insert(object);
            }

            // Construct the multivariate distribution
            final BatchEMTISweepArea emArea = (BatchEMTISweepArea) this.area;
            final MultivariateMixtureDistribution model = emArea.getModel();
            if (model != null) {
                model.setAttributes(this.attributes);
                final MultivariateMixtureDistribution[] outputValDistributions = new MultivariateMixtureDistribution[distributions.length + 1];
                for (final int attribute : this.attributes) {
                    outputVal.setAttribute(attribute, new ProbabilisticDouble(distributions.length));
                }
                // Copy the old distribution to the new tuple
                System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
                // And append the new distribution to tThe end of the array
                outputValDistributions[distributions.length] = model;
                outputVal.setDistributions(outputValDistributions);
                // KTHXBYE
                this.transfer(outputVal);
            }
        }
        catch (MathIllegalArgumentException | MaxCountExceededException | ConvergenceException e) {
            EMPO.LOG.debug(e.getMessage(), e);
        }
    }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
    
}
