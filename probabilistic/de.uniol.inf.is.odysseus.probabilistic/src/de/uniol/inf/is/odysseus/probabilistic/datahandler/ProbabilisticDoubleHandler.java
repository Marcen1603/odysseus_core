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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDoubleHandler extends
		AbstractDataHandler<ProbabilisticDouble> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticDoubleHandler.types.add("ProbabilisticDouble");
		ProbabilisticDoubleHandler.types.add("ProbabilisticFloat");
	}

	@Override
	public IDataHandler<ProbabilisticDouble> getInstance(final SDFSchema schema) {
		return new ProbabilisticDoubleHandler();
	}

	public ProbabilisticDoubleHandler() {
		super();
	}

	@Override
	public ProbabilisticDouble readData(final ObjectInputStream inputStream)
			throws IOException {
		final int length = inputStream.readInt();
		final Map<Double, Double> values = new HashMap<Double, Double>();
		for (int i = 0; i < length; i++) {
			final Double value = inputStream.readDouble();
			final Double probability = inputStream.readDouble();
			values.put(value, probability);
		}
		return new ProbabilisticDouble(values);
	}

	@Override
	public ProbabilisticDouble readData(final String string) {
		final String[] discreteValues = string.split(";");
		final Map<Double, Double> values = new HashMap<Double, Double>();
		for (int i = 0; i < discreteValues.length; i++) {
			final String[] discreteValue = discreteValues[i].split(":");
			values.put(Double.parseDouble(discreteValue[0]),
					Double.parseDouble(discreteValue[1]));
		}
		return new ProbabilisticDouble(values);
	}

	@Override
	public ProbabilisticDouble readData(final ByteBuffer buffer) {
		final int length = buffer.getInt();
		final Map<Double, Double> values = new HashMap<Double, Double>();
		for (int i = 0; i < length; i++) {
			final Double value = buffer.getDouble();
			final Double probability = buffer.getDouble();
			values.put(value, probability);
		}
		return new ProbabilisticDouble(values);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticDouble values = (ProbabilisticDouble) data;
		buffer.putInt(values.getValues().size());
		for (final Entry<Double, Double> value : values.getValues().entrySet()) {
			buffer.putDouble(value.getKey());
			buffer.putDouble(value.getValue());
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ProbabilisticDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (((ProbabilisticDouble) attribute).getValues().size()
				* Double.SIZE * 2) / 8;
	}
}
