package de.uniol.inf.is.odysseus.wrapper.kinect.datahandler;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.SKELETON_MARKER;
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
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectSkeletonMap;

public class KinectSkeletonMapHandler extends AbstractDataHandler<KinectSkeletonMap> {
    /** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add("SkeletonMap");
    }

    /**
     * Standard constructor.
     */
    public KinectSkeletonMapHandler() {
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public KinectSkeletonMapHandler(SDFSchema schema) {
    }
    
    @Override
    public KinectSkeletonMap readData(ByteBuffer buffer) {
        buffer.position(0);
        byte countFields = buffer.get();
        for (byte i = 0; i < countFields; i++) {
            byte marker = buffer.get();
            if (marker != SKELETON_MARKER) {
                buffer.position(buffer.position() + INTEGER_BYTES_SIZE);
                continue;
            }

            int pos = buffer.getInt();
            buffer.position(pos);
            return new KinectSkeletonMap(buffer);
        }

        throw new IllegalArgumentException(
                "Skeleton map was not recorded. Use ['skeleton','true'] in options.");
    }
    
    @Override
    public KinectSkeletonMap readData(ObjectInputStream inputStream)
            throws IOException {
        throw new RuntimeException("Method is not implemented.");
    }
    
    @Override
    public KinectSkeletonMap readData(String string) {
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
    protected IDataHandler<KinectSkeletonMap> getInstance(SDFSchema schema) {
        return new KinectSkeletonMapHandler(schema);
    }
    
    @Override
    public List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(types);
    }
    
    @Override
    public Class<?> createsType() {
    	return KinectSkeletonMap.class;
    }
}
