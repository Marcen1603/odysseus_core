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
public class FMTChunk {
    private final String id;
    private final int size;
    private final short format;
    private final short channels;
    private final int sampleRate;
    private final int byteRate;
    private final short blockAlign;
    private final short bitsPerSample;

    /**
     * Class constructor.
     *
     */
    public FMTChunk(final ByteBuffer message) {
        message.order(ByteOrder.BIG_ENDIAN);
        this.id = new String(message.get(new byte[4]).array());

        message.order(ByteOrder.LITTLE_ENDIAN);
        this.size = message.getInt();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.format = message.getShort();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.channels = message.getShort();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.sampleRate = message.get();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.byteRate = message.get();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.blockAlign = message.getShort();

        // message.order(ByteOrder.LITTLE_ENDIAN);
        this.bitsPerSample = message.getShort();

    }

    /**
     * Class constructor.
     * 
     * @throws IOException
     *
     */
    public FMTChunk(InputStream stream) throws IOException {
        this.id = new String(new byte[] { (byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read() });
        this.size = (stream.read() & 0xff) | (stream.read() & 0xff) << 8 | (stream.read() & 0xff) << 16 | (stream.read() & 0xff) << 24;
        this.format = (short) ((stream.read() & 0xff) | (stream.read() & 0xff) << 8);
        this.channels = (short) ((stream.read() & 0xff) | (stream.read() & 0xff) << 8);
        this.sampleRate = (stream.read() & 0xff) | (stream.read() & 0xff) << 8 | (stream.read() & 0xff) << 16 | (stream.read() & 0xff) << 24;
        this.byteRate = (stream.read() & 0xff) | (stream.read() & 0xff) << 8 | (stream.read() & 0xff) << 16 | (stream.read() & 0xff) << 24;
        this.blockAlign = (short) ((stream.read() & 0xff) | (stream.read() & 0xff) << 8);
        this.bitsPerSample = (short) ((stream.read() & 0xff) | (stream.read() & 0xff) << 8);
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
    public short getFormat() {
        return this.format;
    }

    /**
     * @return the channels
     */
    public short getChannels() {
        return this.channels;
    }

    /**
     * @return the sampleRate
     */
    public int getSampleRate() {
        return this.sampleRate;
    }

    /**
     * @return the byteRate
     */
    public int getByteRate() {
        return this.byteRate;
    }

    /**
     * @return the blockAlign
     */
    public short getBlockAlign() {
        return this.blockAlign;
    }

    /**
     * @return the bitsPerSample
     */
    public short getBitsPerSample() {
        return this.bitsPerSample;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FMTChunk [id=" + this.id + ", size=" + this.size + ", format=" + this.format + ", channels=" + this.channels + ", sampleRate=" + this.sampleRate + ", byteRate=" + this.byteRate
                + ", blockAlign=" + this.blockAlign + ", bitsPerSample=" + this.bitsPerSample + "]";
    }

}
