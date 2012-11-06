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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

public class ReceiverPO<R, W> extends AbstractSource<W> implements
		 ITransferHandler<W> {

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ReceiverPO.class);
		}
		return _logger;
	}

	protected IObjectHandler<W> objectHandler;
	private boolean opened;

	IAccessConnectionHandler<R> accessHandler;
	private IInputDataHandler<R, W> inputDataHandler;

	private IProtocolHandler<W> protocolHandler;

	public ReceiverPO(IObjectHandler<W> objectHandler,
			IInputDataHandler<R, W> inputDataHandler,
			IAccessConnectionHandler<R> accessHandler) {
		super();
		this.objectHandler = objectHandler;
		this.inputDataHandler = inputDataHandler;
		this.accessHandler = accessHandler;
		setName("ReceiverPO " + accessHandler);
		this.opened = false;
	}

	public ReceiverPO(IProtocolHandler<W> protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	public ReceiverPO() {
	}

	public void setProtocolHandler(IProtocolHandler<W> ph) {
		this.protocolHandler = ph;
	}

	@SuppressWarnings("unchecked")
	public ReceiverPO(ReceiverPO<R, W> other) {
		super();
		objectHandler = (IObjectHandler<W>) other.objectHandler.clone();
		inputDataHandler = other.inputDataHandler.clone();
		accessHandler = (IAccessConnectionHandler<R>) other.clone();

		opened = other.opened;

		throw new IllegalArgumentException("CLONE CURRENTLY NOT SUPPORTED.");
	}

//	@Override
//	public boolean isOpened() {
//		return super.isOpen();
//	}

	@Override
	public synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				if (protocolHandler != null) {
					protocolHandler.open();
				} //else {
//					objectHandler.clear();
//					inputDataHandler.init();
//					accessHandler.open(this);
//				}
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

				if (protocolHandler != null) {
					protocolHandler.close();
				}
//				else {
//					accessHandler.close(this);
//					inputDataHandler.done();
//					objectHandler.clear();
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	@Override
//	public void done() {
//		propagateDone();
//	}

	@Override
	public synchronized void transfer(W toTransfer) {
		super.transfer(toTransfer);
	}

//	@Override
//	public void process(R object) throws ClassNotFoundException {
//		if (isOpen()) {
//			inputDataHandler
//					.process(object, objectHandler, accessHandler, this);
//		}
//	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ReceiverPO)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		ReceiverPO bbrpo = (ReceiverPO) ipo;
		if (this.objectHandler.equals(bbrpo.objectHandler)
				&& this.accessHandler.equals(bbrpo.accessHandler)) {
			return true;
		}
		return false;
	}

	@Override
	public AbstractSource<W> clone() {
		return new ReceiverPO<R, W>(this);
	}

}
