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
    public CovarianceMatrix readData(ByteBuffer buffer) {
        final int size = buffer.getInt();
        double[] entries = new double[size];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = buffer.getDouble();
        }
        return new CovarianceMatrix(entries);
    }

    @Override
    public CovarianceMatrix readData(ObjectInputStream inputStream) throws IOException {
        final int size = inputStream.readInt();
        double[] entries = new double[size];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = inputStream.readDouble();
        }
        return new CovarianceMatrix(entries);
    }

    @Override
    public CovarianceMatrix readData(String string) {
        final String[] covarianceMatrix = string.split(":");
        double[] entries = new double[covarianceMatrix.length];
        for (int i = 0; i < covarianceMatrix.length; i++) {
            entries[i] = Double.parseDouble(covarianceMatrix[i]);
        }
        return new CovarianceMatrix(entries);
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        final CovarianceMatrix covarianceMatrix = (CovarianceMatrix) data;
        buffer.putInt(covarianceMatrix.size());
        for (Double value : covarianceMatrix.getEntries()) {
            buffer.putDouble(value);
        }
    }

    @Override
    public int memSize(Object attribute) {
        final CovarianceMatrix covarianceMatrix = (CovarianceMatrix) attribute;
        return (Integer.SIZE + covarianceMatrix.getEntries().length * Double.SIZE) / 8;
    }

    @Override
    protected IDataHandler<CovarianceMatrix> getInstance(SDFSchema schema) {
        return new CovarianceMatrixHandler();
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return types;
    }

}
