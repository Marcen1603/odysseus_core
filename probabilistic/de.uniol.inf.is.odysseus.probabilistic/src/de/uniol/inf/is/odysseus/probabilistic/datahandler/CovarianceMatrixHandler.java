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

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CovarianceMatrixHandler extends AbstractDataHandler<CovarianceMatrix> {
    static protected List<String> types = new ArrayList<String>();
    static {
        CovarianceMatrixHandler.types.add("CovarianceMatrix");
    }

    public CovarianceMatrixHandler() {
        super();
    }

    @Override
    protected IDataHandler<CovarianceMatrix> getInstance(SDFSchema schema) {
        return new CovarianceMatrixHandler();
    }

    @Override
    public CovarianceMatrix readData(ByteBuffer buffer) {
        final int id = buffer.getInt();
        final int length = buffer.getInt();
        double[] entries = new double[length];
        for (int i = 0; i < length; i++) {
            entries[i] = buffer.getDouble();
        }

        return new CovarianceMatrix(id, entries);
    }

    @Override
    public CovarianceMatrix readData(ObjectInputStream inputStream) throws IOException {
        final int id = inputStream.readInt();
        final int length = inputStream.readInt();
        double[] entries = new double[length];
        for (int i = 0; i < length; i++) {
            entries[i] = inputStream.readDouble();
        }
        return new CovarianceMatrix(id, entries);
    }

    @Override
    public CovarianceMatrix readData(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        // TODO Auto-generated method stub

    }

    @Override
    public int memSize(Object attribute) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return types;
    }
}
