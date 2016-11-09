/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
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
package de.uniol.inf.is.odysseus.wrapper.lms1xx.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractFileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.LMS1xxConstants;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.LMS1xxLoginException;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.LMS1xxReadErrorException;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.LMS1xxUnknownMessageException;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Sample;

/**
 * Protocol Handler for the SICK protocol supporting LMS100 and LMS151 laser
 * scanner
 *
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LMS1xxProtocolHandler extends
		LineProtocolHandler<KeyValueObject<IMetaAttribute>> {
	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(LMS1xxProtocolHandler.class);
	/** The name of the protocol handler. */
	public static final String NAME = "LMS1xx";

	/** Init parameter. */
	public static final String BYTEORDER = "byteorder";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String INIT_RAWDATA = "rawdata";
	public static final String IGNORE_TIMESTAMP = "ignoretimestamp";
	public static final String PASSIVE_MODE = "passivemode";

	/** Key value parameter names. */
	private final static String RAW_DATA = "RAWDATA";
	private final static String TIMESTAMP_ATTRIBUTE = "TIMESTAMP";
	private final static String VERSION_ATTRIBUTE = "VERSION";
	private final static String DEVICE_ATTRIBUTE = "DEVICE";
	private final static String SERIAL_ATTRIBUTE = "SERIAL";
	private final static String STATUS_ATTRIBUTE = "STATUS";
	private final static String MESSAGE_COUNT_ATTRIBUTE = "MESSAGECOUNT";
	private final static String SCAN_COUNT_ATTRIBUTE = "SCANCOUNT";
	private final static String POWERUP_DURATION_ATTRIBUTE = "POWERUPDURATION";
	private final static String TRANSMISSION_DURATION_ATTRIBUTE = "TRANSMISSIONDURATION";
	private final static String INPUT_STATUS_ATTRIBUTE = "INPUTSTATUS";
	private final static String OUTPUT_STATUS_ATTRIBUTE = "OUTPUTSTATUS";
	private final static String SCANNING_FREQUENCY_ATTRIBUTE = "SCANNINGFREQUENCY";
	private final static String MEASUREMENT_FREQUENCY_ATTRIBUTE = "MEASUREMENTFREQUENCY";
	private final static String POSITION_ATTRIBUTE = "POSITION";
	private final static String POSITIONROTATIONTYPE_ATTRIBUTE = "POSITIONROTATIONTYPE";
	private final static String DISTANCE_8BIT_ATTRIBUTE = "DISTANCE8BIT";
	private final static String REMISSION_8BIT_ATTRIBUTE = "REMISSION8BIT";
	private final static String DISTANCE2_8BIT_ATTRIBUTE = "DISTANCE8BIT2";
	private final static String REMISSION2_8BIT_ATTRIBUTE = "REMISSION8BIT2";
	private final static String DISTANCE_16BIT_ATTRIBUTE = "DISTANCE16BIT";
	private final static String REMISSION_16BIT_ATTRIBUTE = "REMISSION16BIT";
	private final static String DISTANCE2_16BIT_ATTRIBUTE = "DISTANCE16BIT2";
	private final static String REMISSION2_16BIT_ATTRIBUTE = "REMISSION16BIT2";

	private final Charset charset = Charset.forName("ASCII");

	private ByteOrder byteOrder;
	private String password;
	private String username;
	private ByteBuffer buffer;
	private boolean addRawData;
	private boolean ignoreTimestamp;
	private boolean passiveMode;

	/**
     *
     */
	public LMS1xxProtocolHandler() {
		super();
	}

	/**
	 *
	 * @param direction
	 * @param access
	 * @param dataHandler
	 */
	public LMS1xxProtocolHandler(
			final ITransportDirection direction,
			final IAccessPattern access,
			final IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		init_internal();
	}

	private void init_internal() {
		this.buffer = ByteBuffer.allocateDirect(1024);
		setByteOrder(optionsMap.get(BYTEORDER, "BIG_ENDIAN"));
		setUsername(optionsMap.get(USERNAME, "client"));
		setPassword(optionsMap.get(PASSWORD, "client"));
		setAddRawData(optionsMap.getBoolean(INIT_RAWDATA, false));
		setIgnoreTimestamp(optionsMap.getBoolean(IGNORE_TIMESTAMP, false));
		setPassiveMode(optionsMap.getBoolean(PASSIVE_MODE, false));
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return LMS1xxProtocolHandler.NAME;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(
			final ITransportDirection direction,
			final IAccessPattern access,
			final OptionMap options,
			final IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		final LMS1xxProtocolHandler instance = new LMS1xxProtocolHandler(
				direction, access, dataHandler, options);
		return instance;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onConnect(final ITransportHandler caller) {
		if (!(caller instanceof AbstractFileHandler) && !passiveMode) {
			// if
			// (caller.getExchangePattern().equals(ITransportExchangePattern.InOut))
			// {
			try {
				LMS1xxProtocolHandler.LOG.debug("Connected");
				if (username.equalsIgnoreCase("maintainer")) {
					send(LMS1xxConstants.SET_ACCESS_MODE_MAINTAINER_COMMAND.getBytes(this.charset));
				} else if (this.username.equalsIgnoreCase("client")) {
					send(LMS1xxConstants.SET_ACCESS_MODE_CLIENT_COMMAND.getBytes(this.charset));
				} else if (this.username.equalsIgnoreCase("service")) {
					send(LMS1xxConstants.SET_ACCESS_MODE_SERVICE_COMMAND.getBytes(this.charset));
				}

				TimeZone tz = TimeZone.getTimeZone("UTC");
				Calendar now = Calendar.getInstance(tz);
				SimpleDateFormat df = new SimpleDateFormat("+yyyy +M +d +h +m +s +S");
				df.setTimeZone(tz);
				String timeString = LMS1xxConstants.SET_DATETIME_COMMAND + " " + df.format(now.getTime());
				send(timeString.toString().getBytes(charset));

				send(LMS1xxConstants.RUN_COMMAND.getBytes(charset));

				send(LMS1xxConstants.STOP_SCAN_COMMAND.getBytes(charset));

				send(LMS1xxConstants.START_SCAN_COMMAND.getBytes(charset));

			} catch (IOException e) {
				LMS1xxProtocolHandler.LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public KeyValueObject<IMetaAttribute> getNext()
			throws IOException {
		final String line = super.getNextLine(reader);
		final CharsetEncoder encoder = this.charset.newEncoder();
		// Check if the first value is STX or just STX as a char (50)
		if (line.startsWith("2")) {
			this.checkOverflow(line.length());
			this.buffer.put(LMS1xxConstants.STX);
			this.buffer.put(encoder.encode(CharBuffer.wrap(line.substring(1,
					line.length() - 1))));
			this.buffer.put(LMS1xxConstants.ETX);
		} else {
			this.checkOverflow(line.length());
			this.buffer.put(encoder.encode(CharBuffer.wrap(line)));
		}
		this.buffer.flip();
		KeyValueObject<IMetaAttribute> object = null;
		try {
			object = this.parse();
			if (object == null) {
				object = this.getNext();
			}
		} catch (LMS1xxReadErrorException | LMS1xxLoginException
				| LMS1xxUnknownMessageException e) {
			LMS1xxProtocolHandler.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} finally {
			this.buffer.clear();
		}
		return object;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void process(long callerId, final ByteBuffer message) {
		// TODO: check if callerId is relevant
		int startPosition = 0;
		while (message.remaining() > 0) {
			final byte value = message.get();
			if (value == LMS1xxConstants.ETX) {
				final int endPosition = message.position() - 1;
				message.position(startPosition);
				final int size = (endPosition - message.position()) + 1;
				this.checkOverflow(size);
				this.buffer.put(message.array(), message.position(), size);
				message.position(endPosition + 1);
				startPosition = message.position();
				this.buffer.flip();
				KeyValueObject<IMetaAttribute> object = null;
				try {
					object = this.parse();
					if (object != null) {
						this.getTransfer().transfer(object);
					}
				} catch (LMS1xxReadErrorException | LMS1xxLoginException
						| LMS1xxUnknownMessageException e) {
					LMS1xxProtocolHandler.LOG.error(e.getMessage(), e);
					throw new RuntimeException(e);
				} finally {
					this.buffer.clear();
				}
			}
			if (value == LMS1xxConstants.STX) {
				this.buffer.clear();
				startPosition = message.position() - 1;
			}
		}
		if (startPosition < message.limit()) {
			message.position(startPosition);
			this.checkOverflow(message.remaining());
			this.buffer.put(message);
		}
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onDisonnect(final ITransportHandler caller) {
		if (caller.getExchangePattern().equals(ITransportExchangePattern.InOut) && !passiveMode) {
			try {
				this.send(LMS1xxConstants.STOP_SCAN_COMMAND
						.getBytes(this.charset));
			} catch (final IOException e) {
				LMS1xxProtocolHandler.LOG.error(e.getMessage(), e);
			}
			LMS1xxProtocolHandler.LOG.debug("Disconnected");
		}
		getTransfer().propagateDone();
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void write(final KeyValueObject<IMetaAttribute> object)
			throws IOException {
		// TODO 20140321 christian@kuka.cc Implement reverse protocol
		this.writer.write(object.toString());
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
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * @return the byteOrder
	 */
	public ByteOrder getByteOrder() {
		return this.byteOrder;
	}

	/**
	 * @param byteOrder
	 *            the byteOrder to set
	 */
	public void setByteOrder(final ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	/**
	 * @param byteOrder
	 *            the byteOrder to set
	 */
	public void setByteOrder(final String byteOrder) {
		if ("LITTLE_ENDIAN".equalsIgnoreCase(byteOrder)) {
			this.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		} else {
			this.setByteOrder(ByteOrder.BIG_ENDIAN);
		}
	}

	public boolean doesAddRawData() {
		return addRawData;
	}

	public void setAddRawData(boolean addRawData) {
		this.addRawData = addRawData;
	}

	public void setIgnoreTimestamp(boolean ignore) {
		ignoreTimestamp  = ignore;
	}

	public boolean doesIgnoreTimestamp() {
		return ignoreTimestamp;
	}

	public void setPassiveMode(boolean passiveMode) {
		this.passiveMode = passiveMode;
	}

	public boolean isInPassiveMode() {
		return passiveMode;
	}

	private KeyValueObject<IMetaAttribute> parse()
			throws LMS1xxReadErrorException, LMS1xxLoginException,
			LMS1xxUnknownMessageException {
		final CharsetDecoder decoder = this.charset.newDecoder();

		// Skip STX byte
		this.buffer.get();
		// Skip ETX byte
		this.buffer.limit(this.buffer.limit() - 1);
		try {
			final CharBuffer charBuffer = decoder.decode(this.buffer);
			return this.parseLMS1xx(charBuffer.toString());
		} catch (final CharacterCodingException e) {
			throw new LMS1xxReadErrorException(e.getMessage(), e);
		}
	}

	public static Measurement parseLMS1xxScanData(
			final String[] data) {
		Preconditions.checkPositionIndex(19, data.length);
		final Measurement measurement = new Measurement();
		final Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		int pos = 2;
		measurement.setVersion(data[pos++]);
		measurement.setDevice(data[pos++]);
		measurement.setSerial(data[pos++]);
		measurement.setStatus(Integer.parseInt(data[pos], 16),
				Integer.parseInt(data[pos + 1], 16));
		pos += 2;
		measurement.setMessageCount(Integer.parseInt(data[pos++], 16));
		measurement.setScanCount(Integer.parseInt(data[pos++], 16));

		measurement.setPowerUpDuration(Long.parseLong(data[pos++], 16));
		measurement.setTransmissionDuration(Long.parseLong(data[pos++], 16));

		measurement.setInputStatus("3".equals(data[pos])
				|| "3".equals(data[pos + 1]));
		pos += 2;
		measurement.setOutputStatus("7".equals(data[pos])
				|| "7".equals(data[pos + 1]));
		pos += 2;
		// ReservedByteA
		pos += 1;
		measurement.setScanningFrequency(Long.parseLong(data[pos++], 16) * 10);
		measurement
				.setMeasurementFrequency(Long.parseLong(data[pos++], 16) * 10);

		measurement.setEncoders(Integer.parseInt(data[pos++], 16));
		for (int i = 0; i < measurement.getEncoders(); i++) {
			measurement.setEncoderPosition(Long.parseLong(data[pos++], 16));
			measurement.setEncoderSpeed(Integer.parseInt(data[pos++], 16));
		}

		final int channels16Bit = Integer.parseInt(data[pos++], 16);
		for (int i = 0; i < channels16Bit; i++) {
			final String name = data[pos++];
			if ((name.equalsIgnoreCase(LMS1xxConstants.DIST1))
					|| (name.equalsIgnoreCase(LMS1xxConstants.DIST2))
					|| (name.equalsIgnoreCase(LMS1xxConstants.RSSI1))
					|| (name.equalsIgnoreCase(LMS1xxConstants.RSSI2))) {

				final float scalingFactor = Float.intBitsToFloat(((Long) Long
						.parseLong(data[pos++], 16)).intValue());
				final float scalingOffset = Float.intBitsToFloat(((Long) Long
						.parseLong(data[pos++], 16)).intValue());
				final double startingAngle = Double.longBitsToDouble(Long
						.parseLong(data[pos++], 16)) / 10000;
				final double angularStepWidth = ((double) Integer.parseInt(
						data[pos++], 16)) / 10000;

				final int samples = Integer.parseInt(data[pos++], 16);
				if (measurement.get16BitSamples() == null) {
					measurement.set16BitSamples(new Sample[samples]);
				}
				for (int j = 0; j < samples; j++) {
					if (measurement.get16BitSamples()[j] == null) {
						measurement.get16BitSamples()[j] = new Sample(j,
								Math.toRadians((j * angularStepWidth)
										+ startingAngle));
					}
					final float value = (Integer.parseInt(data[pos++], 16) * scalingFactor)
							+ scalingOffset;
					if (name.equalsIgnoreCase(LMS1xxConstants.DIST1)) {
						measurement.get16BitSamples()[j].setDist1(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) {
						measurement.get16BitSamples()[j].setDist2(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI1)) {
						measurement.get16BitSamples()[j].setRssi1(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI2)) {
						measurement.get16BitSamples()[j].setRssi2(value);
					}

				}
			}
		}
		final int channels8Bit = Integer.parseInt(data[pos++], 16);
		for (int i = 0; i < channels8Bit; i++) {
			final String name = data[pos++];
			if ((name.equalsIgnoreCase(LMS1xxConstants.DIST1))
					|| (name.equalsIgnoreCase(LMS1xxConstants.DIST2))
					|| (name.equalsIgnoreCase(LMS1xxConstants.RSSI1))
					|| (name.equalsIgnoreCase(LMS1xxConstants.RSSI2))) {

				final float scalingFactor = Float.intBitsToFloat(((Long) Long
						.parseLong(data[pos++], 16)).intValue());
				final float scalingOffset = Float.intBitsToFloat(((Long) Long
						.parseLong(data[pos++], 16)).intValue());
				final double startingAngle = Double.longBitsToDouble(Long
						.parseLong(data[pos++], 16)) / 10000;
				final double angularStepWidth = ((double) Integer.parseInt(
						data[pos++], 16)) / 10000;

				final int samples = Integer.parseInt(data[pos++], 16);
				if (measurement.get8BitSamples() == null) {
					measurement.set8BitSamples(new Sample[samples]);
				}
				for (int j = 0; j < samples; j++) {
					if (measurement.get8BitSamples()[j] == null) {
						measurement.get8BitSamples()[j] = new Sample(j,
								Math.toRadians((j * angularStepWidth)
										+ startingAngle));
					}

					final float value = (Integer.parseInt(data[pos++], 16) * scalingFactor)
							+ scalingOffset;
					if (name.equalsIgnoreCase(LMS1xxConstants.DIST1)) {
						measurement.get8BitSamples()[j].setDist1(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.DIST2)) {
						measurement.get8BitSamples()[j].setDist2(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI1)) {
						measurement.get8BitSamples()[j].setRssi1(value);
					} else if (name.equalsIgnoreCase(LMS1xxConstants.RSSI2)) {
						measurement.get8BitSamples()[j].setRssi2(value);
					}

				}

			}

		}

		final int hasPosition = Integer.parseInt(data[pos++], 16);
		if (hasPosition == 1) {
			final double[][] position = new double[3][3];
			position[0][0] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			position[1][0] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			position[2][0] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			position[0][1] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			position[1][1] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			position[2][1] = Float.intBitsToFloat(((Long) Long.parseLong(
					data[pos++], 16)).intValue());
			final int rotationType = Integer.parseInt(data[pos++]);
			measurement.setPosition(position);
			measurement.setPositionRotationType(rotationType);
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

		measurement.setTimeStamp(calendar.getTimeInMillis());

		return measurement;
	}

	public static Map<String, Object> measurementToMap(Measurement measurement)
	{
		final Map<String, Object> event = new HashMap<String, Object>();
		event.put(LMS1xxProtocolHandler.TIMESTAMP_ATTRIBUTE,
				measurement.getTimeStamp());
		event.put(LMS1xxProtocolHandler.VERSION_ATTRIBUTE,
				measurement.getVersion());
		event.put(LMS1xxProtocolHandler.DEVICE_ATTRIBUTE,
				measurement.getDevice());
		event.put(LMS1xxProtocolHandler.SERIAL_ATTRIBUTE,
				measurement.getSerial());

		event.put(LMS1xxProtocolHandler.STATUS_ATTRIBUTE,
				measurement.getStatus());
		event.put(LMS1xxProtocolHandler.MESSAGE_COUNT_ATTRIBUTE,
				measurement.getMessageCount());
		event.put(LMS1xxProtocolHandler.SCAN_COUNT_ATTRIBUTE,
				measurement.getScanCount());
		event.put(LMS1xxProtocolHandler.POWERUP_DURATION_ATTRIBUTE,
				measurement.getPowerUpDuration());
		event.put(LMS1xxProtocolHandler.TRANSMISSION_DURATION_ATTRIBUTE,
				measurement.getTransmissionDuration());
		event.put(LMS1xxProtocolHandler.INPUT_STATUS_ATTRIBUTE,
				measurement.isInputStatus());
		event.put(LMS1xxProtocolHandler.OUTPUT_STATUS_ATTRIBUTE,
				measurement.isOutputStatus());
		event.put(LMS1xxProtocolHandler.SCANNING_FREQUENCY_ATTRIBUTE,
				measurement.getScanningFrequency());
		event.put(LMS1xxProtocolHandler.MEASUREMENT_FREQUENCY_ATTRIBUTE,
				measurement.getMeasurementFrequency());
		if (measurement.getPosition() != null) {
			event.put(LMS1xxProtocolHandler.POSITION_ATTRIBUTE,
					measurement.getPosition());
			event.put(LMS1xxProtocolHandler.POSITIONROTATIONTYPE_ATTRIBUTE,
					measurement.getPositionRotationType());
		}
		if (measurement.get8BitSamples() != null) {
			final double[][] distance8Bit = new double[measurement
					.get8BitSamples().length][2];
			final double[] remission8Bit = new double[measurement
					.get8BitSamples().length];
			final double[][] distance8Bit2 = new double[measurement
					.get8BitSamples().length][2];
			final double[] remission8Bit2 = new double[measurement
					.get8BitSamples().length];
			for (int i = 0; i < measurement.get8BitSamples().length; i++) {
				final Sample sample = measurement.get8BitSamples()[i];
				distance8Bit[i][0] = sample.getDist1();
				distance8Bit[i][1] = sample.getAngle();
				remission8Bit[i] = sample.getRssi1();
				distance8Bit2[i][0] = sample.getDist2();
				distance8Bit2[i][1] = sample.getAngle();
				remission8Bit2[i] = sample.getRssi2();
			}
			event.put(LMS1xxProtocolHandler.DISTANCE_8BIT_ATTRIBUTE,
					distance8Bit);
			event.put(LMS1xxProtocolHandler.REMISSION_8BIT_ATTRIBUTE,
					remission8Bit);
			event.put(LMS1xxProtocolHandler.DISTANCE2_8BIT_ATTRIBUTE,
					distance8Bit2);
			event.put(LMS1xxProtocolHandler.REMISSION2_8BIT_ATTRIBUTE,
					remission8Bit2);
		}
		if (measurement.get16BitSamples() != null) {
			final double[][] distance16Bit = new double[measurement
					.get16BitSamples().length][2];
			final double[] remission16Bit = new double[measurement
					.get16BitSamples().length];
			final double[][] distance16Bit2 = new double[measurement
					.get16BitSamples().length][2];
			final double[] remission16Bit2 = new double[measurement
					.get16BitSamples().length];
			for (int i = 0; i < measurement.get16BitSamples().length; i++) {
				final Sample sample = measurement.get16BitSamples()[i];
				distance16Bit[i][0] = sample.getDist1();
				distance16Bit[i][1] = sample.getAngle();
				remission16Bit[i] = sample.getRssi1();
				distance16Bit2[i][0] = sample.getDist2();
				distance16Bit2[i][1] = sample.getAngle();
				remission16Bit2[i] = sample.getRssi2();
			}
			event.put(LMS1xxProtocolHandler.DISTANCE_16BIT_ATTRIBUTE,
					distance16Bit);
			event.put(LMS1xxProtocolHandler.REMISSION_16BIT_ATTRIBUTE,
					remission16Bit);
			event.put(LMS1xxProtocolHandler.DISTANCE2_16BIT_ATTRIBUTE,
					distance16Bit2);
			event.put(LMS1xxProtocolHandler.REMISSION2_16BIT_ATTRIBUTE,
					remission16Bit2);
		}

		return event;
	}

	private KeyValueObject<IMetaAttribute> parseLMS1xx(final String message)
			throws LMS1xxLoginException,	LMS1xxUnknownMessageException
	{
		final String[] data = message.split(" ");
		if (message.startsWith(LMS1xxConstants.SRA))
		{
			if (LMS1xxConstants.LCM_STATE.equalsIgnoreCase(data[1]))
			{
				final int dirtyness = Integer.parseInt(data[2]);
				LMS1xxProtocolHandler.LOG.info("Dirtyness {} ", dirtyness);
			}
		}
		else if (message.startsWith(LMS1xxConstants.SEA))
		{
			LMS1xxProtocolHandler.LOG.debug("Receive message {} ({} byte)", message, message.getBytes().length);
		}
		else if (message.startsWith(LMS1xxConstants.SAN))
		{
			if (LMS1xxConstants.SET_ACCESS_MODE.equalsIgnoreCase(data[1]))
			{
				LMS1xxProtocolHandler.LOG.info("Login success {}", data[2]);
			}
			else if (LMS1xxConstants.SET_DATETIME.equalsIgnoreCase(data[1]))
			{
				LMS1xxProtocolHandler.LOG.info("Set date time success {}", data[2]);
			}
			else
			{
				LMS1xxProtocolHandler.LOG.debug("Receive message {} ({} byte)", message, message.getBytes().length);
			}
		}
		else if (message.startsWith(LMS1xxConstants.SSN))
		{
			if (LMS1xxConstants.LMD_SCANDATA.equalsIgnoreCase(data[1]))
			{
				Measurement measurement = parseLMS1xxScanData(data);
//				System.out.println("Timestamp diff = " + (measurement.getTimeStamp() - System.currentTimeMillis()));

				if (ignoreTimestamp)
					measurement.setTimeStamp(System.currentTimeMillis());

				Map<String, Object> event = measurementToMap(measurement);

				if (addRawData)
					event.put(LMS1xxProtocolHandler.RAW_DATA, (char) LMS1xxConstants.STX + message + (char) LMS1xxConstants.ETX);

				return KeyValueObject.createInstance(event);
			}
		}
		else if (message.startsWith(LMS1xxConstants.SFA))
			throw new LMS1xxLoginException(message);
		else
			throw new LMS1xxUnknownMessageException(message);

		return null;
	}

	private void send(final byte[] message) throws IOException {
		final CharsetDecoder decoder = this.charset.newDecoder();
		final byte[] messageBuffer = new byte[message.length + 2];
		messageBuffer[0] = LMS1xxConstants.STX;
		System.arraycopy(message, 0, messageBuffer, 1, message.length);
		messageBuffer[messageBuffer.length - 1] = LMS1xxConstants.ETX;
		this.getTransportHandler().send(messageBuffer);

		LMS1xxProtocolHandler.LOG.debug("SICK: Send message {}",
				decoder.decode(ByteBuffer.wrap(message)));
	}

	private void checkOverflow(final int size) {
		if ((size + this.buffer.position()) >= this.buffer.capacity()) {
			final ByteBuffer newBuffer = ByteBuffer.allocate((this.buffer
					.limit() + size + this.buffer.position()) * 2);
			final int pos = this.buffer.position();
			this.buffer.flip();
			newBuffer.put(this.buffer);
			this.buffer = newBuffer;
			this.buffer.position(pos);
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> other) {
		if (!(other instanceof LMS1xxProtocolHandler)) {
			return false;
		}
		return true;
	}
}
