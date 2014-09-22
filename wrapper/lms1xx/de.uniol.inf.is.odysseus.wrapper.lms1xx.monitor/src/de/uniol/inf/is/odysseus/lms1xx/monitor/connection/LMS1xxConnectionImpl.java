/*******************************************************************************
 * LMS1xx protocol visualization and logging
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
package de.uniol.inf.is.odysseus.lms1xx.monitor.connection;

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
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.lms1xx.monitor.Settings;
import de.uniol.inf.is.odysseus.lms1xx.monitor.LMS1xxConnection;
import de.uniol.inf.is.odysseus.lms1xx.monitor.LMS1xxListener;
import de.uniol.inf.is.odysseus.lms1xx.monitor.model.Measurement;
import de.uniol.inf.is.odysseus.lms1xx.monitor.model.Sample;
import de.uniol.inf.is.odysseus.lms1xx.monitor.ui.LMSScreen;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LMS1xxConnectionImpl implements LMS1xxConnection {
    /**
     * 
     * @author Christian Kuka <christian@kuka.cc>
     * 
     */
	
    public static Measurement parseLmsMessage(String message, Calendar calendar) throws LMS1xxReadErrorException
    {
        final String[] data = message.split(" ");

        if (message.startsWith(LMS1xxConnectionImpl.SRA)) {
            if (LMS1xxConnectionImpl.LCM_STATE.equalsIgnoreCase(data[1])) {
                final int dirtyness = Integer.parseInt(data[2]);
                LMS1xxConnectionImpl.LOG.warn(String.format("Dirtyness %s ", dirtyness));
            }
        }
        else if (message.startsWith(LMS1xxConnectionImpl.SEA)) {
            LMS1xxConnectionImpl.LOG.info(String.format("Receive message %s (%s byte)", message, message.getBytes().length + 2));
        }
        else if (message.startsWith(LMS1xxConnectionImpl.SSN)) {
            if (LMS1xxConnectionImpl.LMD_SCANDATA.equalsIgnoreCase(data[1])) {
                if (data.length >= 19) {
                    final Measurement measurement = new Measurement();
                    int pos = 2;
                    measurement.setVersion(data[pos++]);
                    measurement.setDevice(data[pos++]);
                    measurement.setSerial(data[pos++]);
                    measurement.setStatus(Integer.parseInt(data[pos], 16), Integer.parseInt(data[pos + 1], 16));
                    pos += 2;
                    measurement.setMessageCount(Integer.parseInt(data[pos++], 16));
                    measurement.setScanCount(Integer.parseInt(data[pos++], 16));

                    measurement.setPowerUpDuration(Long.parseLong(data[pos++], 16));
                    measurement.setTransmissionDuration(Long.parseLong(data[pos++], 16));

                    measurement.setInputStatus("3".equals(data[pos]) || "3".equals(data[pos + 1]));
                    pos += 2;
                    measurement.setOutputStatus("7".equals(data[pos]) || "7".equals(data[pos + 1]));
                    pos += 2;
                    // ReservedByteA
                    pos += 1;
                    measurement.setScanningFrequency(Long.parseLong(data[pos++], 16) * 10);
                    measurement.setMeasurementFrequency(Long.parseLong(data[pos++], 16) * 10);

                    measurement.setEncoders(Integer.parseInt(data[pos++], 16));
                    for (int i = 0; i < measurement.getEncoders(); i++) {
                        measurement.setEncoderPosition(Long.parseLong(data[pos++], 16));
                        measurement.setEncoderSpeed(Integer.parseInt(data[pos++], 16));
                    }

                    final int channels16Bit = Integer.parseInt(data[pos++], 16);
                    for (int i = 0; i < channels16Bit; i++) {
                        final String name = data[pos++];
                        if ((name.equalsIgnoreCase(LMS1xxConstants.DIST1)) || (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) || (name.equalsIgnoreCase(LMS1xxConstants.RSSI1))
                                || (name.equalsIgnoreCase(LMS1xxConstants.RSSI2))) {

                            final float scalingFactor = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                            final float scalingOffset = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                            final double startingAngle = Double.longBitsToDouble(Long.parseLong(data[pos++], 16)) / 10000;
                            final double angularStepWidth = ((double) Integer.parseInt(data[pos++], 16)) / 10000;

                            final int samples = Integer.parseInt(data[pos++], 16);
                            if (measurement.get16BitSamples() == null) {
                                measurement.set16BitSamples(new Sample[samples]);
                            }
                            for (int j = 0; j < samples; j++) {
                                if (measurement.get16BitSamples()[j] == null) {
                                    measurement.get16BitSamples()[j] = new Sample(j, Math.toRadians((j * angularStepWidth) + startingAngle));
                                }
                                final float value = (Integer.parseInt(data[pos++], 16) * scalingFactor) + scalingOffset;
                                if (name.equalsIgnoreCase(LMS1xxConstants.DIST1)) {
                                    measurement.get16BitSamples()[j].setDist1(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) {
                                    measurement.get16BitSamples()[j].setDist2(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI1)) {
                                    measurement.get16BitSamples()[j].setRssi1(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI2)) {
                                    measurement.get16BitSamples()[j].setRssi2(value);
                                }
                            }
                        }
                        else {
                            throw new LMS1xxReadErrorException(message);
                        }
                    }
                    final int channels8Bit = Integer.parseInt(data[pos++], 16);
                    for (int i = 0; i < channels8Bit; i++) {
                        final String name = data[pos++];
                        if ((name.equalsIgnoreCase(LMS1xxConstants.DIST1)) || (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) || (name.equalsIgnoreCase(LMS1xxConstants.RSSI1))
                                || (name.equalsIgnoreCase(LMS1xxConstants.RSSI2))) {

                            final float scalingFactor = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                            final float scalingOffset = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                            final double startingAngle = Double.longBitsToDouble(Long.parseLong(data[pos++], 16)) / 10000;
                            final double angularStepWidth = ((double) Integer.parseInt(data[pos++], 16)) / 10000;

                            final int samples = Integer.parseInt(data[pos++], 16);
                            if (measurement.get8BitSamples() == null) {
                                measurement.set8BitSamples(new Sample[samples]);
                            }
                            for (int j = 0; j < samples; j++) {
                                if (measurement.get8BitSamples()[j] == null) {
                                    measurement.get8BitSamples()[j] = new Sample(j, Math.toRadians((j * angularStepWidth) + startingAngle));
                                }
                                final float value = (Integer.parseInt(data[pos++], 16) * scalingFactor) + scalingOffset;
                                if (name.equalsIgnoreCase(LMS1xxConstants.DIST1)) {
                                    measurement.get8BitSamples()[j].setDist1(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) {
                                    measurement.get8BitSamples()[j].setDist2(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI1)) {
                                    measurement.get8BitSamples()[j].setRssi1(value);
                                }
                                else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI2)) {
                                    measurement.get8BitSamples()[j].setRssi2(value);
                                }
                            }

                        }
                        else {
                            throw new LMS1xxReadErrorException(message);
                        }
                    }

                    final int hasPosition = Integer.parseInt(data[pos++], 16);
                    if (hasPosition == 1) {
                        @SuppressWarnings("unused")
                        final float xPosition = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final float yPosition = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final float zPosition = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final float xRotation = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final float yRotation = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final float zRotation = Float.intBitsToFloat(((Long) Long.parseLong(data[pos++], 16)).intValue());
                        @SuppressWarnings("unused")
                        final int rotationType = Integer.parseInt(data[pos++]);
                    }
                    final int hasName = Integer.parseInt(data[pos++], 16);
                    if (hasName == 1) {
                        final StringBuffer buffer = new StringBuffer();
                        String name = data[pos++];
                        while ((!"0".equals(name)) && (!"1".equals(name))) {
                            buffer.append(name);
                            name = data[pos++];
                        }
                        measurement.setName(buffer.toString());
                        pos--;
                    }
                    final int hasComment = Integer.parseInt(data[pos++], 16);
                    if (hasComment == 1) {
                        final StringBuffer buffer = new StringBuffer();
                        String comment = data[pos++];
                        while ((!"0".equals(comment)) && (!"1".equals(comment))) {
                            buffer.append(comment);
                            comment = data[pos++];
                        }
                        measurement.setName(buffer.toString());
                        pos--;
                    }
                    final int hasTimeInfo = Integer.parseInt(data[pos++], 16);
                    if (hasTimeInfo == 1) {
                        final int year = Integer.parseInt(data[pos++], 16);
                        final int month = Integer.parseInt(data[pos++], 16) - 1;
                        final int day = Integer.parseInt(data[pos++], 16);
                        final int hour = Integer.parseInt(data[pos++], 16);
                        final int minute = Integer.parseInt(data[pos++], 16);
                        final int second = Integer.parseInt(data[pos++], 16);
                        final Long milliseconds = Long.parseLong(data[pos++], 16) / 1000;
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, second);
                        calendar.set(Calendar.MILLISECOND, milliseconds.intValue());
                    }
                    final int hasEventInfo = Integer.parseInt(data[pos++], 16);
                    if (hasEventInfo == 1) {
                        @SuppressWarnings("unused")
                        final String eventType = data[pos++];
                        @SuppressWarnings("unused")
                        final int encoderPosition = Integer.parseInt(data[pos++], 16);
                        @SuppressWarnings("unused")
                        final int eventTime = Integer.parseInt(data[pos++], 16);
                        @SuppressWarnings("unused")
                        final int angularPosition = Integer.parseInt(data[pos++], 16);
                    }

                    final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    measurement.setDelay(now.getTimeInMillis() - calendar.getTimeInMillis());
                    return measurement;
                }
            }
        }
        else 
        {
            LMS1xxConnectionImpl.LOG.info(String.format("Receive message %s (%s byte)", message, message.getBytes().length + 2));
        }
		return null;    	
    }	
	
    class SickConnectionHandler extends Thread {

        private final String host;
        private final int port;
        private SocketChannel channel;
        private FileOutputStream rawStream;
        private FileOutputStream csvStream;
        private final Charset charset = Charset.forName("ASCII");
        private final LMS1xxConnectionImpl connection;
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss.SSSZ");

        public SickConnectionHandler(final String host, final int port, final LMS1xxConnectionImpl connection) {
            this.host = host;
            this.port = port;
            this.connection = connection;
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
                    LMS1xxConnectionImpl.LOG.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            try {
                out.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isConnected() {
            if (this.channel != null) {
                return this.channel.isConnected();
            }
            else {
                return false;
            }
        }

        private void onOpen() throws InterruptedException, FileNotFoundException {
            LMS1xxConnectionImpl.LOG.debug("Connected");
            this.send(LMS1xxConstants.SET_ACCESS_MODE_CLIENT_COMMAND);
            Thread.sleep(1000);
            final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddTHHmmssZ");
            final File rawFile = new File(("lms-" + this.host + "-" + sdf.format(now.getTime()) + ".raw"));
            this.rawStream = new FileOutputStream(rawFile, true);
            final File csvFile = new File(("lms-" + this.host + "-" + sdf.format(now.getTime()) + ".csv"));
            this.csvStream = new FileOutputStream(csvFile, true);
            final StringBuilder timeString = new StringBuilder();
            timeString.append(LMS1xxConstants.SET_DATETIME_COMMAND).append(" ");
            timeString.append("+").append(now.get(Calendar.YEAR)).append(" ");
            timeString.append("+").append((now.get(Calendar.MONTH) + 1)).append(" ");
            timeString.append("+").append(now.get(Calendar.DATE)).append(" ");
            timeString.append("+").append(now.get(Calendar.HOUR_OF_DAY)).append(" ");
            timeString.append("+").append(now.get(Calendar.MINUTE)).append(" ");
            timeString.append("+").append(now.get(Calendar.SECOND)).append(" ");
            timeString.append("+").append(now.get(Calendar.MILLISECOND));
            this.send(timeString.toString());
            Thread.sleep(1000);
            this.send(LMS1xxConstants.RUN_COMMAND);
            Thread.sleep(1000);
            this.send(LMS1xxConstants.STOP_SCAN_COMMAND);
            Thread.sleep(1000);
            this.send(LMS1xxConstants.START_SCAN_COMMAND);
        }

        private void onClose() throws IOException {
            try {
                this.send(LMS1xxConstants.STOP_SCAN_COMMAND);
            }
            finally {
                this.rawStream.flush();
                this.rawStream.close();
                this.csvStream.flush();
                this.csvStream.close();
            }
        }
        
        private void onMessage(final String message, final long timestamp) throws LMS1xxReadErrorException, IOException 
        {
        	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        	Measurement measurement = LMS1xxConnectionImpl.parseLmsMessage(message, calendar);
        	
        	if (measurement != null)
        	{
        		if (Settings.LOGGING) 
        		{
        			this.writeCSV(measurement, calendar);
        			this.writeRaw(message.getBytes());
        		}
        		this.connection.onMeasurement(measurement, calendar.getTimeInMillis());
        	}
        }

        private void writeRaw(final byte[] message) throws IOException {
            final byte[] messageBuffer = new byte[message.length + 3];
            messageBuffer[0] = LMS1xxConstants.STX;
            System.arraycopy(message, 0, messageBuffer, 1, message.length);
            messageBuffer[messageBuffer.length - 2] = LMS1xxConstants.ETX;
            messageBuffer[messageBuffer.length - 1] = '\n';
            this.rawStream.getChannel().write(ByteBuffer.wrap(messageBuffer));
        }

        private void writeCSV(final Measurement measurement, final Calendar calendar) throws IOException {
            this.csvStream.getChannel().write(ByteBuffer.wrap((this.sdf.format(calendar.getTime()) + "," + measurement.toCSVString() + System.lineSeparator()).getBytes()));
        }

        @Override
        public void run() {
            try {
                this.channel = SocketChannel.open();
                final InetSocketAddress address = new InetSocketAddress(this.host, this.port);
                this.channel.connect(address);
                this.channel.configureBlocking(true);
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
                            if (buffer.get(i) == LMS1xxConstants.ETX) {
                                buffer.position(i + 1);
                                buffer.flip();
                                final CharBuffer charBuffer = decoder.decode(buffer);
                                try {
                                    this.onMessage(charBuffer.subSequence(1, charBuffer.length() - 1).toString(), System.currentTimeMillis());
                                }
                                catch (final Exception e) {
                                    e.printStackTrace();
                                    if (LMS1xxConnectionImpl.LOG.isDebugEnabled()) {
                                        LMS1xxConnectionImpl.LOG.debug(e.getMessage(), e);
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
                LMS1xxConnectionImpl.LOG.info("SICK connection interrupted");
            }
            catch (final Exception e) {
                LMS1xxConnectionImpl.LOG.error(e.getMessage(), e);
                e.printStackTrace();
            }
            finally {
                if (this.channel != null) {
                    try {
                        this.channel.close();
                    }
                    catch (final IOException e) {
                        LMS1xxConnectionImpl.LOG.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }
        }

        public void send(final String message) {
            int nBytes = 0;
            try {
                final ByteBuffer messageBuffer = ByteBuffer.allocate(message.getBytes(this.charset).length + 2);
                messageBuffer.put(LMS1xxConstants.STX);
                messageBuffer.put(message.getBytes(this.charset));
                messageBuffer.put(LMS1xxConstants.ETX);
                messageBuffer.rewind();
                nBytes = this.channel.write(messageBuffer);
                LMS1xxConnectionImpl.LOG.info(String.format("Send message %s (%s byte)", message, nBytes));
                System.out.println(String.format("Send message %s (%s byte)", message, nBytes));
            }
            catch (final IOException e) {
                LMS1xxConnectionImpl.LOG.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(LMS1xxConnection.class);

    public static final String SRA = "sRA";
    public static final String SEA = "sEA";
    public static final String SSN = "sSN";
    public static final String LCM_STATE = "LCMstate";

    public static final String LMD_SCANDATA = "LMDscandata";
    private SickConnectionHandler handler = null;
    private final String host;
    private final CharsetEncoder encoder;
    private final List<LMS1xxListener> listeners = new ArrayList<LMS1xxListener>();

    public LMS1xxConnectionImpl(final String host, final int port) {
        this.host = host;
        final Charset charset = Charset.defaultCharset();
        this.encoder = charset.newEncoder();
        this.handler = new SickConnectionHandler(host, port, this);
    }

    public LMS1xxConnectionImpl(final LMS1xxConnectionImpl connection) {
        this.host = connection.host;
        this.encoder = connection.encoder;
        this.handler = connection.handler;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new LMS1xxConnectionImpl(this);
    }

    @Override
    public void close() throws IOException {
        this.handler.interrupt();
    }

    @Override
    public void registerListener(final LMSScreen lmsScreen) {
        this.listeners.add(lmsScreen);
    }

    @Override
    public boolean isConnected() {
        return this.handler.isConnected();
    }

    public void onMeasurement(final Measurement measurement, final long timestamp) {
        for (final LMS1xxListener listener : this.listeners) {
            listener.onScan(measurement);
        }
    }

    @Override
    public void open() throws FileNotFoundException {
        this.handler.start();

    }
}
