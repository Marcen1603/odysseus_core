package de.uniol.inf.is.odysseus.spatial.physicaloperator.access.transport;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.SpatioTemporalDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public class SpatioTemporalDataStructureTransportHandler extends AbstractPushTransportHandler
		implements ISpatialListener {

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
		SpatioTemporalDataStructureProvider.getInstance().getDataStructure(this.dataStructureName).addListener(this);
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
		SpatioTemporalDataStructureProvider.getInstance().getDataStructure(this.dataStructureName).removeListener(this);
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

	@Override
	public void onMovingObjectDataStructureChange(IMovingObjectDataStructure dataStructure) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
		tuple.setAttribute(0, dataStructure);
		fireProcess(tuple);

	}

	public String getDataStructureName() {
		return this.dataStructureName;
	}

}
