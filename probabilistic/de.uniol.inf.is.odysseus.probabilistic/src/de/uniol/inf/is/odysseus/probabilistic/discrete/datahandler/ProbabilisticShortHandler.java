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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticShort;

/**
 * Data handler for probabilistic short values.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticShortHandler extends AbstractDataHandler<ProbabilisticShort> {
	/** Supported data types. */
	private static final List<String> TYPES = new ArrayList<String>();
	static {
		ProbabilisticShortHandler.TYPES.add("ProbabilisticShort");
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public final IDataHandler<ProbabilisticShort> getInstance(final SDFSchema schema) {
		return new ProbabilisticShortHandler();
	}

	/**
	 * Default constructor.
	 */
	public ProbabilisticShortHandler() {
		super();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.io.ObjectInputStream)
	 */
	@Override
	public final ProbabilisticShort readData(final ObjectInputStream inputStream) throws IOException {
		final int length = inputStream.readInt();
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (int i = 0; i < length; i++) {
			final Short value = inputStream.readShort();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticShort(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.lang.String)
	 */
	@Override
	public final ProbabilisticShort readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (final String discreteValue2 : discreteValues) {
			final String[] discreteValue = discreteValue2.split(":");
			values.put(Short.parseShort(discreteValue[0]), Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticShort(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public final ProbabilisticShort readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (int i = 0; i < length; i++) {
			final Short value = buffer.getShort();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticShort(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticShort values = (ProbabilisticShort) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Short, Double> value : values.getValues().entrySet()) {
			buffer.putShort(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticShortHandler.TYPES);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang.Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
		return (((ProbabilisticShort) attribute).getValues().size() * (Short.SIZE + Double.SIZE)) / 8;
	}

}