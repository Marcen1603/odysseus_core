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
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractProtocolHandler<T> implements IProtocolHandler<T> {
    private final ITransportDirection direction;
    private final IAccessPattern      access;
    private ITransportHandler         transportHandler;
    private IDataHandler<T>           dataHandler;
    private ITransferHandler<T>       transfer;

    public AbstractProtocolHandler() {
        direction = null;
        access = null;
    }

    public AbstractProtocolHandler(ITransportDirection direction, IAccessPattern access) {
        this.direction = direction;
        this.access = access;
    }

    final protected ITransportHandler getTransportHandler() {
        return transportHandler;
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

    public final void setTransportHandler(ITransportHandler transportHandler) {
        this.transportHandler = transportHandler;
    }

    @Override
    public ITransportDirection getDirection() {
        return this.direction;
    }

    @Override
    public IAccessPattern getAccess() {
        return this.access;
    }

    @Override
    public boolean hasNext() throws IOException {
        throw new RuntimeException("Sorry. Currently not implemented");
    }

    @Override
    public T getNext() throws IOException {
        throw new RuntimeException("Sorry. Currently not implemented");
    }

    @Override
    public void write(T object) throws IOException {
        throw new RuntimeException("Sorry. Currently not implemented");
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }
}
