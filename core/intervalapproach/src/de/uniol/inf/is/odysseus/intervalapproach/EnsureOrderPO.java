/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * Physical implementation of the EnsureOrderAO. All incoming tuples are stored
 * in a SweepArea and ordered and transfered when a Punctuation is processed.
 * 
 * There has to be an AssureHeartBeatOperator in the plan before this.
 * 
 * @author Merlin Wasmann
 * 
 */
public class EnsureOrderPO<K extends ITimeInterval, T extends IStreamObject<K>>
		extends AbstractPipe<T, T> {

	private final static Logger LOG = LoggerFactory
			.getLogger(EnsureOrderPO.class);

	private ITransferArea<T, T> transferFunction;
	
	private boolean firstElement = true;

	public EnsureOrderPO() {

	}

	public EnsureOrderPO(ITransferArea<T, T> transferArea) {
		this.transferFunction = transferArea;
	}

	public EnsureOrderPO(EnsureOrderPO<K, T> ensureOrderPO) {
		super(ensureOrderPO);
		this.transferFunction = ensureOrderPO.transferFunction.clone();
		this.transferFunction.init(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(T object, int port) {
		LOG.debug("Process {}", object);
		// to not lose the first tuples before the first heartbeat.
		if(this.firstElement) {
			this.firstElement = false;
			this.transferFunction.newElement(object, port);
		} else {
			// insert object into the transferFunction.
			this.transferFunction.transfer(object);
		}
		
		if (isDone()) {
			return;
		}

		if (LOG.isDebugEnabled()) {
			if (!isOpen()) {
				LOG.error("process next called on non opened operator " + this
						+ " with " + object + " from " + port);
				return;
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat()) {
			LOG.debug("Received Heartbeat at " + punctuation.getTime());
			// this inserts the punctuation and allows transferFunction to write
			// out all tuples with older timestamps.
			this.transferFunction.newElement(punctuation, port);
		}
		this.transferFunction.sendPunctuation(punctuation);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.transferFunction.init(this);
	}
	
	@Override
	protected synchronized void process_done() {
		this.transferFunction.done();
	}

	@Override
	protected boolean isDone() {
		try {
			return getSubscribedToSource(0).isDone();
		} catch (ArrayIndexOutOfBoundsException ex) {
			return true;
		}
	}

	public void setTransferFunction(ITransferArea<T, T> transferFunction) {
		this.transferFunction = transferFunction;
	}

	public ITransferArea<T, T> getTransferFunction() {
		return this.transferFunction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
	 * ()
	 */
	@Override
	public EnsureOrderPO<K, T> clone() {
		return new EnsureOrderPO<K, T>(this);
	}

}
