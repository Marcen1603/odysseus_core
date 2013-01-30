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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticShort;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticShortHandler extends
		AbstractDataHandler<ProbabilisticShort> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticShortHandler.types.add("ProbabilisticShort");
	}

	@Override
	public IDataHandler<ProbabilisticShort> getInstance(final SDFSchema schema) {
		return new ProbabilisticShortHandler();
	}

	public ProbabilisticShortHandler() {
		super();
	}

	@Override
	public ProbabilisticShort readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (int i = 0; i < length; i++) {
			final Short value = inputStream.readShort();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticShort(values);
	}

	@Override
	public ProbabilisticShort readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(Short.parseShort(discreteValue[0]),
					Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticShort(values);
	}

	@Override
	public ProbabilisticShort readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Short, Double> values = new HashMap<Short, Double>();
		for (int i = 0; i < length; i++) {
			final Short value = buffer.getShort();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticShort(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticShort values = (ProbabilisticShort) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Short, Double> value : values.getValues().entrySet()) {
			buffer.putShort(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticShort) attribute).getValues().size() * (Short.SIZE + Double.SIZE)) / 8;
	}

}