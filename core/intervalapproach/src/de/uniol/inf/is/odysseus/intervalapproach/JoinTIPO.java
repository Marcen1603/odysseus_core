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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;

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
		IHasMetadataMergeFunction<K> {
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

	private boolean inOrder = true;

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
		this.transferFunction.init(this);
		this.creationFunction = join.creationFunction.clone();

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
		if (!isOpen()) {
			getLogger().error(
					"process next called on non opened operator " + this
							+ " with " + object + " from " + port);
			return;
		}
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);

		if (inOrder) {
			synchronized (this.areas[otherport]) {
				areas[otherport].purgeElements(object, order);
			}
		}
		
		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, order);
			}
			transferFunction.newElement(object, port);
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			T newElement = dataMerge.merge(object, next, metadataMerge, order);
			transferFunction.transfer(newElement);
		}
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
			this.areas[i].init();
		}
		this.dataMerge.init();
		this.metadataMerge.init();
		this.transferFunction.init(this);
	}

	@Override
	protected synchronized void process_close() {
		areas[0].clear();
		areas[1].clear();
	}

	@Override
	protected synchronized void process_done() {
		transferFunction.done();
		if (isOpen()) {
			areas[0].clear();
			areas[1].clear();
		}
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
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		if (punctuation.isHeartbeat()) {
			this.areas[port ^ 1].purgeElementsBefore(punctuation.getTime());
		}
		this.transferFunction.newElement(punctuation, port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>>) ipo;

		// Falls die Operatoren verschiedene Quellen haben, wird false zur�ck
		// gegeben
		if (!this.hasSameSources(jtipo)
				|| !dataMerge.getClass().toString()
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

		// Falls die Operatoren verschiedene Quellen haben, wird false zur�ck
		// gegeben
		if (!this.hasSameSources(jtipo)
				|| !dataMerge.getClass().toString()
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

	@Override
	public String getName() {
		return super.getName();
		// return super.getName() + "Left SA: " + this.getAreas()[0].toString()
		// + " Right SA: " + this.getAreas()[1].toString();
	}

}
