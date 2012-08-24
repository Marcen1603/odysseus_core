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
import de.uniol.inf.is.odysseus.probabilistic.datatype.MultivariateCovarianceMatrix;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MultivariateCovarianceMatrixHandler extends AbstractDataHandler<MultivariateCovarianceMatrix> {
    static protected List<String> types = new ArrayList<String>();
    static {
        MultivariateCovarianceMatrixHandler.types.add("MultivariateCovarianceMatrix");
    }

    public MultivariateCovarianceMatrixHandler() {
        super();
    }

    @Override
    protected IDataHandler<MultivariateCovarianceMatrix> getInstance(final SDFSchema schema) {
        return new MultivariateCovarianceMatrixHandler();
    }

    @Override
    public MultivariateCovarianceMatrix readData(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        final MultivariateCovarianceMatrix multivariateCovarianceMatrix = new MultivariateCovarianceMatrix(length);
        for (int i = 0; i < length; i++) {
            final Byte id = buffer.get();
            final int size = buffer.getInt();
            final double[] entries = new double[size];
            for (int j = 0; j < entries.length; j++) {
                entries[j] = buffer.getDouble();
            }
            multivariateCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariateCovarianceMatrix;
    }

    @Override
    public MultivariateCovarianceMatrix readData(final ObjectInputStream inputStream) throws IOException {
        final int length = inputStream.readInt();
        final MultivariateCovarianceMatrix multivariateCovarianceMatrix = new MultivariateCovarianceMatrix(length);
        for (int i = 0; i < length; i++) {
            final Byte id = inputStream.readByte();
            final int size = inputStream.readInt();
            final double[] entries = new double[size];
            for (int j = 0; j < entries.length; j++) {
                entries[j] = inputStream.readDouble();
            }
            multivariateCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariateCovarianceMatrix;
    }

    @Override
    public MultivariateCovarianceMatrix readData(final String string) {
        final String[] covarianceMatrices = string.split(";");
        final MultivariateCovarianceMatrix multivariateCovarianceMatrix = new MultivariateCovarianceMatrix(
                covarianceMatrices.length);
        for (final String covarianceMatrice : covarianceMatrices) {
            final String[] covarianceMatrix = covarianceMatrice.split(":");
            final Byte id = Byte.parseByte(covarianceMatrix[0]);
            final double[] entries = new double[covarianceMatrix.length - 1];
            for (int j = 0; j < (covarianceMatrix.length - 1); j++) {
                entries[j] = Double.parseDouble(covarianceMatrix[1 + j]);
            }
            multivariateCovarianceMatrix.put(id, new CovarianceMatrix(entries));
        }
        return multivariateCovarianceMatrix;
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final MultivariateCovarianceMatrix multivariateCovarianceMatrix = (MultivariateCovarianceMatrix) data;
        buffer.putInt(multivariateCovarianceMatrix.size());
        for (final Entry<Byte, CovarianceMatrix> entry : multivariateCovarianceMatrix.entrySet()) {
            buffer.put(entry.getKey());
            buffer.putInt(entry.getValue().getEntries().length);
            for (final double value : entry.getValue().getEntries()) {
                buffer.putDouble(value);
            }
        }
    }

    @Override
    public int memSize(final Object attribute) {
        final MultivariateCovarianceMatrix multivariateCovarianceMatrix = (MultivariateCovarianceMatrix) attribute;
        int size = 0;
        for (final Entry<Byte, CovarianceMatrix> entry : multivariateCovarianceMatrix.entrySet()) {
            size += Byte.SIZE + (entry.getValue().getEntries().length * Double.SIZE) + Integer.SIZE;
        }
        return (size + Integer.SIZE) / 8;
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return MultivariateCovarianceMatrixHandler.types;
    }
}
