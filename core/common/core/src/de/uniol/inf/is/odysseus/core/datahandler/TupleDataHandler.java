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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.WriteOptions;
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

	static final private Logger logger = LoggerFactory.getLogger(TupleDataHandler.class);

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
			throw new RuntimeException("TupleDataHandler is immutable. Values already set");
		}
	}

	public void init(List<SDFDatatype> schema) {
		if (dataHandlers == null) {
			createDataHandler(schema);
		} else {
			throw new RuntimeException("TupleDataHandler is immutable. Values already set");
		}
	}
	
	@Override
	public void setConversionOptions(ConversionOptions conversionOptions) {
		super.setConversionOptions(conversionOptions);
		if (dataHandlers != null) {
			for (IDataHandler<?> handler : dataHandlers) {
				handler.setConversionOptions(conversionOptions);
			}
		}
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(InputStream inputStream, boolean handleMetaData)
			throws IOException {
		Object[] attributes = new Object[dataHandlers.length];
		for (int i = 0; i < this.dataHandlers.length; i++) {
			try {
				attributes[i] = dataHandlers[i].readData(inputStream);
			} catch (Exception e) {
				if (dataHandlers.length > i) {
					logger.trace("Error parsing stream with " + dataHandlers[i].getClass() + " " + e.getMessage());
				} else {
					logger.trace("Error parsing stream with no data handler defined " + e.getMessage());
				}
				attributes[i] = null;
			}
		}
		Tuple<IMetaAttribute> ret = new Tuple<IMetaAttribute>(attributes, false);
		readAndAddMetadata(inputStream, handleMetaData, ret);
		return ret;
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(String input, boolean handleMetaData) {
		throw new UnsupportedOperationException("Currently not avaialable");
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(Iterator<String> input, boolean handleMetaData) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(dataHandlers.length, false);
		int i = 0;
		for (i = 0; i < dataHandlers.length; i++) {
			String nextElem = input.hasNext() ? input.next() : null;
			// Avoid errors when reading null
			if (nextElem != null) {
				try {
					tuple.setAttribute(i, this.dataHandlers[i].readData(nextElem));
				} catch (Exception e) {
					logger.trace("Error parsing " + nextElem + " with data handler " + dataHandlers[i] + ". Cause: "
							+ e.getMessage());
					tuple.setAttribute(i, (Object) null);
				}
			}else{
				tuple.setAttribute(i, (Object) null);
			}
		}

		readAndAddMetadata(input, handleMetaData, tuple);
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
		Tuple<IMetaAttribute> tuple = null;
		synchronized (buffer) {
			// buffer.flip(); // DO NOT FLIP THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
			// System.err.println("ByteBuffer "+buffer);
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
							logger.trace("Error parsing stream with " + dataHandlers[i].getClass() + " " + e.getMessage()
									+ " " + e);
						} else {
							logger.trace("Error parsing stream with no data handler defined " + e.getMessage());
						}
						attributes[i] = null;
					}
				}
			}
			tuple = new Tuple<IMetaAttribute>(attributes, false);
			// buffer.clear(); // DO NOT CLEAR THIS BUFFER, OTHER READERS MIGHT
			// STILL USE IT
			readAndAddMetadata(buffer, handleMetaData, tuple);
		}
		return tuple;
	}

	@Override
	public Tuple<? extends IMetaAttribute> readData(String string) {
		if (string.startsWith("[")) {
			StringTokenizer tokens = new StringTokenizer(string, "[|]");
			List<Object> objects = new ArrayList<Object>();
			int dhPos = 0;
			while (tokens.hasMoreTokens() && dhPos < dataHandlers.length) {
				objects.add(dataHandlers[dhPos++].readData(tokens.nextToken()));
			}
			if (tokens.hasMoreTokens()) {
				logger.trace("Ignoring additional part of " + string);
			}
			Tuple<?> tuple = new Tuple<IMetaAttribute>(objects, false);
			return tuple;

		} else {
			List<String> str = new ArrayList<String>();
			str.add(string);
			return readData(str.iterator());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#writeData
	 * (java.util.List, java.lang.Object)
	 */
	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		@SuppressWarnings("unchecked")
		Tuple<IMetaAttribute> r = (Tuple<IMetaAttribute>) data;

		synchronized (output) {
			for (int i = 0; i < dataHandlers.length; i++) {

				Object attribute = r.getAttribute(i);
				if (attribute != null) {
					dataHandlers[i].writeData(output, attribute, options);
				} else {
					output.add(options.getNullValueString());
				}
			}
			if (handleMetaData) {
				writeMetaData(output, r.getMetadata(), options);
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
		if (data instanceof Tuple) {
			Tuple<?> r = (Tuple<?>) data;
			writeData(buffer, r, handleMetaData);
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Tuple<? extends IMetaAttribute> object, boolean handleMetaData) {
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


			if (!existsDataHandler(type)) {
				throw new IllegalArgumentException("Datatype "+type+" of Attribute "+attribute+" cannot be used in transport!" );
			}

			SDFSchema subSchema;
			if (type.isTuple()) {
				subSchema = attribute.getDatatype().getSchema();
			} else {
				subSchema = SDFSchemaFactory.createNewTupleSchema("", attribute);
				if (schema.getConstraints() != null) {
					subSchema = SDFSchemaFactory.createNewWithContraints(schema.getConstraints(), subSchema);
				}
			}

			dataHandlers[i++] = getDataHandler(type, subSchema);

		}
	}

	private void createDataHandler(List<SDFDatatype> schema) {
		this.dataHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (SDFDatatype attribute : schema) {

			IDataHandler<?> handler = getDataHandler(attribute, (SDFSchema) null);

			if (handler == null) {
				throw new IllegalArgumentException("Unregistered datatype " + attribute);
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
