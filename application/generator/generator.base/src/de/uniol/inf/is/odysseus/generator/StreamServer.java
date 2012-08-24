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
package de.uniol.inf.is.odysseus.generator;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class StreamServer extends Thread {

	private ServerSocket socket;
	private StreamClientHandler streamClientHandler;	
	private List<StreamClientHandler> clients = new ArrayList<StreamClientHandler>();

	public StreamServer(int port, StreamClientHandler streamClientHandler) throws Exception {
		this.streamClientHandler = streamClientHandler;
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		socket = serverChannel.socket();
		socket.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(true);
	}

	@Override
	public void run() {
		System.out.println("Starting new server on port " + this.socket.getLocalPort());
		while (true) {
			Socket connection = null;
			try {
				System.out.println("Waiting for connection...");
				connection = socket.accept();
				System.out.println("New connection from " + connection.getInetAddress());
				StreamClientHandler streamClient = this.streamClientHandler.clone();
				streamClient.setConnection(connection);
				streamClient.start();
				this.clients.add(streamClient);
			} catch (Exception ex) {
				System.err.println("Error: " + ex.getStackTrace());
				continue;
			}
		}
	}

	public void stopClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.stopClient();
			}
		}
	}

	public void proceedClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.proceed();
			}
		}
	}

	public void pauseClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.pause();
			}
		}
	}
}
