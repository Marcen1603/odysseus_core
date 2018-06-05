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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioConnection;
import de.uniol.inf.is.odysseus.core.physicaloperator.CloseFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class NonBlockingTcpHandler extends AbstractTransportHandler implements IConnectionListener, IAccessConnectionListener<ByteBuffer> {
    static final Logger LOG = LoggerFactory.getLogger(NonBlockingTcpHandler.class);

    private static final String NAME = "NonBlockingTcp";

    // The host name
    private static final String HOST = "host";
    // The port
    private static final String PORT = "port";
    // Auto reconnect on disconnect
    private static final String AUTOCONNECT = "autoconnect";
    // The initialization command
    private static final String INITIALIZE = "init";
    // Deprecated
    private static final String INITIALIZE_ALIAS = "logininfo";
    // Deprecated
    private static final String PASSWORD = "password";
    // Deprecated
    private static final String USERNAME = "user";

    static NioConnection nioConnection = null;
    private String host;
    private int port;
    private byte[] initializeCommand;

    private boolean autoconnect;
    @SuppressWarnings("unused")
    private boolean open;

    public NonBlockingTcpHandler() {
        super();
    }

    public NonBlockingTcpHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
        try {
            NonBlockingTcpHandler.nioConnection = NioConnection.getInstance();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        NonBlockingTcpHandler.nioConnection.addConnectionListener(this);

    }

    @Override
    public void send(final byte[] message) throws IOException {
        throw new IllegalArgumentException("send() not supported");
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final NonBlockingTcpClientHandler handler = new NonBlockingTcpClientHandler(protocolHandler, options);
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public String getName() {
        return NonBlockingTcpHandler.NAME;
    }

    @Override
    public void processInOpen() throws OpenFailedException {
        try {
            NonBlockingTcpHandler.nioConnection.connectToServer(this, this.getHost(), this.getPort(), this.getInitializeCommand());
        }
        catch (final Exception e) {
            throw new OpenFailedException(e);
        }
        this.open = true;
    }

    @Override
    public void processInClose() throws CloseFailedException {
        try {
            NonBlockingTcpHandler.nioConnection.disconnectFromServer(this);
            this.open = false;
        }
        catch (final IOException e) {
            throw new CloseFailedException(e);
        }
    }

    @Override
    public void processOutClose() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void notify(final IConnection connection, final ConnectionMessageReason reason) {
        // TODO: Reconnect??
    }

    @Override
    public void process(long callerId, final ByteBuffer buffer) throws ClassNotFoundException {
        super.fireProcess(callerId, buffer);
    }

    @Override
    public void done() {
        // TODO: Done
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public void socketDisconnected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void socketException(final Exception ex) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof NonBlockingTcpHandler)) {
            return false;
        }
        final NonBlockingTcpHandler other = (NonBlockingTcpHandler) o;
        if (!this.host.equals(other.host)) {
            return false;
        }
        else if (this.port != other.port) {
            return false;
        }
        else if (this.initializeCommand.equals(other.initializeCommand)) {
            return false;
        }
        else if (this.autoconnect != other.autoconnect) {
            return false;
        }
        return true;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(NonBlockingTcpHandler.HOST)) {
            this.setHost(options.get(NonBlockingTcpHandler.HOST));
        }
        else {
            this.setHost("127.0.0.1");
        }
        if (options.containsKey(NonBlockingTcpHandler.PORT)) {
            this.setPort(Integer.parseInt(options.get(NonBlockingTcpHandler.PORT)));
        }
        else {
            this.setPort(8080);
        }
        if (options.containsKey(NonBlockingTcpHandler.AUTOCONNECT)) {
            this.setAutoconnect(Boolean.parseBoolean(options.get(NonBlockingTcpHandler.AUTOCONNECT)));
        }
        else {
            this.setAutoconnect(true);
        }
        if (options.containsKey(NonBlockingTcpHandler.INITIALIZE_ALIAS)) {
            this.setInitializeCommand(options.get(NonBlockingTcpHandler.INITIALIZE_ALIAS));
            NonBlockingTcpHandler.LOG.warn("Parameter " + NonBlockingTcpHandler.INITIALIZE_ALIAS + " is deprecated. Please use " + NonBlockingTcpHandler.INITIALIZE);
        }
        if (options.containsKey(NonBlockingTcpHandler.USERNAME)) {
            this.setInitializeCommand(options.get(NonBlockingTcpHandler.USERNAME) + "\n" + options.get(NonBlockingTcpHandler.PASSWORD) + "\n");
            NonBlockingTcpHandler.LOG.warn("Parameter " + NonBlockingTcpHandler.USERNAME + " is deprecated. Please use " + NonBlockingTcpHandler.INITIALIZE);
        }
        if (options.containsKey(NonBlockingTcpHandler.INITIALIZE)) {
            this.setInitializeCommand(options.get(NonBlockingTcpHandler.INITIALIZE));
        }
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param initializeCommand
     *            the initializeCommand to set
     */
    public void setInitializeCommand(final String initializeCommand) {
        if (initializeCommand != null) {
            this.initializeCommand = initializeCommand.replace("\\\\n", "\n").getBytes();
        }
        else {
            this.initializeCommand = null;
        }
    }

    /**
     * @param initializeCommand
     *            the initializeCommand to set
     */
    public void setInitializeCommand(final byte[] initializeCommand) {
        this.initializeCommand = initializeCommand;
    }

    /**
     * @return the initializeCommand
     */
    public byte[] getInitializeCommand() {
        return this.initializeCommand;
    }

    /**
     * @param autoconnect
     *            the autoconnect to set
     */
    public void setAutoconnect(final boolean autoconnect) {
        this.autoconnect = autoconnect;
    }

    /**
     * @return the autoconnect
     */
    public boolean isAutoconnect() {
        return this.autoconnect;
    }
}
