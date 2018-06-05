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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class ProbabilisticDiscreteUnNestPO<T extends IProbabilisticTimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticDiscreteUnNestPO.class);
    /** The position of the probabilistic attribute. */
    private final int probabilisticAttributePos;

    /**
     * Creates a new {@link ProbabilisticDiscreteUnNestPO} instance.
     * 
     * @param nestedAttributePos
     *            The position of the probabilistic attribute
     */
    public ProbabilisticDiscreteUnNestPO(final int nestedAttributePos) {
        this.probabilisticAttributePos = nestedAttributePos;
    }

    /**
     * ] Copy constructor.
     * 
     * @param po
     *            The object to copy from
     */
    public ProbabilisticDiscreteUnNestPO(final ProbabilisticDiscreteUnNestPO<T> po) {
        this.probabilisticAttributePos = po.probabilisticAttributePos;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected final void process_next(final ProbabilisticTuple<T> tuple, final int port) {
        try {
            // final AbstractProbabilisticValue<?> probabilisticValue =
            // (AbstractProbabilisticValue<?>)
            // tuple.getAttribute(this.probabilisticAttributePos);
            // for (final Map.Entry<?, Double> entry :
            // probabilisticValue.getValues().entrySet()) {
            // final ProbabilisticTuple<T> outputTuple = new
            // ProbabilisticTuple<T>(tuple);
            // outputTuple.getMetadata().setExistence(tuple.getMetadata().getExistence()
            // * entry.getValue());
            // outputTuple.setAttribute(this.probabilisticAttributePos,
            // entry.getKey());
            // this.transfer(outputTuple);
            // }
        }
        catch (final Exception e) {
            ProbabilisticDiscreteUnNestPO.LOG.error(e.getMessage(), e);
        }
    }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
    
}
