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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;

public class AvgSumPartialAggregateDataHandler extends
		AbstractDataHandler<AvgSumPartialAggregate<?>> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StringHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE.getURI());
	}
	
	public AvgSumPartialAggregateDataHandler() {
		super(null);
	}
	
	@Override
	public IDataHandler<AvgSumPartialAggregate<?>> getInstance(
			List<SDFDatatype> schema) {
		return new AvgSumPartialAggregateDataHandler();
	}
	
	@Override
	protected IDataHandler<AvgSumPartialAggregate<?>> getInstance(
			SDFSchema schema) {
		return new AvgSumPartialAggregateDataHandler();
	}
	
	@Override
	public AvgSumPartialAggregate<?> readData(ByteBuffer buffer) {
		int count = buffer.getInt();
		double sum = buffer.getDouble();
		return new AvgSumPartialAggregate<>(sum, count);
	}

	@Override
	public AvgSumPartialAggregate<?> readData(String string) {
		throw new IllegalArgumentException("Sorry, currently not implemented");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		AvgSumPartialAggregate<?> agg = (AvgSumPartialAggregate<?>) data;
		buffer.putInt(agg.getCount());
		buffer.putDouble(agg.getAggValue());
	}

	@Override
	public int memSize(Object attribute) {
		return Integer.SIZE/8+Double.SIZE/8;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public Class<?> createsType() {
		return AvgSumPartialAggregate.class;
	}



}
