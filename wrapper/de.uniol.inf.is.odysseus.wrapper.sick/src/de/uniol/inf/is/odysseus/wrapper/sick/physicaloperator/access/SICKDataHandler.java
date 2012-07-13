/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.sick.physicaloperator.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.wrapper.sick.SICKConstants;
import de.uniol.inf.is.odysseus.wrapper.sick.impl.SickReadErrorException;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Sample;

/**
 * Data Handler for the SICK protocol supporting LMS100 and LMS151 laser scanner
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SICKDataHandler extends AbstractDataHandler<Tuple<?>> {
	private final static Logger LOG = LoggerFactory
			.getLogger(SICKDataHandler.class);
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Tuple");
	}

	IDataHandler<?>[] dataHandlers = null;
	private SDFSchema schema;
	private final Charset charset = Charset.forName("ASCII");
	private final static String VERSION_ATTRIBUTE = "Version";
	private final static String DEVICE_ATTRIBUTE = "Device";
	private final static String SERIAL_ATTRIBUTE = "Serial";
	private final static String STATUS_ATTRIBUTE = "Status";
	private final static String MESSAGE_COUNT_ATTRIBUTE = "MessageCount";
	private final static String SCAN_COUNT_ATTRIBUTE = "ScanCount";
	private final static String POWERUP_DURATION_ATTRIBUTE = "PowerUpDuration";
	private final static String TRANSMISSION_DURATION_ATTRIBUTE = "TransmissionDuration";
	private final static String INPUT_STATUS_ATTRIBUTE = "InputStatus";
	private final static String OUTPUT_STATUS_ATTRIBUTE = "OutputStatus";
	private final static String SCANNING_FREQUENCY_ATTRIBUTE = "ScanningFrequency";
	private final static String MEASUREMENT_FREQUENCY_ATTRIBUTE = "MeasurementFrequency";

	public SICKDataHandler() {
	}

	/**
	 * Create a new SCAI Data Handler
	 * 
	 * @param schema
	 */
	public SICKDataHandler(SDFSchema schema) {
		this.schema = schema;
	}

	@Override
	public IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new SICKDataHandler(schema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * nio.ByteBuffer)
	 */
	@Override
	public Tuple<?> readData(ByteBuffer buffer) {
		final CharsetDecoder decoder = this.charset.newDecoder();
		CharBuffer charBuffer;
		try {
			charBuffer = decoder.decode(buffer);
			return readData(charBuffer.toString());
		} catch (CharacterCodingException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * io.ObjectInputStream)
	 */
	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		StringBuilder inputBuffer = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));
		String buffer = "";
		while ((buffer = in.readLine()) != null) {
			inputBuffer.append(buffer);
		}
		return readData(inputBuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * lang.String)
	 */
	@Override
	public Tuple<?> readData(String string) {
		try {
			return process(string);
		} catch (SickReadErrorException e) {
			LOG.warn(e.getMessage(), e);
			dumpPackage(string);
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java
	 * .nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang
	 * .Object)
	 */
	@Override
	public int memSize(Object attribute) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#
	 * getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

	/**
	 * Process an incoming measurement from the laser scanner
	 * 
	 * @param message
	 *            the received packet including distance and remissions
	 * @return a tuple with the distance, remissions, and additional status
	 *         information depending on the given schema
	 * @throws SickReadErrorException
	 */
	@SuppressWarnings("rawtypes")
	private Tuple<?> process(String message) throws SickReadErrorException {
		Tuple<?> ret = null;
		final String[] data = message.split(" ");
		if (message.startsWith(SICKConstants.SRA)) {
			if (SICKConstants.LCM_STATE.equalsIgnoreCase(data[1])) {
				final int dirtyness = Integer.parseInt(data[2]);
				LOG.warn(String.format("Dirtyness %s ", dirtyness));
			}
		} else if (message.startsWith(SICKConstants.SEA)) {
			LOG.debug(String.format("Receive message %s (%s byte)", message,
					message.getBytes().length + 2));
		} else if (message.startsWith(SICKConstants.SSN)) {
			if (SICKConstants.LMD_SCANDATA.equalsIgnoreCase(data[1])) {
				if (data.length >= 19) {
					final Measurement measurement = new Measurement();
					Calendar calendar = Calendar.getInstance(TimeZone
							.getTimeZone("UTC"));
					try {
						int pos = 2;
						measurement.setVersion(data[pos++]);
						measurement.setDevice(data[pos++]);
						measurement.setSerial(data[pos++]);
						measurement.setStatus(Integer.parseInt(data[pos], 16),
								Integer.parseInt(data[pos + 1], 16));
						pos += 2;
						measurement.setMessageCount(Integer.parseInt(
								data[pos++], 16));
						measurement.setScanCount(Integer.parseInt(data[pos++],
								16));

						measurement.setPowerUpDuration(Long.parseLong(
								data[pos++], 16));
						measurement.setTransmissionDuration(Long.parseLong(
								data[pos++], 16));

						measurement.setInputStatus("3".equals(data[pos])
								|| "3".equals(data[pos + 1]));
						pos += 2;
						measurement.setOutputStatus("7".equals(data[pos])
								|| "7".equals(data[pos + 1]));
						pos += 2;
						// ReservedByteA
						pos += 1;
						measurement.setScanningFrequency(Long.parseLong(
								data[pos++], 16) * 10);
						measurement.setMeasurementFrequency(Long.parseLong(
								data[pos++], 16) * 10);

						measurement.setEncoders(Integer.parseInt(data[pos++],
								16));
						for (int i = 0; i < measurement.getEncoders(); i++) {
							// TODO Support encoders
							// measurement.setEncoderPosition(Long.parseLong(
							// data[pos++], 16));
							// measurement.setEncoderSpeed(Integer.parseInt(
							// data[pos++], 16));

							pos += 2;
						}

						final int channels16Bit = Integer.parseInt(data[pos++],
								16);
						for (int i = 0; i < channels16Bit; i++) {
							final String name = data[pos++];
							if ((name.equalsIgnoreCase(SICKConstants.DIST1))
									|| (name.equalsIgnoreCase(SICKConstants.DIST2))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI1))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI2))) {

								final float scalingFactor = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[pos++], 16)).intValue());
								final float scalingOffset = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[pos++], 16)).intValue());
								final double startingAngle = Double
										.longBitsToDouble(Long.parseLong(
												data[pos++], 16)) / 10000;
								final double angularStepWidth = ((double) Integer
										.parseInt(data[pos++], 16)) / 10000;

								final int samples = Integer.parseInt(
										data[pos++], 16);
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
												data[pos++], 16)
												* scalingFactor + scalingOffset;
										if (name.equalsIgnoreCase(SICKConstants.DIST1)) {
											measurement.getSamples()[j]
													.setDist1(value);

										} else if (name
												.equalsIgnoreCase(SICKConstants.DIST2)) {
											measurement.getSamples()[j]
													.setDist2(value);
										} else if (name
												.equalsIgnoreCase(SICKConstants.RSSI1)) {
											measurement.getSamples()[j]
													.setRssi1(value);
										} else if (name
												.equalsIgnoreCase(SICKConstants.RSSI2)) {
											measurement.getSamples()[j]
													.setRssi2(value);
										}
									} catch (final Exception e) {
										throw new SickReadErrorException(
												message);
									}
								}
							} else {
								throw new SickReadErrorException(message);
							}
						}
						final int channels8Bit = Integer.parseInt(data[pos++],
								16);
						for (int i = 0; i < channels8Bit; i++) {
							final String name = data[pos++];
							if ((name.equalsIgnoreCase(SICKConstants.DIST1))
									|| (name.equalsIgnoreCase(SICKConstants.DIST2))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI1))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI2))) {

								final float scalingFactor = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[pos++], 16)).intValue());
								final float scalingOffset = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[pos++], 16)).intValue());
								@SuppressWarnings("unused")
								final double startingAngle = Double
										.longBitsToDouble(Long.parseLong(
												data[pos++], 16)) / 10000;
								@SuppressWarnings("unused")
								final double angularStepWidth = ((double) Integer
										.parseInt(data[pos++], 16)) / 10000;

								final int samples = Integer.parseInt(
										data[pos++], 16);
								if (measurement.getSamples() == null) {
									measurement.setSamples(new Sample[samples]);
								}
								for (int j = 0; j < samples; j++) {
									final float value = Integer.parseInt(
											data[pos++], 16)
											* scalingFactor
											+ scalingOffset;
									if (name.equalsIgnoreCase(SICKConstants.DIST1)) {
										measurement.getSamples()[j]
												.setDist1(value);

									} else if (name
											.equalsIgnoreCase(SICKConstants.DIST2)) {
										measurement.getSamples()[j]
												.setDist2(value);
									} else if (name
											.equalsIgnoreCase(SICKConstants.RSSI1)) {
										measurement.getSamples()[j]
												.setRssi1(value);
									} else if (name
											.equalsIgnoreCase(SICKConstants.RSSI2)) {
										measurement.getSamples()[j]
												.setRssi2(value);
									}
								}

							} else {
								throw new SickReadErrorException(message);
							}
						}

						int hasPosition = Integer.parseInt(data[pos++], 16);
						if (hasPosition == 1) {
							@SuppressWarnings("unused")
							float xPosition = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							float yPosition = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							float zPosition = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							float xRotation = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							float yRotation = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							float zRotation = Float.intBitsToFloat(((Long) Long
									.parseLong(data[pos++], 16)).intValue());
							@SuppressWarnings("unused")
							int rotationType = Integer.parseInt(data[pos++]);
						}
						int hasName = Integer.parseInt(data[pos++], 16);
						if (hasName == 1) {
							StringBuffer buffer = new StringBuffer();
							String name = data[pos++];
							while ((!"0".equals(name)) && (!"1".equals(name))) {
								buffer.append(name);
								name = data[pos++];
							}
							measurement.setName(buffer.toString());
							pos--;
						}
						int hasComment = Integer.parseInt(data[pos++], 16);
						if (hasComment == 1) {
							StringBuffer buffer = new StringBuffer();
							String comment = data[pos++];
							while ((!"0".equals(comment))
									&& (!"1".equals(comment))) {
								buffer.append(comment);
								comment = data[pos++];
							}
							measurement.setName(buffer.toString());
							pos--;
						}
						int hasTimeInfo = Integer.parseInt(data[pos++], 16);
						if (hasTimeInfo == 1) {
							int year = Integer.parseInt(data[pos++], 16);
							int month = Integer.parseInt(data[pos++], 16) - 1;
							int day = Integer.parseInt(data[pos++], 16);
							int hour = Integer.parseInt(data[pos++], 16);
							int minute = Integer.parseInt(data[pos++], 16);
							int second = Integer.parseInt(data[pos++], 16);
							Long milliseconds = Long.parseLong(data[pos++], 16) / 1000;
							calendar.set(Calendar.YEAR, year);
							calendar.set(Calendar.MONTH, month);
							calendar.set(Calendar.DAY_OF_MONTH, day);
							calendar.set(Calendar.HOUR_OF_DAY, hour);
							calendar.set(Calendar.MINUTE, minute);
							calendar.set(Calendar.SECOND, second);
							calendar.set(Calendar.MILLISECOND,
									milliseconds.intValue());
						}
						int hasEventInfo = Integer.parseInt(data[pos++], 16);
						if (hasEventInfo == 1) {
							@SuppressWarnings("unused")
							String eventType = data[pos++];
							@SuppressWarnings("unused")
							int encoderPosition = Integer.parseInt(data[pos++],
									16);
							@SuppressWarnings("unused")
							int eventTime = Integer.parseInt(data[pos++], 16);
							@SuppressWarnings("unused")
							int angularPosition = Integer.parseInt(data[pos++],
									16);
						}
					} catch (final Exception e) {
						throw new SickReadErrorException(message);
					}
					final Map<String, Object> event = new HashMap<String, Object>();
					event.put(VERSION_ATTRIBUTE, measurement.getVersion());
					event.put(DEVICE_ATTRIBUTE, measurement.getDevice());
					event.put(SERIAL_ATTRIBUTE, measurement.getSerial());

					event.put(STATUS_ATTRIBUTE, measurement.getStatus());
					event.put(MESSAGE_COUNT_ATTRIBUTE,
							measurement.getMessageCount());
					event.put(SCAN_COUNT_ATTRIBUTE, measurement.getScanCount());
					event.put(POWERUP_DURATION_ATTRIBUTE,
							measurement.getPowerUpDuration());
					event.put(TRANSMISSION_DURATION_ATTRIBUTE,
							measurement.getTransmissionDuration());
					event.put(INPUT_STATUS_ATTRIBUTE,
							measurement.isInputStatus());
					event.put(OUTPUT_STATUS_ATTRIBUTE,
							measurement.isOutputStatus());
					event.put(SCANNING_FREQUENCY_ATTRIBUTE,
							measurement.getScanningFrequency());
					event.put(MEASUREMENT_FREQUENCY_ATTRIBUTE,
							measurement.getMeasurementFrequency());

					final List<PolarCoordinate> dist1Coordinates = new ArrayList<PolarCoordinate>(
							measurement.getSamples().length);
					final List<Double> remission1 = new ArrayList<Double>(
							measurement.getSamples().length);
					final List<PolarCoordinate> dist2Coordinates = new ArrayList<PolarCoordinate>(
							measurement.getSamples().length);
					final List<Double> remission2 = new ArrayList<Double>(
							measurement.getSamples().length);
					for (int j = 0; j < measurement.getSamples().length; j++) {
						final Sample sample = measurement.getSamples()[j];
						dist1Coordinates.add(new PolarCoordinate(
								(double) sample.getDist1(), sample.getAngle()));
						remission1.add((double) sample.getRssi1());
						dist2Coordinates.add(new PolarCoordinate(
								(double) sample.getDist2(), sample.getAngle()));
						remission2.add((double) sample.getRssi2());
					}

					event.put(SICKConstants.DIST1, dist1Coordinates);
					event.put(SICKConstants.RSSI1, remission1);
					event.put(SICKConstants.DIST2, dist2Coordinates);
					event.put(SICKConstants.RSSI2, remission2);

					Object[] retObj = new Object[schema.size()];
					for (int i = 0; i < retObj.length; i++) {
						retObj[i] = event.get(schema.get(i));
					}
					ret = new Tuple(retObj, false);
				}
			}
		} else {
			LOG.debug(String.format("Receive message %s (%s byte)", message,
					message.getBytes().length + 2));
		}
		return ret;
	}

	private void dumpPackage(final String message) {
		final File debug = new File("debug.out");
		try {
			FileWriter fw = new FileWriter(debug);
			fw.write(message);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
