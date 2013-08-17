package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class KinectSkeletonMap {
    private ArrayList<KinectSkeleton> skeletons = new ArrayList<>();
    
    public KinectSkeletonMap(ByteBuffer buf) {
        byte userCount = buf.get();
        for (int u = 0; u < userCount; u++) {
            KinectSkeleton skeleton = new KinectSkeleton(buf);
            skeletons.add(skeleton);
        }
    }

    public void render(GL2 gl) {
        for (KinectSkeleton skeleton : skeletons) {
            skeleton.render(gl);
        }
    }
    
    public ArrayList<KinectSkeleton> getSkeletons() {
        return skeletons;
    }
}
