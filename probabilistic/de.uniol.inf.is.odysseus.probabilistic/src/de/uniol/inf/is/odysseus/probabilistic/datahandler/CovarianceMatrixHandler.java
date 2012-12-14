package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;

public class CovarianceMatrixHandler extends AbstractDataHandler<CovarianceMatrix> {
    static protected List<String> types = new ArrayList<String>();
    static {
        CovarianceMatrixHandler.types.add("CovarianceMatrix");
    }

    public CovarianceMatrixHandler() {
        super();
    }

    @Override
    public CovarianceMatrix readData(final ByteBuffer buffer) {
        final int size = buffer.getInt();
        final double[] entries = new double[size];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = buffer.getDouble();
        }
        return null;
        // return new CovarianceMatrix(entries,buffer.getDouble());
    }

    @Override
    public CovarianceMatrix readData(final ObjectInputStream inputStream) throws IOException {
        final int size = inputStream.readInt();
        final double[] entries = new double[size];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = inputStream.readDouble();
        }
        return null;
      //  return new CovarianceMatrix(entries,inputStream.readDouble());
    }

    @Override
    public CovarianceMatrix readData(final String string) {
        final String[] covarianceMatrix = string.split(":");
        final double[] entries = new double[covarianceMatrix.length];
        for (int i = 0; i < covarianceMatrix.length; i++) {
            entries[i] = Double.parseDouble(covarianceMatrix[i]);
        }
        // FIXME read probability of matrix
        return null;
        //  return new CovarianceMatrix(entries,0.0);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final CovarianceMatrix covarianceMatrix = (CovarianceMatrix) data;
        buffer.putInt(covarianceMatrix.size());
        for (final Double value : covarianceMatrix.getEntries()) {
            buffer.putDouble(value);
        }
      //  buffer.putDouble(covarianceMatrix.getProbability());
    }

    @Override
    public int memSize(final Object attribute) {
        final CovarianceMatrix covarianceMatrix = (CovarianceMatrix) attribute;
        return (Integer.SIZE + (covarianceMatrix.getEntries().length * Double.SIZE)) / 8;
    }

    @Override
    protected IDataHandler<CovarianceMatrix> getInstance(final SDFSchema schema) {
        return new CovarianceMatrixHandler();
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return CovarianceMatrixHandler.types;
    }

}
