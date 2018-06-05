/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.opcua.common.access;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.DoubleHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IntegerHandler;
import de.uniol.inf.is.odysseus.core.datahandler.LongHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.opcua.common.core.SDFOPCUADatatype;

/**
 * The data handler for OPC UA.
 *
 * @param <T>
 *            the generic type
 */
public class OPCUADataHandler<T> extends AbstractDataHandler<OPCValue<T>> implements IDataHandler<OPCValue<T>> {

	/** The Constant types. */
	private static final List<String> types = Arrays.asList(SDFOPCUADatatype.OPCVALUE.getURI());

	/** The Constant sep. */
	private static final char sep = ';';

	/** The long handler. */
	private final IDataHandler<Long> longHandler = new LongHandler();

	/** The int handler. */
	private final IDataHandler<Integer> intHandler = new IntegerHandler();

	/** The value handler. */
	private final IDataHandler<?> valueHandler;

	/**
	 * Instantiates a new OPC UA data handler.
	 */
	public OPCUADataHandler() {
		this(null);
	}

	/**
	 * Instantiates a new OPC UA data handler.
	 *
	 * @param child
	 *            the child
	 */
	public OPCUADataHandler(SDFSchema child) {
		super(child);
		// In case of default constructor
		if (child == null) {
			valueHandler = new DoubleHandler();
			return;
		}
		// Extract parameters
		String dataType = child.getAttribute(0).getAttributeName();
		SDFSchema schema = child;
		// Get data handler (for example: DOUBLE)
		IDataHandler<?> myValueHandler = DataHandlerRegistry.getDataHandler(dataType, schema);
		// Maybe-hack for handling of KeyValueObject
		if (myValueHandler == null && child.getAttribute(0).getDatatype().getSubType() != null) {
			dataType = child.getAttribute(0).getDatatype().getSubType().toString();
			myValueHandler = DataHandlerRegistry.getDataHandler(dataType, schema);
		}
		// Set it once
		valueHandler = myValueHandler;
	}

	@Override
	public OPCValue<T> readData(ByteBuffer buffer) {
		long timestamp = longHandler.readData(buffer).longValue();
		int quality = intHandler.readData(buffer).intValue();
		long error = longHandler.readData(buffer).longValue();
		@SuppressWarnings("unchecked")
		T value = (T) valueHandler.readData(buffer);
		return new OPCValue<T>(timestamp, value, quality, error);
	}

	@Override
	public OPCValue<T> readData(String text) {
		String[] parts = text.split(sep + "");
		long timestamp = longHandler.readData(parts[0]).longValue();
		int quality = intHandler.readData(parts[1]).intValue();
		long error = longHandler.readData(parts[2]).longValue();
		@SuppressWarnings("unchecked")
		T value = (T) valueHandler.readData(parts[3]);
		return new OPCValue<T>(timestamp, value, quality, error);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		@SuppressWarnings("unchecked")
		OPCValue<T> value = (OPCValue<T>) data;
		longHandler.writeData(buffer, value.getTimestamp());
		intHandler.writeData(buffer, value.getQuality());
		longHandler.writeData(buffer, value.getError());
		valueHandler.writeData(buffer, value.getValue());
	}

	@Override
	public void writeData(StringBuilder builder, Object data) {
		@SuppressWarnings("unchecked")
		OPCValue<T> value = (OPCValue<T>) data;
		longHandler.writeData(builder, value.getTimestamp());
		builder.append(sep);
		intHandler.writeData(builder, value.getQuality());
		builder.append(sep);
		longHandler.writeData(builder, value.getError());
		builder.append(sep);
		valueHandler.writeData(builder, value.getValue());
	}

	@Override
	public int memSize(Object attribute) {
		return (Long.SIZE + Double.SIZE + Integer.SIZE + Long.SIZE) / 8;
	}

	@Override
	public Class<?> createsType() {
		return OPCValue.class;
	}

	@Override
	protected IDataHandler<OPCValue<T>> getInstance(SDFSchema schema) {
		return new OPCUADataHandler<T>(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
}