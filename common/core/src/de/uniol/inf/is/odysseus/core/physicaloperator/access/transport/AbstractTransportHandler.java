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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractTransportHandler implements ITransportHandler{

	final AbstractTransportHandlerDelegate<?> delegate;
	
	public AbstractTransportHandler(){
		delegate = new AbstractTransportHandlerDelegate<>(null, this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractTransportHandler(IProtocolHandler protocolHandler) {
		delegate = new AbstractTransportHandlerDelegate<>(protocolHandler.getExchangePattern(), this);
		protocolHandler.setTransportHandler(this);
		delegate.addListener(protocolHandler);
	}

	@Override
	public void setSchema(SDFSchema schema) {
		delegate.setSchema(schema);
	}
	
	public SDFSchema getSchema() {
		return delegate.getSchema();
	}

	@Override
	public boolean isDone() {
		return false;
	}

	
	@Override
	public boolean isSemanticallyEqual(ITransportHandler other) {
		if(!this.getExchangePattern().equals(other.getExchangePattern())) {
			return false;
		} else if(!this.getName().equals(other.getName())) {
			return false;
		}
		return isSemanticallyEqualImpl(other);
	}
	
	public abstract boolean isSemanticallyEqualImpl(ITransportHandler other);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addListener(ITransportHandlerListener listener) {
		delegate.addListener(listener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void removeListener(ITransportHandlerListener listener) {
		delegate.removeListener(listener);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		delegate.open();
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}


	@Override
	public ITransportExchangePattern getExchangePattern() {
		return delegate.getExchangePattern();
	}
	
	public Map<String, String> getOptionsMap() {
		return delegate.getOptionsMap();
	}
	
	public void setOptionsMap(Map<String, String> options) {
		delegate.setOptionsMap(options);
	}
	
	
	public void fireProcess(ByteBuffer message) {
		delegate.fireProcess(message);
	}

	public void fireProcess(String[] message) {
		delegate.fireProcess(message);
	}

	public void fireOnConnect() {
		delegate.fireOnConnect(this);
	}

	public void fireOnDisconnect() {
		delegate.fireOnDisconnect(this);
	}
	


}
