package de.uniol.inf.is.odysseus.wrapper.kinect.datahandler;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.COLORMAP_MARKER;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.INTEGER_BYTES_SIZE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants;

/**
 * This handler can search a given buffer for a {@link KinectColorMap} and return it as an
 * object.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectColorMapHandler extends AbstractDataHandler<KinectColorMap> {
    /** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add("ColorMap");
    }

    /**
     * Standard constructor.
     */
    public KinectColorMapHandler() {
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public KinectColorMapHandler(SDFSchema schema) {
    }

    @Override
    public KinectColorMap readData(ByteBuffer buffer) {
        buffer.position(0);
        byte countFields = buffer.get();
        for (byte i = 0; i < countFields; i++) {
            byte marker = buffer.get();
            if (marker != COLORMAP_MARKER) {
                buffer.position(buffer.position() + INTEGER_BYTES_SIZE);
                continue;
            }

            int pos = buffer.getInt();
            buffer.position(pos);
            return new KinectColorMap(buffer);
        }

        throw new IllegalArgumentException(
                "ColorMap was not recorded. Use ['color','true'] in options.");
    }

    @Override
    public KinectColorMap readData(ObjectInputStream inputStream)
            throws IOException {
        throw new RuntimeException("Method is not implemented.");
    }

    @Override
    public KinectColorMap readData(String string) {
        throw new RuntimeException("Method is not implemented.");
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
    	buffer.clear();
    	KinectColorMap map = (KinectColorMap) data;
    	buffer.put((byte)1);
    	buffer.put(Constants.COLORMAP_MARKER);
    	buffer.putInt(6);
    	buffer.put(map.getData());
//        throw new RuntimeException("Method is not implemented.");
    }

    @Override
    public int memSize(Object attribute) {
        return 0;
    }

    @Override
    protected IDataHandler<KinectColorMap> getInstance(SDFSchema schema) {
        return new KinectColorMapHandler(schema);
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(types);
    }

    @Override
    public Class<?> createsType() {
    	return KinectColorMap.class;
    }
    
}
