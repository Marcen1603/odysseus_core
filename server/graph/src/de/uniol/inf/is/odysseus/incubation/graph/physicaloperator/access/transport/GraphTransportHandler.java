package de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.listener.IGraphListener;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

/**
 * TransportHandler getting graph from provider as a listener. 
 * 
 * @author Kristian Bruns
 */
public class GraphTransportHandler extends AbstractPushTransportHandler implements IGraphListener {
	public static final String NAME = "Graph";
	public static final String STRUCTURENAME = "structurename";
	
	private String structureName;
	
	public GraphTransportHandler() {
		super();
	}
	
	public GraphTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		if (options.containsKey(STRUCTURENAME)) {
			this.structureName = options.get(STRUCTURENAME);
		}
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		final GraphTransportHandler handler = new GraphTransportHandler(protocolHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		System.out.println("START");
		GraphDataStructureProvider.getInstance().addListener(this, this.structureName);
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processInClose() throws IOException {
		System.out.println("CLOSE");
		GraphDataStructureProvider.getInstance().removeListener(this);
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		if (!(other instanceof GraphTransportHandler)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void graphDataStructureChange(Graph graph) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
		tuple.setAttribute(0, graph);
		
		GraphTransportHandler.this.fireProcess(tuple);
	}
	
	public void setStructureName(String name) {
		this.structureName = name;
	}

}
