package de.uniol.inf.is.odysseus.wrapper.scai.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.offis.xml.schema.scai20.DataElementValueDescription;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SensorDataDescription;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

/**
 * Binding service to push processed sensor data as SCAI over HTTP
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SCAISinkAdapter extends AbstractSinkAdapter implements SinkAdapter {
	public static final String SEPARATOR = ":";
	private Map<String, SinkSpec> sources = new HashMap<String, SinkSpec>();
	private final HttpClient client;
	private static final Logger LOG = LoggerFactory
			.getLogger(SCAISinkAdapter.class);

	public SCAISinkAdapter() {
		final HttpConnectionManagerParams connMgrParams = new HttpConnectionManagerParams();
		connMgrParams.setConnectionTimeout(4000);
		connMgrParams.setSoTimeout(4000);
		connMgrParams.setMaxConnectionsPerHost(
				HostConfiguration.ANY_HOST_CONFIGURATION, 1);
		connMgrParams.setMaxTotalConnections(300);
		final MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(connMgrParams);
		this.client = new HttpClient(manager);
	}

	@Override
	public String getName() {
		return "SCAI";
	}

	@Override
	public void transfer(SinkSpec sink, long timestamp, Object[] data) {
		String domain = sink.getConfiguration().get("domain").toString();
		String name = sink.getConfiguration().get("sensor").toString();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTimeInMillis(timestamp);
		// First create a SCAI document
		final SCAIDocument scai = SCAIDocument.Factory.newInstance();

		if (scai.getSCAI() == null) {
			scai.addNewSCAI();
		}
		if (scai.getSCAI().getPayload() == null) {
			scai.getSCAI().addNewPayload();
		}

		final SensorDataDescription sensorData = scai.getSCAI().getPayload()
				.addNewMeasurements().addNewDataStream();
		sensorData.setSensorDomainName(domain);
		sensorData.setSensorName(name);

		sensorData.setTimeStamp(calendar);
		// Insert each attribute
		for (int i = 0; i < sink.getSchema().size(); i++) {
			final String key = sink.getSchema().get(i);
			if (!key.endsWith("_quality")) {
				final DataElementValueDescription nameData = sensorData
						.addNewDataStreamElement();
				nameData.setPath(key);
				nameData.setData(data[i].toString());
				int qualityIndex = sink.getSchema().indexOf(key + "_quality");
				if ((qualityIndex >= 0) && (qualityIndex < data.length)) {
					nameData.setQuality(new BigDecimal(data[qualityIndex]
							.toString()));
				} else {
					nameData.setQuality(new BigDecimal(0));
				}
			}
		}

		// Use HTTP PUT to push the sensor data to the target
		final PutMethod put = new PutMethod(sink.getConfiguration().get("target").toString());
		try {
			put.setRequestEntity(new StringRequestEntity(scai.xmlText(),
					"application/xml", "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}

		try {
			this.client.executeMethod(put);
		} catch (final HttpException e) {
			LOG.debug(e.getMessage(), e);
		} catch (final IOException e) {
			LOG.debug(e.getMessage(), e);
		} catch (final Exception e) {
			LOG.debug(e.getMessage(), e);
		} finally {
			put.releaseConnection();
		}
	}

	@Override
	protected void destroy(SinkSpec sink) {
		String domain = sink.getConfiguration().get("domain").toString();
		String name = sink.getConfiguration().get("sensor").toString();
		sources.remove(domain + SEPARATOR + name);
	}

	@Override
	protected void init(SinkSpec sink) {
		String domain = sink.getConfiguration().get("domain").toString();
		String name = sink.getConfiguration().get("sensor").toString();
		sources.put(domain + SEPARATOR + name, sink);
	}

}
