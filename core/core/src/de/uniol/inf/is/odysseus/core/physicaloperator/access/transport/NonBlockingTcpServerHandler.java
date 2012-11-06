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
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpServer;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic TCP Server
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingTcpServerHandler extends AbstractTransportHandler implements
        IAccessConnectionListener<ByteBuffer> {
    private static final Logger LOG = LoggerFactory.getLogger(NonBlockingTcpServerHandler.class);
    private NioTcpServer        server;
    private String              host;
    private int                 port;

    public NonBlockingTcpServerHandler() {
        super();
    }

    public NonBlockingTcpServerHandler(IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(byte[] message) throws IOException {
        server.write(this, message);
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        NonBlockingTcpServerHandler handler = new NonBlockingTcpServerHandler(protocolHandler);

        try {
            handler.server = NioTcpServer.getInstance();
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        handler.host = options.containsKey("host") ? options.get("host") : "127.0.0.1";
        handler.port = options.containsKey("port") ? Integer.parseInt(options.get("port")) : 8080;
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public String getName() {
        return "TCPServer";
    }

    @Override
    public void process(ByteBuffer buffer) throws ClassNotFoundException {
        super.fireProcess(buffer);
    }

    @Override
    public void done() {

    }

    @Override
    public OutputStream getOutputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            server.bind(address, this);
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            server.bind(address, this);
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processInClose() throws IOException {
        server.close(this);
    }

    @Override
    public void processOutClose() throws IOException {
        server.close(this);
    }
}
