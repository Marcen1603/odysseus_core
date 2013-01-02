package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

public class ProbabilisticContinuousDoubleHandler extends AbstractDataHandler<ProbabilisticContinuousDouble> {
    static protected List<String> types = new ArrayList<String>();
    static {
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousDouble");
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousFloat");
    }

    @SuppressWarnings("unused")
	@Override
    public ProbabilisticContinuousDouble readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final List<NormalDistribution> mixtures = new ArrayList<NormalDistribution>(length);
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = inputStream.readByte();
            final Integer covarianceMatrixIndex = inputStream.readInt();
            final Double mean = inputStream.readDouble();
            // mixtures.add(new NormalDistribution(mean, covarianceMatrixId,
            // covarianceMatrixIndex));
        }
        return null;
        //   return new ProbabilisticContinuousDouble(mixtures);
    }

    @SuppressWarnings("unused")
	@Override
    public ProbabilisticContinuousDouble readData(final String string) {
        final String[] continuousValues = string.split(";");
        final List<NormalDistribution> mixtures = new ArrayList<NormalDistribution>(continuousValues.length);
        for (int i = 0; i < continuousValues.length; i++) {
            final String[] continuousValue = continuousValues[i].split(":");
            final Byte covarianceMatrixId = Byte.parseByte(continuousValue[0]);
            final Integer covarianceMatrixIndex = Integer.parseInt(continuousValue[1]);
            final Double mean = Double.parseDouble(continuousValue[2]);
            // mixtures.add(new NormalDistribution(mean, covarianceMatrixId,
            // covarianceMatrixIndex));
        }
        return null;
        //   return new ProbabilisticContinuousDouble(mixtures);
    }

    @SuppressWarnings("unused")
	@Override
    public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final List<NormalDistribution> mixtures = new ArrayList<NormalDistribution>(length);
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = buffer.get();
            final Integer covarianceMatrixIndex = buffer.getInt();
            final Double mean = buffer.getDouble();
            // mixtures.add(new NormalDistribution(mean, covarianceMatrixId,
            // covarianceMatrixIndex));
        }
        return null;
        //   return new ProbabilisticContinuousDouble(mixtures);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        @SuppressWarnings("unused")
		final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
      //  buffer.putInt(value.getMixtures().size());
     //   for (final NormalDistribution mixture : value.getMixtures()) {
            // buffer.put(mixture.getCovarianceMatrixId());
       //     buffer.putInt(mixture.getCovarianceMatrixIndex());
     //       buffer.putDouble(mixture.getMean());
      //  }
    }

    @Override
    public int memSize(final Object attribute) {
        @SuppressWarnings("unused")
		final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) attribute;
        return 0;
        //  return (Integer.SIZE * value.getMixtures().size() * (Byte.SIZE + Integer.SIZE + Double.SIZE)) / 8;
    }

    @Override
    protected IDataHandler<ProbabilisticContinuousDouble> getInstance(final SDFSchema schema) {
        return new ProbabilisticContinuousDoubleHandler();
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return ProbabilisticContinuousDoubleHandler.types;
    }

}
