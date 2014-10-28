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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

public class UnionPO<R extends IStreamObject<?>> extends AbstractPipe<R, R>
		implements IStatefulOperator, IStatefulPO {

	Logger logger = LoggerFactory.getLogger(UnionPO.class);

	private boolean useInputPortAsOutputPort = false;

	protected ITransferArea<R, R> transferArea;

	public UnionPO(ITransferArea<R, R> transferFunction) {
		this.transferArea = transferFunction;
		transferFunction.init(this, getSubscribedToSource().size());
	}

	public UnionPO(UnionPO<R> unionPO) {
		this.transferArea = unionPO.transferArea.clone();
	}

	@Override
	public UnionPO<R> clone() {
		return new UnionPO<R>(this);
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
			PhysicalSubscription<ISource<? extends R>> sub) {
		transferArea.addNewInput(sub.getSinkInPort());
	}

	@Override
	protected void sourceUnsubscribed(
			PhysicalSubscription<ISource<? extends R>> sub) {
		transferArea.removeInput(sub.getSinkInPort());
	}

	@Override
	protected void process_open() throws OpenFailedException {
		transferArea.init(this, getSubscribedToSource().size());
	}

	@Override
	protected void process_next(R object, int port) {
		if (useInputPortAsOutputPort) {
			transferArea.transfer(object, port);
		} else {
			transferArea.transfer(object);
		}
		transferArea.newElement(object, port);
	}

	@Override
	protected void process_close() {
		for (int i = 0; i < getSubscribedToSource().size(); i++) {
			transferArea.done(i);
		}
	}

	@Override
	protected void process_done(int port) {
		transferArea.done(port);
		logger.debug("Done on " + port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
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

	@Override
	public Serializable getState() {
		UnionPOState state = new UnionPOState();
		state.transferArea = this.transferArea;
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable s) {
		try {
			UnionPOState state = (UnionPOState) s;
			this.transferArea = state.transferArea;
			this.transferArea.setTransfer(this);
		} catch (Throwable T) {
			logger.error("The serializable state to set for the UnionPO is not a valid UnionPOState!");
		}
	}

	/**
	 * The current state of an {@link UnionPO} is defined by its transfer area
	 * and its groups.
	 * 
	 * @author Chris Tönjes-Deye
	 * 
	 */
	private class UnionPOState implements Serializable {

		private static final long serialVersionUID = 9088231287860150949L;

		ITransferArea<R, R> transferArea;
	}
}
