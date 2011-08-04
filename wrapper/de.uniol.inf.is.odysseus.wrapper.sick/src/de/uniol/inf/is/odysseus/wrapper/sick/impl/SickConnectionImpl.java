package de.uniol.inf.is.odysseus.wrapper.sick.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.sick.MeasurementListener;
import de.uniol.inf.is.odysseus.wrapper.sick.SickConnection;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Background;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Sample;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SickConnectionImpl implements SickConnection {
    class SickConnectionHandler extends Thread {

        private final String host;
        private final int port;
        private SocketChannel channel;
        private final Charset charset = Charset.forName("ASCII");
        private Background background;
        private boolean record = false;
        private long recordInterval;
        private long recordEnd;
        private final SickConnectionImpl connection;

        public SickConnectionHandler(final String host, final int port, final long recordInterval,
                final SickConnectionImpl connection) {
            this.host = host;
            this.port = port;
            this.connection = connection;
            this.recordInterval = recordInterval;
            this.recordEnd = System.currentTimeMillis() + Long.MAX_VALUE;
            if (recordInterval > 0l) {
                this.record = true;
            }
            else {
                this.record = false;
            }
        }

        public void setRecordBackground(boolean record) {
            this.record = record;
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
                    SickConnectionImpl.LOG.error(e.getMessage(), e);
                }
            }
        }

        public Background getBackground() {
            return this.background;
        }

        public boolean isConnected() {
            if (this.channel != null) {
                return this.channel.isConnected();
            }
            else {
                return false;
            }
        }

        private void onClose() {
            this.sendMessage(SickConnectionImpl.STOP_SCAN);
        }

        private void onMessage(final String message, final long timestamp)
                throws SickReadErrorException {
            final String[] data = message.split(" ");

            if (message.startsWith(SickConnectionImpl.SRA)) {
                if (SickConnectionImpl.LCM_STATE.equalsIgnoreCase(data[1])) {
                    final int dirtyness = Integer.parseInt(data[2]);
                    SickConnectionImpl.LOG.warn(String.format("Dirtyness %s ", dirtyness));
                }
            }
            else if (message.startsWith(SickConnectionImpl.SEA)) {
                SickConnectionImpl.LOG.info(String.format("Receive message %s (%s byte)", message,
                        message.getBytes().length + 2));
            }
            else if (message.startsWith(SickConnectionImpl.SSN)) {
                if (SickConnectionImpl.LMD_SCANDATA.equalsIgnoreCase(data[1])) {
                    if (data.length >= 19) {
                        final Measurement measurement = new Measurement();
                        try {
                            int pos = 0;
                            measurement.setVersion(data[pos + 2]);
                            measurement.setDevice(data[pos + 3]);
                            measurement.setSerial(data[pos + 4]);
                            measurement.setStatus(Integer.parseInt(data[pos + 5], 16),
                                    Integer.parseInt(data[pos + 5], 16));

                            measurement.setMessageCount(Integer.parseInt(data[pos + 7], 16));
                            measurement.setScanCount(Integer.parseInt(data[pos + 8], 16));

                            measurement.setPowerUpDuration(Long.parseLong(data[pos + 9], 16));
                            measurement.setTransmissionDuration(Long.parseLong(data[pos + 10], 16));

                            measurement.setInputStatus("3".equals(data[pos + 11])
                                    || "3".equals(data[pos + 11]));
                            measurement.setOutputStatus("7".equals(data[pos + 13])
                                    || "7".equals(data[pos + 14]));

                            measurement
                                    .setScanningFrequency(Long.parseLong(data[pos + 16], 16) * 10);
                            measurement
                                    .setMeasurementFrequency(Long.parseLong(data[pos + 17], 16) * 10);

                            measurement.setEncoders(Integer.parseInt(data[pos + 18], 16));
                            if (measurement.getEncoders() > 0) {
                                measurement.setEncoderPosition(Long.parseLong(data[pos + 19], 16));
                                measurement.setEncoderSpeed(Integer.parseInt(data[pos + 20], 16));
                                pos += 2;
                            }

                            final int channels = Integer.parseInt(data[pos + 19], 16);
                            int index = pos + 20;
                            for (int i = 0; i < channels; i++) {
                                final String name = data[index];
                                if ((name.equalsIgnoreCase(SickConnectionImpl.DIST1))
                                        || (name.equalsIgnoreCase(SickConnectionImpl.DIST2))
                                        || (name.equalsIgnoreCase(SickConnectionImpl.RSSI1))
                                        || (name.equalsIgnoreCase(SickConnectionImpl.RSSI2))) {

                                    final float scalingFactor = Float.intBitsToFloat(((Long) Long
                                            .parseLong(data[index + 1], 16)).intValue());
                                    final float scalingOffset = Float.intBitsToFloat(((Long) Long
                                            .parseLong(data[index + 2], 16)).intValue());
                                    final double startingAngle = Double.longBitsToDouble(Long
                                            .parseLong(data[index + 3], 16)) / 10000;
                                    final double angularStepWidth = ((double) Integer.parseInt(
                                            data[index + 4], 16)) / 10000;

                                    final int samples = Integer.parseInt(data[index + 5], 16);
                                    if (measurement.getSamples() == null) {
                                        measurement.setSamples(new Sample[samples]);
                                    }
                                    for (int j = 0; j < samples; j++) {
                                        if (measurement.getSamples()[j] == null) {
                                            measurement.getSamples()[j] = new Sample();
                                            measurement.getSamples()[j].setIndex(j);
                                            measurement.getSamples()[j]
                                                    .setAngle((j * angularStepWidth)
                                                            + startingAngle);
                                        }
                                        try {
                                            final float value = Integer.parseInt(
                                                    data[index + 6 + j], 16)
                                                    * scalingFactor
                                                    + scalingOffset;
                                            if (name.equalsIgnoreCase(SickConnectionImpl.DIST1)) {
                                                measurement.getSamples()[j].setDist1(this
                                                        .substractBackground(j,
                                                                value <= 3 ? Float.MAX_VALUE
                                                                        : value));

                                            }
                                            else if (name
                                                    .equalsIgnoreCase(SickConnectionImpl.DIST2)) {
                                                measurement.getSamples()[j].setDist2(this
                                                        .substractBackground(j,
                                                                value <= 3 ? Float.MAX_VALUE
                                                                        : value));
                                            }
                                            else if (name
                                                    .equalsIgnoreCase(SickConnectionImpl.RSSI1)) {
                                                measurement.getSamples()[j].setRssi1(value);
                                            }
                                            else if (name
                                                    .equalsIgnoreCase(SickConnectionImpl.RSSI2)) {
                                                measurement.getSamples()[j].setRssi2(value);
                                            }
                                        }
                                        catch (final Exception e) {
                                            throw new SickReadErrorException(message);
                                        }
                                    }

                                    index += samples + 6;
                                }
                                else {
                                    throw new SickReadErrorException(message);
                                }
                            }
                        }
                        catch (final Exception e) {
                            throw new SickReadErrorException(message);
                        }
                        if (this.record) {
                            if (System.currentTimeMillis() > recordEnd) {
                                this.record = false;
                            }
                            if (this.background == null) {
                                this.background = new Background(measurement);
                            }
                            this.background = Background.merge(this.background, measurement);
                        }
                        else {
                            this.connection.onMeasurement(measurement, timestamp);
                        }
                    }
                }
            }
            else {
                SickConnectionImpl.LOG.info(String.format("Receive message %s (%s byte)", message,
                        message.getBytes().length + 2));
            }
        }

        private void onOpen() {
            this.sendMessage(SickConnectionImpl.START_SCAN);
            recordEnd = System.currentTimeMillis() + recordInterval;

        }

        @Override
        public void run() {
            try {
                this.channel = SocketChannel.open();
                final InetSocketAddress address = new InetSocketAddress(this.host, this.port);
                this.channel.connect(address);
                this.channel.configureBlocking(false);
                final CharsetDecoder decoder = this.charset.newDecoder();
                this.onOpen();
                final ByteBuffer buffer = ByteBuffer.allocateDirect(64 * 1024);
                int nbytes = 0;
                int pos = 0;
                int size = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    while ((nbytes = this.channel.read(buffer)) > 0) {
                        size += nbytes;
                        for (int i = pos; i < size; i++) {
                            // for (int i = 0; i < size; i++) {
                            if (buffer.get(i) == SickConnectionImpl.END) {
                                buffer.position(i + 1);
                                buffer.flip();
                                final CharBuffer charBuffer = decoder.decode(buffer);
                                try {
                                    this.onMessage(
                                            charBuffer.subSequence(1, charBuffer.length() - 1)
                                                    .toString(), System.currentTimeMillis());
                                }
                                catch (final Exception e) {
                                    if (SickConnectionImpl.LOG.isDebugEnabled()) {
                                        SickConnectionImpl.LOG.debug(e.getMessage(), e);
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
                this.onClose();
                SickConnectionImpl.LOG.info("SICK connection interrupted");
            }
            catch (final Exception e) {
                SickConnectionImpl.LOG.error(e.getMessage(), e);
            }
            finally {
                if (this.channel != null) {
                    try {
                        this.channel.close();
                    }
                    catch (final IOException e) {
                        SickConnectionImpl.LOG.error(e.getMessage(), e);
                    }
                }
            }
        }

        public void sendMessage(final String message) {
            int nBytes = 0;
            try {
                final Charset charset = Charset.forName("utf-8");
                final ByteBuffer messageBuffer = ByteBuffer.allocate(message.getBytes().length + 2);
                messageBuffer.put(SickConnectionImpl.START);
                messageBuffer.put(message.getBytes(charset));
                messageBuffer.put(SickConnectionImpl.END);
                messageBuffer.rewind();
                nBytes = this.channel.write(messageBuffer);
                SickConnectionImpl.LOG.info(String.format("Send message %s (%s byte)", message,
                        nBytes));
            }
            catch (final IOException e) {
                SickConnectionImpl.LOG.error(e.getMessage(), e);
            }
        }

        private float substractBackground(final int index, final float value) {
            if (this.background != null) {
                return value > this.background.getDistance(index) ? Float.MAX_VALUE : value;
            }
            else {
                return value;
            }
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(SickConnection.class);
    private static final String START_SCAN = "sEN LMDscandata 1";
    private static final String STOP_SCAN = "sEN LMDscandata 0";
    private static final byte START = (byte) 0x02;
    private static final byte END = (byte) 0x03;
    private static final String SRA = "sRA";
    private static final String SEA = "sEA";
    private static final String SSN = "sSN";
    private static final String LCM_STATE = "LCMstate";

    private static final String LMD_SCANDATA = "LMDscandata";
    private static final String DIST1 = "DIST1";
    private static final String DIST2 = "DIST2";
    private static final String RSSI1 = "RSSI1";
    private static final String RSSI2 = "RSSI2";

    private SickConnectionHandler handler = null;
    private MeasurementListener listener;
    private SourceSpec source;

    public SickConnectionImpl(final String host, final int port, final long record) {
        this.handler = new SickConnectionHandler(host, port, record, this);
    }

    public SickConnectionImpl(SickConnectionImpl connection) {
        this.handler = connection.handler;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SickConnectionImpl(this);
    }

    @Override
    public void close() {
        this.handler.interrupt();
    }

    @Override
    public Background getBackground() {
        return this.handler.getBackground();
    }

    @Override
    public boolean isConnected() {
        return this.handler.isConnected();
    }

    public void onMeasurement(final Measurement measurement, final long timestamp) {
        this.listener.onMeasurement(source, measurement, timestamp);
    }

    @Override
    public void open() {
        this.handler.start();
    }

    @Override
    public void setListener(final SourceSpec source, final MeasurementListener listener) {
        this.listener = listener;
        this.source = source;
    }
}
