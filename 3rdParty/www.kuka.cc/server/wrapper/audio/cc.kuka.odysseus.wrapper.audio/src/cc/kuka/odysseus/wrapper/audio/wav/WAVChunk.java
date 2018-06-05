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
package cc.kuka.odysseus.wrapper.audio.wav;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class WAVChunk {
    private final String id;
    private final int size;
    private final String format;
    private final FMTChunk fmt;
    private final DataChunk data;

    /**
     * Class constructor.
     *
     */
    public WAVChunk(final ByteBuffer message) {
        message.order(ByteOrder.BIG_ENDIAN);
        this.id = new String(message.get(new byte[4]).array());
        message.order(ByteOrder.LITTLE_ENDIAN);
        this.size = message.getInt();
        message.order(ByteOrder.BIG_ENDIAN);
        this.format = new String(message.get(new byte[4]).array());
        this.fmt = new FMTChunk(message);
        this.data = new DataChunk(message);
    }

    /**
     * Class constructor.
     * 
     * @throws IOException
     *
     */
    public WAVChunk(InputStream stream) throws IOException {
        this.id = new String(new byte[] { (byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read() });
        this.size = (stream.read() & 0xff) | (stream.read() & 0xff) << 8 | (stream.read() & 0xff) << 16 | (stream.read() & 0xff) << 24;
        this.format = new String(new byte[] { (byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read() });
        this.fmt = new FMTChunk(stream);
        this.data = new DataChunk(stream);
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * @return the fmt
     */
    public FMTChunk getFmt() {
        return this.fmt;
    }

    /**
     * @return the data
     */
    public DataChunk getData() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "WAVChunk [id=" + this.id + ", size=" + this.size + ", format=" + this.format + ", fmt=" + this.fmt + ", data=" + this.data + "]";
    }

}
