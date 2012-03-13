/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

abstract public class AbstractByteBufferReceiverPO<W> extends AbstractSource<W> implements IAccessConnectionListener {

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractByteBufferReceiverPO.class);
		}
		return _logger;
	}

	protected IObjectHandler<W> objectHandler;
	private boolean opened;
	
	final IAccessConnection accessHandler;


	public AbstractByteBufferReceiverPO(IObjectHandler<W> objectHandler, IAccessConnection accessHandler) {
		super();
		this.objectHandler = objectHandler;
		this.accessHandler = accessHandler;
		setName("ByteBufferReceiverPO " + accessHandler);
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public AbstractByteBufferReceiverPO(AbstractByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super();
		objectHandler = (IObjectHandler<W>) byteBufferReceiverPO.objectHandler.clone();
		accessHandler = (IAccessConnection) byteBufferReceiverPO.clone();
		opened = byteBufferReceiverPO.opened;
	}

	@Override
	public boolean isOpened() {
		return super.isOpen();
	}

	@Override
	public synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				objectHandler.clear();
				accessHandler.open(this);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	public void process_close() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false; // Do not read any data anymore
				accessHandler.close(this);
				objectHandler.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void done() {
		propagateDone();
	}

  


	protected synchronized void transfer()  {

		W toTrans = null;
		try {
			toTrans = objectHandler.create();
		} catch (Exception e) {
			getLogger().error(e.getMessage() + ". Terminating Processing ...");
			e.printStackTrace();
			process_done();
			process_close();
			return;
		}
		// logger.debug("Transfer "+toTrans);
		transfer(toTrans);
	}
	
	@Override
	abstract public void process(ByteBuffer buffer);

	@Override
	public String getSourceName() {
		return toString();
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AbstractByteBufferReceiverPO)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		AbstractByteBufferReceiverPO bbrpo = (AbstractByteBufferReceiverPO) ipo;
		if (this.objectHandler.equals(bbrpo.objectHandler) && this.accessHandler.equals(bbrpo.accessHandler)) {
			return true;
		}
		return false;
	}


}
