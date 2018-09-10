/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class StringHandler extends AbstractDataHandler<String> {

	private static final Logger LOG = LoggerFactory.getLogger(StringHandler.class);

	static protected List<String> types = new ArrayList<String>();

	static {
		types.add(SDFDatatype.STRING.getURI());
		types.add(SDFDatatype.START_TIMESTAMP_STRING.getURI());
	}


	@Override
	public IDataHandler<String> getInstance(SDFSchema schema) {
		return new StringHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readData(InputStream inputStream) throws IOException {
		int size = inputStream.read();
		if (size >= 0) {
			byte[] buffer = new byte[size];
			for (int i = 0; i < size && inputStream.available() > 0; i++) {
				buffer[i] = (byte) inputStream.read();
			}
			try {
				CharBuffer decoded = conversionOptions.getDecoder().decode(ByteBuffer.wrap(buffer));
				return decoded.toString();
			} catch (CharacterCodingException e) {
				LOG.error("Could not decode data with string handler", e);
			}
			return "";
		}
		return null;
	}

	@Override
	public String readData(ByteBuffer b) {
		int size = b.getInt();
		if (size >= 0) {
			int limit = b.limit();
			b.limit(b.position() + size);
			try {
				CharBuffer decoded = conversionOptions.getDecoder().decode(b);
				return decoded.toString();
			} catch (CharacterCodingException e) {
				LOG.error("Could not decode data with string handler", e);
			} finally {
				b.limit(limit);
			}
			return "";
		} else {
			return null;
		}
	}

	@Override
	public void writeData(List<String> output, Object data, WriteOptions options) {
		if (options.hasTextSeperator()) {
			StringBuilder text = new StringBuilder();
			text.append(options.getTextSeperator());
			text.append(data);
			text.append(options.getTextSeperator());
			output.add(text.toString());
		} else {
			if (data != null){
				output.add(data.toString());
			}else{
				output.add(null);
			}

		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		String s = (String) data;
		try {
			if (data != null) {
				ByteBuffer charBuffer = conversionOptions.getEncoder().encode(CharBuffer.wrap(s));
				buffer.putInt(charBuffer.limit());
				buffer.put(charBuffer);
			} else {
				buffer.putInt(-1);
			}
		} catch (CharacterCodingException e) {
			LOG.error("Could not encode '{}'", s, e);
		}
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		try {
			String attr = (String) attribute;
			if (attr == null) {
				attr = "";
			}
			int val = conversionOptions.getEncoder().encode(CharBuffer.wrap(attr)).limit() + Integer.SIZE / 8;
			return val;
		} catch (CharacterCodingException e) {
			LOG.error("Could not encode '{}' for calculation mem-size", attribute, e);
		}
		return Integer.SIZE / 8;
	}

	@Override
	public Class<?> createsType() {
		return String.class;
	}
}
