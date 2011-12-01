package de.uniol.inf.is.odysseus.wrapper.scai.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class SCAISourceAdapter extends AbstractPushingSourceAdapter {
	private static final Logger LOG = LoggerFactory
			.getLogger(SCAISourceAdapter.class);
	public static final String SEPARATOR = ":";
	private Map<String, SourceSpec> sources = new HashMap<String, SourceSpec>();

	public SCAISourceAdapter() {
		SCAISensorPool.setAdapter(this);
	}

	@Override
	public String getName() {
		return "SCAI";
	}

	@Override
	protected void doDestroy(SourceSpec source) {
		sources.remove(source.getName());

	}

	@Override
	protected void doInit(SourceSpec source) {
		sources.put(source.getName(), source);

	}

	public void pushSensorData(String domain, String name,
			Map<String, Object> event, Calendar timestamp) {
		String sourceName = domain + SEPARATOR + name;

		SourceSpec source = sources.get(sourceName);
		if (sources.containsKey(sourceName)) {
			Object[] data = new Object[source.getSchema().size()];
			for (int i = 0; i < data.length; i++) {
				if (LOG.isDebugEnabled()) {
					if (!event.containsKey(source.getSchema().get(i))) {
						LOG.debug(String.format(
								"Attribute '%s' is missing in event %s.",
								source.getSchema().get(i), event));
					}
				}
				data[i] = event.get(source.getSchema().get(i));
			}
			this.transfer(source, timestamp.getTimeInMillis(), data);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"Sensor '%s' from domain '%s' not registered.", name,
						domain));
			}
		}
	}
}
