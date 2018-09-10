/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.sink.ISinkStreamHandler;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.util.OSGI;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

@SuppressWarnings("rawtypes")
class SinkConnectionListener extends Thread implements ISinkConnection {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);

	final int port;
	final private ISinkStreamHandlerBuilder sinkStreamHandlerBuilder;

	private boolean useNIO;

	final private boolean loginNeeded;

	final private boolean loginWithSessionId;

	final Set<String> allowedSessionIds = new TreeSet<>();

	private IServerSocketProvider serverSocketProvider;

	final private SocketSinkPO po;

	public SinkConnectionListener(int port, ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, /* OUT!! */
			SocketSinkPO po, boolean useNIO, boolean loginNeeded, boolean loginWithSessionId) {
		this.port = port;
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.po = po;
		this.useNIO = useNIO;
		this.loginNeeded = loginNeeded;
		this.loginWithSessionId = loginWithSessionId;
	}

	public SinkConnectionListener(IServerSocketProvider serverSocketProvider,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, SocketSinkPO po, boolean loginNeeded,
			boolean loginWithSessionId) {
		if (serverSocketProvider == null) {
			throw new IllegalArgumentException("ServerSocketProvider cannot be null!");
		}
		this.port = serverSocketProvider.getServerSocket().getLocalPort();
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.po = po;
		this.loginNeeded = loginNeeded;
		this.loginWithSessionId = loginWithSessionId;
		this.serverSocketProvider = serverSocketProvider;
	}

	@Override
	public void addSessionId(String sessionId) {
		this.allowedSessionIds.add(sessionId);
	}

	@Override
	public void removeSessionId(String sessionId) {
		this.allowedSessionIds.remove(sessionId);
	}

	@Override
	public void run() {
		ServerSocket server = null;
		try {
			if (serverSocketProvider != null) {
				server = serverSocketProvider.getServerSocket();
			} else if (useNIO) {
				ServerSocketChannel serverChannel = ServerSocketChannel.open();
				server = serverChannel.socket();
				server.bind(new InetSocketAddress(port));
				serverChannel.configureBlocking(true);
			} else {
				server = new ServerSocket(port);
				server.setSoTimeout(0);
			}

		} catch (IOException e) {
			logger.error("Exception during creating socket server", e);
		}

		if (server != null) {
			while (true) {
				Socket socket = null;
				try {
					boolean connectionAllowed = false;
					logger.debug("Waiting for Client to connect on " + port + " loginNeeded: " + loginNeeded);
					socket = server.accept();
					logger.debug("Connection from " + socket.getRemoteSocketAddress());
					if (loginNeeded) {
						logger.debug("Waiting for login ");
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						if (loginWithSessionId) {
							String sessionId = in.readLine();
							connectionAllowed = allowedSessionIds.contains(sessionId);
						} else {

							String username = in.readLine();
							String password = in.readLine();
							String tenantName = in.readLine();
							ITenant tenant = OSGI.get(UserManagementProvider.class).getTenant(tenantName);
							ISession user = OSGI.get(SessionManagement.class).login(username, password.getBytes(), tenant);

							if (user != null) {
								// TODO: Test if User has right to access sink
								connectionAllowed = true;
							}
						}
					} else {
						connectionAllowed = true;
					}
					if (connectionAllowed) {
						logger.debug("Adding Handler");
						ISinkStreamHandler temp = sinkStreamHandlerBuilder.newInstance(socket);
						po.addSubscriber(temp);
						logger.debug("Adding Handler done");
					} else {
						logger.debug("Connection " + (connectionAllowed == false ? "not allowed." : " failed"));
					}
				} catch (IOException e) {
					logger.error("Exception during getting new connection", e);
				}
			}
		}
		logger.error("Could not create server for socket sink");
	}

	@Override
	public String toString() {
		return super.toString() + " p=" + port;
	}
}
