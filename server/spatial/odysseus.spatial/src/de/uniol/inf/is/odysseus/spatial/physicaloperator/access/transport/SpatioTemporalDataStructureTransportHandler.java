package de.uniol.inf.is.odysseus.spatial.physicaloperator.access.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class SpatioTemporalDataStructureTransportHandler extends AbstractPushTransportHandler {

	public static final String NAME = "SpatioTemporal";
	public static final String STRUCTURENAME = "dataStructureName";

	private String dataStructureName;

	public SpatioTemporalDataStructureTransportHandler() {
		super();
	}

	public SpatioTemporalDataStructureTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		if (options.containsKey(STRUCTURENAME)) {
			this.dataStructureName = options.get(STRUCTURENAME);

			// Output schema
			SDFAttribute dataStructureID = new SDFAttribute(null, "dataStructure", SDFDatatype.STRING, null, null,
					null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(dataStructureID);
			SDFSchema outputSchema = SDFSchemaFactory.createNewTupleSchema("", outputAttributes);
			setSchema(outputSchema);
		}

	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		final SpatioTemporalDataStructureTransportHandler handler = new SpatioTemporalDataStructureTransportHandler(
				protocolHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {

	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		if (other instanceof SpatioTemporalDataStructureTransportHandler) {
			if (((SpatioTemporalDataStructureTransportHandler) other).getDataStructureName()
					.equals(this.dataStructureName)) {
				return true;
			}
		}

		return false;
	}

	public String getDataStructureName() {
		return this.dataStructureName;
	}

}
