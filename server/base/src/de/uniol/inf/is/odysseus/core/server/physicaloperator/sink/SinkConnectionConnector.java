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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Geesen
 * 
 */
public class SinkConnectionConnector implements ISinkConnection {

	private int port;
	private String host;
	private ISinkStreamHandlerBuilder sinkStreamHandlerBuilder;
	@SuppressWarnings("rawtypes")
	private List<ISinkStreamHandler> subscribe;
//	private boolean loginNeeded;
	private boolean useNIO;
	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);

	/**
	 * @param serverPort
	 * @param host
	 * @param sinkStreamHandlerBuilder
	 * @param subscribe
	 * @param useNIO
	 * @param loginNeeded
	 */
	public SinkConnectionConnector(int serverPort, String host, ISinkStreamHandlerBuilder sinkStreamHandlerBuilder,
			@SuppressWarnings("rawtypes") List<ISinkStreamHandler> subscribe, boolean useNIO, boolean loginNeeded) {
		this.port = serverPort;
		this.host = host;
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.subscribe = subscribe;
		this.useNIO = useNIO;
	//	this.loginNeeded = loginNeeded;
	}

	@Override
	public void start() {
		Socket client = null;
		try {
			if (useNIO) {
				SocketChannel socketChannel = SocketChannel.open();
				client = socketChannel.socket();
				client.connect(new InetSocketAddress(host, port));
				socketChannel.configureBlocking(true);
			} else {
				client = new Socket(host, port);
				client.setSoTimeout(0);
			}
		

			logger.debug("Connecting to server " + host + " on " + port + "...");
			logger.debug("Adding Handler....");
			ISinkStreamHandler<?> temp = sinkStreamHandlerBuilder.newInstance(client);
			synchronized (subscribe) {
				subscribe.add(temp);
				logger.debug("Adding Handler done");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
