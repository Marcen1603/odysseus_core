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
package cc.kuka.odysseus.wrapper.audio.physicaloperator.access;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.wrapper.audio.wav.WAVChunk;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class WAVProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(WAVProtocolHandler.class);

    private static final String DEBUG = "debug";

    private static final String NAME = "WAV";
    private boolean isDone = false;
    private WAVChunk wavChunk;
    private boolean debug = false;

    /**
     *
     */
    public WAVProtocolHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param direction
     *            The transport direction
     * @param access
     *            The access pattern
     * @param dataHandler
     *            The data handler
     */
    private WAVProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IStreamObjectDataHandler<T> dataHandler, final OptionMap options) {
        super(direction, access, dataHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
        final WAVProtocolHandler<T> instance = new WAVProtocolHandler<>(direction, access, dataHandler, options);
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void open() throws IOException {
        this.getTransportHandler().open();
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            this.wavChunk = new WAVChunk(this.getTransportHandler().getInputStream());
        }
        this.isDone = false;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            this.wavChunk.getData().getData().close();
        }
        this.getTransportHandler().close();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() throws IOException {
        try {
            if (this.wavChunk.getData().getData().available() == 0) {
                this.isDone = true;
                return false;
            }
        }
        catch (Exception e) {
            LOG.error("Could not determine hasNext()", e);
            this.isDone = true;
            return false;
        }
        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public T getNext() throws IOException {
        ByteBuffer sample = ByteBuffer.allocate(this.wavChunk.getFmt().getBlockAlign());
        this.wavChunk.getData().getData().read(sample.array());
        return getDataHandler().readData(sample);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return this.isDone;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> o) {
        if (!(o instanceof WAVProtocolHandler)) {
            return false;
        }
        WAVProtocolHandler<?> other = (WAVProtocolHandler<?>) o;
        if (this.debug != other.isDebug()) {
            return false;
        }
        return true;
    }

    public boolean isDebug() {
        return this.debug;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.get(DEBUG) != null) {
            this.debug = Boolean.parseBoolean(options.get(DEBUG));
        }
    }

    /**
     * {@inheritDoc} TODO why is this method no longer in the protocol handlers?
     * 20150127 (CKu)
     * */
    public void process(final ByteBuffer message) {
        this.wavChunk = new WAVChunk(message);
        try {
            while (this.wavChunk.getData().getData().available() > 0) {
                ByteBuffer sample = ByteBuffer.allocate(this.wavChunk.getFmt().getBlockAlign());
                this.wavChunk.getData().getData().read(sample.array());
                this.getTransfer().transfer(getDataHandler().readData(sample));
            }
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String example = "example.wav";
        try (FileInputStream stream = new FileInputStream(example)) {
            WAVChunk chunk = new WAVChunk(stream);
            System.out.println(chunk.toString());
        }
    }
}
