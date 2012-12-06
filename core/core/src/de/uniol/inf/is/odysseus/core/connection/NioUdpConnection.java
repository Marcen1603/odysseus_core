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
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic UDP *connection* allowing to send datagrams to an address and read
 * datagrams on a channel
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioUdpConnection implements IConnection, ReadWriteSelectorHandler {
    private static final Logger                   LOG         = LoggerFactory.getLogger(NioUdpConnection.class);
    private ByteBuffer                            writeBuffer = null;
    private final ByteBuffer                      readBuffer;
    protected final SelectorThread                selector;
    private final DatagramChannel                 channel;
    private IAccessConnectionListener<ByteBuffer> listener;

    public NioUdpConnection(DatagramChannel datagramChannel, SelectorThread selector,
            IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        this.selector = selector;
        this.channel = datagramChannel;
        this.listener = listener;
        readBuffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        writeBuffer = ByteBuffer.allocate(channel.socket().getSendBufferSize());
        // readBuffer.position(readBuffer.capacity());

        // selector.registerChannelNow(channel, 0, this);
        selector.registerChannel(this.channel, 0, this, new CallbackErrorHandler() {
            public void handleError(final Exception ex) {
                NioUdpConnection.this.listener.socketException(ex);
            }
        });
    }

    public void resumeReading() throws IOException, ClassNotFoundException {
        processInBuffer();
    }

    public void onRead() {
        try {
            SocketAddress address = channel.receive(readBuffer);
            if (address == null) {
                close();
                listener.socketDisconnected();
                return;
            }
            // readBuffer.flip();
            processInBuffer();
        }
        catch (IOException | ClassNotFoundException ex) {
            listener.socketException(ex);
            close();
        }

    }

    @Override
    public void onWrite() {
        try {
            synchronized (writeBuffer) {
                writeBuffer.flip();
                channel.send(writeBuffer, channel.getRemoteAddress());
                if (writeBuffer.hasRemaining()) {
                    requestWrite();
                }
                else {
                    writeBuffer.clear();
                    resumeReading();
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            close();
            listener.socketException(e);
        }
    }

    public DatagramChannel getDatagramChannel() {
        return channel;
    }

    private void requestWrite() throws IOException {
        selector.addChannelInterestNow(channel, SelectionKey.OP_WRITE);
    }

    public void write(byte[] message) {
        write(ByteBuffer.wrap(message));
    }

    public void write(ByteBuffer packet) {
        synchronized (writeBuffer) {
            writeBuffer.put(packet);
        }
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
}
