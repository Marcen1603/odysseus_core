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
import java.io.OutputStream;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * An Interface for all TransportHandler
 *
 * @author Christian Kuka, Marco Grawunder
 *
 */

public interface ITransportHandler {

	/**
	 * This method is used to create a new instance of the transport handler
	 *
	 * @param protocolHandler The connected protocol handler
	 * @param options A set of key-value pairs, can be used to configure the handler
	 * @return
	 */
    ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options);

    /**
     * This method is used to retrieve the name of the handler, is used for registering
     * @return
     */
    String getName();

	/**
	 * Is called from the framework, when the handler should be initialized and open the connection to the source
	 * @throws UnknownHostException
	 * @throws IOException
	 */
    void open() throws UnknownHostException, IOException;

	void start();

	void processInStart();

	void processInOpen() throws IOException;

	void processOutOpen() throws IOException;

    /**
     * Is called from the framework, when the handler should close the connection to the source
     * @throws IOException
     */
    void close() throws IOException;

    void processInClose() throws IOException;

	void processOutClose() throws IOException;

    /**
     * called from the framework to determine, if the transport handler will not deliver any elements (typically for
     * a file based source)
     * @return
     */
    boolean isDone();


    /**
     * This message is used, when the transport handler works as sender
     * @param message
     * @throws IOException
     */
    void send(byte[] message) throws IOException;
    
    /**
     * This message is used, when the transport handler works as sender
     * @param message
     * @throws IOException
     */
    void send(String message, boolean addNewline) throws IOException;

    /**
     * This message is used, when the transport handler works as sender
     * @param message
     * @throws IOException
     */
    void send(Object message) throws IOException;

    /**
     * Send punctuations to transport handler
     * @param punctuation
     */
    void processPunctuation(IPunctuation punctuation);

    /**
     * If the transport handler has a schema it can be provided here
     * @param schema
     */
    SDFSchema getSchema();


    /**
     * If the transport handler needs to know the schema, it will be called here
     * @param schema
     */
    void setSchema(SDFSchema schema);

    /**
     * This method will be called from the protocol handler to create a pull based
     * transport handler in a source
     *
     * @return A new input stream delivering the values to the protocol handler
     */
    InputStream getInputStream();

    /**
     * This method will be called from the protocol handler to create a pull based
     * transport handler in a sink
     *
     * @return A new output stream where the protocol handler writes its output to
     */

    OutputStream getOutputStream();

    /**
     * What kind of exchange pattern does this transport handler provide
     *
     * @return the Exchange Pattern (InOnly, RobustInOnly, InOut, InOptionalOut, OutOnly, RobustOutOnly, OutIn, OutOptionalIn)
     */
    ITransportExchangePattern getExchangePattern();


    /**
     * For query sharing purposes. If two sources contain the same set of handlers they can be shared
     * @param other
     * @return
     */
	boolean isSemanticallyEqual(ITransportHandler other);

    @SuppressWarnings("rawtypes")
    /**
     * Add a TransportHandlerListener
     * @param listener
     */
	void addListener(ITransportHandlerListener listener);

    @SuppressWarnings("rawtypes")
	/**
	 * Remove a TransportHandlerListener
	 * @param listener
	 */
    void removeListener(ITransportHandlerListener listener);

    void setExecutor(IExecutor executor);

    IExecutor getExecutor();


    /**
     * option update
     */

    void updateOption(String key, String value);


}
