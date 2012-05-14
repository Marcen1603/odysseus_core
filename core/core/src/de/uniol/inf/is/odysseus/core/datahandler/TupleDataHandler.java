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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author André Bolles, Marco Grawunder
 * 
 */
public class TupleDataHandler extends AbstractDataHandler<Tuple<?>> {

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Tuple");
	}

	IDataHandler<?>[] dataHandlers = null;

	// Default Constructor for declarative Service needed
	public TupleDataHandler() {
	}

	public TupleDataHandler(SDFSchema schema) {
		this.createDataHandler(schema);
	}

	public TupleDataHandler(List<String> schema) {
		this.createDataHandler(schema);
	}

	public void init(SDFSchema schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		}else{
			throw new RuntimeException("TupleDataHandler is immutable. Values already set");
		}
	}
	
	public void init(List<String> schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		}else{
			throw new RuntimeException("TupleDataHandler is immutable. Values already set");
		}
	}
	
	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			attributes[i] = dataHandlers[i].readData(inputStream);
		}

		return new Tuple<IMetaAttribute>(attributes);
	}

	@Override
	public Tuple<?> readData(String[] input) {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < input.length; i++) {
			attributes[i] = dataHandlers[i].readData(input[i]);
		}

		return new Tuple<IMetaAttribute>(attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #readData (java.nio.ByteBuffer)
	 */
	@Override
	public Tuple<?> readData(ByteBuffer buffer) {
		Tuple<?> r = null;
		synchronized (buffer) {
			// buffer.flip(); // DO NOT FLIP THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
			// logger.debug("create "+byteBuffer);
			Object[] attributes = new Object[dataHandlers.length];
			for (int i = 0; i < dataHandlers.length; i++) {
				attributes[i] = dataHandlers[i].readData(buffer);
			}
			r = new Tuple<IMetaAttribute>(attributes);
			// buffer.clear(); // DO NOT CLEAR THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
		}
		return r;
	}

	@Override
	public Tuple<?> readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #writeData (java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		Tuple<?> r = (Tuple<?>) data;

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
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.
	 * AbstractDataHandler #getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

	private void createDataHandler(SDFSchema schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();

			if (type.isTuple()) {
				TupleDataHandler handler = new TupleDataHandler(
						type.getSchema());
				this.dataHandlers[i++] = handler;
			} else if (type.isListValue()) {
				ListDataHandler handler = new ListDataHandler(type.getSubType());
				this.dataHandlers[i++] = handler;
			} else {
				String uri = attribute.getDatatype().getURI(false);
				IDataHandler<?> handler = DataHandlerRegistry
						.getDataHandler(uri);

				if (handler == null) {
					throw new IllegalArgumentException("Unregistered datatype "
							+ uri);
				}

				this.dataHandlers[i++] = handler;
			}
		}
	}

	private void createDataHandler(List<String> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (String attribute : schema) {

			IDataHandler<?> handler = DataHandlerRegistry
					.getDataHandler(attribute);

			if (handler == null) {
				throw new IllegalArgumentException("Unregistered datatype "
						+ attribute);
			}

			this.dataHandlers[i++] = handler;
		}
	}

	@Override
	public int memSize(Object attribute) {
		Tuple<?> r = (Tuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandlers.length; i++) {
			size += dataHandlers[i].memSize(r.getAttribute(i));
		}
		return size;
	}

}
