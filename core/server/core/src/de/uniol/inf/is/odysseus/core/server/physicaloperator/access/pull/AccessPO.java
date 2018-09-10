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

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSchedulerSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IterableSchedulerSourceDelegate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;

/**
 * This class represents all sources that need to be scheduled to deliver input
 * (pull). For all sources that push their data use ReceivePO
 *
 *
 * @author Marco Grawunder
 *
 * @param <R>
 *            The immediate values that are send to
 * @param <W>
 *            The Output that is written by this operator.
 */
public class AccessPO<W extends IStreamObject<M>, M extends IMetaAttribute> extends ReceiverPO<W, M>
		implements IMetadataInitializer<M, W>, IIterableSource<W> {

	private static final Logger LOG = LoggerFactory.getLogger(AccessPO.class);

	private boolean isDone = false;
	private final long maxTimeToWaitForNewEventMS;
	private long lastTransfer = 0;

	final private AtomicBoolean isClosing = new AtomicBoolean(false);

	IIterableSchedulerSource iteratableSourceDelegate = new IterableSchedulerSourceDelegate();

	public AccessPO(IProtocolHandler<W> protocolHandler, long maxTimeToWaitForNewEventMS, boolean readMetaData) {
		super(protocolHandler, readMetaData);
		this.maxTimeToWaitForNewEventMS = maxTimeToWaitForNewEventMS;
	}

	@Override
	public void setDelay(long delay) {
		iteratableSourceDelegate.setDelay(delay);
	}

	@Override
	public long getDelay() {
		return iteratableSourceDelegate.getDelay();
	}

	@Override
	public void setYieldRate(int yieldRate) {
		iteratableSourceDelegate.setYieldRate(yieldRate);
	}

	@Override
	public int getYieldRate() {
		return iteratableSourceDelegate.getYieldRate();
	}

	@Override
	public int getYieldDurationNanos() {
		return iteratableSourceDelegate.getYieldDurationNanos();
	}

	@Override
	public void setYieldDurationNanos(int yieldDuration) {
		iteratableSourceDelegate.setYieldDurationNanos(yieldDuration);
	}

	@Override
	public boolean tryLock() {
		return iteratableSourceDelegate.tryLock();
	}

	@Override
	public void unlock() {
		iteratableSourceDelegate.unlock();
	}

	@Override
	public synchronized boolean hasNext() {
		if (isDone || !isOpen()) {
			return false;
		}

		if (maxTimeToWaitForNewEventMS > 0 && lastTransfer > 0) {
			if (System.currentTimeMillis() - lastTransfer > maxTimeToWaitForNewEventMS) {
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
			sendError("Exception reading input", e);
		}

		tryPropagateDone();
		return false;
	}

	private boolean doDone() {
		if (!isDone) {
			isDone = true;
			propagateDone();
		}
		return false;
	}

	private void tryPropagateDone() {
		try {
			doDone();
		} catch (Throwable throwable) {
			LOG.error("Exception during propagating done", throwable);
			sendError("Exception during propagating done", throwable);
		}
	}

	@Override
	public synchronized void transferNext() {

		M meta = null;
		try {
			if (!readMetaData) {
				meta = getMetadataInstance();
			}
		} catch (InstantiationException | IllegalAccessException e1) {
			LOG.error("Error creating meta data", e1);
		}

		if (isOpen() && !isDone()) {
			W toTransfer = null;
			try {
				toTransfer = protocolHandler.getNext();
				if (toTransfer == null) {
					// When close is called, do not call propagate done!
					if (!isClosing.get()) {
						isDone = true;
						propagateDone();
					}
				} else {
					if (maxTimeToWaitForNewEventMS > 0) {
						lastTransfer = System.currentTimeMillis();
					}
					if (!readMetaData) {
						toTransfer.setMetadata(meta);
						updateMetadata(toTransfer);
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
	public synchronized void process_open() throws OpenFailedException {
		isDone = false;
		super.process_open();
		isClosing.set(false);
	}

	@Override
	public void process_close() {
		isClosing.set(true);
		super.process_close();
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		// This is not needed. Access Operators with the same name are shared
		// automatically
		return false;
	}

}
