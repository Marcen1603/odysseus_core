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
import java.net.UnknownHostException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
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
    
    private Map<String, String> optionsMap;

    public AbstractProtocolHandler() {
        direction = null;
        access = null;
    }

    public AbstractProtocolHandler(ITransportDirection direction, IAccessPattern access) {
        this.direction = direction;
        this.access = access;
    }

    final public ITransportHandler getTransportHandler() {
        return transportHandler;
    }

    final public IDataHandler<T> getDataHandler() {
        return dataHandler;
    }

    final protected void setDataHandler(IDataHandler<T> dataHandler) {
        this.dataHandler = dataHandler;
    }

    final protected ITransferHandler<T> getTransfer() {
        return transfer;
    }

    @Override
	public final void setTransfer(ITransferHandler<T> transfer) {
        this.transfer = transfer;
    }

    @Override
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
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
	}

	@Override
	public void onConnect(ITransportHandler caller) {
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
	}
	
	@Override
	public void process(String[] message) {
		throw new RuntimeException("Sorry. Currently not implemented!");
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
    
    @Override
    public boolean isDone() {
    	return transportHandler.isDone();
    }
    
    @Override
    public void writePunctuation(IPunctuation punctuation) throws IOException {
    	// do noting
    }
    
    /**
     * This method is supposed to retrieve the options for an instance,
     * which were used during the call of {@link IProtocolHandler#createInstance(ITransportDirection, IAccessPattern, Map, IDataHandler, ITransferHandler)}
     * based on the current configuration. This is useful serialising different ProtocolHandler-instances.
     * CANNOT be used for comparisons to check if two AbstractProtocolHandler-instances are semantically equivalent.
     */
    public Map<String, String> getOptionsMap() {
    	return optionsMap;
    }
    
	public void setOptionsMap(Map<String, String> options) {
		this.optionsMap = options;
	}
    
    /**
     * checks if another IProtocolHandler-instance is semantically equivalent to this one
     */
    @Override
    public boolean isSemanticallyEqual(IProtocolHandler<?> o) {
    	if(!(o instanceof AbstractProtocolHandler)) {
    		return false;
    	}
    	AbstractProtocolHandler<?> other = (AbstractProtocolHandler<?>)o;
    	if(!this.direction.equals(other.getDirection())) {
    		return false;
    	} else if(!this.access.equals(other.getAccess())) {
    		return false;
    	} else if(!this.dataHandler.isSemanticallyEqual(other.getDataHandler())) {
    		return false;
    	} else if(!this.transportHandler.isSemanticallyEqual(other.getTransportHandler())) {
    		return false;
    	}
    	return isSemanticallyEqualImpl(other);
    }
    
    public abstract boolean isSemanticallyEqualImpl(IProtocolHandler<?> other);
}
