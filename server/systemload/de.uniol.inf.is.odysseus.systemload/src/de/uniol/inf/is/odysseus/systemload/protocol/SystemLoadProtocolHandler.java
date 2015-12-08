package de.uniol.inf.is.odysseus.systemload.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.systemload.impl.SystemLoadMeasurement;
import de.uniol.inf.is.odysseus.systemload.impl.SystemLoadMeasurement.MeasureType;

public class SystemLoadProtocolHandler extends AbstractProtocolHandler<Tuple<?>> {

	public static final String NAME = "SystemLoad";

	private List<MeasureType> outputElements;
	
	private static SDFSchema defaultSchema;
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("", "ts",
				SDFDatatype.START_TIMESTAMP));
		attributes.add(new SDFAttribute("", "CPU_MAX",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "CPU_FREE",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "NET_INPUT_RATE",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "NET_MAX",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "NET_OUTPUT_RATE",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "MEM_FREE",
				SDFDatatype.DOUBLE));
		attributes.add(new SDFAttribute("", "MEM_TOTAL",
				SDFDatatype.DOUBLE));

		defaultSchema = SDFSchemaFactory.createNewSchema("",
				Tuple.class, attributes);	
	}

	public SystemLoadProtocolHandler() {
		super();
		setSchema(defaultSchema);
	}

	public SystemLoadProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler) {
		super(direction, access, dataHandler, options);
		init_internal();
		setSchema(defaultSchema);
	}

	private void init_internal() {
		// Infos are read from schema, currently no need for configuration
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void setSchema(SDFSchema schema) {
		if (schema != null && schema.size()>0){
			super.setSchema(schema);
		}
		
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		if (outputElements == null) {
			initOutput();
		}
		// Connects to time transport handler
		// Ignore message, just use as trigger
		Tuple<IMetaAttribute> value = new Tuple<IMetaAttribute>(outputElements.size(), false);
		for (int i = 0; i < outputElements.size(); i++) {
			value.setAttribute(i, SystemLoadMeasurement.getValue(outputElements.get(i)));
		}
		getTransfer().transfer(value);
	}

	private void initOutput() {
		SDFSchema schema = getSchema();
		outputElements = new ArrayList<MeasureType>(schema.size());
		for (int i = 0; i < schema.size(); i++) {
			String name = schema.getAttribute(i).getAttributeName();
			if (name.equalsIgnoreCase("TIME") || name.equalsIgnoreCase("TS")) {
				outputElements.add(MeasureType.TIME);
			} else if (name.equalsIgnoreCase("CPU_MAX")) {
				outputElements.add(MeasureType.CPU_MAX);
			} else if (name.equalsIgnoreCase("CPU_FREE")) {
				outputElements.add(MeasureType.CPU_FREE);
			} else if (name.equalsIgnoreCase("NET_INPUT_RATE")) {
				outputElements.add(MeasureType.NET_INPUT_RATE);
			} else if (name.equalsIgnoreCase("NET_MAX")) {
				outputElements.add(MeasureType.NET_MAX);
			} else if (name.equalsIgnoreCase("NET_OUTPUT_RATE")) {
				outputElements.add(MeasureType.NET_OUTPUT_RATE);
			} else if (name.equalsIgnoreCase("MEM_FREE")) {
				outputElements.add(MeasureType.MEM_FREE);
			} else if (name.equalsIgnoreCase("MEM_TOTAL")) {
				outputElements.add(MeasureType.MEM_TOTAL);
			} else {
				throw new IllegalArgumentException(name + " is no available measurement");
			}
		}
	}

	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler) {
		return new SystemLoadProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return getSchema().equals(other.getSchema());
	}

}
