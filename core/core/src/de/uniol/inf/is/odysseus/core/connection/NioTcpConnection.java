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
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioTcpConnection implements IConnection, ReadWriteSelectorHandler {
    private static final Logger                   LOG         = LoggerFactory.getLogger(NioTcpConnection.class);
    private ByteBuffer                            writeBuffer = null;
    private final ByteBuffer                      readBuffer;
    protected final TCPSelectorThread                selector;
    private final SocketChannel                   channel;
    private Object                                writeLock   = new Object();
    private IAccessConnectionListener<ByteBuffer> listener;

    public NioTcpConnection(SocketChannel socketChannel, TCPSelectorThread selector,
            IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        this.selector = selector;
        this.channel = socketChannel;
        this.listener = listener;
        readBuffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        // readBuffer.position(readBuffer.capacity());
        selector.registerChannelNow(channel, 0, this);
    }

    public void resumeReading() throws IOException, ClassNotFoundException {
        processInBuffer();
    }

    public void onRead() {
        try {
            int readBytes = channel.read(readBuffer);
            if (readBytes == -1) {
                close();
                listener.socketDisconnected(this);
                return;
            }

            if (readBytes == 0) {
                reactivateReading();
                return;
            }

            // readBuffer.flip();
            processInBuffer();
        }
        catch (IOException | ClassNotFoundException  ex) {
            listener.socketException(this, ex);
            close();
        }

    }

    @Override
    public void onWrite() {
        try {
            int bytes = channel.write(writeBuffer);
            if (writeBuffer.hasRemaining()) {
                requestWrite();
            }
            else {
                writeBuffer = null;
            }
        }
        catch (IOException ioe) {
            close();
            listener.socketException(this, ioe);
        }
    }

    public SocketChannel getSocketChannel() {
        return channel;
    }

    private void requestWrite() throws IOException {
        selector.addChannelInterestNow(channel, SelectionKey.OP_WRITE);
    }

    public void write(ByteBuffer packet) {
        writeBuffer = packet;
        onWrite();
    }

    public void close() {
        try {
            channel.close();
        }
        catch (IOException e) {
            // Ignore
        }
    }

    public void disableReading() throws IOException {
        selector.removeChannelInterestNow(channel, SelectionKey.OP_READ);
    }

    private void reactivateReading() throws IOException {
        selector.addChannelInterestNow(channel, SelectionKey.OP_READ);
    }

    private void processInBuffer() throws IOException, ClassNotFoundException {
        listener.process(readBuffer);
        // readBuffer.clear();
        reactivateReading();
    }

    public int write(byte[] message) {
        synchronized (writeLock) {
            writeBuffer.put(message);
        }
        write(ByteBuffer.wrap(message));
        return 0;
    }

}
