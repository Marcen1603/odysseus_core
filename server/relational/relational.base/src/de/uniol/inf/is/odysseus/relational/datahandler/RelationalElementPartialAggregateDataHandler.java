package de.uniol.inf.is.odysseus.relational.datahandler;

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
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalElementPartialAggregate;

public class RelationalElementPartialAggregateDataHandler extends AbstractDataHandler<RelationalElementPartialAggregate> {

	private static final Logger LOG = LoggerFactory.getLogger(RelationalElementPartialAggregateDataHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE.getURI());
	}
	
	StringHandler typeHandler = new StringHandler();
	
	@Override
	public RelationalElementPartialAggregate readData(ByteBuffer buffer) {
		String type = typeHandler.readData(buffer);
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(type, (SDFSchema)null);
		if (contentHandler != null){
			Object data = contentHandler.readData(buffer);
			return new RelationalElementPartialAggregate(data, type);
		}else{
			throw new IllegalArgumentException("No Datahandler von type "+type+" registered!");
		}
	}


	@Override
	public RelationalElementPartialAggregate readData(String string) {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		RelationalElementPartialAggregate agg = (RelationalElementPartialAggregate) data;
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(agg.getDatatype(), (SDFSchema)null);
		if (contentHandler != null){
			typeHandler.writeData(buffer, agg.getDatatype());
			contentHandler.writeData(buffer, agg.getValue());
		}else{
			
		}
	}

	@Override
	public int memSize(Object attribute) {
		RelationalElementPartialAggregate agg = (RelationalElementPartialAggregate) attribute;
		IDataHandler<?> contentHandler = DataHandlerRegistry.getDataHandler(agg.getDatatype(), (SDFSchema)null);
		int size = 0;
		if (contentHandler != null){
			size += typeHandler.memSize(agg.getDatatype());
			size += contentHandler.memSize(agg.getValue());
		}else{
			LOG.error("No datahandler for "+agg.getDatatype()+" found!");
		}
		return size;
	}

	@Override
	protected IDataHandler<RelationalElementPartialAggregate> getInstance(
			SDFSchema schema) {
		return new RelationalElementPartialAggregateDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public Class<?> createsType() {
		return RelationalElementPartialAggregate.class;
	}


}
