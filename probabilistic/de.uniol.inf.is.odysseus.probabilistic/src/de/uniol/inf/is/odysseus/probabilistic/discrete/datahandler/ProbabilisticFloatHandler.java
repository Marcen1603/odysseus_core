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
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticFloat;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticFloatHandler extends AbstractDataHandler<ProbabilisticFloat> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticFloatHandler.types.add("ProbabilisticFloat");
	}

	@Override
	public IDataHandler<ProbabilisticFloat> getInstance(final SDFSchema schema) {
		return new ProbabilisticFloatHandler();
	}

	public ProbabilisticFloatHandler() {
		super();
	}

	@Override
	public ProbabilisticFloat readData(final ObjectInputStream inputStream) throws IOException {
		final int length = inputStream.readInt();
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (int i = 0; i < length; i++) {
			final Float value = inputStream.readFloat();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public ProbabilisticFloat readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (final String discreteValue2 : discreteValues) {
			final String[] discreteValue = discreteValue2.split(":");
			values.put(Float.parseFloat(discreteValue[0]), Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public ProbabilisticFloat readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (int i = 0; i < length; i++) {
			final Float value = buffer.getFloat();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticFloat values = (ProbabilisticFloat) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Float, Double> value : values.getValues().entrySet()) {
			buffer.putFloat(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticFloatHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticFloat) attribute).getValues().size() * (Float.SIZE + Double.SIZE)) / 8;
	}

}
