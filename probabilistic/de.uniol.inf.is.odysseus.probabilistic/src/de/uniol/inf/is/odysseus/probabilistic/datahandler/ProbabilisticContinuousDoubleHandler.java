package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

public class ProbabilisticContinuousDoubleHandler extends
		AbstractDataHandler<ProbabilisticContinuousDouble> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ProbabilisticContinuousDoubleHandler.types
				.add("ProbabilisticContinuousDouble");
		ProbabilisticContinuousDoubleHandler.types
				.add("ProbabilisticContinuousFloat");
	}

	@Override
	public ProbabilisticContinuousDouble readData(
			final ObjectInputStream inputStream) throws IOException {
		final int distributionIndex = inputStream.readInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public ProbabilisticContinuousDouble readData(final String string) {
		final int distributionIndex = Integer.parseInt(string);
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
		final int distributionIndex = buffer.getInt();
		return new ProbabilisticContinuousDouble(distributionIndex);
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
		buffer.putInt(value.getDistribution());
	}

	@Override
	public int memSize(final Object attribute) {
		return Integer.SIZE / 8;
	}

	@Override
	protected IDataHandler<ProbabilisticContinuousDouble> getInstance(
			final SDFSchema schema) {
		return new ProbabilisticContinuousDoubleHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return ProbabilisticContinuousDoubleHandler.types;
	}

}
