package de.uniol.inf.is.soop.scaiport.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.apache.xmlbeans.XmlException;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.offis.xml.schema.scai20.DataElementValueDescription;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SensorDataDescription;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SCAISensorPool {

	private static final Logger LOG = LoggerFactory
			.getLogger(SCAISensorPool.class);
	private static Map<String, SCAISensorPool> instances = new HashMap<String, SCAISensorPool>();
	public static ScaiPort adapter;

	public SCAISensorPool(BundleContext context) {
	}

	public static void setAdapter(ScaiPort adapter) {
		SCAISensorPool.adapter = adapter;
	}

	public static SCAISensorPool getInstance(String client,
			BundleContext context) {
		if (!SCAISensorPool.instances.containsKey(client)) {
			SCAISensorPool.instances.put(client, new SCAISensorPool(context));
		}
		return SCAISensorPool.instances.get(client);
	}

	/**
	 * Process SCAI from a stream
	 * 
	 * @param data
	 */
	public boolean process(ServletInputStream data) {
		SCAIDocument scai = null;
		try {
			final StringBuilder sb = new StringBuilder();
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(data));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
			scai = SCAIDocument.Factory.parse(sb.toString());
		} catch (final XmlException e) {
			LOG.error(e.getMessage(), e);
		}
		if (scai != null) {
			return this.process(scai);
		}
		return false;
	}

	/**
	 * Process SCAIDocument object
	 * 
	 * @param data
	 */
	public boolean process(final SCAIDocument data) {
		final SensorDataDescription[] sensorDataDescriptions = data.getSCAI()
				.getPayload().getMeasurements().getDataStreamArray();
		for (int i = 0; i < sensorDataDescriptions.length; ++i) {

			final Calendar timestamp = sensorDataDescriptions[i].getTimeStamp();
			final String name = sensorDataDescriptions[i].getSensorName();
			final String domain = sensorDataDescriptions[i]
					.getSensorDomainName();
			final DataElementValueDescription[] dataStreamElements = sensorDataDescriptions[i]
					.getDataStreamElementArray();

			final Map<String, Object> event = new HashMap<String, Object>();
			for (int j = 0; j < dataStreamElements.length; ++j) {
				final String value = dataStreamElements[j].getData();
				final String path = dataStreamElements[j].getPath();
				BigDecimal quality = dataStreamElements[j].getQuality();
				if (quality == null) {
					quality = new BigDecimal(0);
				}
				event.put(path, value);
				event.put(path + "_quality", quality.doubleValue());
			}
			try {
				LOG.debug(">" + event);
				adapter.pushSensorData(domain, name, event, timestamp);
			} catch (final Exception e) {
				LOG.warn(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

	/**
	 * Process String representation of SCAI
	 * 
	 * @param data
	 */
	public boolean process(final String data) {
		SCAIDocument scai = null;
		try {
			scai = SCAIDocument.Factory.parse(data);
		} catch (final XmlException e) {
			LOG.warn(e.getMessage(), e);
		}
		if (scai != null) {
			return this.process(scai);
		}
		return false;
	}
}
