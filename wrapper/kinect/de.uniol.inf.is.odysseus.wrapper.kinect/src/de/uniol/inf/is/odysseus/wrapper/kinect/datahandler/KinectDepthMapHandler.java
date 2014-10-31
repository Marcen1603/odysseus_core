package de.uniol.inf.is.odysseus.wrapper.kinect.datahandler;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.DEPTHMAP_MARKER;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.INTEGER_BYTES_SIZE;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectDepthMap;

/**
 * This handler can search a given buffer for a {@link KinectDepthMap} and return it as an
 * object.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectDepthMapHandler extends AbstractDataHandler<KinectDepthMap> {
    /** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add("DepthMap");
    }

    /**
     * Standard constructor.
     */
    public KinectDepthMapHandler() {
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public KinectDepthMapHandler(SDFSchema schema) {
    }

    @Override
    public KinectDepthMap readData(ByteBuffer buffer) {
        buffer.position(0);
        byte countFields = buffer.get();
        for (byte i = 0; i < countFields; i++) {
            byte marker = buffer.get();
            if (marker != DEPTHMAP_MARKER) {
                buffer.position(buffer.position() + INTEGER_BYTES_SIZE);
                continue;
            }

            int pos = buffer.getInt();
            buffer.position(pos);
            return new KinectDepthMap(buffer);
        }

        throw new IllegalArgumentException(
                "DepthMap was not recorded. Use ['depth','true'] in options.");
    }

    @Override
    public KinectDepthMap readData(String string) {
        throw new RuntimeException("Method is not implemented.");
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        throw new RuntimeException("Method is not implemented.");
    }

    @Override
    public int memSize(Object attribute) {
        return 0;
    }

    @Override
    protected IDataHandler<KinectDepthMap> getInstance(SDFSchema schema) {
        return new KinectDepthMapHandler(schema);
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(types);
    }
    
    @Override
    public Class<?> createsType() {
    	return KinectDepthMap.class;
    }

}
