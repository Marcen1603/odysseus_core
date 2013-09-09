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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractTransportHandler implements ITransportHandler {

	private final List<ITransportHandlerListener> transportHandlerListener = new ArrayList<ITransportHandlerListener>();
	private int openCounter = 0;
	private final ITransportExchangePattern exchangePattern;

	public AbstractTransportHandler() {
		this.exchangePattern = null;
	}

	public AbstractTransportHandler(ITransportExchangePattern exchangePattern) {
		this.exchangePattern = exchangePattern;
	}

	public AbstractTransportHandler(IProtocolHandler<?> protocolHandler) {
		this.exchangePattern = protocolHandler.getExchangePattern();
		protocolHandler.setTransportHandler(this);
		addListener(protocolHandler);
	}

	@Override
	public void addListener(ITransportHandlerListener listener) {
		this.transportHandlerListener.add(listener);
	}

	@Override
	public void removeListener(ITransportHandlerListener listener) {
		this.transportHandlerListener.remove(listener);
	}

	public void fireProcess(ByteBuffer message) {
		for (ITransportHandlerListener l : transportHandlerListener) {
			// TODO: flip() erases the contents of the message if
			// it was already flipped or just created...
			// In other words: This method expects that the byte buffer
			// is not fully prepared
			message.flip();
			l.process(message);
		}
	}

	public void fireProcess(String[] message) {
		for (ITransportHandlerListener l : transportHandlerListener) {
			l.process(message);
		}
	}

	public void fireOnConnect() {
		for (ITransportHandlerListener l : transportHandlerListener) {
			l.onConnect(this);
		}
	}

	public void fireOnDisconnect() {
		for (ITransportHandlerListener l : transportHandlerListener) {
			l.onDisonnect(this);
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return this.exchangePattern;
	}

	@Override
	final synchronized public void open() throws UnknownHostException,
			IOException {
		if (openCounter == 0) {
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.InOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.InOptionalOut) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				processInOpen();
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				processOutOpen();
			}
		}
		openCounter++;
	}

	abstract public void processInOpen() throws IOException;

	abstract public void processOutOpen() throws IOException;

	@Override
	final synchronized public void close() throws IOException {
		openCounter--;
		if (openCounter == 0) {
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.InOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.InOptionalOut) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				processInClose();
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				processOutClose();
			}
		}
	}

	abstract public void processInClose() throws IOException;

	abstract public void processOutClose() throws IOException;

	@Override
	public boolean isDone() {
		return false;
	}
	
    /**
     * This method is supposed to retrieve the options for an instance, which were used during the call of {@link ITransportHandler#createInstance(IProtocolHandler, Map))}
     * based on the current configuration. This is useful for comparing and serialising different TransportHandler-instances.
     * @return
     */
	public Map<String, String> getOptionsMap() {
		//return optionsMap;
		// TODO: change the handling of the options to be the same as for the ProtocolHandlers, cleanup child classes
		return getOptions();
	}
	
	public abstract Map<String, String> getOptions();
	
	public void setOptionsMap(Map<String, String> options) {
		//this.optionsMap = options;
	}
	
	@Override
	public boolean isSemanticallyEqual(ITransportHandler other) {
		if(!this.exchangePattern.equals(other.getExchangePattern())) {
			return false;
		} else if(!this.getName().equals(other.getName())) {
			return false;
		}
		return false;
		// TODO: Implement in child classes
		//return isSemanticallyEqualImpl(other);
	}
	
	//abstract boolean isSemanticallyEqualImpl(ITransportHandler other);

}
