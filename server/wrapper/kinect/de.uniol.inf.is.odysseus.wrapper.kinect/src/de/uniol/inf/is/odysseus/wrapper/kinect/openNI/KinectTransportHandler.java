package de.uniol.inf.is.odysseus.wrapper.kinect.openNI;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.COLORMAP_MARKER;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.COUNT_JOINTS;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.DEPTHMAP_MARKER;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.INTEGER_BYTES_SIZE;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.SHORT_BYTES_SIZE;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.SKELETON_MARKER;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import org.openni.DepthMap;
import org.openni.GeneralException;
import org.openni.ImageMap;
import org.openni.SkeletonJoint;
import org.openni.SkeletonJointPosition;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * This class handles the connection via USB to the Kinect camera and constructs a
 * ByteBuffer to be processed by the protocol handlers.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectTransportHandler extends AbstractPushTransportHandler
        implements DataReceivedListener {
    /** Should the color map be recorded by the handler. */
    private boolean recordColorMap = true;
    
    /** Should the depth map be recorded by the handler. */
    private boolean recordDepthMap = true;
    
    /** Should the skeleton map be recorded by the handler. */
    private boolean recordSkeletonMap = false;

    /**
     * Standard constructor.
     */
    public KinectTransportHandler() {
        super();
    }

    /**
     * Copy constructor.
     * @param protocolHandler
     * Instance to copy.
     */
    public KinectTransportHandler(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        super(protocolHandler, options);
        if (options.containsKey("color")) {
            recordColorMap = Boolean.parseBoolean(options.get("color"));
        }
        if (options.containsKey("depth")) {
            recordDepthMap = Boolean.parseBoolean(options.get("depth"));
        }
        if (options.containsKey("skeleton")) {
            recordSkeletonMap = Boolean.parseBoolean(options.get("skeleton"));
        }

    }

    @Override
    public void send(byte[] message) throws IOException {
    }

    @Override
    public ITransportHandler createInstance(
            IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        KinectTransportHandler handler = new KinectTransportHandler(
                protocolHandler, options);
        return handler;
    }

    @Override
    public String getName() {
        return "Kinect";
    }

    @Override
    public void processInOpen() throws IOException {
        ContextHandler.getInstance().addDataReceivedListener(this);
    }

    @Override
    public void processInClose() throws IOException {
        ContextHandler.getInstance().removeDataReceivedListener(this);
    }

    @Override
    public void processOutOpen() throws IOException {
        throw new IllegalArgumentException("The " + getName()
                + " transporthandler cannot be used for output.");
    }

    @Override
    public void processOutClose() throws IOException {
        throw new IllegalArgumentException("The " + getName()
                + " transporthandler cannot be used for output.");
    }

    @Override
    public void onDataReceived() {
        try {
            int bufferLen = 1; // 1 byte for number of fields
            byte countFields = 0;
            byte[] colorData = null;
            short[] depthData = null;
            ByteBuffer skeletonData = null;
            if (recordColorMap) {
                ImageMap img = ContextHandler.getInstance().getImageGenerator()
                        .getImageMap();
                ByteBuffer imgData = img.createByteBuffer();
                bufferLen += imgData.capacity() + 1 + INTEGER_BYTES_SIZE;
                // 1 byte for marker + 1 int for position

                countFields++;
                colorData = new byte[imgData.capacity()];
                imgData.get(colorData);
            }
            if (recordDepthMap) {
                DepthMap dpt = ContextHandler.getInstance().getDepthGenerator()
                        .getDepthMap();
                ShortBuffer dptData = dpt.createShortBuffer();
                bufferLen += dptData.capacity() * SHORT_BYTES_SIZE + 1 + INTEGER_BYTES_SIZE;
                countFields++;
                depthData = new short[dptData.capacity()];
                dptData.get(depthData);
            }
            if (recordSkeletonMap) {
                skeletonData = bufferFromJoints();
                bufferLen += skeletonData.capacity() + 1 + INTEGER_BYTES_SIZE;
                countFields++;
            }

            // write header
            ByteBuffer res = ByteBuffer.allocateDirect(bufferLen);
            int dataOffset = 1 + (1 + INTEGER_BYTES_SIZE) * countFields;
            res.put(countFields);
            if (recordColorMap) {
                res.put(COLORMAP_MARKER);
                res.putInt(dataOffset);
                dataOffset += colorData.length;
            }
            if (recordDepthMap) {
                res.put(DEPTHMAP_MARKER);
                res.putInt(dataOffset);
                dataOffset += depthData.length * SHORT_BYTES_SIZE;
            }
            if (recordSkeletonMap) {
                res.put(SKELETON_MARKER);
                res.putInt(dataOffset);
                dataOffset += skeletonData.capacity();
            }

            // write data
            if (recordColorMap) {
                res.put(colorData);
            }
            if (recordDepthMap) {
                res.asShortBuffer().put(depthData);
                res.position(res.position() + depthData.length * SHORT_BYTES_SIZE);
            }
            if (recordSkeletonMap) {
                res.put(skeletonData);
            }

            res.position(res.limit());
            super.fireProcess(res);
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }
    
    private ByteBuffer bufferFromJoints() {
        HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> joints = ContextHandler.getInstance().getJoints();
        byte countUsers = (byte)joints.size();
        int bufferLen = 1 + countUsers * (INTEGER_BYTES_SIZE + COUNT_JOINTS * 3 * INTEGER_BYTES_SIZE);
        ByteBuffer res = ByteBuffer.allocateDirect(bufferLen);
        
        res.put(countUsers);
        for (Integer userId : joints.keySet()) {
            res.putInt(userId);
            HashMap<SkeletonJoint, SkeletonJointPosition> userJ = joints.get(userId);
            
            putJointToBuffer(userJ.get(SkeletonJoint.HEAD), res);
            putJointToBuffer(userJ.get(SkeletonJoint.NECK), res);
            
            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_SHOULDER), res);
            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_ELBOW), res);
            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_HAND), res);
            
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_SHOULDER), res);
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_ELBOW), res);
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_HAND), res);
            
            putJointToBuffer(userJ.get(SkeletonJoint.TORSO), res);

            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_HIP), res);
            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_KNEE), res);
            putJointToBuffer(userJ.get(SkeletonJoint.LEFT_FOOT), res);
            
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_HIP), res);
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_KNEE), res);
            putJointToBuffer(userJ.get(SkeletonJoint.RIGHT_FOOT), res);
        }
        
        res.rewind();
        return res;
    }
    
    private void putJointToBuffer(SkeletonJointPosition pos, ByteBuffer buf) {
        buf.putFloat(pos.getPosition().getX());
        buf.putFloat(pos.getPosition().getY());
        buf.putFloat(pos.getPosition().getZ());
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }
    
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof KinectTransportHandler)) {
    		return false;
    	}
    	KinectTransportHandler other = (KinectTransportHandler)o;
    	if(this.recordColorMap != other.recordColorMap) {
    		return false;
    	} else if(this.recordDepthMap != other.recordDepthMap) {
    		return false;
    	} else if(this.recordSkeletonMap != other.recordSkeletonMap) {
    		return false;
    	}
    	
    	return true;
    }
}
