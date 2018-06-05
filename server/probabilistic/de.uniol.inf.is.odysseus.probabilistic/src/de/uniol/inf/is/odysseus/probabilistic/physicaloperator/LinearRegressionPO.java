/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class LinearRegressionPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LinearRegressionPO.class);

    /** The sweep area. */
    private final LinearRegressionTISweepArea area;

    /**
     * 
     * @param dependentList
     *            The list of dependent attribute positions
     * @param explanatoryList
     *            The list of explanatory attribute positions
     */
    public LinearRegressionPO(final int[] dependentList, final int[] explanatoryList) {
        this.area = new LinearRegressionTISweepArea(dependentList, explanatoryList);
    }

    /**
     * Clone constructor.
     * 
     * @param linearRegressionPO
     *            The copy
     */
    public LinearRegressionPO(final LinearRegressionPO<T> linearRegressionPO) {
        super(linearRegressionPO);
        this.area = linearRegressionPO.area.clone();
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
    @SuppressWarnings("unchecked")
    @Override
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        synchronized (this.area) {
            this.area.insert(object);
        }
        if (this.area.isEstimable()) {
            final RealMatrix regressionCoefficients = this.area.getRegressionCoefficients();
            final RealMatrix residual = this.area.getResidual();

            // final ExtendedMixtureMultivariateRealDistribution mixture = new
            // ExtendedMixtureMultivariateRealDistribution(residual.getColumnDimension());
            // mixture.setAttributes(this.area.getExplanatoryAttributePos());

            final MultivariateMixtureDistribution[] distributions = object.getDistributions();
            final Object[] attributes = object.getAttributes();

            final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(new Object[attributes.length + 2], new MultivariateMixtureDistribution[distributions.length + 1],
                    object.requiresDeepClone());
            // outputVal.setDistribution(distributions.length, mixture);

            System.arraycopy(distributions, 0, outputVal.getDistributions(), 0, distributions.length);
            System.arraycopy(object.getAttributes(), 0, outputVal.getAttributes(), 0, object.getAttributes().length);

            for (int i = 0; i < this.area.getExplanatoryAttributePos().length; i++) {
                final int pos = this.area.getExplanatoryAttributePos()[i];
                outputVal.setAttribute(pos, new ProbabilisticDouble(distributions.length));
            }
            outputVal.setAttribute(object.getAttributes().length, regressionCoefficients.getData());
            outputVal.setAttribute(object.getAttributes().length + 1, residual.getData());
            outputVal.setMetadata((T) object.getMetadata().clone());
            // KTHXBYE
            this.transfer(outputVal);
        }
    }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
}
