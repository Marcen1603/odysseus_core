package de.uniol.inf.is.odysseus.server.opcua.access;

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
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class OPCUADataHandler<T> extends AbstractDataHandler<OPCValue<T>>implements IDataHandler<OPCValue<T>> {

	private static final List<String> types = Arrays.asList(SDFOPCUADatatype.OPCVALUE.getURI());
	private static final char sep = ';';

	private final IDataHandler<Long> longHandler = new LongHandler();
	private final IDataHandler<Integer> intHandler = new IntegerHandler();
	private final IDataHandler<?> valueHandler;

	public OPCUADataHandler() {
		this(null);
	}

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