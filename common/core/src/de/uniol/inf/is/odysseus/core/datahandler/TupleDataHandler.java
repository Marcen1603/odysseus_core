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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Andr� Bolles, Marco Grawunder
 * 
 */
public class TupleDataHandler extends AbstractStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> {

	static final private Logger logger = LoggerFactory
			.getLogger(TupleDataHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.TUPLE.getURI());
	}

	IDataHandler<?>[] dataHandlers = null;
	final private boolean nullMode;

	// Default Constructor for declarative Service needed
	public TupleDataHandler() {
		super(null);
		nullMode = false;
	}

	protected TupleDataHandler(boolean nullMode) {
		super(null);
		this.nullMode = nullMode;
	}

	protected TupleDataHandler(SDFSchema schema, boolean nullMode) {
		super(schema);
		this.nullMode = nullMode;
		this.createDataHandler(schema);
	}

	@Override
	protected IDataHandler<Tuple<? extends IMetaAttribute>> getInstance(SDFSchema schema) {
		return new TupleDataHandler(schema, false);
	}

	@Override
	protected IDataHandler<Tuple<? extends IMetaAttribute>> getInstance(List<SDFDatatype> schema) {
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

	public void init(List<SDFDatatype> schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException(
					"TupleDataHandler is immutable. Values already set");
		}
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(InputStream inputStream, boolean handleMetaData) throws IOException {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			try {
				attributes[i] = dataHandlers[i].readData(inputStream);
			} catch (Exception e) {
				if (dataHandlers.length > i) {
					logger.warn("Error parsing stream with "
							+ dataHandlers[i].getClass() + " " + e.getMessage());
				} else {
					logger.warn("Error parsing stream with no data handler defined "
							+ e.getMessage());
				}
				attributes[i] = null;
			}
		}
		Tuple<IMetaAttribute> ret = new Tuple<IMetaAttribute>(attributes, false);
		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(inputStream);
			ret.setMetadata(meta);
		}
		return ret;
	}
	
	@Override
	public Tuple<? extends IMetaAttribute> readData(String input, boolean handleMetaData) {
		throw new UnsupportedOperationException("Currently not avaialable");
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(String[] input, boolean handleMetaData) {
		Object[] attributes = new Object[dataHandlers.length];
		int min = Math.min(dataHandlers.length, input.length);
		for (int i = 0; i < min; i++) {
			try {
				if (dataHandlers[i].getClass() == ListDataHandler.class 
						&& getSchema() != null 
						&& getSchema().size() < input.length
						&& i == getSchema().size() - 1) {
					attributes[i] =  ((ListDataHandler) dataHandlers[i])
							.readData(input, i, input.length);
				} else {
					attributes[i] = this.dataHandlers[i].readData(input[i]);
				}
			} catch (Exception e) {
				if (dataHandlers.length > i) {
					logger.warn("Error parsing " + input[i] + " with "
							+ dataHandlers[i].getClass() + " " + e.getMessage());
				} else {
					logger.warn("Error parsing " + input[i]
							+ " with no data handler defined " + e.getMessage());
				}
				attributes[i] = null;
			}
		}
		Tuple<IMetaAttribute> ret = new Tuple<IMetaAttribute>(attributes, false);
		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(input);
			ret.setMetadata(meta);
		}
		return ret;
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(List<String> input, boolean handleMetaData) {
		int min = Math.min(dataHandlers.length, input.size());
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(min,
				false);
		for (int i = 0; i < min; i++) {
			try {
				if (dataHandlers[i].getClass() == ListDataHandler.class 
						&& getSchema() != null 
						&& getSchema().size() < input.size() 
						&& i == getSchema().size() - 1) {
					tuple.setAttribute(i, ((ListDataHandler) dataHandlers[i])
							.readData(input, i, input.size()));
				} else {
					tuple.setAttribute(i,
							this.dataHandlers[i].readData(input.get(i)));
				}
			} catch (Exception e) {
				if (dataHandlers.length > i) {
					logger.warn("Error parsing " + input.get(i) + " with "
							+ dataHandlers[i].getClass() + " " + e.getMessage());
				} else {
					logger.warn("Error parsing " + input.get(i)
							+ " with no data handler defined " + e.getMessage());
				}
				tuple.setAttribute(i, (Object) null);
			}
		}

		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(input);
			tuple.setMetadata(meta);
		}
		return tuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #readData (java.nio.ByteBuffer)
	 */
	@Override
	public Tuple<? extends IMetaAttribute> readData(ByteBuffer buffer, boolean handleMetaData) {
		Tuple<IMetaAttribute> r = null;
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
					} catch (Exception e) {
						if (dataHandlers.length > i) {
							logger.warn("Error parsing stream with "
									+ dataHandlers[i].getClass() + " "
									+ e.getMessage());
						} else {
							logger.warn("Error parsing stream with no data handler defined "
									+ e.getMessage());
						}
						attributes[i] = null;
					}
				}
			}
			r = new Tuple<IMetaAttribute>(attributes, false);
			// buffer.clear(); // DO NOT CLEAR THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
			if (handleMetaData) {
				IMetaAttribute meta = readMetaData(buffer);
				r.setMetadata(meta);
			}
		}
		return r;
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(String string) {
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
	public void writeData(List<String> output, Object data, boolean handleMetaData) {
		@SuppressWarnings("unchecked")
		Tuple<IMetaAttribute> r = (Tuple<IMetaAttribute>) data;

		synchronized (output) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(output, r.getAttribute(i));
			}
			if (handleMetaData) {
				writeMetaData(output, r.getMetadata());
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
	public void writeData(StringBuilder string, Object data, boolean handleMetaData) {
		// super.writeData(string, data);
		Tuple<?> r = (Tuple<?>) data;

		synchronized (string) {
			for (int i = 0; i < dataHandlers.length; i++) {
				dataHandlers[i].writeData(string, r.getAttribute(i));
			}
			if (handleMetaData) {
				writeMetaData(string, r.getMetadata());
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
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		if (data instanceof Tuple){
			Tuple<?> r = (Tuple<?>) data;
			writeData(buffer, r, handleMetaData);
		}
	}
	
	@Override
	public void writeData(ByteBuffer buffer, Tuple<? extends IMetaAttribute> object,
			boolean handleMetaData) {
		synchronized (buffer) {
			for (int i = 0; i < dataHandlers.length; i++) {
				Object v = object.getAttribute(i);
				if (nullMode) {
					if (v == null) {
						buffer.put((byte) 0);
					} else {
						buffer.put((byte) 1);
					}
				}
				if (!nullMode || (nullMode && v != null)) {
					dataHandlers[i].writeData(buffer, object.getAttribute(i));
				}
			}
			if (handleMetaData) {
				writeMetaData(buffer, object.getMetadata());
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
		if (schema == null)
			return;

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
			} else if (type.isListValue()){
				uri = "LIST";
			}

			if (!DataHandlerRegistry.containsDataHandler(uri)) {
				throw new IllegalArgumentException("Datatype cannot be used in transport!"
						+ uri);
			}
			
			SDFSchema subSchema;
			if (type.isTuple()) {
				subSchema = attribute.getDatatype().getSchema();
			} else {
				subSchema = SDFSchemaFactory
						.createNewTupleSchema("", attribute);
			}

			dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri,
					subSchema);

		}
	}

	private void createDataHandler(List<SDFDatatype> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (SDFDatatype attribute : schema) {

			IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(
					attribute.toString(), (SDFSchema) null);

			if (handler == null) {
				throw new IllegalArgumentException("Unregistered datatype "
						+ attribute);
			}

			this.dataHandlers[i++] = handler;
		}
	}

	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		Tuple<?> r = (Tuple<?>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandlers.length; i++) {
			size += dataHandlers[i].memSize(r.getAttribute(i));
		}
		// Marker for null or not null values
		if (nullMode) {
			size += dataHandlers.length;
		}
		if (handleMetaData) {
			size += getMetaDataMemSize(r.getMetadata());
		}
		return size;
	}

	@Override
	public Class<?> createsType() {
		return Tuple.class;
	}

}
