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
package de.uniol.inf.is.odysseus.wrapper.nmea.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea.NMEAMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea.NMEAStack;

public class NMEASourceAdapter extends AbstractPushingSourceAdapter {
	private final Map<SourceSpec, NMEAStack> stackMap = new ConcurrentHashMap<SourceSpec, NMEAStack>();

	@Override
	public String getName() {
		return "NMEA";
	}

	@Override
	protected void doDestroy(SourceSpec source) {
		NMEAStack stack = this.stackMap.remove(source);
		stack.interrupt();

	}

	@Override
	protected void doInit(final SourceSpec source) {
		String device = source.getConfiguration().get("dev").toString();
		NMEAStack stack = new NMEAStack(device) {

			@Override
			public void process(long timestamp, NMEAMessage message) {
				Map<String, Object> dataMap = message.toMap();
				Object[] data = new Object[source.getSchema().size()];
				for (int i = 0; i < data.length; i++) {
					data[i] = dataMap.get(source.getSchema().get(i));
				}
				if (data.length == source.getSchema().size()) {
					transfer(source, timestamp, data);
				}
			}
		};
		this.stackMap.put(source, stack);
		stack.start();

	}
}
