package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class KinectSkeletonMap {
    private ArrayList<KinectSkeleton> skeletons = new ArrayList<>();
    
    public KinectSkeletonMap(ByteBuffer buf) {
        byte userCount = buf.get();
        for (int u = 0; u < userCount; u++) {
            KinectSkeleton skeleton = new KinectSkeleton(buf);
            skeletons.add(skeleton);
        }
    }
    
    public ArrayList<KinectSkeleton> getSkeletons() {
        return skeletons;
    }
}
