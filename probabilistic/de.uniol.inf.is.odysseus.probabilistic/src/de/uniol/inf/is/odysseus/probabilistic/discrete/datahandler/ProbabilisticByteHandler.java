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
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticByte;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticByteHandler extends AbstractDataHandler<ProbabilisticByte> {
	/** Supported data types. */
	private static final List<String> TYPES = new ArrayList<String>();
	static {
		ProbabilisticByteHandler.TYPES.add("ProbabilisticByte");
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public final IDataHandler<ProbabilisticByte> getInstance(final SDFSchema schema) {
		return new ProbabilisticByteHandler();
	}

	/**
	 * Default constructor.
	 */
	public ProbabilisticByteHandler() {
		super();
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.io.ObjectInputStream)
	 */
	@Override
	public final ProbabilisticByte readData(final ObjectInputStream inputStream) throws IOException {
		final int length = inputStream.readInt();
		final Map<Byte, Double> values = new HashMap<Byte, Double>();
		for (int i = 0; i < length; i++) {
			final Byte value = inputStream.readByte();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticByte(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.lang.String)
	 */
	@Override
	public final ProbabilisticByte readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Byte, Double> values = new HashMap<Byte, Double>();
		for (final String discreteValue2 : discreteValues) {
			final String[] discreteValue = discreteValue2.split(":");
			values.put(Byte.parseByte(discreteValue[0]), Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticByte(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.nio.ByteBuffer)
	 */
	@Override
	public final ProbabilisticByte readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Byte, Double> values = new HashMap<Byte, Double>();
		for (int i = 0; i < length; i++) {
			final Byte value = buffer.get();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticByte(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticByte values = (ProbabilisticByte) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Byte, Double> value : values.getValues().entrySet()) {
			buffer.put(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticByteHandler.TYPES);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang.Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
		return (((ProbabilisticByte) attribute).getValues().size() * (Byte.SIZE + Double.SIZE)) / 8;
	}

}
