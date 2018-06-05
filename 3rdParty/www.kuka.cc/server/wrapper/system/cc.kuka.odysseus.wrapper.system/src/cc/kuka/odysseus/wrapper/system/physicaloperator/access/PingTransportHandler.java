/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.wrapper.system.physicaloperator.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PingTransportHandler extends AbstractPullTransportHandler {
    /** Logger */
    static Logger LOG = LoggerFactory.getLogger(PingTransportHandler.class);
    private static final String NAME = "Ping";

    private static final String HOST = "host";

    private String host;
    /** Input for data transfer */
    private InputStream input;

    /**
     *
     */
    public PingTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public PingTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    @Override
    public void send(final byte[] message) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final PingTransportHandler handler = new PingTransportHandler(protocolHandler, options);
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        return this.input;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public String getName() {
        return PingTransportHandler.NAME;
    }

    @Override
    public void processInOpen() throws IOException {
        final InetAddress addr = InetAddress.getByName(this.host);
        this.input = new PingInputStream(addr);
    }

    @Override
    public void processOutOpen() throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public void processInClose() throws IOException {
        this.input = null;
        this.fireOnDisconnect();
    }

    @Override
    public void processOutClose() throws IOException {
        this.fireOnDisconnect();
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(PingTransportHandler.HOST)) {
            this.setHost(options.get(PingTransportHandler.HOST));
        }
    }

    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof PingTransportHandler)) {
            return false;
        }
        final PingTransportHandler other = (PingTransportHandler) o;

        if (!other.host.equals(this.getHost())) {
            return false;
        }

        return true;
    }

    private class PingInputStream extends InputStream {
        private final InetAddress addr;
        private final ByteBuffer buffer;
        private boolean refetch = false;

        public PingInputStream(final InetAddress addr) throws IOException {
            this.addr = addr;
            this.buffer = ByteBuffer.allocate((Byte.SIZE + Long.SIZE + Double.SIZE) / 8);
            this.fetch();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized int read() throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }
            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            return this.buffer.get() & 0xFF;
        }

        @Override
        public synchronized int read(final byte[] b, final int off, final int len) throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }

            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            final int length = Math.min(len, this.buffer.remaining());

            this.buffer.get(b, off, length);
            return length;
        }

        @Override
        public void close() throws IOException {
            this.buffer.clear();
        }

        @Override
        public int available() throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }
            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            return this.buffer.remaining();
        }

        private void fetch() throws IOException {
            this.buffer.clear();
            String command;

            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                command = "ping -n 1 " + this.addr.getHostAddress();
            }
            else {
                command = "ping -c 1 " + this.addr.getHostAddress();
            }
            this.buffer.putLong(System.currentTimeMillis());
            double value = Double.NaN;

            final Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                final InputStream stream = process.getInputStream();
                final InputStreamReader isr = new InputStreamReader(stream);
                try (final BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        final String[] parameter = line.split(":");
                        if (parameter.length > 1) {
                            final String[] fields = parameter[1].split(" ");
                            for (final String field : fields) {
                                final String[] keyValue = field.split("=");
                                if (keyValue.length > 1) {
                                    if (keyValue[0].equals("time")) {
                                        value = Double.parseDouble(keyValue[1]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (final InterruptedException e) {
                PingTransportHandler.LOG.error(e.getMessage(), e);
            }
            this.buffer.putDouble(value);
            this.buffer.flip();
        }

        private boolean isRefetch() {
            return this.refetch;
        }

        private void setRefetch(final boolean refetch) {
            this.refetch = refetch;
        }
    }

}
