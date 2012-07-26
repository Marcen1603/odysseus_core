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
package de.uniol.inf.is.odysseus.wrapper.nmea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.GGAMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.GSVMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.RMCMessage;

public class NMEADatHandler extends AbstractDataHandler<Tuple<?>> {
	private static final Logger LOG = LoggerFactory.getLogger(NMEADatHandler.class);
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("NMEA");
	}
	public static final Pattern GGA = Pattern
			.compile("^\\$GPGGA,([0-9.]+),([0-9.]+),(N|S),([0-9.]+),(E|W),([0-8]?),([0-9]+),([0-9.]*),([0-9.]+),M,([0-9.]+),M,,\\*([0-9A-Za-z]+)$");
	public static final Pattern RMC = Pattern
			.compile("^\\$GPRMC,([0-9.]+),(A|V),([0-9.]+),(N|S),([0-9.]+),(E|W),([0-9.]+),([0-9.]+),([0-9]+),([0-9.]*),(E|W)?,(N|A|D|E)\\*([0-9A-Za-z]+)$");
	public static final Pattern GSV = Pattern
			.compile("^\\$GPGSV,([1-3]+),([1-3]+),([0-9]+),([0-9,]*)\\*([0-9A-Za-z]+)$");
	public static final Pattern GSVSatellite = Pattern
			.compile("([0-9]{1,2}),([0-9]+),([0-9]+),([0-9]*)");
	private final Charset charset = Charset.forName("ASCII");
	private Date time;
	private Date date;
	private SDFSchema schema;

	public NMEADatHandler() {

	}

	public NMEADatHandler(SDFSchema schema) {
		this.schema = schema;
	}

	@Override
	public IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new NMEADatHandler(schema);
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
		return process(string);
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
	private Tuple<?> process(String data) {
		Tuple<?> ret = null;
		Map<String, Object> event = new HashMap<String, Object>();
		Matcher ggaMatcher = GGA.matcher(data);
		if (ggaMatcher.find()) {
			GGAMessage message = new GGAMessage();
			SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
			try {
				message.setTime(sdf.parse((!"".equals(ggaMatcher.group(1)) ? ggaMatcher
						.group(1) : "0")));
				time = message.getTime();
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}
			message.setLatitude(new Double(
					(!"".equals(ggaMatcher.group(2)) ? ggaMatcher.group(2)
							: "0.0")));
			message.setLongitude(new Double(
					(!"".equals(ggaMatcher.group(4)) ? ggaMatcher.group(4)
							: "0.0")));
			message.setQuality(new BigDecimal(
					(!"".equals(ggaMatcher.group(6)) ? ggaMatcher.group(6)
							: "0")));
			message.setSatellites(new Integer(
					(!"".equals(ggaMatcher.group(7)) ? ggaMatcher.group(7)
							: "0")));
			message.setDilution(new Double(
					(!"".equals(ggaMatcher.group(8)) ? ggaMatcher.group(8)
							: "0.0")));
			message.setAltitude(new Double(
					(!"".equals(ggaMatcher.group(9)) ? ggaMatcher.group(9)
							: "0.0")));
			message.setHeight(new Double(
					(!"".equals(ggaMatcher.group(10)) ? ggaMatcher.group(10)
							: "0.0")));
			message.setChecksum(ggaMatcher.group(11));
			event = message.toMap();
		}
		Matcher rmcMatcher = RMC.matcher(data);
		if (rmcMatcher.find()) {
			RMCMessage message = new RMCMessage();
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss.SSS");
			SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyy");
			try {
				message.setTime(sdfTime.parse((!"".equals(rmcMatcher.group(1)) ? rmcMatcher
						.group(1) : "0.0")));
				time = message.getTime();
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}
			message.setStatus(rmcMatcher.group(2));
			message.setLatitude(new Double(
					(!"".equals(rmcMatcher.group(3)) ? rmcMatcher.group(3)
							: "0.0")));
			message.setLongitude(new Double(
					(!"".equals(rmcMatcher.group(5)) ? rmcMatcher.group(5)
							: "0.0")));
			message.setSpeed(new Double(
					(!"".equals(rmcMatcher.group(7)) ? rmcMatcher.group(7)
							: "0.0")));
			message.setCourse(new Double(
					(!"".equals(rmcMatcher.group(8)) ? rmcMatcher.group(8)
							: "0.0")));
			try {
				message.setDate(sdfDate.parse((!"".equals(rmcMatcher.group(9)) ? rmcMatcher
						.group(9) : "0")));
				date = message.getDate();
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}
			message.setVariation(new Double(
					(!"".equals(rmcMatcher.group(10)) ? rmcMatcher.group(10)
							: "0.0")));
			message.setMode(rmcMatcher.group(12));
			message.setChecksum(rmcMatcher.group(13));
			event = message.toMap();
		}
		Matcher gsvMatcher = GSV.matcher(data);
		if (gsvMatcher.find()) {
			GSVMessage message = new GSVMessage();
			message.setTotal(new Integer(
					(!"".equals(gsvMatcher.group(1)) ? gsvMatcher.group(1)
							: "0")));
			message.setCount(new Integer(
					(!"".equals(gsvMatcher.group(2)) ? gsvMatcher.group(2)
							: "0")));
			Integer satellites = new Integer(
					(!"".equals(gsvMatcher.group(3)) ? gsvMatcher.group(3)
							: "0"));

			String satelliteList = gsvMatcher.group(4);
			Matcher gsvSatelliteMatcher = GSVSatellite.matcher(satelliteList);
			if (gsvSatelliteMatcher.find()) {
				Integer satelliteId = new Integer(
						(!"".equals(gsvSatelliteMatcher.group(1)) ? gsvSatelliteMatcher
								.group(1) : "0"));
				Integer elevation = new Integer((!"".equals(gsvSatelliteMatcher
						.group(2)) ? gsvSatelliteMatcher.group(2) : "0"));
				Integer azimuth = new Integer((!"".equals(gsvSatelliteMatcher
						.group(3)) ? gsvSatelliteMatcher.group(3) : "0"));
				Integer snr = new Integer((!"".equals(gsvSatelliteMatcher
						.group(4)) ? gsvSatelliteMatcher.group(4) : "0"));
				message.addSatellite(satelliteId, elevation, azimuth, snr);
			}
			message.setSatellitesNum(satellites);
			message.setChecksum(gsvMatcher.group(5));
			event = message.toMap();
		}
		event.put("timestamp", date.getTime() + time.getTime());
		Object[] retObj = new Object[schema.size()];
		for (int i = 0; i < retObj.length; i++) {
			retObj[i] = event.get(schema.get(i).getAttributeName()
					.toLowerCase());
		}
		ret = new Tuple(retObj, false);
		return ret;
	}
}
