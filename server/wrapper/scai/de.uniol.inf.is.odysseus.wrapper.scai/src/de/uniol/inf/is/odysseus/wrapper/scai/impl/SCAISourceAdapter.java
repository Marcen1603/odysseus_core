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
package de.uniol.inf.is.odysseus.wrapper.scai.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
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
		String domain = source.getConfiguration().get("domain").toString();
		String name = source.getConfiguration().get("sensor").toString();
		sources.remove(domain + SEPARATOR + name);

	}

	@Override
	protected void doInit(SourceSpec source) {
		String domain = source.getConfiguration().get("domain").toString();
		String name = source.getConfiguration().get("sensor").toString();
		sources.put(domain + SEPARATOR + name, source);
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
