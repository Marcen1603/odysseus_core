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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DataChunk {
    private final String id;
    private final int size;
    private final InputStream data;

    /**
     * Class constructor.
     *
     */
    public DataChunk(final ByteBuffer message) {
        message.order(ByteOrder.BIG_ENDIAN);
        this.id = new String(message.get(new byte[4]).array());

        message.order(ByteOrder.LITTLE_ENDIAN);
        this.size = message.getInt();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer = new byte[message.remaining()];
        message.get(buffer);
        this.data = new ByteArrayInputStream(buffer);
    }

    /**
     * Class constructor.
     * 
     * @throws IOException
     *
     */
    public DataChunk(InputStream stream) throws IOException {
        this.id = new String(new byte[] { (byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read() });
        this.size = (stream.read() & 0xff) | (stream.read() & 0xff) << 8 | (stream.read() & 0xff) << 16 | (stream.read() & 0xff) << 24;
        this.data = stream;
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
     * @return the data
     */
    public InputStream getData() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        try {
            return "DataChunk [id=" + this.id + ", size=" + this.size + ", data=" + this.data.available() + "]";
        }
        catch (IOException e) {
            return "DataChunk [id=" + this.id + ", size=" + this.size + ", data=" + e.getMessage() + "]";
        }
    }

}
