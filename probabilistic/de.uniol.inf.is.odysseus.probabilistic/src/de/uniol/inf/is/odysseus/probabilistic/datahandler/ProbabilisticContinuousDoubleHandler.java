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
        final Double mean = inputStream.readDouble();
        final Double sigma = inputStream.readDouble();
        return new ProbabilisticContinuousDouble(mean, sigma);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final String string) {
        final String[] continuousValues = string.split(":");
        final Double mean = Double.parseDouble(continuousValues[0]);
        final Double sigma = Double.parseDouble(continuousValues[1]);
        return new ProbabilisticContinuousDouble(mean, sigma);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
        final Double mean = buffer.getDouble();
        final Double sigma = buffer.getDouble();

        return new ProbabilisticContinuousDouble(mean, sigma);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
        buffer.putDouble(value.mean());
        buffer.putDouble(value.sigma());
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return ProbabilisticContinuousDoubleHandler.types;
    }

    @Override
    public int memSize(final Object attribute) {
        return (Double.SIZE * 2) / 8;
    }

}
