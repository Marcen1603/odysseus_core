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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.state.JoinTIPOState;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von
 * Daten- und Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse
 * implementierten Operators entspricht jedoch der Logik eines JoinOperators im
 * Intervallansatz.
 * 
 * @author Jonas Jacobi, abolles, jan steinke
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
public class JoinTIPO<K extends ITimeInterval, T extends IStreamObject<K>>
		extends AbstractPipe<T, T> implements IHasPredicate,
		IHasMetadataMergeFunction<K>, IStatefulOperator, IStatefulPO, IPhysicalOperatorKeyValueProvider {
	private static Logger _logger = null;

	private static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(JoinTIPO.class);
		}
		return _logger;
	}

	protected ITimeIntervalSweepArea<T>[] areas;
	protected IPredicate<? super T> joinPredicate;

	protected IDataMergeFunction<T, K> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;
	protected ITransferArea<T, T> transferFunction;
	protected IDummyDataCreationFunction<K, T> creationFunction;

	protected boolean inOrder = true;
	protected Cardinalities card = null;
	protected String sweepAreaName = null;

	// ------------------------------------------------------------------------------------

	public JoinTIPO() {

	}

	public JoinTIPO(IDataMergeFunction<T, K> dataMerge,
			IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction,
			ITimeIntervalSweepArea<T>[] areas) {
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.areas = areas;
	}

	public JoinTIPO(JoinTIPO<K, T> join) {
		super(join);
		this.areas = join.areas.clone();
		int i = 0;
		for (ITimeIntervalSweepArea<T> ja : join.areas) {
			this.areas[i] = ja.clone();
			i++;
		}

		this.joinPredicate = join.joinPredicate.clone();
		this.dataMerge = join.dataMerge.clone();
		dataMerge.init();

		this.metadataMerge = join.metadataMerge.clone();
		metadataMerge.init();
		this.transferFunction = join.transferFunction.clone();
		this.transferFunction.init(this, getSubscribedToSource().size());
		this.creationFunction = join.creationFunction.clone();
		this.card = join.card;
		this.sweepAreaName = join.sweepAreaName;
	}

	public IDataMergeFunction<T, K> getDataMerge() {
		return dataMerge;
	}

	public void setDataMerge(IDataMergeFunction<T, K> dataMerge) {
		this.dataMerge = dataMerge;
	}

	@Override
	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

	public void setMetadataMerge(IMetadataMergeFunction<K> metadataMerge) {
		this.metadataMerge = metadataMerge;
	}

	public void setAreas(ITimeIntervalSweepArea<T>[] areas) {
		this.areas = areas;
		if (this.joinPredicate != null) {
			areas[0].setQueryPredicate(this.joinPredicate);
			areas[1].setQueryPredicate(this.joinPredicate);
		}
	}

	public IPredicate<? super T> getJoinPredicate() {
		return joinPredicate;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPredicate getPredicate() {
		return getJoinPredicate();
	}

	public void setJoinPredicate(IPredicate<? super T> joinPredicate) {
		this.joinPredicate = joinPredicate;
		if (this.areas != null && this.joinPredicate != null) {
			areas[0].setQueryPredicate(this.joinPredicate);
			areas[1].setQueryPredicate(this.joinPredicate);
		}
	}

	public void setCardinalities(Cardinalities card) {
		this.card = card;
	}

	public void setSweepAreaName(String name) {
		this.sweepAreaName = name;
	}

	public String getSweepAreaName() {
		return sweepAreaName;
	}

	public void setTransferFunction(ITransferArea<T, T> transferFunction) {
		this.transferFunction = transferFunction;
	}

	public void setOutOfOrder(boolean outOfOrder) {
		this.inOrder = !outOfOrder;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {

		transferFunction.newElement(object, port);

		if (isDone()) {
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
		if (getLogger().isDebugEnabled()) {
			if (!isOpen()) {
				getLogger().error(
						"process next called on non opened operator " + this
								+ " with " + object + " from " + port);
				return;
			}
		}
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		Iterator<T> qualifies;
		// Avoid removing elements while querying for potential hits
		synchronized (this) {

			if (inOrder) {
				areas[otherport].purgeElements(object, order);
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
			if (card != null) {
				switch (card) {
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

			qualifies = areas[otherport].queryCopy(object, order, extract);

			boolean hit = qualifies.hasNext();
			while (qualifies.hasNext()) {
				T next = qualifies.next();
				T newElement = dataMerge.merge(object, next, metadataMerge,
						order);
				transferFunction.transfer(newElement);

			}
			// Depending on card insert elements into sweep area
			if (card == null || card == Cardinalities.MANY_MANY) {
				areas[port].insert(object);
			} else {
				switch (card) {
				case ONE_ONE:
					// If one to one case, a hit cannot be produce another hit
					if (!hit) {
						areas[port].insert(object);
					}
					break;
				case ONE_MANY:
					// If from left insert
					// if from right and no hit, insert (corresponding left
					// element not found now)
					if (port == 0 || (port == 1 && !hit)) {
						areas[port].insert(object);
					}
					break;
				case MANY_ONE:
					// If from rightt insert
					// if from left and no hit, insert (corresponding right
					// element not found now)
					if (port == 1 || (port == 0 && !hit)) {
						areas[port].insert(object);
					}
					break;
				default:
					areas[port].insert(object);
					break;
				}
			}
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
			this.areas[i].init();
		}
		this.dataMerge.init();
		this.metadataMerge.init();
		this.transferFunction.init(this, getSubscribedToSource().size());
	}

	@Override
	protected synchronized void process_close() {
		areas[0].clear();
		areas[1].clear();
	}

	@Override
	protected synchronized void process_done() {
//		if (isOpen()) {
//			areas[0].clear();
//			areas[1].clear();
//		}
	}

	@Override
	protected void process_done(int port) {
		transferFunction.done(port);
	}

	@Override
	protected boolean isDone() {
		try {

			if (getSubscribedToSource(0).isDone()) {
				return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
			}
			return getSubscribedToSource(1).isDone()
					&& getSubscribedToSource(0).isDone() && areas[1].isEmpty();
		} catch (ArrayIndexOutOfBoundsException e) {
			// Can happen if sources are unsubscribed while asking for done
			// Ignore
			return true;
		}
	}

	public ITimeIntervalSweepArea<T>[] getAreas() {
		return areas;
	}

	public ITransferArea<T, T> getTransferFunction() {
		return transferFunction;
	}

	public IDummyDataCreationFunction<K, T> getCreationFunction() {
		return creationFunction;
	}

	public void setCreationFunction(
			IDummyDataCreationFunction<K, T> creationFunction) {
		this.creationFunction = creationFunction;
	}

	@Override
	public JoinTIPO<K, T> clone() {
		return new JoinTIPO<K, T>(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat()) {
			this.areas[port ^ 1].purgeElementsBefore(punctuation.getTime());
		}
		this.transferFunction.sendPunctuation(punctuation);
		this.transferFunction.newElement(punctuation, port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>>) ipo;

		if (this.card != jtipo.card) {
			return false;
		}

		if (!dataMerge.getClass().toString()
				.equals(jtipo.dataMerge.getClass().toString())
				|| !metadataMerge.getClass().toString()
						.equals(jtipo.metadataMerge.getClass().toString())
				|| !creationFunction.getClass().toString()
						.equals(jtipo.creationFunction.getClass().toString())) {
			return false;
		}

		// Vergleichen des Join-Pr�dikats und des Output-Schemas
		if (this.getJoinPredicate().equals(jtipo.getJoinPredicate())
				&& this.getOutputSchema().compareTo(jtipo.getOutputSchema()) == 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isContainedIn(IPipe<T, T> ip) {
		if (!(ip instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>>) ip;

		if (this.card != jtipo.card) {
			return false;
		}

		if (!dataMerge.getClass().toString()
				.equals(jtipo.dataMerge.getClass().toString())
				|| !metadataMerge.getClass().toString()
						.equals(jtipo.metadataMerge.getClass().toString())
				|| !creationFunction.getClass().toString()
						.equals(jtipo.creationFunction.getClass().toString())) {
			return false;
		}

		// Vergleichen des Join-Pr�dikats
		if (this.getJoinPredicate().isContainedIn(jtipo.getJoinPredicate())) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the latest endtimestamp in both sweepareas.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PointInTime getLatestEndTimestamp() {
		PointInTime max = null;
		for (int i = 0; i < 2; i++) {
			synchronized (this.areas[i]) {
				PointInTime maxi = ((DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>) this.areas[i])
						.getMaxEndTs();
				if (max == null || (maxi != null && maxi.after(max))) {
					max = maxi;
				}
			}
		}
		return max;
	}

	@Override
	public String toString() {
		return super.toString() + " predicate: " + this.joinPredicate;
	}

	@Override
	public long getElementsStored1() {
		return areas[0].size();
	}

	@Override
	public long getElementsStored2() {
		return areas[1].size();
	}

	@Override
	public Serializable getState() {
		JoinTIPOState<K, T> state = new JoinTIPOState<K, T>();
		state.setSweepAreas(areas);
		state.setTransferArea(transferFunction);
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable s) {
		try {
			JoinTIPOState<K, T> state = (JoinTIPOState<K, T>) s;

			// set sweep area from state
			synchronized (this.areas) {
				for (int i = 0; i < state.getSweepAreas().length; i++) {
					// save the remove predicate
					IPredicate<? super IStreamObject<? extends ITimeInterval>> tempRemovePredicate = null;
					if (this.areas[i] instanceof DefaultTISweepArea) {
						tempRemovePredicate = ((DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>) this.areas[i])
								.getRemovePredicate();
					} else {
						throw new IllegalArgumentException("sweepArea type "+ this.areas[i].getName() +" is not supported");
					}

					// set the sweep area from the state
					this.areas[i] = state.getSweepAreas()[i];

					// set and initialize query and remove predicate
					if (this.joinPredicate != null && tempRemovePredicate != null){
						this.areas[i].setQueryPredicate(this.joinPredicate);
						this.areas[i].setRemovePredicate(tempRemovePredicate);
						this.areas[i].init();						
					} else {
						throw new IllegalArgumentException("query predicate or remove predicate must not be null");
					}
				}
			}

			// set transfer area from state
			this.transferFunction = state.getTransferArea();
			this.transferFunction.setTransfer(this);
		} catch (Exception e) {
			_logger.error("Error setting state for JoinTIPO. Error: " + e);
		}
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("Left Area Size", areas[0].size()+"");
		map.put("Right Area Size", areas[1].size()+"");
		map.put("OutputQueue", transferFunction.size()+"");
		map.put("Watermark",transferFunction.getWatermark()+"");
		return map;
	}

}
