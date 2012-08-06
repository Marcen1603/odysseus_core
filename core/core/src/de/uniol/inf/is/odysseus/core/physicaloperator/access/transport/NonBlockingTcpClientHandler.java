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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpServer;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

/**
 * Handler for generic TCP Client
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingTcpClientHandler extends AbstractTransportHandler
		implements IAccessConnectionHandler<ByteBuffer>, IConnectionListener, IAccessConnectionListener<ByteBuffer> {
	private static final Logger LOG = LoggerFactory
			.getLogger(NonBlockingTcpClientHandler.class);
	private NioTcpServer client;
	private String host;
	private int port;
	private boolean open;

	public NonBlockingTcpClientHandler() {

	}

	public NonBlockingTcpClientHandler(Map<String, String> options) {
		try {
			this.client = NioTcpServer.getInstance();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		this.host = options.get("host");
		this.port = Integer.parseInt(options.get("port"));
	}

	public NonBlockingTcpClientHandler(
			NonBlockingTcpClientHandler nonBlockingTcpClientHandler) {
		this.client = nonBlockingTcpClientHandler.client;
		this.host = nonBlockingTcpClientHandler.host;
		this.port = nonBlockingTcpClientHandler.port;
	}

	@Override
	public void send(byte[] message) throws IOException {
		client.write(this, message);
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		return new NonBlockingTcpClientHandler(options);
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public String getName() {
		return "TCPClient";
	}

	@Override
	public void process(ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(buffer);
	}

	@Override
	public void done() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpened() {
		return open;
	}

	@Override
	public void process_open() {
		InetSocketAddress address = new InetSocketAddress(host, port);
		try {
			this.client.connect(address, this);
		} catch (IOException e) {
			throw new OpenFailedException(e);
		}

	}

	@Override
	public void process_close() {
		this.client.close(this);
	}

	@Override
	public NonBlockingTcpClientHandler clone() {
		return new NonBlockingTcpClientHandler(this);
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
	public void open(IAccessConnectionListener<ByteBuffer> caller)
			throws OpenFailedException {
	this.process_open();
		
	}

	@Override
	public void close(IAccessConnectionListener<ByteBuffer> caller)
			throws IOException {
		this.process_close();
		
	}

	@Override
	public void reconnect() {
		this.process_close();
		this.process_open();
		
	}

	@Override
	public String getUser() {
		return "";
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public IAccessConnectionHandler<ByteBuffer> getInstance(
			Map<String, String> options) {
		return new NonBlockingTcpClientHandler(options);
	}
}
