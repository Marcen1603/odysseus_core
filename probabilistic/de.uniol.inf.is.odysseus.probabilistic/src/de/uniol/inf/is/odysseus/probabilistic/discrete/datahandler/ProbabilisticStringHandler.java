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
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticString;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticStringHandler extends AbstractDataHandler<ProbabilisticString> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticStringHandler.types.add("ProbabilisticString");
	}
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetEncoder encoder = ProbabilisticStringHandler.charset.newEncoder();
	private static CharsetDecoder decoder = ProbabilisticStringHandler.charset.newDecoder();

	@Override
	public IDataHandler<ProbabilisticString> getInstance(final SDFSchema schema) {
		return new ProbabilisticStringHandler();
	}

	public ProbabilisticStringHandler() {
		super();
	}

	@Override
	public ProbabilisticString readData(final ObjectInputStream inputStream) throws IOException {
		final int length = inputStream.readInt();
		final Map<String, Double> values = new HashMap<String, Double>();
		for (int i = 0; i < length; i++) {
			try {
				final String value = (String) inputStream.readObject();
				final Double probability = inputStream.readDouble();
				values.put(value, probability);
			} catch (final ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
		return new ProbabilisticString(values);
	}

	@Override
	public ProbabilisticString readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<String, Double> values = new HashMap<String, Double>();
		for (final String discreteValue2 : discreteValues) {
			final String[] discreteValue = discreteValue2.split(":");
			values.put(discreteValue[0], Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticString(values);
	}

	@Override
	public ProbabilisticString readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<String, Double> values = new HashMap<String, Double>();
		for (int i = 0; i < length; i++) {
			try {
				final int stringLength = buffer.getInt();
				final int limit = buffer.limit();
				buffer.limit(buffer.position() + stringLength);
				final String value = ProbabilisticStringHandler.decoder.decode(buffer).toString();
				buffer.limit(limit);
				final Double probability = buffer.getDouble();
				values.put(value, probability);
			} catch (final CharacterCodingException e) {
				e.printStackTrace();
			}
		}
		return new ProbabilisticString(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticString values = (ProbabilisticString) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<String, Double> value : values.getValues().entrySet()) {
			try {
				final ByteBuffer encodedValue = ProbabilisticStringHandler.encoder.encode(CharBuffer.wrap(value.getKey()));
				buffer.putInt(encodedValue.remaining());
				buffer.put(encodedValue);
				buffer.putDouble(value.getValue());
			} catch (final CharacterCodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticStringHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		int size = 0;
		final ProbabilisticString values = (ProbabilisticString) attribute;
		for (final Entry<String, Double> value : values.getValues().entrySet()) {
			try {
				final ByteBuffer encodedValue = ProbabilisticStringHandler.encoder.encode(CharBuffer.wrap(value.getKey()));
				size += encodedValue.remaining();
			} catch (final CharacterCodingException e) {
				e.printStackTrace();
			}
		}
		return ((((ProbabilisticString) attribute).getValues().size() * Double.SIZE) + size) / 8;
	}

}