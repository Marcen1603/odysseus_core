package de.uniol.inf.is.odysseus.scai;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;

import de.offis.xml.schema.scai20.DataElementValueDescription;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SensorDataDescription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaiDataHandler extends AbstractDataHandler<Tuple<?>>{

	Logger LOG = LoggerFactory.getLogger(ScaiDataHandler.class);
	
	private SDFSchema schema;

	public ScaiDataHandler(SDFSchema schema) {
		 this.schema= schema;
	}
	
	@Override
	public Tuple<?> readData(ByteBuffer buffer) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		throw new IllegalArgumentException("Currently not implemented");	}

	@Override
	public Tuple<?> readData(String string) {
		SCAIDocument scai = null;
		try {
			scai = SCAIDocument.Factory.parse(string);
		} catch (XmlException e) {
			e.printStackTrace();
		}
		
		return process(scai);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new IllegalArgumentException("Currently not implemented");		
	}

	@Override
	public int memSize(Object attribute) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	private Tuple<?> process(final SCAIDocument data) {
		Tuple<?> ret = null;
		final SensorDataDescription[] sensorDataDescriptions = data.getSCAI()
				.getPayload().getMeasurements().getDataStreamArray();
		for (int i = 0; i < sensorDataDescriptions.length; ++i) {

			// TODO: Put these elements to the tuple or are they part of the tuple?
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
				Object[] retObj = new Object[schema.size()];
				for (int ii = 0; i < retObj.length; ii++) {
					if (LOG.isDebugEnabled()) {
						if (!event.containsKey(schema.get(ii))) {
							LOG.debug(String.format(
									"Attribute '%s' is missing in event %s.",
									schema.get(ii), event));
						}
					}
					retObj[ii] = event.get(schema.get(ii));
				}
				ret = new Tuple(retObj);

			} catch (final Exception e) {
				LOG.warn(e.getMessage(), e);
			}
		}
		return ret;
	}
	
}
