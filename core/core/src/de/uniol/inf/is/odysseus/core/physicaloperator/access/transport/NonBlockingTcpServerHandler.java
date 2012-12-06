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
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.AcceptorSelectorHandler;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpConnection;
import de.uniol.inf.is.odysseus.core.connection.SelectorThread;
import de.uniol.inf.is.odysseus.core.connection.TCPAcceptor;
import de.uniol.inf.is.odysseus.core.connection.TCPAcceptorListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic TCP Server
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingTcpServerHandler extends AbstractTransportHandler implements
        IAccessConnectionListener<ByteBuffer>, IConnectionListener, TCPAcceptorListener {
    private static final Logger          LOG         = LoggerFactory.getLogger(NonBlockingTcpServerHandler.class);
    private SelectorThread               selector;
    // private String host;
    private int                          port;
    private TCPAcceptor                  acceptor;
    private final List<NioTcpConnection> connections = new ArrayList<NioTcpConnection>();
    private int                          readBufferSize;
    private int                          writeBufferSize;

    public NonBlockingTcpServerHandler() {
        super();
    }

    public NonBlockingTcpServerHandler(final IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(final byte[] message) throws IOException {
        for (final NioTcpConnection connection : this.connections) {
            connection.write(message);
        }
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final NonBlockingTcpServerHandler handler = new NonBlockingTcpServerHandler(protocolHandler);
        handler.readBufferSize = options.containsKey("read") ? Integer.parseInt(options.get("read")) : 1024;
        handler.writeBufferSize = options.containsKey("write") ? Integer.parseInt(options.get("write")) : 1024;
        // handler.host = options.containsKey("host") ? options.get("host") :
        // "127.0.0.1";
        handler.port = options.containsKey("port") ? Integer.parseInt(options.get("port")) : 8080;
        try {
            handler.selector = SelectorThread.getInstance();
            handler.acceptor = new TCPAcceptor(handler.port, handler.selector, handler);
        }
        catch (final IOException e) {
            NonBlockingTcpServerHandler.LOG.error(e.getMessage(), e);
        }

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
    public void process(final ByteBuffer buffer) throws ClassNotFoundException {
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
        catch (final IOException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        try {
            this.acceptor.open();
        }
        catch (final IOException e) {
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
    public void notify(final IConnection connection, final ConnectionMessageReason reason) {
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
    public void socketConnected(final AcceptorSelectorHandler acceptor, final SocketChannel channel) {
        try {
            channel.socket().setReceiveBufferSize(this.readBufferSize);
            channel.socket().setSendBufferSize(this.writeBufferSize);
            final NioTcpConnection connection = new NioTcpConnection(channel, this.selector, this);
            this.connections.add(connection);
            connection.resumeReading();
        }
        catch (IOException | ClassNotFoundException e) {
            NonBlockingTcpServerHandler.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void socketError(final AcceptorSelectorHandler acceptor, final Exception ex) {
        NonBlockingTcpServerHandler.LOG.error(ex.getMessage(), ex);
    }

    @Override
    public void socketDisconnected() {
        super.fireOnDisconnect();
    }

    @Override
    public void socketException(final Exception ex) {
        NonBlockingTcpServerHandler.LOG.error(ex.getMessage(), ex);
    }

}
