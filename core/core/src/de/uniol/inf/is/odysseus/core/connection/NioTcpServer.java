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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic TCP Server and Client controller
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioTcpServer extends Thread implements IConnection {
    private static final Logger                                             LOG          = LoggerFactory
                                                                                                 .getLogger(NioTcpServer.class);
    static NioTcpServer                                                     instance     = null;
    private Map<IAccessConnectionListener<ByteBuffer>, NioTcpConnection>    receiverMap  = new ConcurrentHashMap<IAccessConnectionListener<ByteBuffer>, NioTcpConnection>();
    private Map<ServerSocketChannel, IAccessConnectionListener<ByteBuffer>> serverMap    = new ConcurrentHashMap<ServerSocketChannel, IAccessConnectionListener<ByteBuffer>>();
    private Queue<NioTcpConnection>                                         deferredList = new ConcurrentLinkedQueue<NioTcpConnection>();
    private Selector                                                        selector;
    private int                                                             readBufferSize;
    private int                                                             writeBufferSize;
    private boolean                                                         doRouting    = true;
    private long                                                            timeout      = -1;
    

    public static synchronized NioTcpServer getInstance(int readBufferSize, int writeBufferSize) throws IOException {
        if (instance == null) {
            instance = new NioTcpServer(readBufferSize, writeBufferSize);
            instance.start();
        }
        else {
            if (writeBufferSize > instance.writeBufferSize) {
                instance.writeBufferSize = writeBufferSize;
            }
            if (readBufferSize > instance.readBufferSize) {
                instance.readBufferSize = readBufferSize;
            }
        }
        return instance;
    }

    public void bind(InetSocketAddress address, IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        ServerSocketChannel channel = selector.provider().openServerSocketChannel();
        channel.socket().bind(address);
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        serverMap.put(channel, listener);
    }

    public void connect(InetSocketAddress address, IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        NioTcpConnection connection = new NioTcpConnection(this.selector, address, this.readBufferSize,
                this.writeBufferSize, listener);
        receiverMap.put(listener, connection);
        deferredList.add(connection);
        this.selector.wakeup();
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void stopRouting() {
        doRouting = false;
        this.selector.wakeup();
    }

    @Override
    public void run() {
        while ((!Thread.interrupted()) && (doRouting)) {
            long now = System.currentTimeMillis();
            try {
                int select = 0;
                if (timeout > 0) {
                    select = this.selector.select(timeout);
                }
                else {
                    select = this.selector.select();
                }
                processRegister(this.selector);
                if (select > 0) {
                    Set<SelectionKey> keys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iter = keys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey selectionKey = iter.next();
                        iter.remove();
                        Object attachment = selectionKey.attachment();
                        if (attachment != null) {
                            NioTcpConnection connection = (NioTcpConnection) attachment;
                            if ((selectionKey.isValid()) && (selectionKey.isConnectable())
                                    && (((SocketChannel) selectionKey.channel()).finishConnect())) {
                                if (((SocketChannel) selectionKey.channel()).isConnected()) {
                                    selectionKey.interestOps(SelectionKey.OP_READ);
                                    ((IConnectionListener) connection.getListener()).notify(this,
                                            ConnectionMessageReason.ConnectionOpened);
                                }
                            }
                            else if ((selectionKey.isValid()) && (selectionKey.isReadable())) {
                                if (!((SocketChannel) selectionKey.channel()).isConnected()) {
                                    selectionKey.interestOps(SelectionKey.OP_CONNECT);
                                }
                                else {
                                    ByteBuffer buffer = connection.read();
                                    try {
                                        connection.getListener().process(buffer);
                                    }
                                    catch (ClassNotFoundException e) {
                                        LOG.error(e.getMessage(), e);
                                    }
                                }
                            }
                            else if ((selectionKey.isValid()) && (selectionKey.isWritable())) {
                                connection.write();
                            }
                        }
                        if ((selectionKey.isValid()) && (selectionKey.isAcceptable())) {
                            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                            NioTcpConnection connection = new NioTcpConnection(selector, channel.accept(),
                                    this.readBufferSize, this.writeBufferSize, serverMap.get(channel));
                            receiverMap.put(serverMap.get(channel), connection);
                            ((IConnectionListener) connection.getListener()).notify(this,
                                    ConnectionMessageReason.ConnectionOpened);
                        }
                    }
                }
                else {
                    long diff = System.currentTimeMillis() - now;
                    if (diff < 25) {
                        try {
                            Thread.sleep(25 - diff);
                        }
                        catch (InterruptedException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public void close(IAccessConnectionListener<ByteBuffer> sink) {
        receiverMap.remove(sink).close();
    }

    public void write(IAccessConnectionListener<ByteBuffer> sink, byte[] message) {
        receiverMap.get(sink).write(message);
    }

    private void processRegister(Selector selector) {
        while (deferredList.size() > 0) {
            NioTcpConnection connection = deferredList.poll();
            try {
                connection.register(selector);
            }
            catch (ClosedChannelException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private NioTcpServer(int readBufferSize, int writeBufferSize) {
        this.readBufferSize = readBufferSize;
        this.writeBufferSize = writeBufferSize;
        try {
            this.selector = Selector.open();
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
