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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.state.UnionPOState;

public class UnionPO<R extends IStreamObject<?>> extends AbstractPipe<R, R>
		implements IStatefulOperator, IStatefulPO,
		IPhysicalOperatorKeyValueProvider {

	Logger logger = LoggerFactory.getLogger(UnionPO.class);

	private boolean useInputPortAsOutputPort = false;

	protected ITransferArea<R, R> transferArea;

	private boolean drainAtClose;

	public UnionPO(ITransferArea<R, R> transferFunction) {
		this.transferArea = transferFunction;
	}

	public UnionPO(UnionPO<R> unionPO) {
		this.transferArea = unionPO.transferArea.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	public void setUseInputPortAsOutputPort(boolean useInputPortAsOutputPort) {
		this.useInputPortAsOutputPort = useInputPortAsOutputPort;
	}

	@Override
	protected void newSourceSubscribed(
			AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> sub) {
		transferArea.addNewInput(sub.getSinkInPort());
	}

	@Override
	protected void sourceUnsubscribed(
			AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> sub) {
		transferArea.removeInput(sub.getSinkInPort());
	}

	@Override
	protected void process_open() throws OpenFailedException {
		transferArea.init(this, getSubscribedToSource().size());
	}

	@Override
	public PointInTime getLatestEndTimestamp() {
		return transferArea.calcMaxEndTs();
	}

	@Override
	protected synchronized void process_next(R object, int port) {
		if (useInputPortAsOutputPort) {
			transferArea.transfer(object, port);
		} else {
			transferArea.transfer(object);
		}
		transferArea.newElement(object, port);
	}

	@Override
	protected void process_close() {
		if (drainAtClose) {
			for (int i = 0; i < getSubscribedToSource().size(); i++) {
				transferArea.done(i);
			}
		}
		super.process_close();
	}

	@Override
	protected void process_done(int port) {
		transferArea.done(port);
		logger.debug("Done on " + port);
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		if (useInputPortAsOutputPort) {
			transferArea.sendPunctuation(punctuation, port);
		} else {
			transferArea.sendPunctuation(punctuation);
		}
		transferArea.newElement(punctuation, port);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof UnionPO)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public long getElementsStored1() {
		return transferArea.size();
	}

	public PointInTime getWatermark() {
		return transferArea.getWatermark();
	}

	@Override
	public IOperatorState getState() {
		UnionPOState<R> state = new UnionPOState<R>();
		state.setTransferArea(this.transferArea);
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setStateInternal(Serializable s) {
		try {
			UnionPOState<R> state = (UnionPOState<R>) s;
			this.transferArea = state.getTransferArea();
			this.transferArea.setTransfer(this);
		} catch (Throwable T) {
			logger.error("The serializable state to set for the UnionPO is not a valid UnionPOState!");
		}
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("Elements read", transferArea.getElementsRead() + "");
		map.put("Punctuations read", transferArea.getPunctuationsRead() + "");
		map.put("Elements written", transferArea.getElementsWritten() + "");
		map.put("Punctuations written", transferArea.getPunctuationsWritten()
				+ "");
		map.put("Heartbeats suppressed",
				transferArea.getPunctuationsSuppressed() + "");
		map.put("OutputQueueSize", transferArea.size() + "");
		map.put("Watermark", transferArea.getWatermark() + "");
		return map;
	}

	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
	}

}
