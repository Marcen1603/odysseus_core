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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticMultivariateContinuousDouble;

public class ProbabilisticMultivariateContinuousDoubleHandler extends
        AbstractDataHandler<ProbabilisticMultivariateContinuousDouble> {
    static protected List<String> types = new ArrayList<String>();
    static {
        ProbabilisticMultivariateContinuousDoubleHandler.types.add("ProbabilisticMultivariateContinuousDouble");
        ProbabilisticMultivariateContinuousDoubleHandler.types.add("ProbabilisticMultivariateContinuousFloat");
    }

    @Override
    public ProbabilisticMultivariateContinuousDouble readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final Pair<ProbabilisticContinuousDouble, Double>[] mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = inputStream.readByte();
            final Integer covarianceMatrixIndex = inputStream.readInt();
            final Double mean = inputStream.readDouble();
            final Double probability = inputStream.readDouble();
            final ProbabilisticContinuousDouble distribution = new ProbabilisticContinuousDouble(mean,
                    covarianceMatrixId, covarianceMatrixIndex);
            mixtures[i] = new Pair<ProbabilisticContinuousDouble, Double>(distribution, probability);
        }
        return new ProbabilisticMultivariateContinuousDouble(mixtures);
    }

    @Override
    public ProbabilisticMultivariateContinuousDouble readData(final String string) {
        final String[] continuousValues = string.split(";");
        final Pair<ProbabilisticContinuousDouble, Double>[] mixtures = new Pair[continuousValues.length];
        for (int i = 0; i < continuousValues.length; i++) {
            final String[] continuousValue = continuousValues[i].split(":");
            final Byte covarianceMatrixId = Byte.parseByte(continuousValue[0]);
            final Integer covarianceMatrixIndex = Integer.parseInt(continuousValue[1]);
            final Double mean = Double.parseDouble(continuousValue[2]);
            final Double probability = Double.parseDouble(continuousValue[3]);
            mixtures[i] = new Pair<ProbabilisticContinuousDouble, Double>(new ProbabilisticContinuousDouble(mean,
                    covarianceMatrixId, covarianceMatrixIndex), probability);
        }
        return new ProbabilisticMultivariateContinuousDouble(mixtures);
    }

    @Override
    public ProbabilisticMultivariateContinuousDouble readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final Pair<ProbabilisticContinuousDouble, Double>[] mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Byte covarianceMatrixId = buffer.get();
            final Integer covarianceMatrixIndex = buffer.getInt();
            final Double mean = buffer.getDouble();
            final Double probability = buffer.getDouble();
            final ProbabilisticContinuousDouble distribution = new ProbabilisticContinuousDouble(mean,
                    covarianceMatrixId, covarianceMatrixIndex);
            mixtures[i] = new Pair<ProbabilisticContinuousDouble, Double>(distribution, probability);
        }
        return new ProbabilisticMultivariateContinuousDouble(mixtures);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticMultivariateContinuousDouble value = (ProbabilisticMultivariateContinuousDouble) data;
        buffer.putInt(value.getMixtures().length);
        for (final Pair<ProbabilisticContinuousDouble, Double> mixture : value.getMixtures()) {
            buffer.put(mixture.getE1().getCovarianceMatrixId());
            buffer.putInt(mixture.getE1().getCovarianceMatrixIndex());
            buffer.putDouble(mixture.getE1().getMean());
            buffer.putDouble(mixture.getE2());
        }
    }

    @Override
    public int memSize(final Object attribute) {
        final ProbabilisticMultivariateContinuousDouble value = (ProbabilisticMultivariateContinuousDouble) attribute;
        return (Integer.SIZE * value.getMixtures().length * (Byte.SIZE + Integer.SIZE + Double.SIZE)) / 8;
    }

    @Override
    protected IDataHandler<ProbabilisticMultivariateContinuousDouble> getInstance(final SDFSchema schema) {
        return new ProbabilisticMultivariateContinuousDoubleHandler();
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return ProbabilisticMultivariateContinuousDoubleHandler.types;
    }

}
