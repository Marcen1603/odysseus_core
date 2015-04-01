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
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractTransportHandler implements ITransportHandler{

	final AbstractTransportHandlerDelegate<?> delegate;
	IExecutor executor;
	
	public AbstractTransportHandler(){
		delegate = new AbstractTransportHandlerDelegate<>(null, null, this,null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractTransportHandler(IProtocolHandler protocolHandler, OptionMap optionsMap) {
		delegate = new AbstractTransportHandlerDelegate<>(protocolHandler.getExchangePattern(), protocolHandler.getDirection(), this, optionsMap);
		protocolHandler.setTransportHandler(this);
		delegate.addListener(protocolHandler);
	}

	@Override
	final public void setSchema(SDFSchema schema) {
		delegate.setSchema(schema);
	}
	
	final public SDFSchema getSchema() {
		return delegate.getSchema();
	}
	
	@Override
	public void setExecutor(IExecutor executor) {
		this.executor = executor;
	}

	@Override
	public IExecutor getExecutor() {
		return executor;
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
	final public void addListener(ITransportHandlerListener listener) {
		delegate.addListener(listener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public void removeListener(ITransportHandlerListener listener) {
		delegate.removeListener(listener);
	}

	@Override
	final public void open() throws UnknownHostException, IOException {
		delegate.open();
	}

	@Override
	final public void close() throws IOException {
		delegate.close();
	}


	@Override
	public ITransportExchangePattern getExchangePattern() {
		return delegate.getExchangePattern();
	}
	
	final public OptionMap getOptionsMap() {
		return delegate.getOptionsMap();
	}
		
	final public void fireProcess(long callerId, ByteBuffer message) {
		delegate.fireProcess(callerId, message);
	}

	final public void fireProcess(ByteBuffer message) {
		delegate.fireProcess(0, message);
	}
	
	final public void fireProcess(InputStream message){
		delegate.fireProcess(message);
	}
	
	final public void fireProcess(String[] message) {
		delegate.fireProcess(message);
	}
	
	final public void fireProcess(IStreamObject<?> message){
		delegate.fireProcess(message);
	}

	final public void fireOnConnect() {
		delegate.fireOnConnect(this);
	}

	final public void fireOnDisconnect() {
		delegate.fireOnDisconnect(this);
	}
	


}
