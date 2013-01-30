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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticFloat;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticFloatHandler extends
		AbstractDataHandler<ProbabilisticFloat> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticFloatHandler.types.add("ProbabilisticFloat");
	}

	@Override
	public IDataHandler<ProbabilisticFloat> getInstance(final SDFSchema schema) {
		return new ProbabilisticFloatHandler();
	}

	public ProbabilisticFloatHandler() {
		super();
	}

	@Override
	public ProbabilisticFloat readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (int i = 0; i < length; i++) {
			final Float value = inputStream.readFloat();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public ProbabilisticFloat readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(Float.parseFloat(discreteValue[0]),
					Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public ProbabilisticFloat readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Float, Double> values = new HashMap<Float, Double>();
		for (int i = 0; i < length; i++) {
			final Float value = buffer.getFloat();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticFloat(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticFloat values = (ProbabilisticFloat) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Float, Double> value : values.getValues().entrySet()) {
			buffer.putFloat(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticFloat) attribute).getValues().size() * (Float.SIZE + Double.SIZE)) / 8;
	}

}
