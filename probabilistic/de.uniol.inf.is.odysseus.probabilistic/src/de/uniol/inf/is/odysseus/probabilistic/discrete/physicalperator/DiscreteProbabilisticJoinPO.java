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

package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

/**
 * Join operator for discrete probabilistic values
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class DiscreteProbabilisticJoinPO extends JoinTIPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> {
    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(DiscreteProbabilisticJoinPO.class);
    private final int[] leftViewAttributePos;
    private final int[] rightViewAttributePos;

    public DiscreteProbabilisticJoinPO(int[] leftViewAttributePos, int[] rightViewAttributePos) {
        super();
        this.leftViewAttributePos = leftViewAttributePos;
        this.rightViewAttributePos = rightViewAttributePos;
    }

    // FIXME Needs review
    @Override
    protected void process_next(ProbabilisticTuple<ITimeIntervalProbabilistic> object, int port) {
        Order order = Order.fromOrdinal(port);
        if (order == Order.LeftRight) {
            for (int i = 0; i < leftViewAttributePos.length; i++) {
                AbstractProbabilisticValue<?> viewAttribute = (AbstractProbabilisticValue<?>) object.getAttribute(leftViewAttributePos[i]);
                for (Entry<?, Double> value : viewAttribute.getValues().entrySet()) {
                    ProbabilisticTuple<ITimeIntervalProbabilistic> copy = object.clone();
                    copy.setAttribute(leftViewAttributePos[i], value.getKey());
                    ITimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
                    metadata.setExistence(copy.getMetadata().getExistence() * value.getValue());
                    copy.setMetadata(metadata);
                    super.process_next(copy, port);
                }
            }
        } else {
            for (int i = 0; i < rightViewAttributePos.length; i++) {
                AbstractProbabilisticValue<?> viewAttribute = (AbstractProbabilisticValue<?>) object.getAttribute(rightViewAttributePos[i]);
                for (Entry<?, Double> value : viewAttribute.getValues().entrySet()) {
                    ProbabilisticTuple<ITimeIntervalProbabilistic> copy = object.clone();
                    copy.setAttribute(rightViewAttributePos[i], value.getKey());
                    ITimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
                    metadata.setExistence(copy.getMetadata().getExistence() * value.getValue());
                    copy.setMetadata(metadata);
                    super.process_next(copy, port);
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
