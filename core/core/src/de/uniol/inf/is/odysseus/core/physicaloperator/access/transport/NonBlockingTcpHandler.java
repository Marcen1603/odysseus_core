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
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioConnection;
import de.uniol.inf.is.odysseus.core.physicaloperator.CloseFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class NonBlockingTcpHandler extends AbstractTransportHandler implements IConnectionListener, IAccessConnectionListener<ByteBuffer> {

	static NioConnection nioConnection = null;
	private String host;
	private int port;
	private String user;
	private String password;
	@SuppressWarnings("unused")
	private boolean autoconnect;
	private boolean open;
	
	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		NonBlockingTcpHandler handler = new NonBlockingTcpHandler();
		try {
			NonBlockingTcpHandler.nioConnection = NioConnection.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NonBlockingTcpHandler.nioConnection.addConnectionListener(handler);
		handler.host = options.get("host");
		handler.port = Integer.parseInt(options.get("port"));
		handler.autoconnect = Boolean.parseBoolean(options.get("autoconnect"));
		handler.user = options.get("user");
		handler.password = options.get("password");
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public String getName() {
		return "NonBlockingTcp";
	}

	@Override
	public void process_open() throws OpenFailedException {
		try {
			nioConnection.connectToServer(this, host, port, user, password);
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}		
		this.open = true;
	}

	@Override
	public void process_close() throws CloseFailedException {
		try {
			nioConnection.disconnectFromServer(this);
		} catch (IOException e) {
			throw new CloseFailedException(e);
		}
	}

	@Override
	public void notify(IConnection connection, ConnectionMessageReason reason) {
		// TODO: Reconnect??
	}

	@Override
	public void process(ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(buffer);
	}

	@Override
	public void done() {
		// TODO: Done
	}

	@Override
	public boolean isOpened() {
		return open;
	}

}
