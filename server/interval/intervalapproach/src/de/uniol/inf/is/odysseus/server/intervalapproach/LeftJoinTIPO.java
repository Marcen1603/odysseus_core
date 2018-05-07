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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Iterator;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;

/**
 * Left Join: Works as a join with one exception: elements on the left input (0)
 * will be transfered even if there is no join partner. In this case the schema
 * will be filled with null values.
 *
 * @author Michael Brand
 *
 * @param <K>
 * @param <T>
 */
public class LeftJoinTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(LeftJoinTIPO.class);

	/**
	 * The key for the temporal meta data, which marks if an element has a join
	 * partner.
	 */
	private static final String cMetaDataKey = "JoinPartner";

	// Start constructors from super

	public LeftJoinTIPO(IMetadataMergeFunction<K> metaDataMergeFunction) {
		super(metaDataMergeFunction);
	}

	public LeftJoinTIPO(ILeftMergeFunction<T, K> dataMergeFunction, IMetadataMergeFunction<K> metaDataMergeFunction,
			ITransferArea<T, T> transferArea) {
		super(dataMergeFunction, metaDataMergeFunction, transferArea);
	}

	// End constructors from super

	@Override
	protected void process_next(T object, int port) {
		// If not marked, it's the same algorithm as for normal joins

		this.transferFunction.newElement(object, port);

		if (isDone()) {
			return;
		} else if (cLog.isDebugEnabled() && !isOpen()) {
			cLog.error("process next called on non opened operator " + this + " with " + object + " from " + port);
		}

		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		Iterator<T> qualifies;

		// Avoid removing elements while querying for potential hits
		synchronized (this) {

			if (this.inOrder) {
				// Left Join: if elements in the left sweep area (0) shall be
				// purged, check, if they had join partners before.
				if (otherport == 0) {
					Iterator<T> extracted = this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).extractElements(object, order);
					while (extracted.hasNext()) {
						T next = extracted.next();
						if (!next.hasTransientMarker(cMetaDataKey)) {
							T out = ((ILeftMergeFunction<T, K>) this.dataMerge).createLeftFilledUp(next);
							this.transferFunction.transfer(out);
						}
					}
				} else {
					this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).purgeElements(object, order);
				}
			}

			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
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

			boolean hit = qualifies.hasNext();

			// Left Join: if this is the left input port, mark the object as
			// join partner found
			if (hit && port == 0) {
				object.setTransientMarker(cMetaDataKey, Boolean.TRUE);
			}

			while (qualifies.hasNext()) {
				T next = qualifies.next();
				T newElement = this.dataMerge.merge(object, next, this.metadataMerge, order);
				this.transferFunction.transfer(newElement);

				// Left Join: if "next" is from the left sweep area, mark it as
				// join partner found
				if (hit && otherport == 0 && (!next.hasTransientMarker(cMetaDataKey))) {
					this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).remove(next);
					next.setTransientMarker(cMetaDataKey, Boolean.TRUE);
					this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).insert(next);
				}

			}
			// Depending on card insert elements into sweep area
			if (this.card == null || this.card == Cardinalities.MANY_MANY) {
				this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
			} else {
				switch (this.card) {
				case ONE_ONE:
					// If one to one case, a hit cannot be produce another hit
					if (!hit) {
						this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					}
					break;
				case ONE_MANY:
					// If from left insert
					// if from right and no hit, insert (corresponding left
					// element not found now)
					if (port == 0 || (port == 1 && !hit)) {
						this.getSweepArea(port, DEFAULT_GROUPING_KEY).insert(object);
					}
					break;
				case MANY_ONE:
					// If from right insert
					// if from left and no hit, insert (corresponding right
					// element not found now)
					if (port == 1 || (port == 0 && !hit)) {
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

	@Override
	protected void process_done(int port) {
		if (port == 1) {
			// There are no more join partners for every element in sweep area
			// with port 0

			StreamSupport.stream(this.getSweepArea(0, DEFAULT_GROUPING_KEY).spliterator(), false)
					.filter(elem -> !elem.hasTransientMarker(cMetaDataKey)).forEach(elem -> {
						T out = ((ILeftMergeFunction<T, K>) this.dataMerge).createLeftFilledUp(elem);
						this.transferFunction.transfer(out);
					});
		}
		super.process_done(port);
	}

	@Override
	public void processPunctuation(IPunctuation inPunctuation, int port) {
		IPunctuation punctuation = joinPredicate.processPunctuation(inPunctuation);
		if (punctuation.isHeartbeat()) {
			synchronized (this) {
				// Left Join: if elements in the left sweep area (0) shall be
				// purged, check, if they had join partners before.
				if (port == 1) {
					Iterator<T> extracted = this.getSweepArea(port ^ 1, DEFAULT_GROUPING_KEY).extractElementsBefore(punctuation.getTime());
					while (extracted.hasNext()) {
						T next = extracted.next();
						if (!next.hasTransientMarker(cMetaDataKey)) {
							T out = ((ILeftMergeFunction<T, K>) this.dataMerge).createLeftFilledUp(next);
							this.transferFunction.transfer(out);
						}
					}
				} else {
					this.getSweepArea(port ^ 1, DEFAULT_GROUPING_KEY).purgeElementsBefore(punctuation.getTime());
				}
			}
		}
		this.transferFunction.sendPunctuation(punctuation);
		this.transferFunction.newElement(punctuation, port);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof LeftJoinTIPO)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}

	@Override
	public boolean isContainedIn(IPipe<T, T> ip) {
		if (!(ip instanceof LeftJoinTIPO)) {
			return false;
		}
		return super.isContainedIn(ip);
	}

}