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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic TCP connection
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioTcpConnection implements IConnection {
    private static final Logger                   LOG       = LoggerFactory.getLogger(NioTcpConnection.class);
    private final ByteBuffer                      writeBuffer;
    private final ByteBuffer                      readBuffer;
    private SelectionKey                          selectionKey;
    private SocketChannel                         channel;
    private Object                                writeLock = new Object();
    private IAccessConnectionListener<ByteBuffer> listener;

    public NioTcpConnection(Selector selector, InetSocketAddress address, int readBufferSize, int writeBufferSize,
            IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        this.listener = listener;
        this.readBuffer = ByteBuffer.allocate(readBufferSize);
        this.writeBuffer = ByteBuffer.allocate(writeBufferSize);
        this.channel = selector.provider().openSocketChannel();
        this.channel.configureBlocking(false);
        this.channel.connect(address);
    }

    public NioTcpConnection(Selector selector, SocketChannel channel, int readBufferSize, int writeBufferSize,
            IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        this.listener = listener;
        this.readBuffer = ByteBuffer.allocate(readBufferSize);
        this.writeBuffer = ByteBuffer.allocate(writeBufferSize);
        this.channel = channel;
        this.channel.configureBlocking(false);
    }

    public ByteBuffer read() throws IOException {
        readBuffer.clear();
        int count = this.channel.read(readBuffer);
        if (count == -1) {
            this.channel.close();
            this.selectionKey.cancel();
        }
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
        int nbytes = 0;

        synchronized (writeLock) {
            this.writeBuffer.flip();
            try {
                while (writeBuffer.hasRemaining()) {
                    int bytes = this.channel.write(writeBuffer);
                    if (bytes == 0) {
                        break;
                    }
                    nbytes += bytes;
                }
                writeBuffer.compact();
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            if (writeBuffer.position() == 0) {
                selectionKey.selector().wakeup();
            }
            else {
                selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        }
        return nbytes;
    }

    public void register(Selector selector) throws ClosedChannelException {
        this.selectionKey = this.channel.register(selector, SelectionKey.OP_CONNECT);
        this.selectionKey.attach(this);
    }

    public void close() {
        if (this.channel != null) {
            try {
                this.channel.close();
            }
            catch (IOException e) {
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
