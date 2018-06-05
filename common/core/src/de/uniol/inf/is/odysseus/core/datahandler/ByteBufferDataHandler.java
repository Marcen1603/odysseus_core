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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.ByteBufferWrapper;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Henrik Surm
 *
 */
public class ByteBufferDataHandler extends AbstractDataHandler<ByteBufferWrapper> {
	static protected List<String> types = new ArrayList<String>();

	static {
		types.add(SDFDatatype.BYTEBUFFER.getURI());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ByteBufferWrapper readData(String string) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		if (data instanceof ByteBuffer)
			buffer.put((ByteBuffer) data);
		else if (data instanceof Byte)
			buffer.put((Byte) data);
		else
			throw new UnsupportedOperationException(
					"Cannot write \"" + data.getClass().getName() + "\" to ByteBuffer!");
	}

	@Override
	public ByteBufferWrapper readData(ByteBuffer buffer) {
		// "Read" all data from buffer
		buffer.position(buffer.limit());
		return new ByteBufferWrapper(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int memSize(Object data) {
		ByteBuffer buf = (ByteBuffer) data;
		return buf.capacity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> createsType() {
		return ByteBuffer.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IDataHandler<ByteBufferWrapper> getInstance(SDFSchema schema) {
		return new ByteBufferDataHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
}
