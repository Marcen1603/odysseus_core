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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractProtocolHandler<T> implements IProtocolHandler<T> {

	private ITransportHandler transportHandler;
	private IDataHandler<T> dataHandler;
	private ITransferHandler<T> transfer;

	
	final protected ITransportHandler getTransportHandler() {
		return transportHandler;
	}
	
	final protected void setTransportHandler(ITransportHandler transportHandler) {
		this.transportHandler = transportHandler;
	}
	
	final protected IDataHandler<T> getDataHandler() {
		return dataHandler;
	}
	
	final protected void setDataHandler(IDataHandler<T> dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	final protected ITransferHandler<T> getTransfer() {
		return transfer;
	}
	
	final protected void setTransfer(ITransferHandler<T> transfer) {
		this.transfer = transfer;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		return false;
	}

	@Override
	public T getNext() throws IOException {
		return null;
	}
	
}
