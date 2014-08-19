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
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Andr� Bolles, Marco Grawunder
 * 
 */
public class TupleDataHandler extends AbstractDataHandler<Tuple<?>> {

	static final private Logger logger = LoggerFactory.getLogger(TupleDataHandler.class);
	
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.TUPLE.getURI());
	}

	IDataHandler<?>[] dataHandlers = null;
	final private boolean nullMode;

	// Default Constructor for declarative Service needed
	public TupleDataHandler() {
		nullMode = false;
	}

	protected TupleDataHandler(boolean nullMode) {
		this.nullMode = nullMode;
	}

	protected TupleDataHandler(SDFSchema schema, boolean nullMode) {
		this.nullMode = nullMode;
		this.createDataHandler(schema);
	}

	@Override
	public IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new TupleDataHandler(schema, false);
	}

	@Override
	public IDataHandler<Tuple<?>> getInstance(List<String> schema) {
		TupleDataHandler handler = new TupleDataHandler(false);
		handler.init(schema);
		return handler;
	}

	public void init(SDFSchema schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException(
					"TupleDataHandler is immutable. Values already set");
		}
	}

	public void init(List<String> schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException(
					"TupleDataHandler is immutable. Values already set");
		}
	}

	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
            try {
                attributes[i] = dataHandlers[i].readData(inputStream);
            }
            catch (Exception e) {
                logger.warn("Error parsing stream with " + dataHandlers[i].getClass() + " " + e.getMessage());
                attributes[i] = null;
            }
		}
		return new Tuple<IMetaAttribute>(attributes, false);
	}
	
	@Override
	public Tuple<?> readData(InputStream inputStream) throws IOException {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
            try {
                attributes[i] = dataHandlers[i].readData(inputStream);
            }
            catch (Exception e) {
                logger.warn("Error parsing stream with " + dataHandlers[i].getClass() + " " + e.getMessage());
                attributes[i] = null;
            }
		}
		return new Tuple<IMetaAttribute>(attributes, false);
	}

	@Override
	public Tuple<?> readData(String[] input) {
		Object[] attributes = new Object[dataHandlers.length];
		int min = Math.min(dataHandlers.length, input.length);
		for (int i = 0; i < min; i++) {
            try {
                attributes[i] = dataHandlers[i].readData(input[i]);
            }
            catch (Exception e) {
                logger.warn("Error parsing " + input[i] + " with " + dataHandlers[i].getClass() + " " + e.getMessage());
                attributes[i] = null;
            }
		}
		return new Tuple<IMetaAttribute>(attributes, false);
	}

	@Override
	public Tuple<?> readData(List<String> input) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(input.size(),
				false);
		for (int i = 0; i < input.size(); i++) {
            try {
                tuple.setAttribute(i, this.dataHandlers[i].readData(input.get(i)));
            }
            catch (Exception e) {
                logger.warn("Error parsing " + input.get(i) + " with " + dataHandlers[i].getClass() + " " + e.getMessage());
                tuple.setAttribute(i, (Object) null);
            }
		}
		return tuple;
	}
	
	@Override
	public Tuple<?> readData(Tuple<?> input) {
		return input;
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
				byte type = -1;
				if (nullMode) {
					type = buffer.get();
				}
				if (!nullMode || type != 0) {
                    try {
                        attributes[i] = dataHandlers[i].readData(buffer);
                    }
                    catch (Exception e) {
                        logger.warn("Error parsing stream with " + dataHandlers[i].getClass() + " " + e.getMessage());
                        attributes[i] = null;
                    }
				}
			}
			r = new Tuple<IMetaAttribute>(attributes, false);
			// buffer.clear(); // DO NOT CLEAR THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
		}
		return r;
	}

	@Override
	public Tuple<?> readData(String string) {
		String[] str = new String[1];
		str[0] = string;
		return readData(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#writeData
	 * (java.util.List, java.lang.Object)
	 */
	@Override
	public void writeData(List<String> output, Object data) {
		Tuple<?> r = (Tuple<?>) data;

		synchronized (output) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(output, r.getAttribute(i));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#writeData
	 * (java.lang.StringBuilder, java.lang.Object)
	 */
	@Override
	public void writeData(StringBuilder string, Object data) {
	//	super.writeData(string, data);
		Tuple<?> r = (Tuple<?>) data;

		synchronized (string) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(string, r.getAttribute(i));
			}
		}
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

		// Dieser Code macht keinen Sinn...
		// Denn im Falle eines zu großen Objekts wird ein
		// temporärer ByteBuffer erzeugt und beschrieben. Der BYteBuffer
		// des aufrufers wäre gar nicht angefasst worden
		
//		int size = memSize(r);
//		
//		if (size > buffer.capacity()) {
//			buffer = ByteBuffer.allocate(size * 2);
//		}

		synchronized (buffer) {
			for (int i = 0; i < dataHandlers.length; i++) {
				Object v = r.getAttribute(i);
				if (nullMode) {
					if (v == null) {
						buffer.put((byte) 0);
					} else {
						buffer.put((byte) 1);
					}
				}
				if (!nullMode || (nullMode && v != null)) {
					dataHandlers[i].writeData(buffer, r.getAttribute(i));
				}
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
		if (schema == null) return;
		
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();
			String uri = attribute.getDatatype().getURI(false);
			;

			// is this really needed??
			if (type.isTuple()) {
				uri = "TUPLE";
			} else if (type.isMultiValue()) {
				uri = "MULTI_VALUE";
			}

			if (!DataHandlerRegistry.containsDataHandler(uri)) {
				throw new IllegalArgumentException("Unregistered datatype "
						+ uri);
			}

			dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri,
					new SDFSchema("", Tuple.class, attribute));

		}
	}

	private void createDataHandler(List<String> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (String attribute : schema) {

			IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(
					attribute, (SDFSchema) null);

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
		// Marker for null or not null values
		if (nullMode){
			size += dataHandlers.length;
		}
		return size;
	}
	
	@Override
	public Class<?> createsType() {
		return Tuple.class;
	}

}
