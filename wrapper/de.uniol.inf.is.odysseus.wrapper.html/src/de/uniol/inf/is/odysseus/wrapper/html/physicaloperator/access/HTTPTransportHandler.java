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
package de.uniol.inf.is.odysseus.wrapper.html.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HTTPTransportHandler extends AbstractTransportHandler {
    /** Logger */
    private Logger            LOG    = LoggerFactory.getLogger(HTTPTransportHandler.class);
    /** HTTP Client */
    private final HttpClient  client = new HttpClient();
    /** Pipe in and output for data transfer */
    private PipedInputStream  pipeInput;
    private PipedOutputStream pipeOutput;
    private String            uri;
    @SuppressWarnings("unused")
    private IAccessPattern    transportPattern;

    /**
 * 
 */
    public HTTPTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public HTTPTransportHandler(IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(byte[] message) throws IOException {
        GetMethod request = new GetMethod(message.toString());
        this.client.executeMethod(request);
        try {
            this.pipeOutput.write(request.getResponseBody());
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        HTTPTransportHandler handler = new HTTPTransportHandler(protocolHandler);
        handler.uri = options.get("uri");
        handler.pipeInput = new PipedInputStream();
        try {
            handler.pipeOutput = new PipedOutputStream(handler.pipeInput);
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        return this.pipeInput;
    }

    @Override
    public String getName() {
        return "HTTP";
    }

    @Override
    public OutputStream getOutputStream() {
        return this.pipeOutput;
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {
        GetMethod request = new GetMethod(this.uri);
        this.client.executeMethod(request);
        try {
            this.pipeOutput.write(request.getResponseBody());
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void processInClose() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void processOutClose() throws IOException {
        // TODO Auto-generated method stub

    }

}
