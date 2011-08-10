/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

@SuppressWarnings("rawtypes")
class SinkConnectionListener extends Thread {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);

	final int port;
	final private ISinkStreamHandlerBuilder sinkStreamHandlerBuilder;
	final List<ISinkStreamHandler> subscribe;

	private boolean useNIO;

	private boolean loginNeeded;

	public SinkConnectionListener(int port,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, /*OUT!!*/List<ISinkStreamHandler> subscribe2,
			boolean useNIO, boolean loginNeeded) {
		this.port = port;
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.subscribe = subscribe2;
		this.useNIO = useNIO;
		this.loginNeeded = loginNeeded;
	}

	@Override
	public void run() {
		ServerSocket server = null;
		try {
			if (useNIO){
				ServerSocketChannel serverChannel = ServerSocketChannel.open();
				server = serverChannel.socket();
				server.bind(new InetSocketAddress(port));
				serverChannel.configureBlocking(true);
			}else{
				server = new ServerSocket(port);
				server.setSoTimeout(0);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			Socket socket = null;
			try {
				boolean connectionAllowed = false;
				logger.debug("Waiting for Server to connect on "+port);
				socket = server.accept();
				logger.debug("Connection from "
						+ socket.getRemoteSocketAddress());
				if (loginNeeded){
					InputStream inputStream = socket.getInputStream();
					// TODO: Login und Passwort auslesen
					String username = "";
					String password = "";
					User user = UserManagement.getInstance().login(username, password, false);
					if (user != null){
						// TODO: Test if User has right to access sink
						connectionAllowed = true;
					}
				}else{
					connectionAllowed = true;
				}
				if (socket != null && connectionAllowed) {
					logger.debug("Adding Handler");
					ISinkStreamHandler temp = sinkStreamHandlerBuilder.newInstance(socket);
					synchronized (subscribe) {
						subscribe.add(temp);
					}
					logger.debug("Adding Handler done");
				}else{
					logger.debug("Connection "+(connectionAllowed==false?"not allowed.":" failed"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
