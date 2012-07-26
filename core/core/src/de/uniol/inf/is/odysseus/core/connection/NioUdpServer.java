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
package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic UDP Server and Client controller
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioUdpServer extends Thread implements IConnection {
	private static final Logger LOG = LoggerFactory
			.getLogger(NioUdpServer.class);
	static NioUdpServer instance = null;
	private Map<IAccessConnectionListener<ByteBuffer>, NioUdpConnection> receiverMap = new ConcurrentHashMap<IAccessConnectionListener<ByteBuffer>, NioUdpConnection>();

	private int readBufferSize;
	private int writeBufferSize;
	private Selector selector;
	private boolean doRouting = true;
	private long timeout = 40;

	public static synchronized NioUdpServer getInstance() throws IOException {
		if (instance == null) {
			instance = new NioUdpServer(1024, 1024);
			instance.start();
		}
		return instance;
	}

	public void bind(InetSocketAddress address,
			IAccessConnectionListener<ByteBuffer> listener) throws IOException {
		DatagramChannel channel = selector.provider().openDatagramChannel();
		channel.socket().bind(address);
		NioUdpConnection connection = new NioUdpConnection(selector, channel,
				readBufferSize, writeBufferSize, listener);
		this.receiverMap.put(listener, connection);
		selector.wakeup();
	}

	public void connect(InetSocketAddress address,
			IAccessConnectionListener<ByteBuffer> listener) throws IOException {
		NioUdpConnection connection = new NioUdpConnection(selector, address,
				this.readBufferSize, this.writeBufferSize, listener);
		this.receiverMap.put(listener, connection);
		selector.wakeup();
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public void stopRouting() {
		doRouting = false;
		selector.wakeup();
	}

	public void write(IAccessConnectionListener<ByteBuffer> sink, byte[] message) {
		receiverMap.get(sink).write(message);
	}

	public void close(IAccessConnectionListener<ByteBuffer> sink) {
		receiverMap.remove(sink).close();
	}

	@Override
	public void run() {
		while ((!Thread.interrupted()) && (doRouting)) {
			try {
				int select = 0;
				if (timeout > 0) {
					select = selector.select(timeout);
				} else {
					select = selector.selectNow();
				}
				if (select > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					synchronized (keys) {
						for (Iterator<SelectionKey> iter = keys.iterator(); iter
								.hasNext();) {
							SelectionKey selectionKey = iter.next();
							iter.remove();

							NioUdpConnection connection = (NioUdpConnection) selectionKey
									.attachment();
							int ops = selectionKey.readyOps();

							if ((ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
								ByteBuffer buffer = connection.read();
								try {
									connection.getListener().process(buffer);
								} catch (ClassNotFoundException e) {
									LOG.error(e.getMessage(), e);
								}
							}
						}
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private NioUdpServer(int readBufferSize, int writeBufferSize) {
		this.readBufferSize = readBufferSize;
		this.writeBufferSize = writeBufferSize;
		try {
			this.selector = Selector.open();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
