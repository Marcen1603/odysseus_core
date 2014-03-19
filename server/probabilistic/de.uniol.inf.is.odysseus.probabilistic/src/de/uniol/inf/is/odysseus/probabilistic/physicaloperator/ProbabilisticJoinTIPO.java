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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;

/**
 * Join operator for discrete probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticJoinTIPO<K extends IProbabilisticTimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticJoinTIPO.class);

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO#process_next
     * (de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
     */
    @Override
    protected final void process_next(final T object, final int port) {
        this.transferFunction.newElement(object, port);

        if (this.isDone()) {
            return;
        }
        if (ProbabilisticJoinTIPO.LOG.isDebugEnabled()) {
            if (!this.isOpen()) {
                ProbabilisticJoinTIPO.LOG.error("process next called on non opened operator " + this + " with " + object + " from " + port);
                return;
            }
        }
        final int otherport = port ^ 1;
        final Order order = Order.fromOrdinal(port);
        Iterator<T> qualifies;

        synchronized (this) {

            if (this.inOrder) {
                this.areas[otherport].purgeElements(object, order);
            }

            if (this.isDone()) {
                this.propagateDone();
                return;
            }

            qualifies = this.areas[otherport].queryCopy(object, order, false);

            this.areas[port].insert(object);
        }
        while (qualifies.hasNext()) {
            final T next = qualifies.next();
            // We already merge the two tuples in the sweep area to not expand
            // the worlds again
            // KTHXBYE
            this.transferFunction.transfer(next);
        }
    }
}
