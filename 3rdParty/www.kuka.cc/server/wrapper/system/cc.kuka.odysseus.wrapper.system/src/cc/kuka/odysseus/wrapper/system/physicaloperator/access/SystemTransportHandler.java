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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SystemTransportHandler extends AbstractPullTransportHandler {
    /** Logger */
    static Logger LOG = LoggerFactory.getLogger(SystemTransportHandler.class);
    private static final String NAME = "System";

    private static final String COMMAND = "command";
    private static final String ENVIRONMENT = "env";

    private String commandString;
    private String[] envParameter;
    /** In and output for data transfer */
    private InputStream input;
    private OutputStream output;

    /**
     *
     */
    public SystemTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public SystemTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    @Override
    public void send(final byte[] message) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final SystemTransportHandler handler = new SystemTransportHandler(protocolHandler, options);
        return handler;
    }

    @Override
    public InputStream getInputStream() {
        return this.input;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.output;
    }

    @Override
    public String getName() {
        return SystemTransportHandler.NAME;
    }

    @Override
    public void processInOpen() throws IOException {
        this.input = new SystemInputStream(this.commandString, this.envParameter);
    }

    @Override
    public void processOutOpen() throws IOException {
        this.output = new SystemOutputStream(this.commandString, this.envParameter);
    }

    @Override
    public void processInClose() throws IOException {
        this.input = null;
        this.fireOnDisconnect();
    }

    @Override
    public void processOutClose() throws IOException {
        this.output = null;
        this.fireOnDisconnect();
    }

    public void setCommand(final String command) {
        this.commandString = command;
    }

    public String getCommand() {
        return this.commandString;
    }

    public void setEnv(final String[] env) {
        this.envParameter = env;
    }

    public void setEnv(final String env) {
        this.envParameter = env.split(",");
    }

    public String[] getEnv() {
        return this.envParameter;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(SystemTransportHandler.COMMAND)) {
            this.setCommand(options.get(SystemTransportHandler.COMMAND));
        }
        if (options.containsKey(SystemTransportHandler.ENVIRONMENT)) {
            this.setEnv(options.get(SystemTransportHandler.ENVIRONMENT));
        }
        else {
            this.setEnv("");
        }
    }

    private class SystemInputStream extends InputStream {
        private final String command;
        private final String[] env;
        private InputStream stream;

        public SystemInputStream(final String command, final String[] env) {
            this.command = command;
            this.env = env;
        }

        @Override
        public int read() throws IOException {
            if (this.isStreamEmpty()) {
                this.call();
            }
            return this.stream.read();
        }

        @Override
        public int available() throws IOException {
            if (this.isStreamEmpty()) {
                this.call();
            }
            return this.stream.available();
        }

        private boolean isStreamEmpty() throws IOException {
            return (this.stream == null) || (this.stream.available() == 0);
        }

        private void call() throws IOException {
            if (this.stream != null) {
                this.stream.close();
            }
            final Process process = Runtime.getRuntime().exec(this.command, this.env);
            try {
                process.waitFor();
                this.stream = process.getInputStream();
            }
            catch (final InterruptedException e) {
                SystemTransportHandler.LOG.error(e.getMessage(), e);
            }
        }
    }

    private class SystemOutputStream extends OutputStream {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private final String command;
        private final String[] env;

        public SystemOutputStream(final String command, final String[] env) {
            this.command = command;
            this.env = env;
        }

        @Override
        public void write(final int b) throws IOException {
            if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
                final ByteBuffer newBuffer = ByteBuffer.allocate((1 + this.buffer.position()) * 2);
                final int pos = this.buffer.position();
                this.buffer.flip();
                newBuffer.put(this.buffer);
                this.buffer = newBuffer;
                this.buffer.position(pos);
                SystemTransportHandler.LOG.debug("Extending buffer to " + this.buffer.capacity());
            }
            this.buffer.put((byte) b);
        }

        @Override
        public void flush() throws IOException {
            this.buffer.flip();
            final Process process = Runtime.getRuntime().exec(String.format(this.command, this.buffer.asCharBuffer().toString()), this.env);
            try {
                process.waitFor();
            }
            catch (final InterruptedException e) {
                SystemTransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                process.destroy();
            }
        }
    }

    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof SystemTransportHandler)) {
            return false;
        }
        final SystemTransportHandler other = (SystemTransportHandler) o;

        if (!other.commandString.equals(this.getCommand())) {
            return false;
        }
        else if (other.getEnv().length != this.getEnv().length) {
            return false;
        }
        final String[] a = this.getEnv();
        final String[] b = other.getEnv();
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }
}
