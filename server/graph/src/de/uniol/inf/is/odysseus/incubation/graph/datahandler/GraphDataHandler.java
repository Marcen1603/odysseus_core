package de.uniol.inf.is.odysseus.incubation.graph.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

public class GraphDataHandler extends AbstractDataHandler<Graph> {
	static protected List<String> types = new ArrayList<String>();
	static {
		GraphDataHandler.types.add(SDFGraphDatatype.GRAPH.getURI());
	}
	
	public GraphDataHandler() {
		super(null);
	}
	
	@Override
	public Graph readData(final ByteBuffer buffer) {
		return Graph.fromBuffer(buffer);
	}
	
	@Override
	public Graph readData(final String string) {
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	@Override
	public Graph readData(InputStream inputStream) throws IOException{
		return Graph.fromStream(inputStream);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		Graph graph = (Graph) data;
		graph.appendToByteBuffer(buffer);
	}

	@Override
	public int memSize(Object attribute) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<?> createsType() {
		return Graph.class;
	}

	@Override
	protected IDataHandler<Graph> getInstance(SDFSchema schema) {
		return new GraphDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return GraphDataHandler.types;
	}

}
