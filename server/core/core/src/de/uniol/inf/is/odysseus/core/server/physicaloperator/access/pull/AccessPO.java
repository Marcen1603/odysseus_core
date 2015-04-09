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

import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataInitializerAdapter;
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
public class AccessPO<W extends IStreamObject<M>, M extends IMetaAttribute>
		extends AbstractIterableSource<W> implements IMetadataInitializer<M, W>, ICommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(AccessPO.class);

	private boolean isDone = false;
	private final long maxTimeToWaitForNewEventMS;
	private long lastTransfer = 0;

	private IProtocolHandler<W> protocolHandler;

	private IMetadataInitializer<M, W> metadataInitializer = new MetadataInitializerAdapter<>();

	// public AccessPO(IProtocolHandler<W> protocolHandler) {
	// this.protocolHandler = protocolHandler;
	// this.maxTimeToWaitForNewEventMS = 0;
	// }

	public AccessPO(IProtocolHandler<W> protocolHandler,
			long maxTimeToWaitForNewEventMS) {
		this.protocolHandler = protocolHandler;
		this.maxTimeToWaitForNewEventMS = maxTimeToWaitForNewEventMS;
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
			sendError("Exception during propagating done", throwable);
		}
	}

	@Override
	public synchronized void transferNext() {

		M meta = null;
		try {
			meta = getMetadataInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			LOG.error("Error creating meta data",e1);
			sendError("Error creating meta data",e1);
		}

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
					toTransfer.setMetadata(meta);
					updateMetadata(toTransfer);
					transfer(toTransfer);
				}
			} catch (Exception e) {
				LOG.error("Error Reading from input", e);
				sendError("Error Reading from input", e);
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
				sendError("Error when opening query " + e.getMessage(), e);
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

	@Override
	public void setMetadataType(Class<M> type) {
		this.metadataInitializer.setMetadataType(type);
	}

	@Override
	public M getMetadataInstance() throws InstantiationException,
			IllegalAccessException {
		return this.metadataInitializer.getMetadataInstance();
	}

	@Override
	public void addMetadataUpdater(IMetadataUpdater<M, W> mFac) {
		this.metadataInitializer.addMetadataUpdater(mFac);
	}

	@Override
	public void updateMetadata(W object) {
		this.metadataInitializer.updateMetadata(object);
	}

	// TODO: This code is duplicated in AccessPO! Move to common base class?
	@Override
	public Command getCommandByName(String commandName, SDFSchema schema) {
		int delimiter = commandName.indexOf('.');
		if (delimiter != -1)
		{
			// Command is for transport, protocol, ... handler
			String target = commandName.substring(0, delimiter);
			String newCommandName = commandName.substring(delimiter+1);
			
			if (target.equalsIgnoreCase("transport"))
			{
				ITransportHandler transportHandler = ((AbstractProtocolHandler<W>) protocolHandler).getTransportHandler();
				if (transportHandler instanceof ICommandProvider)
					return ((ICommandProvider) transportHandler).getCommandByName(newCommandName, schema);
				else
					throw new UnsupportedOperationException("transport handler doesn't implement ICommandProvider");
			}
			else
			if (target.equalsIgnoreCase("protocol"))
			{
				if (protocolHandler instanceof ICommandProvider)
					return ((ICommandProvider) protocolHandler).getCommandByName(newCommandName, schema);
				else
					throw new UnsupportedOperationException("protocol handler doesn't implement ICommandProvider");
			}
			else
				throw new IllegalArgumentException("Unknown target \"" + target + "\" in \"" + commandName + "\"");				
		}
		else
		{
			// Command is for this Operator!
			return null;
		}	
	}	
}
