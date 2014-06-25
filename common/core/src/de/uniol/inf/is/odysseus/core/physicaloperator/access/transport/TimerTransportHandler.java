/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimerTransportHandler extends AbstractPushTransportHandler {
    private static final String PERIOD = "period";
    private Timer timer = null;
    private long period = 1000l;

    /**
 * 
 */
    public TimerTransportHandler() {
        super();
    }

    public TimerTransportHandler(final IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final TimerTransportHandler handler = new TimerTransportHandler(protocolHandler);
        handler.setOptionsMap(options);
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOptionsMap(final Map<String, String> options) {
        super.setOptionsMap(options);
        if (options.containsKey(TimerTransportHandler.PERIOD)) {
            this.period = Long.parseLong(options.get(TimerTransportHandler.PERIOD));
        }
        else {
            throw new IllegalArgumentException("No period given!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Timer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        this.timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
                buffer.putLong(System.currentTimeMillis());
                TimerTransportHandler.this.fireProcess(buffer);
            }
        };
        this.fireOnConnect();
        this.timer.schedule(task, 0, this.period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.timer.cancel();
        this.timer.purge();
        this.timer = null;
        this.fireOnDisconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof TimerTransportHandler)) {
            return false;
        }
        final TimerTransportHandler other = (TimerTransportHandler) o;
        if (this.period != other.period) {
            return false;
        }

        return true;
    }

}
