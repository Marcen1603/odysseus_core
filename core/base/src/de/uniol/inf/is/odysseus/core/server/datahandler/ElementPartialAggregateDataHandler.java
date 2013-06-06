package de.uniol.inf.is.odysseus.core.server.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.StringHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

public class ElementPartialAggregateDataHandler extends AbstractDataHandler<ElementPartialAggregate<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(ElementPartialAggregateDataHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.ELEMENT_PARTIAL_AGGREGATE.getURI());
	}
	
	StringHandler typeHandler = new StringHandler();
	
	@Override
	public ElementPartialAggregate<?> readData(ByteBuffer buffer) {
		String type = typeHandler.readData(buffer);
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(type, (SDFSchema)null);
		if (contentHandler != null){
			Object data = contentHandler.readData(buffer);
			return new ElementPartialAggregate<Object>(data, type);
		}else{
			throw new IllegalArgumentException("No Datahandler von type "+type+" registered!");
		}
	}

	@Override
	public ElementPartialAggregate<?> readData(ObjectInputStream inputStream)
			throws IOException {
		String type = typeHandler.readData(inputStream);
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(type, (SDFSchema)null);
		if (contentHandler != null){
			Object data = contentHandler.readData(inputStream);
			return new ElementPartialAggregate<Object>(data, type);
		}else{
			throw new IllegalArgumentException("No Datahandler von type "+type+" registered!");
		}	
	}

	@Override
	public ElementPartialAggregate<?> readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		ElementPartialAggregate<?> agg = (ElementPartialAggregate<?>) data;
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(agg.getDatatype(), (SDFSchema)null);
		if (contentHandler != null){
			typeHandler.writeData(buffer, agg.getDatatype());
			contentHandler.writeData(buffer, agg.getElem());
		}else{
			
		}
	}

	@Override
	public int memSize(Object attribute) {
		ElementPartialAggregate<?> agg = (ElementPartialAggregate<?>) attribute;
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(agg.getDatatype(), (SDFSchema)null);
		int size = 0;
		if (contentHandler != null){
			size += typeHandler.memSize(agg.getDatatype());
			size += contentHandler.memSize(agg.getElem());
		}else{
			LOG.error("No datahandler for "+agg.getDatatype()+" found!");
		}
		return size;
	}

	@Override
	protected IDataHandler<ElementPartialAggregate<?>> getInstance(
			SDFSchema schema) {
		return new ElementPartialAggregateDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}



}
