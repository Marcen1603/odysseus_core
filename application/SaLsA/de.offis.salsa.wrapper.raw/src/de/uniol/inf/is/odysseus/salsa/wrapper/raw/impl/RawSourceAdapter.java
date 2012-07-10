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
package de.uniol.inf.is.odysseus.salsa.wrapper.raw.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class RawSourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {

    private static Logger LOG = LoggerFactory.getLogger(RawSourceAdapter.class);

    private final Map<SourceSpec, Thread> serverThreads = new HashMap<SourceSpec, Thread>();

    @Override
    public String getName() {
        return "SALSA-Raw";
    }

    @Override
    protected void doDestroy(SourceSpec source) {
        this.serverThreads.get(source).interrupt();
        this.serverThreads.remove(source);
    }

    @Override
    protected void doInit(SourceSpec source) {
        int port = 4444;
        int length = 1;
        if (source.getConfiguration().containsKey("port")) {
            port = Integer.parseInt(source.getConfiguration().get("port").toString());
        }
        if (source.getConfiguration().containsKey("length")) {
            length = Integer.parseInt(source.getConfiguration().get("length").toString());
        }
        final RawServer server = new RawServer(source, this, port, length);
        final Thread serverThread = new Thread(server);
        this.serverThreads.put(source, serverThread);
        serverThread.start();
    }

    class RawServer implements Runnable {
        private ServerSocket serverSocket = null;
        private SourceSpec source;
        private RawSourceAdapter adapter;
        private final int length;

        public RawServer(final SourceSpec source, final RawSourceAdapter adapter, final int port,
                final int length) {
            this.length = length;
            try {
                this.serverSocket = new ServerSocket(port);
            }
            catch (final IOException e) {
                RawSourceAdapter.LOG.error(e.getMessage(), e);
            }
        }

        @Override
        public void run() {
            final List<Thread> processingThreads = new ArrayList<Thread>();
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket;
                try {
                    socket = this.serverSocket.accept();
                    final RawProcessor processor = new RawProcessor(this.source, socket,
                            this.adapter, this.length);
                    final Thread processingThread = new Thread(processor);
                    processingThreads.add(processingThread);
                    processingThread.start();
                }
                catch (final IOException e) {
                    RawSourceAdapter.LOG.error(e.getMessage(), e);
                }
            }
            for (final Thread processingThread : processingThreads) {
                processingThread.interrupt();
            }
        }

        class RawProcessor implements Runnable {
            private final Socket server;
            private final RawSourceAdapter adapter;
            private SourceSpec sourceSpec;
            private final Charset charset = Charset.forName("ASCII");
            private final int length;

            public RawProcessor(final SourceSpec sourceSpec, final Socket server,
                    final RawSourceAdapter adapter, final int length) {
                this.server = server;
                this.adapter = adapter;
                this.sourceSpec = sourceSpec;
                this.length = length;
            }

            @Override
            public void run() {
                SocketChannel channel = this.server.getChannel();
                try {
                    final CharsetDecoder decoder = this.charset.newDecoder();
                    final ByteBuffer buffer = ByteBuffer.allocateDirect(64 * 1024);
                    int nbytes = 0;
                    int pos = 0;
                    int size = 0;

                    while (!Thread.currentThread().isInterrupted()) {
                        while ((nbytes = channel.read(buffer)) > 0) {
                            size += nbytes;
                            for (int i = pos; i < size; i++) {
                                if (i == this.length) {
                                    buffer.position(i + 1);
                                    buffer.flip();
                                    final CharBuffer charBuffer = decoder.decode(buffer);
                                    try {
                                        this.onMessage(
                                                charBuffer.subSequence(0, charBuffer.length() - 1)
                                                        .toString(), System.currentTimeMillis());
                                        this.dumpPackage(buffer);
                                    }
                                    catch (final Exception e) {
                                        if (RawSourceAdapter.LOG.isDebugEnabled()) {
                                            RawSourceAdapter.LOG.debug(e.getMessage(), e);
                                            this.dumpPackage(buffer);
                                        }
                                    }
                                    buffer.limit(size);

                                    buffer.compact();
                                    size -= (i + 1);
                                    pos = 0;
                                    i = 0;
                                }
                            }
                            pos++;
                        }

                    }
                    RawSourceAdapter.LOG.info("SICK connection interrupted");
                }
                catch (final Exception e) {
                    RawSourceAdapter.LOG.error(e.getMessage(), e);
                }
                finally {
                    if (channel != null) {
                        try {
                            channel.close();
                        }
                        catch (final IOException e) {
                            RawSourceAdapter.LOG.error(e.getMessage(), e);
                        }
                    }
                }
            }

            private void dumpPackage(final ByteBuffer buffer) throws FileNotFoundException {
                final File debug = new File("debug.out");
                final FileOutputStream out = new FileOutputStream(debug, true);
                final FileChannel debugChannel = out.getChannel();
                if ((debugChannel != null) && (debugChannel.isOpen())) {
                    try {
                        buffer.flip();
                        debugChannel.write(buffer);
                    }
                    catch (final IOException e) {
                        RawSourceAdapter.LOG.error(e.getMessage(), e);
                    }
                }
            }

            private void onMessage(final String message, final long timestamp) {
                this.adapter.transfer(sourceSpec, timestamp, new Object[] {
                    message
                });
            }
        }

    }

}
