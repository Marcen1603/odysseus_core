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

/**
 * Generic UDP *connection* allowing to send datagrams to an address and read
 * datagrams on a channel
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NioUdpConnection implements IConnection, ReadWriteSelectorHandler {
    private ByteBuffer                                  writeBuffer = null;
    private final ByteBuffer                            readBuffer;
    protected final SelectorThread                      selector;
    private final DatagramChannel                       channel;
    private final IAccessConnectionListener<ByteBuffer> listener;

    public NioUdpConnection(final DatagramChannel datagramChannel, final SelectorThread selector,
            final IAccessConnectionListener<ByteBuffer> listener) throws IOException {
        this.selector = selector;
        this.channel = datagramChannel;
        this.listener = listener;
        this.readBuffer = ByteBuffer.allocate(this.channel.socket().getReceiveBufferSize());
        this.writeBuffer = ByteBuffer.allocate(this.channel.socket().getSendBufferSize());
        // readBuffer.position(readBuffer.capacity());

        // selector.registerChannelNow(channel, 0, this);
        selector.registerChannel(this.channel, 0, this, new CallbackErrorHandler() {
            @SuppressWarnings("unused")
            public void handleError(final Exception ex) {
                NioUdpConnection.this.listener.socketException(ex);
            }
        });
    }

    public void resumeReading() throws IOException, ClassNotFoundException {
        this.processInBuffer();
    }

    @Override
    public void onRead() {
        try {
            final SocketAddress address = this.channel.receive(this.readBuffer);
            if (address == null) {
                this.close();
                this.listener.socketDisconnected();
                return;
            }
            // readBuffer.flip();
            this.processInBuffer();
        }
        catch (IOException | ClassNotFoundException ex) {
            this.listener.socketException(ex);
            this.close();
        }

    }

    @Override
    public void onWrite() {
        try {
            synchronized (this.writeBuffer) {
                this.writeBuffer.flip();
                this.channel.send(this.writeBuffer, this.channel.getRemoteAddress());
                if (this.writeBuffer.hasRemaining()) {
                    this.requestWrite();
                }
                else {
                    this.writeBuffer.clear();
                    this.resumeReading();
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            this.close();
            this.listener.socketException(e);
        }
    }

    public DatagramChannel getDatagramChannel() {
        return this.channel;
    }

    private void requestWrite() throws IOException {
        this.selector.addChannelInterestNow(this.channel, SelectionKey.OP_WRITE);
    }

    public void write(final byte[] message) {
        this.write(ByteBuffer.wrap(message));
    }

    public void write(final ByteBuffer packet) {
        synchronized (this.writeBuffer) {
            this.writeBuffer.put(packet);
        }
        this.onWrite();
    }

    public void close() {
        try {
            this.channel.close();
        }
        catch (final IOException e) {
            this.listener.socketException(e);
        }
    }

    public void disableReading() throws IOException {
        this.selector.removeChannelInterestNow(this.channel, SelectionKey.OP_READ);
    }

    private void reactivateReading() throws IOException {
        this.selector.addChannelInterestNow(this.channel, SelectionKey.OP_READ);
    }

    private void processInBuffer() throws IOException, ClassNotFoundException {
        this.listener.process(this.readBuffer);
        // readBuffer.clear();
        this.reactivateReading();
    }
}
