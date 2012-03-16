/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.relational.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.SetDataHandler;

/**
 * @author André Bolles
 * 
 */
public class RelationalTupleDataHandler extends AbstractDataHandler {

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Tuple");
	}

	SDFSchema schema;

	IDataHandler[] dataHandlers = null;

	public RelationalTupleDataHandler(SDFSchema schema) {
		this.schema = schema;
		this.createDataReader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#readData
	 * ()
	 */
	@Override
	public Object readData() throws IOException {
		return readData(stream);
	}

	
	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		Object[] attributes = new Object[schema.size()];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			attributes[i] = dataHandlers[i].readData(inputStream);
		}

		return new RelationalTuple<IMetaAttribute>(attributes);
	}

	@Override
	public Object readData(String[] input) {
		Object[] attributes = new Object[schema.size()];
		for (int i = 0; i < input.length; i++) {
			attributes[i] = dataHandlers[i].readData(input[i]);
		}

		return new RelationalTuple<IMetaAttribute>(attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#readData
	 * (java.nio.ByteBuffer)
	 */
	@Override
	public Object readData(ByteBuffer buffer) {
		RelationalTuple<?> r = null;
		synchronized (buffer) {
			// buffer.flip(); // DO NOT FLIP THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
			// logger.debug("create "+byteBuffer);
			Object[] attributes = new Object[dataHandlers.length];
			for (int i = 0; i < dataHandlers.length; i++) {
				attributes[i] = dataHandlers[i].readData(buffer);
			}
			r = new RelationalTuple<IMetaAttribute>(attributes);
			// buffer.clear(); // DO NOT CLEAR THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
		}
		return r;
	}

	@Override
	public Object readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler#writeData
	 * (java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		RelationalTuple<?> r = (RelationalTuple<?>) data;

		int size = memSize(r);

		if (size > buffer.capacity()) {
			buffer = ByteBuffer.allocate(size * 2);
		}

		synchronized (buffer) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(buffer, r.getAttribute(i));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractDataHandler
	 * #getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	private void createDataReader() {
		this.dataHandlers = new IDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();

			if (type.isBase() || type.isBean()) {
				String uri = attribute.getDatatype().getURI(false);
				IDataHandler handler = DataHandlerRegistry
						.getDataHandler(uri);

				if (handler == null) {
					throw new RuntimeException("illegal datatype " + uri);
				}

				this.dataHandlers[i++] = handler;
			} else if (type.isTuple()) {
				RelationalTupleDataHandler handler = new RelationalTupleDataHandler(
						type.getSchema());
				this.dataHandlers[i++] = handler;
			} else if (type.isMultiValue()) {
				SetDataHandler handler = new SetDataHandler(type.getSubType());
				this.dataHandlers[i++] = handler;
			}
		}
	}

	@Override
	public int memSize(Object attribute) {
		RelationalTuple<?> r = (RelationalTuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandlers.length; i++) {
			size += dataHandlers[i].memSize(r.getAttribute(i));
		}
		return size;
	}

}
