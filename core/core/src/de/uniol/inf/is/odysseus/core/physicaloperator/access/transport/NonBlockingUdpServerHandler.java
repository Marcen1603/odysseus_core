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
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.AcceptorSelectorHandler;
import de.uniol.inf.is.odysseus.core.connection.CallbackErrorHandler;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioUdpConnection;
import de.uniol.inf.is.odysseus.core.connection.SelectorThread;
import de.uniol.inf.is.odysseus.core.connection.UDPAcceptor;
import de.uniol.inf.is.odysseus.core.connection.UDPAcceptorListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic UDP Server
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingUdpServerHandler extends AbstractTransportHandler implements
        IAccessConnectionListener<ByteBuffer>, IConnectionListener, UDPAcceptorListener {
    private static final Logger          LOG         = LoggerFactory.getLogger(NonBlockingUdpServerHandler.class);
    private SelectorThread               selector;
    // private String host;
    private int                          port;
    private UDPAcceptor                  acceptor;
    private final List<NioUdpConnection> connections = new ArrayList<NioUdpConnection>();
    private int                          readBufferSize;
    private int                          writeBufferSize;

    public NonBlockingUdpServerHandler() {
        super();
    }

    public NonBlockingUdpServerHandler(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        super(protocolHandler);
    }

    @Override
    public void send(byte[] message) throws IOException {
        for (final NioUdpConnection connection : this.connections) {
            connection.write(message);
        }
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        NonBlockingUdpServerHandler handler = new NonBlockingUdpServerHandler(protocolHandler, options);
        handler.readBufferSize = options.containsKey("read") ? Integer.parseInt(options.get("read")) : 1024;
        handler.writeBufferSize = options.containsKey("write") ? Integer.parseInt(options.get("write")) : 1024;
        // handler.host = options.containsKey("host") ? options.get("host") :
        // "127.0.0.1";
        handler.port = options.containsKey("port") ? Integer.parseInt(options.get("port")) : 8080;
        try {
            handler.selector = SelectorThread.getInstance();
            handler.acceptor = new UDPAcceptor(handler.port, handler.selector, handler);
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public String getName() {
        return "UDPServer";
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
        try {
            this.acceptor.open();
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        try {
            this.acceptor.open();
        }
        catch (IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processInClose() throws IOException {
        this.acceptor.close();
    }

    @Override
    public void processOutClose() throws IOException {
        this.acceptor.close();
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
    public void socketDisconnected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void socketException(Exception ex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void socketConnected(AcceptorSelectorHandler acceptor, DatagramChannel channel) {
        try {
            channel.socket().setReceiveBufferSize(this.readBufferSize);
            channel.socket().setSendBufferSize(this.writeBufferSize);
            final NioUdpConnection connection = new NioUdpConnection(channel, this.selector, this);
            this.connections.add(connection);
            selector.addChannelInterest(channel, SelectionKey.OP_READ, new CallbackErrorHandler() {
                public void handleError(final Exception ex) {
                    socketException(ex);
                }
            });
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public void socketError(AcceptorSelectorHandler acceptor, Exception ex) {
        // TODO Auto-generated method stub

    }
}
