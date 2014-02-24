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
public class FloatDataHandler extends AbstractDataHandler<Float> {
    static protected List<String> types = new ArrayList<String>();
    static {
        types.add(SDFDatatype.FLOAT.getURI());
    }

    @Override
    public IDataHandler<Float> getInstance(SDFSchema schema) {
        return new FloatDataHandler();
    }

    public FloatDataHandler() {
        super();
    }

    @Override
    public Float readData(ObjectInputStream inputStream) throws IOException {
        return inputStream.readFloat();
    }

    @Override
    public Float readData(String string) {
        if (string != null && string.length() > 0) {
            return Float.parseFloat(string);
        }
        else {
            return null;
        }
    }

    @Override
    public Float readData(ByteBuffer buffer) {
        return buffer.getFloat();
    }

    @Override
    public void writeData(List<String> output, Object data) {
        output.add(((Number) data).toString());
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        // System.out.println("write Double Data "+(Double)data);
        buffer.putFloat(((Number) data).floatValue());
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return types;
    }

    @Override
    public int memSize(Object attribute) {
        return Float.SIZE / 8;
    }

    @Override
    public Class<?> createsType() {
        return Float.class;
    }

}
