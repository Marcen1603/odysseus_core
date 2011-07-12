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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SinkConnectionListener extends Thread {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);

	final int port;
	final private ISinkStreamHandlerBuilder sinkStreamHandlerBuilder;
	final List<ISinkStreamHandler> subscribe;

	private boolean useNIO;

	public SinkConnectionListener(int port,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, /*OUT!!*/List<ISinkStreamHandler> subscribe2,
			boolean useNIO) {
		this.port = port;
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.subscribe = subscribe2;
		this.useNIO = useNIO;
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
				logger.debug("Waiting for Server to connect on "+port);
				socket = server.accept();
				logger.debug("Connection from "
						+ socket.getRemoteSocketAddress());
				if (socket != null) {
					logger.debug("Adding Handler");
					ISinkStreamHandler temp = sinkStreamHandlerBuilder.newInstance(socket);
					synchronized (subscribe) {
						subscribe.add(temp);
					}
					logger.debug("Adding Handler done");

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
