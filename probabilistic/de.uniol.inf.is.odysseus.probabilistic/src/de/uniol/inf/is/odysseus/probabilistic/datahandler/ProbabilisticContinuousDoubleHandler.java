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
        final Byte covarianceMatrixId = inputStream.readByte();
        final Integer covarianceMatrixIndex = inputStream.readInt();
        final Double mean = inputStream.readDouble();
        return new ProbabilisticContinuousDouble(mean, covarianceMatrixId, covarianceMatrixIndex);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final String string) {
        final String[] continuousValue = string.split(":");
        final Byte covarianceMatrixId = Byte.parseByte(continuousValue[0]);
        final Integer covarianceMatrixIndex = Integer.parseInt(continuousValue[1]);
        final Double mean = Double.parseDouble(continuousValue[2]);
        return new ProbabilisticContinuousDouble(mean, covarianceMatrixId, covarianceMatrixIndex);
    }

    @Override
    public ProbabilisticContinuousDouble readData(final ByteBuffer buffer) {
        final Byte covarianceMatrixId = buffer.get();
        final Integer covarianceMatrixIndex = buffer.getInt();
        final Double mean = buffer.getDouble();
        return new ProbabilisticContinuousDouble(mean, covarianceMatrixId, covarianceMatrixIndex);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) data;
        buffer.put(value.getCovarianceMatrixId());
        buffer.putInt(value.getCovarianceMatrixIndex());
        buffer.putDouble(value.getMean());
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return ProbabilisticContinuousDoubleHandler.types;
    }

    @Override
    public int memSize(final Object attribute) {
        return (Byte.SIZE + Integer.SIZE + Double.SIZE) / 8;
    }

}
