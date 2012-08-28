package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

public class ProbabilisticContinuousDoubleHandler extends
        AbstractDataHandler<ProbabilisticContinuousDouble> {
    static protected List<String> types = new ArrayList<String>();
    static {
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousDouble");
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousFloat");
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final Pair<NormalDistribution, Double>[] mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = inputStream.readByte();
            final Integer covarianceMatrixIndex = inputStream.readInt();
            final Double mean = inputStream.readDouble();
            final Double probability = inputStream.readDouble();
            final NormalDistribution distribution = new NormalDistribution(mean,
                    covarianceMatrixId, covarianceMatrixIndex);
            mixtures[i] = new Pair<NormalDistribution, Double>(distribution, probability);
        }
        return new ProbabilisticContinuousDouble(mixtures);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final String string) {
        final String[] continuousValues = string.split(";");
        final Pair<NormalDistribution, Double>[] mixtures = new Pair[continuousValues.length];
        for (int i = 0; i < continuousValues.length; i++) {
            final String[] continuousValue = continuousValues[i].split(":");
            final Byte covarianceMatrixId = Byte.parseByte(continuousValue[0]);
            final Integer covarianceMatrixIndex = Integer.parseInt(continuousValue[1]);
            final Double mean = Double.parseDouble(continuousValue[2]);
            final Double probability = Double.parseDouble(continuousValue[3]);
            mixtures[i] = new Pair<NormalDistribution, Double>(new NormalDistribution(mean,
                    covarianceMatrixId, covarianceMatrixIndex), probability);
        }
        return new ProbabilisticContinuousDouble(mixtures);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final Pair<NormalDistribution, Double>[] mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = buffer.get();
            final Integer covarianceMatrixIndex = buffer.getInt();
            final Double mean = buffer.getDouble();
            final Double probability = buffer.getDouble();
            final NormalDistribution distribution = new NormalDistribution(mean,
                    covarianceMatrixId, covarianceMatrixIndex);
            mixtures[i] = new Pair<NormalDistribution, Double>(distribution, probability);
        }
        return new ProbabilisticContinuousDouble(mixtures);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
        buffer.putInt(value.getMixtures().length);
        for (final Pair<NormalDistribution, Double> mixture : value.getMixtures()) {
            buffer.put(mixture.getE1().getCovarianceMatrixId());
            buffer.putInt(mixture.getE1().getCovarianceMatrixIndex());
            buffer.putDouble(mixture.getE1().getMean());
            buffer.putDouble(mixture.getE2());
        }
    }

    @Override
    public int memSize(final Object attribute) {
        final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) attribute;
        return (Integer.SIZE * value.getMixtures().length * (Byte.SIZE + Integer.SIZE + Double.SIZE)) / 8;
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
