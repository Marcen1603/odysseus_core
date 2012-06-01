package de.uniol.inf.is.odysseus.wrapper.sick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return 0;
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
			LOG.info(String.format("Receive message %s (%s byte)", message,
					message.getBytes().length + 2));
		} else if (message.startsWith(SICKConstants.SSN)) {
			if (SICKConstants.LMD_SCANDATA.equalsIgnoreCase(data[1])) {
				if (data.length >= 19) {
					final Measurement measurement = new Measurement();
					try {
						int pos = 0;
						measurement.setVersion(data[pos + 2]);
						measurement.setDevice(data[pos + 3]);
						measurement.setSerial(data[pos + 4]);
						measurement.setStatus(
								Integer.parseInt(data[pos + 5], 16),
								Integer.parseInt(data[pos + 5], 16));

						measurement.setMessageCount(Integer.parseInt(
								data[pos + 7], 16));
						measurement.setScanCount(Integer.parseInt(
								data[pos + 8], 16));

						measurement.setPowerUpDuration(Long.parseLong(
								data[pos + 9], 16));
						measurement.setTransmissionDuration(Long.parseLong(
								data[pos + 10], 16));

						measurement.setInputStatus("3".equals(data[pos + 11])
								|| "3".equals(data[pos + 11]));
						measurement.setOutputStatus("7".equals(data[pos + 13])
								|| "7".equals(data[pos + 14]));

						measurement.setScanningFrequency(Long.parseLong(
								data[pos + 16], 16) * 10);
						measurement.setMeasurementFrequency(Long.parseLong(
								data[pos + 17], 16) * 10);

						measurement.setEncoders(Integer.parseInt(
								data[pos + 18], 16));
						if (measurement.getEncoders() > 0) {
							measurement.setEncoderPosition(Long.parseLong(
									data[pos + 19], 16));
							measurement.setEncoderSpeed(Integer.parseInt(
									data[pos + 20], 16));
							pos += 2;
						}

						final int channels = Integer.parseInt(data[pos + 19],
								16);
						int index = pos + 20;
						for (int i = 0; i < channels; i++) {
							final String name = data[index];
							if ((name.equalsIgnoreCase(SICKConstants.DIST1))
									|| (name.equalsIgnoreCase(SICKConstants.DIST2))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI1))
									|| (name.equalsIgnoreCase(SICKConstants.RSSI2))) {

								final float scalingFactor = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[index + 1], 16))
												.intValue());
								final float scalingOffset = Float
										.intBitsToFloat(((Long) Long.parseLong(
												data[index + 2], 16))
												.intValue());
								final double startingAngle = Double
										.longBitsToDouble(Long.parseLong(
												data[index + 3], 16)) / 10000;
								final double angularStepWidth = ((double) Integer
										.parseInt(data[index + 4], 16)) / 10000;

								final int samples = Integer.parseInt(
										data[index + 5], 16);
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
												* scalingFactor + scalingOffset;
										if (name.equalsIgnoreCase(SICKConstants.DIST1)) {
											measurement.getSamples()[j]
													.setDist1(value <= 3 ? Float.MAX_VALUE
															: value);
										} else if (name
												.equalsIgnoreCase(SICKConstants.DIST2)) {
											measurement.getSamples()[j]
													.setDist2(value <= 3 ? Float.MAX_VALUE
															: value);
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

								index += samples + 6;
							} else {
								throw new SickReadErrorException(message);
							}
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
					ret = new Tuple(retObj);

				}
			}
		} else {
			LOG.info(String.format("Receive message %s (%s byte)", message,
					message.getBytes().length + 2));
		}
		return ret;
	}
}
