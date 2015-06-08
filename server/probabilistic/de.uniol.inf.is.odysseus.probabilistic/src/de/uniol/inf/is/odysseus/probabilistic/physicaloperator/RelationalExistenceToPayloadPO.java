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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RelationalExistenceToPayloadPO extends ExistenceToPayloadPO<IProbabilistic, ProbabilisticTuple<IProbabilistic>> {

    /**
     * Default constructor.
     */
    public RelationalExistenceToPayloadPO() {
    }

    /**
     * Clone constructor.
     * 
     * @param relationalExistenceToPayloadPO
     *            The object to copy from
     */
    public RelationalExistenceToPayloadPO(final RelationalExistenceToPayloadPO relationalExistenceToPayloadPO) {
        super(relationalExistenceToPayloadPO);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected final void process_next(final ProbabilisticTuple<IProbabilistic> object, final int port) {
        final int inputSize = object.size();
        final Object[] attributes = new Object[object.size() + 1];
        final ProbabilisticTuple<IProbabilistic> out = new ProbabilisticTuple<IProbabilistic>(object, attributes, false);

        System.arraycopy(object.getAttributes(), 0, attributes, 0, inputSize);

        out.setAttribute(inputSize, object.getMetadata().getExistence());
        this.transfer(out);
    }
    
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}


}
