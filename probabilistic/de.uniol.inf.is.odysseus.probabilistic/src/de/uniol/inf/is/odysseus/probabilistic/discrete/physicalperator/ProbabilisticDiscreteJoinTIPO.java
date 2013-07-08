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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;

/**
 * Join operator for discrete probabilistic values
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticDiscreteJoinTIPO<K extends ITimeIntervalProbabilistic, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(ProbabilisticDiscreteJoinTIPO.class);

	@Override
	protected void process_next(T object, int port) {
		transferFunction.newElement(object, port);

		if (isDone()) {
			return;
		}
		if (LOG.isDebugEnabled()) {
			if (!isOpen()) {
				LOG.error("process next called on non opened operator " + this + " with " + object + " from " + port);
				return;
			}
		}
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		Iterator<T> qualifies;

		synchronized (this) {

			if (inOrder) {
				areas[otherport].purgeElements(object, order);
			}

			if (isDone()) {
				propagateDone();
				return;
			}

			qualifies = areas[otherport].queryCopy(object, order);

			areas[port].insert(object);
		}
		while (qualifies.hasNext()) {
			T next = qualifies.next();
			// We already merge the two tuples in the sweep area to not expand the worlds again
			transferFunction.transfer(next);
		}
	}

}
