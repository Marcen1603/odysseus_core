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
import java.util.Objects;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticInteger;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticIntegerHandler extends AbstractDataHandler<ProbabilisticInteger> {
	/** Supported data types. */
	private static final List<String> TYPES = new ArrayList<String>();
	static {
		ProbabilisticIntegerHandler.TYPES.add("ProbabilisticInteger");
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public final IDataHandler<ProbabilisticInteger> getInstance(final SDFSchema schema) {
		return new ProbabilisticIntegerHandler();
	}

	/**
	 * Default constructor.
	 */
	public ProbabilisticIntegerHandler() {
		super();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.io.ObjectInputStream)
	 */
	@Override
	public final ProbabilisticInteger readData(final ObjectInputStream inputStream) throws IOException {
		Objects.requireNonNull(inputStream);
		Preconditions.checkArgument(inputStream.available()>=4);
		final int length = inputStream.readInt();
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (int i = 0; i < length; i++) {
			final Integer value = inputStream.readInt();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticInteger(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.lang.String)
	 */
	@Override
	public final ProbabilisticInteger readData(final String string) {
		Objects.requireNonNull(string);
		Preconditions.checkArgument(!string.isEmpty());
		final String[] discreteValues = string.split(";");
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (final String discreteValue2 : discreteValues) {
			final String[] discreteValue = discreteValue2.split(":");
			values.put(Integer.parseInt(discreteValue[0]), Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticInteger(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public final ProbabilisticInteger readData(final ByteBuffer buffer) {
		Objects.requireNonNull(buffer);
		Preconditions.checkArgument(buffer.remaining()>=4);
		final int length = buffer.getInt();
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (int i = 0; i < length; i++) {
			final Integer value = buffer.getInt();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticInteger(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		Objects.requireNonNull(buffer);
		Objects.requireNonNull(data);
		final ProbabilisticInteger values = (ProbabilisticInteger) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Integer, Double> value : values.getValues().entrySet()) {
			buffer.putInt(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticIntegerHandler.TYPES);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang.Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
		return (((ProbabilisticInteger) attribute).getValues().size() * (Integer.SIZE + Double.SIZE)) / 8;
	}

	@Override
	public Class<?> createsType() {
		return ProbabilisticInteger.class;
	}

}
