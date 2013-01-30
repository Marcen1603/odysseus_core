package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticInteger;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticIntegerHandler extends
		AbstractDataHandler<ProbabilisticInteger> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticIntegerHandler.types.add("ProbabilisticInteger");
	}

	@Override
	public IDataHandler<ProbabilisticInteger> getInstance(final SDFSchema schema) {
		return new ProbabilisticIntegerHandler();
	}

	public ProbabilisticIntegerHandler() {
		super();
	}

	@Override
	public ProbabilisticInteger readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (int i = 0; i < length; i++) {
			final Integer value = inputStream.readInt();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticInteger(values);
	}

	@Override
	public ProbabilisticInteger readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(Integer.parseInt(discreteValue[0]),
					Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticInteger(values);
	}

	@Override
	public ProbabilisticInteger readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Integer, Double> values = new HashMap<Integer, Double>();
		for (int i = 0; i < length; i++) {
			final Integer value = buffer.getInt();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticInteger(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticInteger values = (ProbabilisticInteger) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Integer, Double> value : values.getValues().entrySet()) {
			buffer.putInt(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticInteger) attribute).getValues().size() * (Integer.SIZE + Double.SIZE)) / 8;
	}

}
