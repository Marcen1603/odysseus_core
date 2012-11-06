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

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractTransportHandler implements ITransportHandler {

    private final List<ITransportHandlerListener> transportHandlerListener = new ArrayList<ITransportHandlerListener>();
    private int                                openCounter              = 0;
    private final ITransportExchangePattern    exchangePattern;

    public AbstractTransportHandler() {
        this.exchangePattern = null;
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
            ((ITransportHandlerListener) l).process(message);
        }
    }

    public void fireOnConnect() {
        for (ITransportHandlerListener l : transportHandlerListener) {
            ((ITransportHandlerListener) l).onConnect(this);
        }
    }

    public void fireOnDisconnect() {
        for (ITransportHandlerListener l : transportHandlerListener) {
            ((ITransportHandlerListener) l).onDisonnect(this);
        }
    }

    
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return this.exchangePattern;
    }

    final synchronized public void open() throws UnknownHostException, IOException {
        if (openCounter == 0) {
            if (getExchangePattern().equals(ITransportExchangePattern.InOnly)
                    || getExchangePattern().equals(ITransportExchangePattern.InOptionalOut)
                    || getExchangePattern().equals(ITransportExchangePattern.InOut)) {
                processInOpen();
            }
            if (getExchangePattern().equals(ITransportExchangePattern.OutOnly)
                    || getExchangePattern().equals(ITransportExchangePattern.OutOptionalIn)
                    || getExchangePattern().equals(ITransportExchangePattern.InOut)) {
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
            if (getExchangePattern().equals(ITransportExchangePattern.InOnly)
                    || getExchangePattern().equals(ITransportExchangePattern.InOptionalOut)
                    || getExchangePattern().equals(ITransportExchangePattern.InOut)) {
                processInClose();
            }
            if (getExchangePattern().equals(ITransportExchangePattern.OutOnly)
                    || getExchangePattern().equals(ITransportExchangePattern.OutOptionalIn)
                    || getExchangePattern().equals(ITransportExchangePattern.InOut)) {
                processOutClose();
            }
        }
    }

    abstract public void processInClose() throws IOException;

    abstract public void processOutClose() throws IOException;

}
