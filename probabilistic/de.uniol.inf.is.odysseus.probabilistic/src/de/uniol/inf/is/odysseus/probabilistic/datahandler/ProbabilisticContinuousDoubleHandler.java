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

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticContinuousDoubleHandler extends AbstractDataHandler<ProbabilisticContinuousDouble> {
    static protected List<String> types = new ArrayList<String>();
    static {
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousDouble");
        ProbabilisticContinuousDoubleHandler.types.add("ProbabilisticContinuousFloat");
    }

    @Override
    public IDataHandler<ProbabilisticContinuousDouble> getInstance(final SDFSchema schema) {
        return new ProbabilisticContinuousDoubleHandler();
    }

    public ProbabilisticContinuousDoubleHandler() {
        super();
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final Pair<Pair<Double, Double>, Double>[] values = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Integer matrixId = inputStream.readInt();
            final Integer matrixIndex = inputStream.readInt();
            final Double mean = inputStream.readDouble();
            final Double sigma = inputStream.readDouble();
            final Double probability = inputStream.readDouble();
            final Pair<Double, Double> distribution = new Pair<Double, Double>(mean, sigma);
            values[i] = new Pair<Pair<Double, Double>, Double>(distribution, probability);
        }
        return new ProbabilisticContinuousDouble(values);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final String string) {
        final String[] continuousValues = string.split(";");
        final Pair<Pair<Double, Double>, Double>[] values = new Pair[continuousValues.length];
        for (int i = 0; i < continuousValues.length; i++) {
            //FIXME use matrix id and index
            final String[] continuousValue = continuousValues[i].split(":");
            values[i] = new Pair<Pair<Double, Double>, Double>(new Pair<Double, Double>(
                    Double.parseDouble(continuousValue[0]), Double.parseDouble(continuousValue[1])),
                    Double.parseDouble(continuousValue[2]));
        }
        return new ProbabilisticContinuousDouble(values);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final Pair<Pair<Double, Double>, Double>[] values = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Integer matrixId = buffer.getInt();
            final Integer matrixIndex = buffer.getInt();
            final Double mean = buffer.getDouble();
            final Double sigma = buffer.getDouble();
            final Double probability = buffer.getDouble();
            final Pair<Double, Double> distribution = new Pair<Double, Double>(mean, sigma);
            values[i] = new Pair<Pair<Double, Double>, Double>(distribution, probability);
        }
        return new ProbabilisticContinuousDouble(values);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
        buffer.putInt(value.values().length);
        //FIXME use matrix id and index
        for (int i = 0; i < value.values().length; i++) {
            buffer.putDouble(value.values()[i].getE1().getE1());
            buffer.putDouble(value.values()[i].getE1().getE2());
            buffer.putDouble(value.values()[i].getE2());
        }
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return ProbabilisticContinuousDoubleHandler.types;
    }

    @Override
    public int memSize(final Object attribute) {
        return (((ProbabilisticContinuousDouble) attribute).values().length * Double.SIZE * 3) / 8;
    }

}
