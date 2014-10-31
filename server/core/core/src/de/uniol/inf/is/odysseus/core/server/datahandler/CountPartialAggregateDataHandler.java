package de.uniol.inf.is.odysseus.core.server.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.StringHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CountPartialAggregate;

public class CountPartialAggregateDataHandler extends
		AbstractDataHandler<CountPartialAggregate<?>> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StringHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.COUNT_PARTIAL_AGGREGATE.getURI());
	}
	
	@Override
	public IDataHandler<CountPartialAggregate<?>> getInstance(
			List<SDFDatatype> schema) {
		return new CountPartialAggregateDataHandler();
	}
	
	@Override
	protected IDataHandler<CountPartialAggregate<?>> getInstance(
			SDFSchema schema) {
		return new CountPartialAggregateDataHandler();
	}
	
	@Override
	public CountPartialAggregate<?> readData(ByteBuffer buffer) {
		int count = buffer.getInt();
		return new CountPartialAggregate<>(count);
	}

	@Override
	public CountPartialAggregate<?> readData(String string) {
		throw new IllegalArgumentException("Sorry, currently not implemented");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		CountPartialAggregate<?> agg = (CountPartialAggregate<?>) data;
		buffer.putInt(agg.getCount());
	}

	@Override
	public int memSize(Object attribute) {
		return Integer.SIZE/8;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public Class<?> createsType() {
		return CountPartialAggregate.class;
	}



}
