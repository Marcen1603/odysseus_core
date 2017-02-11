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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataInitializerAdapter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

public class ReceiverPO<W extends IStreamObject<M>, M extends IMetaAttribute> extends AbstractSource<W>
		implements ITransferHandler<W>, IMetadataInitializer<M, W> {

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ReceiverPO.class);
		}
		return _logger;
	}

	private boolean opened;

	protected IProtocolHandler<W> protocolHandler;
	protected final boolean readMetaData;

	private IMetadataInitializer<M, W> metadataInitializer = new MetadataInitializerAdapter<>();
	private IMetadataUpdater<M, W> metadataUpdater;

	public ReceiverPO(IProtocolHandler<W> protocolHandler, boolean readMetaData) {
		this.protocolHandler = protocolHandler;
		this.protocolHandler.setTransfer(this);
		this.readMetaData = readMetaData;
	}

	public IProtocolHandler<W> getProtocolHandler() {
		return protocolHandler;
	}

	public ReceiverPO() {
		readMetaData = false;
	}

	public void setProtocolHandler(IProtocolHandler<W> ph) {
		this.protocolHandler = ph;
	}

	// @Override
	public boolean isOpened() {
		return super.isOpen();
	}

	@Override
	public synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				protocolHandler.open();
				opened = true;
			} catch (Exception e) {
				sendError("Error Opening " + e.getMessage(), e);
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	protected void process_start() throws StartFailedException {
		protocolHandler.start();
	}

	@Override
	public void process_close() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false; // Do not read any data anymore
				protocolHandler.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// @Override
	public void done() {
		propagateDone();
	}

	@Override
	final public synchronized void transfer(W object, int sourceOutPort) {
		if (opened) {
			try {
				if (!readMetaData) {
					object.setMetadata(getMetadataInstance());
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if (!readMetaData) {
				updateMetadata(object);
			}
			super.transfer(object, sourceOutPort);
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ReceiverPO)) {
			return false;
		}

		// @SuppressWarnings("rawtypes")
		// ReceiverPO bbrpo = (ReceiverPO) ipo;

		// TODO: Implement me
		return false;
	}

	@Override
	public void setMetadataType(IMetaAttribute type) {
		this.metadataInitializer.setMetadataType(type);
	}

	@Override
	public M getMetadataInstance() throws InstantiationException, IllegalAccessException {
		return this.metadataInitializer.getMetadataInstance();
	}

	@Override
	public void addMetadataUpdater(IMetadataUpdater<M, W> mFac) {
		this.metadataInitializer.addMetadataUpdater(mFac);
		this.metadataUpdater = mFac;
	}

	@Override
	public void updateMetadata(W object) {
		this.metadataInitializer.updateMetadata(object);
	}

	public IMetadataUpdater<M, W> getMetadataFactory() {
		return this.metadataUpdater;
	}

}
