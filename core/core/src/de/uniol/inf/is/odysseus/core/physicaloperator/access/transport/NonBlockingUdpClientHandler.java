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

import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpConnection;
import de.uniol.inf.is.odysseus.core.connection.NioUdpServer;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic UDP Client
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingUdpClientHandler extends AbstractTransportHandler implements
        IAccessConnectionListener<ByteBuffer>, IConnectionListener {
    private static final Logger LOG = LoggerFactory.getLogger(NonBlockingUdpClientHandler.class);
    private NioUdpServer        client;
    private String              host;
    private int                 port;

    public NonBlockingUdpClientHandler() {
        super();
    }

    public NonBlockingUdpClientHandler(IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(byte[] message) throws IOException {
        client.write(this, message);
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        NonBlockingUdpClientHandler handler = new NonBlockingUdpClientHandler(protocolHandler);
        int readBufferSize = options.containsKey("read") ? Integer.parseInt(options.get("read")) : 1024;
        int writeBufferSize = options.containsKey("write") ? Integer.parseInt(options.get("write")) : 1024;
        try {
            handler.client = NioUdpServer.getInstance(readBufferSize, writeBufferSize);
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
        return "UDPClient";
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
            client.connect(address, this);
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            client.connect(address, this);
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processInClose() throws IOException {
        client.close(this);
    }

    @Override
    public void processOutClose() throws IOException {
        client.close(this);
    }

    @Override
    public void notify(IConnection connection, ConnectionMessageReason reason) {
        switch (reason) {
            case ConnectionAbort:
                super.fireOnDisconnect();
                break;
            case ConnectionClosed:
                super.fireOnDisconnect();
                break;
            case ConnectionRefused:
                super.fireOnDisconnect();
                break;
            case ConnectionOpened:
                super.fireOnConnect();
                break;
            default:
                break;
        }
    }

    @Override
    public void socketDisconnected(NioTcpConnection nioTcpConnection) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void socketException(NioTcpConnection nioTcpConnection, Exception ex) {
        // TODO Auto-generated method stub
        
    }

}
