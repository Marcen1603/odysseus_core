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
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic UDP *connection* allowing to send datagrams to an address and read
 * datagrams on a channel
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioUdpConnection implements IConnection {
	private static final Logger LOG = LoggerFactory
			.getLogger(NioUdpConnection.class);
	private SelectionKey selectionKey;
	private DatagramChannel channel;
	private Object writeLock = new Object();
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	private IAccessConnectionListener<ByteBuffer> listener;

	public NioUdpConnection(Selector selector, InetSocketAddress address,
			int readBufferSize, int writeBufferSize,
			IAccessConnectionListener<ByteBuffer> listener) throws IOException {
		this.listener = listener;
		this.readBuffer = ByteBuffer.allocate(readBufferSize);
		this.writeBuffer = ByteBuffer.allocate(writeBufferSize);
		this.channel = selector.provider().openDatagramChannel();
		this.channel.connect(address);
		this.channel.configureBlocking(false);
		this.selectionKey = this.channel.register(selector,
				SelectionKey.OP_READ);
		this.selectionKey.attach(this);
	}

	public NioUdpConnection(Selector selector, DatagramChannel channel,
			int readBufferSize, int writeBufferSize,
			IAccessConnectionListener<ByteBuffer> listener) throws IOException {
		this.listener = listener;
		this.readBuffer = ByteBuffer.allocate(readBufferSize);
		this.writeBuffer = ByteBuffer.allocate(writeBufferSize);
		this.channel = channel;
		this.channel.configureBlocking(false);
		this.selectionKey = this.channel.register(selector,
				SelectionKey.OP_READ);
		this.selectionKey.attach(this);
	}

	public ByteBuffer read() throws IOException {
		this.channel.receive(readBuffer);
		return readBuffer;
	}

	public int write(byte[] message) {
		int nbytes = 0;
		synchronized (writeLock) {
			writeBuffer.put(message);
		}
		nbytes = write();
		return nbytes;
	}

	public int write() {
		this.writeBuffer.flip();
		int nbytes = 0;
		try {
			nbytes = this.channel.write(writeBuffer);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			this.writeBuffer.clear();
		}
		return nbytes;
	}
	
    public void register(Selector selector) throws ClosedChannelException {
        this.selectionKey.attach(this);
    }
    
	public void close() {
		if (this.channel != null) {
			try {
				this.channel.close();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
			this.channel = null;
			if (this.selectionKey != null) {
				this.selectionKey.selector().wakeup();
			}
		}
	}

	public IAccessConnectionListener<ByteBuffer> getListener() {
		return listener;
	}
}
