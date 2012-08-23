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
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDoubleHandler extends AbstractDataHandler<ProbabilisticDouble> {
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
    public ProbabilisticDouble readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final Pair<Double, Double>[] values = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Double value = inputStream.readDouble();
            final Double probability = inputStream.readDouble();
            values[i] = new Pair<Double, Double>(value, probability);
        }
        return new ProbabilisticDouble(values);
    }

    @Override
    public ProbabilisticDouble readData(final String string) {
        final String[] discreteValues = string.split(";");
        final Pair<Double, Double>[] values = new Pair[discreteValues.length];
        for (int i = 0; i < discreteValues.length; i++) {
            final String[] discreteValue = discreteValues[i].split(":");
            values[i] = new Pair<Double, Double>(Double.parseDouble(discreteValue[0]),
                    Double.parseDouble(discreteValue[1]));
        }
        return new ProbabilisticDouble(values);
    }

    @Override
    public ProbabilisticDouble readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final Pair<Double, Double>[] values = new Pair[length];
        for (int i = 0; i < length; i++) {
            final Double value = buffer.getDouble();
            final Double probability = buffer.getDouble();
            values[i] = new Pair<Double, Double>(value, probability);
        }
        return new ProbabilisticDouble(values);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticDouble value = (ProbabilisticDouble) data;
        buffer.putInt(value.values().length);
        for (int i = 0; i < value.values().length; i++) {
            buffer.putDouble(value.values()[i].getE1());
            buffer.putDouble(value.values()[i].getE2());
        }
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return ProbabilisticDoubleHandler.types;
    }

    @Override
    public int memSize(final Object attribute) {
        return (((ProbabilisticDouble) attribute).values().length * Double.SIZE * 2) / 8;
    }
}
