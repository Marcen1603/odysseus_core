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
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


abstract public class AbstractProtocolHandler<T> implements IProtocolHandler<T> {
	private final ITransportDirection direction;
    private final IAccessPattern      access;
    private ITransportHandler         transportHandler;
    private final IDataHandler<T>           dataHandler;
    private ITransferHandler<T>       transfer;
    
    private IExecutor executor;
    private SDFSchema schema;
    
    protected final OptionMap optionsMap;

    public AbstractProtocolHandler() {
        direction = null;
        access = null;
        dataHandler = null;
        optionsMap = null;
    }

    public AbstractProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> datahandler, OptionMap optionsMap) {
        this.direction = direction;
        this.access = access;
        this.dataHandler = datahandler;
        this.optionsMap = optionsMap;
    }
    
    @Override
    public IExecutor getExecutor() {
		return executor;
	}
    
    @Override
    public void setExecutor(IExecutor executor) {
		this.executor = executor;
	}
    
    @Override
    public SDFSchema getSchema() {
    	return schema;
    }
    
    @Override
    public void setSchema(SDFSchema schema) {
    	this.schema = schema;
    }

    final public ITransportHandler getTransportHandler() {
        return transportHandler;
    }

    final public IDataHandler<T> getDataHandler() {
        return dataHandler;
    }

    final protected ITransferHandler<T> getTransfer() {
        return transfer;
    }

    @Override
	public final void setTransportHandler(ITransportHandler transportHandler) {
        this.transportHandler = transportHandler;
    }
    
    @Override
    public void setTransfer(ITransferHandler<T> transfer) {
    	if (this.transfer == null){
    		this.transfer = transfer;
    	}else{
    		throw new IllegalArgumentException("Transfer can only be set once");
    	}
    }

    @Override
    public ITransportDirection getDirection() {
        return this.direction;
    }

    @Override
    public IAccessPattern getAccessPattern() {
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
		getTransfer().transfer(getDataHandler().readData(message));
	}
	
	@Override
	public void process(long callerId, ByteBuffer message) {
		getTransfer().transfer(getDataHandler().readData(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(IStreamObject<?> message) {
		getTransfer().transfer((T)message);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(IStreamObject<?> message, int port) {
		getTransfer().transfer((T)message, port);
	}
	

	@Override
	public void process(T m) {
		getTransfer().transfer(getDataHandler().readData(m));		
	}
	
	@Override
	public void process(InputStream message) {
		try {
			getTransfer().transfer(getDataHandler().readData(message));
		} catch (IOException e) {
			e.printStackTrace();
		}		
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
     * which were used during the call of {@link IProtocolHandler#createInstance(ITransportDirection, IAccessPattern, OptionMap, IDataHandler)}
     * based on the current configuration. This is useful serialising different ProtocolHandler-instances.
     * CANNOT be used for comparisons to check if two AbstractProtocolHandler-instances are semantically equivalent.
     */
    public OptionMap getOptionsMap() {
    	return optionsMap;
    }
    
//	public void setOptionsMap(Map<String, String> options) {
//		this.optionsMap = options;
//	}
    
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
    	} else if(!this.access.equals(other.getAccessPattern())) {
    		return false;
    	} else if(!this.dataHandler.isSemanticallyEqual(other.getDataHandler())) {
    		return false;
    	} else if(!this.transportHandler.isSemanticallyEqual(other.getTransportHandler())) {
    		return false;
    	}
    	return isSemanticallyEqualImpl(other);
    }
    
    @Override
    public ProtocolHandlerAction getAction() {
    	return null;
    }
    
    public abstract boolean isSemanticallyEqualImpl(IProtocolHandler<?> other);
}
