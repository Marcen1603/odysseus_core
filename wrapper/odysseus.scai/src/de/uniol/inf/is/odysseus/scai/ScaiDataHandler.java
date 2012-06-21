package de.uniol.inf.is.odysseus.scai;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.offis.scampi.stack.Analyser;
import de.offis.scampi.stack.ProtocolObject;
import de.offis.xml.schema.scai20.DataElementValueDescription;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Measurements;
import de.offis.xml.schema.scai20.SensorDataDescription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerException;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

/**
 * SCAI Data Handler
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ScaiDataHandler extends AbstractDataHandler<Tuple<?>> {
	private static final String DOMAIN_ATTRIBUTE = "DOMAIN";
	private static final String NAME_ATTRIBUTE = "NAME";
	private static final String TIMESTAMP_ATTRIBUTE = "TIMESTAMP";
	Logger LOG = LoggerFactory.getLogger(ScaiDataHandler.class);
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("ScaiTuple");
	}

	private final Charset charset = Charset.forName("UTF-8");
	private SDFSchema schema;

	public ScaiDataHandler() {
		// Needed for declarative service
	}

	/**
	 * Create a new SCAI Data Handler
	 * 
	 * @param schema
	 */
	private ScaiDataHandler(SDFSchema schema) {
		this.schema = schema;
	}

	@Override
	public IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new ScaiDataHandler(schema);
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
		try {
			final CharBuffer charBuffer = decoder.decode(buffer);
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
		String value = string.replace("\n", "").replace("\r", "").trim();
		//LOG.debug(value);
		ProtocolObject stackObject = null;
		Analyser analyser = new Analyser();
		try {
			stackObject = analyser.buildSCAIStackObject(value);
		} catch (Exception e) {
			//LOG.warn(e.getMessage(), e);
			throw new DataHandlerException(e);
		}
		if (stackObject != null && stackObject.getContent() != null) {
			return process((SCAIDocument) stackObject.getContent());
		} else {
			throw new DataHandlerException("Error Reading Scai Document ");
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
		Tuple<?> r = (Tuple<?>) data;
		TimeInterval metadata = (TimeInterval) r.getMetadata();
		final SCAIDocument scai = SCAIDocument.Factory.newInstance();

		if (scai.getSCAI() == null) {
			scai.addNewSCAI();
		}
		if (scai.getSCAI().getPayload() == null) {
			scai.getSCAI().addNewPayload();
		}

		final SensorDataDescription sensorData = scai.getSCAI().getPayload()
				.addNewMeasurements().addNewDataStream();
		SDFAttribute domainAttribute = schema.findAttribute(DOMAIN_ATTRIBUTE);
		SDFAttribute nameAttribute = schema.findAttribute(NAME_ATTRIBUTE);
		if (domainAttribute != null) {
			String domain = r.getAttribute(schema.indexOf(domainAttribute));
			sensorData.setSensorDomainName(domain);
		} else {
			sensorData.setSensorDomainName("UNKNOWN");
		}
		if (nameAttribute != null) {
			String name = r.getAttribute(schema.indexOf(nameAttribute));
			sensorData.setSensorName(name);
		} else {
			sensorData.setSensorName("UNKNOWN");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(metadata.getStart().getMainPoint());
		sensorData.setTimeStamp(calendar);
		// Insert each attribute
		for (int i = 0; i < schema.size(); i++) {
			final String key = schema.get(i).getAttributeName();
			if ((!key.endsWith("_quality"))
					&& (!key.equalsIgnoreCase(DOMAIN_ATTRIBUTE))
					&& (!key.equalsIgnoreCase(NAME_ATTRIBUTE))) {
				final DataElementValueDescription nameData = sensorData
						.addNewDataStreamElement();
				nameData.setPath(key);
				nameData.setData(r.getAttribute(i).toString());
				int qualityIndex = schema.indexOf(key + "_quality");
				if ((qualityIndex >= 0) && (qualityIndex < schema.size())) {
					nameData.setQuality(new BigDecimal(r.getAttribute(
							qualityIndex).toString()));
				} else {
					nameData.setQuality(new BigDecimal(0));
				}
			}
		}

		buffer.asCharBuffer().put(scai.xmlText());
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
	 * Process the given SCAI Document to create a relational tuple
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Tuple<?> process(final SCAIDocument data) {
		Tuple<?> ret = null;
		SCAI scai = data.getSCAI();
		Payload payload = scai.getPayload();
		Measurements measurements = payload.getMeasurements();

		if (measurements != null) {
			final SensorDataDescription[] sensorDataDescriptions = measurements
					.getDataStreamArray();
			for (int i = 0; i < sensorDataDescriptions.length; ++i) {

				final Calendar timestamp = sensorDataDescriptions[i]
						.getTimeStamp();
				final String name = sensorDataDescriptions[i].getSensorName();
				final String domain = sensorDataDescriptions[i]
						.getSensorDomainName();
				final DataElementValueDescription[] dataStreamElements = sensorDataDescriptions[i]
						.getDataStreamElementArray();

				final Map<String, Object> event = new HashMap<String, Object>();
				event.put(TIMESTAMP_ATTRIBUTE, timestamp.getTimeInMillis());
				event.put(DOMAIN_ATTRIBUTE, domain);
				event.put(NAME_ATTRIBUTE, name);
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
					Object[] retObj = new Object[schema.size()];
					for (int ii = 0; ii < schema.size(); ii++) {
						String attr = schema.get(ii).getAttributeName();
						Object value = event.get(attr);
						// If value was not recognized correctly ...
						if (schema.get(ii).getDatatype().isNumeric()
								&& value instanceof String) {
							if (schema.get(ii).getDatatype().isInteger()) {
								retObj[ii] = Integer.parseInt((String) value);
							} else if (schema.get(ii).getDatatype().isFloat()
									|| schema.get(ii).getDatatype().isDouble()) {
								retObj[ii] = Double.parseDouble((String) value);
							}
						} else {
							retObj[ii] = value;
						}
					}
					ret = new Tuple(retObj, false);

				} catch (final Exception e) {
					LOG.warn(e.getMessage(), e);
				}
			}
		}

		return ret;
	}
}
