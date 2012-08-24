package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.MultivariantCovarianceMatrix;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MultivariantCovarianceMatrixHandler extends AbstractDataHandler<MultivariantCovarianceMatrix> {
    static protected List<String> types = new ArrayList<String>();
    static {
        MultivariantCovarianceMatrixHandler.types.add("MultivariantCovarianceMatrix");
    }

    public MultivariantCovarianceMatrixHandler() {
        super();
    }

    @Override
    protected IDataHandler<MultivariantCovarianceMatrix> getInstance(SDFSchema schema) {
        return new MultivariantCovarianceMatrixHandler();
    }

    @Override
    public MultivariantCovarianceMatrix readData(ByteBuffer buffer) {
        final int length = buffer.getInt();
        MultivariantCovarianceMatrix multivariantCovarianceMatrix = new MultivariantCovarianceMatrix(length);
        for (int i = 0; i < length; i++) {
            final Byte id = buffer.get();
            final int size = buffer.getInt();
            double[] entries = new double[size];
            for (int j = 0; j < entries.length; j++) {
                entries[j] = buffer.getDouble();
            }
            multivariantCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariantCovarianceMatrix;
    }

    @Override
    public MultivariantCovarianceMatrix readData(ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        MultivariantCovarianceMatrix multivariantCovarianceMatrix = new MultivariantCovarianceMatrix(length);
        for (int i = 0; i < length; i++) {
            final Byte id = inputStream.readByte();
            final int size = inputStream.readInt();
            double[] entries = new double[size];
            for (int j = 0; j < entries.length; j++) {
                entries[j] = inputStream.readDouble();
            }
            multivariantCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariantCovarianceMatrix;
    }

    @Override
    public MultivariantCovarianceMatrix readData(String string) {
        final String[] covarianceMatrices = string.split(";");
        MultivariantCovarianceMatrix multivariantCovarianceMatrix = new MultivariantCovarianceMatrix(
                covarianceMatrices.length);
        for (int i = 0; i < covarianceMatrices.length; i++) {
            final String[] covarianceMatrix = covarianceMatrices[i].split(":");
            final Byte id = Byte.parseByte(covarianceMatrix[0]);
            double[] entries = new double[covarianceMatrix.length - 1];
            for (int j = 0; j < covarianceMatrix.length - 1; j++) {
                entries[j] = Double.parseDouble(covarianceMatrix[1 + j]);
            }
            multivariantCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariantCovarianceMatrix;
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        final MultivariantCovarianceMatrix multivariantCovarianceMatrix = (MultivariantCovarianceMatrix) data;
        buffer.putInt(multivariantCovarianceMatrix.size());
        for (Entry<Byte, CovarianceMatrix> entry : multivariantCovarianceMatrix.entrySet()) {
            buffer.put(entry.getKey());
            buffer.putInt(entry.getValue().getEntries().length);
            for (double value : entry.getValue().getEntries()) {
                buffer.putDouble(value);
            }
        }
    }

    @Override
    public int memSize(Object attribute) {
        MultivariantCovarianceMatrix multivariantCovarianceMatrix = (MultivariantCovarianceMatrix) attribute;
        int size = 0;
        for (Entry<Byte, CovarianceMatrix> entry : multivariantCovarianceMatrix.entrySet()) {
            size += Byte.SIZE + entry.getValue().getEntries().length * Double.SIZE + Integer.SIZE;
        }
        return (size + Integer.SIZE) / 8;
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return types;
    }
}
