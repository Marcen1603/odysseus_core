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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class BlockingBufferPO<T extends IStreamObject<?>> extends BufferPO<T> {

	Logger LOG = LoggerFactory.getLogger(BlockingBufferPO.class);

	final long maxBufferSize;

	private boolean isRunning = false;

	public BlockingBufferPO(long maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	@Override
	public void transferNext() {
		super.transferNext();
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		isRunning = true;
	}

	@Override
	protected void process_close() {
		isRunning = false;
		LOG.debug("Closing at size " + buffer.size());
		super.process_close();
		synchronized (this) {
			this.notifyAll();
		}
		LOG.debug("Closing done");
	}

	@Override
	protected void process_done(int port) {
		isRunning = false;
		super.process_done(port);
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	protected void process_next(T object, int port) {
		synchronized (this) {
			while (size() >= maxBufferSize && isRunning) {
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (isRunning) {
			super.process_next(object, port);
		}
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof BlockingBufferPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		BlockingBufferPO<T> b = (BlockingBufferPO<T>) ipo;
		if (b.maxBufferSize != this.maxBufferSize) {
			return false;
		}

		return super.process_isSemanticallyEqual(ipo);
	}

	public long getMaxBufferSize() {
		return maxBufferSize;
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = super.getKeyValues();
		map.put("isBlocked", (size() == maxBufferSize) + "");
		return map;
	}
}
