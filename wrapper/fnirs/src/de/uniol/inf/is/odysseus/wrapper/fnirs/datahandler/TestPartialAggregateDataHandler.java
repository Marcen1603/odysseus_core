package de.uniol.inf.is.odysseus.wrapper.fnirs.datahandler;

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
import de.uniol.inf.is.odysseus.wrapper.fnirs.aggregate.functions.TestPartialAggregate;

public class TestPartialAggregateDataHandler extends
		AbstractDataHandler<TestPartialAggregate<?>> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StringHandler.class);

	public static final SDFDatatype TEST_PARTIAL_AGGREGATE = new SDFDatatype(
			"TestPartialAggregate");	
	
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(TEST_PARTIAL_AGGREGATE.getURI());
	}
	
	public TestPartialAggregateDataHandler() {
		super(null);
	}
	
	@Override
	protected IDataHandler<TestPartialAggregate<?>> getInstance(
			SDFSchema schema) {
		return new TestPartialAggregateDataHandler();
	}
	
	@Override
	public TestPartialAggregate<?> readData(ByteBuffer buffer) {
		int count = buffer.getInt();
		return new TestPartialAggregate<>(count);
	}

	@Override
	public TestPartialAggregate<?> readData(String string) {
		throw new IllegalArgumentException("Sorry, currently not implemented");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		TestPartialAggregate<?> agg = (TestPartialAggregate<?>) data;
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
		return TestPartialAggregate.class;
	}



}
