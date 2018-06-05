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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class AudioTransportHandler extends AbstractPushTransportHandler {
    static final Logger LOG = LoggerFactory.getLogger(AudioTransportHandler.class);
    private static final String NAME = "Audio";

    public static final String SAMPLE_RATE = "sampleRate";
    public static final String SAMPLE_SIZE_IN_BITS = "sampleSizeInBits";
    public static final String CHANNELS = "channels";
    public static final String BIG_ENDIAN = "bigEndian";
    public static final String SIGNED = "signed";

    private MicrophoneReader reader;
    private AudioTransportHandlerOutputStream output;

    float sampleRate;
    int sampleSizeInBits;
    int channels;
    boolean bigEndian;
    boolean signed;

    /**
     *
     */
    public AudioTransportHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param protocolHandler
     *            The protocol handler
     * @param options
     *            The options
     */
    private AudioTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final AudioTransportHandler instance = new AudioTransportHandler(protocolHandler, options);
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AudioTransportHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        try {
            this.reader = new MicrophoneReader(this.sampleRate, this.sampleSizeInBits, this.channels, this.signed, this.bigEndian);
            this.reader.start();
        }
        catch (LineUnavailableException e) {
            throw new IOException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        try {
            this.output = new AudioTransportHandlerOutputStream(this.sampleRate, this.sampleSizeInBits, this.channels, this.signed, this.bigEndian);
        }
        catch (LineUnavailableException e) {
            throw new IOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.reader.interrupt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        this.output.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        this.output.write(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof AudioTransportHandler)) {
            return false;
        }
        final AudioTransportHandler other = (AudioTransportHandler) o;
        if (this.sampleRate != other.getSampleRate() || this.sampleSizeInBits != other.getSampleSizeInBits() || this.channels != other.getChannels() || this.signed != other.isSigned()
                || this.bigEndian != other.isBigEndian()) {
            return false;
        }
        return true;
    }

    /**
     * @return the sampleRate
     */
    public float getSampleRate() {
        return this.sampleRate;
    }

    /**
     * @param sampleRate
     *            the sampleRate to set
     */
    public void setSampleRate(final float sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * @return the channels
     */
    public int getChannels() {
        return this.channels;
    }

    /**
     * @param channels
     *            the channels to set
     */
    public void setChannels(final int channels) {
        this.channels = channels;
    }

    /**
     * @return the bigEndian
     */
    public boolean isBigEndian() {
        return this.bigEndian;
    }

    /**
     * @param bigEndian
     *            the bigEndian to set
     */
    public void setBigEndian(final boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    /**
     * @return the sampleSizeInBits
     */
    public int getSampleSizeInBits() {
        return this.sampleSizeInBits;
    }

    /**
     * @param sampleSizeInBits
     *            the sampleSizeInBits to set
     */
    public void setSampleSizeInBits(final int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    /**
     * @return the signed
     */
    public boolean isSigned() {
        return this.signed;
    }

    /**
     * @param signed
     *            the signed to set
     */
    public void setSigned(final boolean signed) {
        this.signed = signed;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        this.sampleRate = options.getFloat(SAMPLE_RATE, 8000.0f);
        this.sampleSizeInBits = options.getInt(SAMPLE_SIZE_IN_BITS, 16);
        this.channels = options.getInt(CHANNELS, 1);
        this.bigEndian = options.getBoolean(BIG_ENDIAN, true);
        this.signed = options.getBoolean(SIGNED, true);
    }

    private class MicrophoneReader extends Thread {
        private TargetDataLine microphone;
        private AudioFormat format;
        private byte[] buffer;

        /**
         * Class constructor.
         * 
         * @throws LineUnavailableException
         *
         */
        public MicrophoneReader(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) throws LineUnavailableException {
            this.format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            this.buffer = new byte[this.format.getFrameSize()];
            final DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.format);
            this.microphone = (TargetDataLine) AudioSystem.getLine(info);
            this.microphone.open(this.format);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                this.microphone.start();
                while (!this.isInterrupted()) {
                    final int numBytesRead = this.microphone.read(this.buffer, 0, this.buffer.length);
                    if (numBytesRead > 0) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(this.buffer);
                        byteBuffer.position(byteBuffer.capacity());
                        AudioTransportHandler.this.fireProcess(byteBuffer);
                    }
                }
                this.microphone.stop();
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            finally {
                this.microphone.close();
            }
        }
    }

    private class AudioTransportHandlerOutputStream extends OutputStream {
        private AudioFormat format;
        private SourceDataLine sourceLine;
        private ByteBuffer buffer;
        private boolean closing = false; /* To avoid recursive closing */

        /**
         * Class constructor.
         * 
         * @throws LineUnavailableException
         *
         */
        public AudioTransportHandlerOutputStream(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) throws LineUnavailableException {
            this.format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.format);
            this.buffer = ByteBuffer.allocate(this.format.getFrameSize());

            this.sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            this.sourceLine.open(this.format);
            this.sourceLine.start();

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(int b) throws IOException {
            synchronized (this) {
                if (this.buffer.remaining() == 0) {
                    this.flush();
                }
                this.buffer.put((byte) b);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
            synchronized (this) {
                this.buffer.flip();
                if (this.buffer.hasRemaining()) {
                    this.sourceLine.write(this.buffer.array(), this.buffer.position(), this.buffer.remaining());
                    this.buffer.clear();
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            synchronized (this) {
                if (!this.closing) {
                    this.closing = true;
                    this.flush();
                    this.buffer = null;
                    this.sourceLine.drain();
                    this.sourceLine.stop();
                    this.sourceLine.close();
                }
            }
        }

    }

}
