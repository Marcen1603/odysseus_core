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

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerListener;

public interface IProtocolHandler<T> extends ITransportHandlerListener<T> {

	/**
	 * Creates a new protocol handler
	 * @param direction is this handler used in a source (IN) or in a sink (OUT)
	 * @param access which kind of access pattern is supported (  PUSH,  PULL,  ROBUST_PUSH,  ROBUST_PULL)
	 * @param options set of options as key value pairs
	 * @param dataHandler the data handler thats connected to the protocol handler
	 * @return
	 */
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access,
            OptionMap options, IDataHandler<T> dataHandler);

    /**
     * The unique name of the protocol handler
     * @return
     */
    String getName();
    
    /**
     * The exchange pattern of the underlying transport handler 
     * @return
     */
    public ITransportExchangePattern getExchangePattern();

	
	/**
	 * Is called from the framework, when the query is started
	 * @throws UnknownHostException
	 * @throws IOException
	 */
    public void open() throws UnknownHostException, IOException;
    
    /**
     * Is called from the framework, when the query is stopped
     * @throws IOException
     */
    public void close() throws IOException;

    /**
     * in case of pull based access,
     * @return true, if another element is available
     * @throws IOException
     */
    public boolean hasNext() throws IOException;

    /**
     * in case of pull based access
     * @return the next element
     * @throws IOException
     */
    public T getNext() throws IOException;

    /**
     * in case of pull based access
     * @return true if the source has no more elements to deliver (e.g. a file reach end of file)
     */
	public boolean isDone();

    
    /**
     * in cased of OUT direction, write this object and send it to the transport handler
     * @param object
     * @throws IOException
     */
    public void write(T object) throws IOException;

    /**
     * in cased of OUT direction, send the punctuation to the transport handler
     * @param punctuation
     * @throws IOException
     */
    public void writePunctuation(IPunctuation punctuation) throws IOException;
    
    
    /**
     * Assigns the transport handler to the protocol handler
     * @param transportHandler
     */
    void setTransportHandler(ITransportHandler transportHandler);
    
    
    /**
     * In push based szenarios: The the class, that should receive the data
     * 
     * @param transfer
     */
    void setTransfer(ITransferHandler<T> transfer);
    
    /**
     * Retrieves direction IN or OUT
     * @return
     */
    ITransportDirection getDirection();

    /**
     * Retrieves the access pattern
     * @return
     */
    IAccessPattern getAccessPattern();

    
    /**
     * Needed for query sharing
     * @param other
     * @return
     */
	public boolean isSemanticallyEqual(IProtocolHandler<?> other);

}
