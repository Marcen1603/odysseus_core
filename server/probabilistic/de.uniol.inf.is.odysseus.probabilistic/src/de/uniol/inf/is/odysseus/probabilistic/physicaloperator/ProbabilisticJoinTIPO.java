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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticJoinTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
	private static Logger LOG = LoggerFactory.getLogger(ProbabilisticJoinTIPO.class);

	public ProbabilisticJoinTIPO(IMetadataMergeFunction<K> metadataMerge) {
		super(metadataMerge);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void process_next(final T object, final int port) {
		this.transferFunction.newElement(object, port);

		if (this.isDone()) {
			// TODO bei den sources abmelden ?? MG: Warum??
			// propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
		if (ProbabilisticJoinTIPO.LOG.isDebugEnabled()) {
			if (!this.isOpen()) {
				ProbabilisticJoinTIPO.LOG.error(
						"process next called on non opened operator " + this + " with " + object + " from " + port);
				return;
			}
		}
		final int otherport = port ^ 1;
		final Order order = Order.fromOrdinal(port);
		Iterator<T> qualifies;
		// Avoid removing elements while querying for potential hits
		synchronized (this) {

			if (this.inOrder) {
				this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).purgeElements(object, order);
			}

			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (this.isDone()) {
				this.propagateDone();
				return;
			}

			// depending on card, delete hits from areas
			// deleting if port is ONE-side
			// cases for ONE_MANY, MANY_ONE:
			// ONE side element is earlier than MANY side elements, nothing will
			// be found
			// and nothing will be removed
			// ONE side element is later than some MANY side elements, find all
			// corresponding elements and remove them
			boolean extract = false;
			if (this.card != null) {
				switch (this.card) {
				case ONE_ONE:
					extract = true;
					break;
				case MANY_ONE:
					extract = port == 1;
					break;
				case ONE_MANY:
					extract = port == 0;
					break;
				default:
					break;
				}
			}

			qualifies = this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).queryCopy(object, order, extract);

			final boolean hit = qualifies.hasNext();
			while (qualifies.hasNext()) {
				final T next = qualifies.next();

				next.setMetadata(this.metadataMerge.mergeMetadata(object.getMetadata(), next.getMetadata()));

				// TODO: Merge function in cases where key is same!!


				this.transferFunction.transfer(next);

			}
			// Depending on card insert elements into sweep area
			if ((this.card == null) || (this.card == Cardinalities.MANY_MANY)) {
				this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
			} else {
				switch (this.card) {
				case ONE_ONE:
					// If one to one case, a hit cannot be produce another
					// hit
					if (!hit) {
						this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					}
					break;
				case ONE_MANY:
					// If from left insert
					// if from right and no hit, insert (corresponding left
					// element not found now)
					if ((port == 0) || ((port == 1) && !hit)) {
						this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					}
					break;
				case MANY_ONE:
					// If from rightt insert
					// if from left and no hit, insert (corresponding right
					// element not found now)
					if ((port == 1) || ((port == 0) && !hit)) {
						this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					}
					break;
				default:
					this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					break;
				}
			}
		}
	}
}
