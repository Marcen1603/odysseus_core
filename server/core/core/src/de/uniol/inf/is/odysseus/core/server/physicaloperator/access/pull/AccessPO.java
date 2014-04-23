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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

/**
 * This class represents all sources that need to be scheduled to deliver input
 * (pull). For all sources that push their data use ReceivePO
 * 
 * @author Marco Grawunder
 * 
 * @param <R>
 *            The immediate values that are send to
 * @param <W>
 *            The Output that is written by this operator.
 */
public class AccessPO<R, W> extends AbstractIterableSource<W> {

	private static final Logger LOG = LoggerFactory.getLogger(AccessPO.class);

	private boolean isDone = false;
	private final long maxTimeToWaitForNewEventMS;
	private long lastTransfer = 0;

	private IProtocolHandler<W> protocolHandler;

//	public AccessPO(IProtocolHandler<W> protocolHandler) {
//		this.protocolHandler = protocolHandler;
//		this.maxTimeToWaitForNewEventMS = 0;
//	}
	
	public AccessPO(IProtocolHandler<W> protocolHandler,long maxTimeToWaitForNewEventMS) {
		this.protocolHandler = protocolHandler;
		this.maxTimeToWaitForNewEventMS = maxTimeToWaitForNewEventMS;
	}

	@Override
	public synchronized boolean hasNext() {
		if (isDone || !isOpen()) {
			return false;
		}

		if (maxTimeToWaitForNewEventMS > 0 && lastTransfer > 0) {
			if (System.currentTimeMillis()-lastTransfer > maxTimeToWaitForNewEventMS){
				return doDone();
			}
		}

		try {
			if (protocolHandler.isDone()) {
				return doDone();
			} else {
				return protocolHandler.hasNext();
			}
		} catch (Exception e) {
			LOG.error("Exception during input", e);
		}

		// TODO: We should think about propagate done ... maybe its better
		// to send a punctuation??
		tryPropagateDone();
		return false;
	}

	public boolean doDone() {
		isDone = true;
		propagateDone();
		return false;
	}

	private void tryPropagateDone() {
		try {
			propagateDone();
		} catch (Throwable throwable) {
			LOG.error("Exception during propagating done", throwable);
		}
	}

	@Override
	public synchronized void transferNext() {
		if (isOpen() && !isDone()) {
			W toTransfer = null;
			try {
				toTransfer = protocolHandler.getNext();
				if (toTransfer == null) {
					isDone = true;
					propagateDone();
				} else {
					if (maxTimeToWaitForNewEventMS > 0) {
						lastTransfer = System.currentTimeMillis();
					}
					transfer(toTransfer);
				}
			} catch (Exception e) {
				LOG.error("Error Reading from input", e);
			}
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		if (!isOpen()) {
			try {
				isDone = false;
				protocolHandler.open();
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	protected void process_close() {
		try {
			if (isOpen()) {
				protocolHandler.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public AbstractSource<W> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AccessPO)) {
			return false;
		}
		// TODO: Check for Equality
		return false;
	}

}
