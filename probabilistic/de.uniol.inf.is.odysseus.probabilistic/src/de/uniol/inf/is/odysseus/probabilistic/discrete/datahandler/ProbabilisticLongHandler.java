/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.probabilistic.discrete.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticLong;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticLongHandler extends
		AbstractDataHandler<ProbabilisticLong> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticLongHandler.types.add("ProbabilisticLong");
	}

	@Override
	public IDataHandler<ProbabilisticLong> getInstance(final SDFSchema schema) {
		return new ProbabilisticLongHandler();
	}

	public ProbabilisticLongHandler() {
		super();
	}

	@Override
	public ProbabilisticLong readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<Long, Double> values = new HashMap<Long, Double>();
		for (int i = 0; i < length; i++) {
			final Long value = inputStream.readLong();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticLong(values);
	}

	@Override
	public ProbabilisticLong readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Long, Double> values = new HashMap<Long, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(Long.parseLong(discreteValue[0]),
					Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticLong(values);
	}

	@Override
	public ProbabilisticLong readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Long, Double> values = new HashMap<Long, Double>();
		for (int i = 0; i < length; i++) {
			final Long value = buffer.getLong();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticLong(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticLong values = (ProbabilisticLong) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Long, Double> value : values.getValues().entrySet()) {
			buffer.putLong(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticLongHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticLong) attribute).getValues().size() * (Long.SIZE + Double.SIZE)) / 8;
	}

}
