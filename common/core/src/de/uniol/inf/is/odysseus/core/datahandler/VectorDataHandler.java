/**
 * 
 */
package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class VectorDataHandler extends AbstractDataHandler<double[]> {
    static protected List<String> types = new ArrayList<String>();
    static {
        types.add(SDFDatatype.VECTOR_DOUBLE.getURI());
    }
    private IDataHandler<Double> handler = new DoubleHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(ByteBuffer buffer) {
        int size = buffer.getInt();
        double[] value = new double[size];
        for (int i = 0; i < size; i++) {
            value[i] = this.handler.readData(buffer);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(ObjectInputStream inputStream) throws IOException {
        int size = inputStream.readInt();
        double[] value = new double[size];
        for (int i = 0; i < size; i++) {
            value[i] = this.handler.readData(inputStream);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(String string) {
        String[] lines = string.split("\n");
        int size = lines.length;
        double[] value = new double[size];
        for (int i = 0; i < size; i++) {
            value[i] = this.handler.readData(lines[i]);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        double[] value = (double[]) data;
        buffer.putInt(value.length);
        for (double v : value) {
            this.handler.writeData(buffer, v);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int memSize(Object data) {
        int size = 0;
        double[] value = (double[]) data;
        for (double v : value) {
            size += this.handler.memSize(v);
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> createsType() {
        return double[].class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IDataHandler<double[]> getInstance(SDFSchema schema) {
        return new VectorDataHandler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSupportedDataTypes() {
        return types;
    }
}
