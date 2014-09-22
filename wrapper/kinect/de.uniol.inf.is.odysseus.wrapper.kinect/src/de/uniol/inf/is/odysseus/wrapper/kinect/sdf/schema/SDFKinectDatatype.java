package de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Stores the data types used by Kinect feature.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class SDFKinectDatatype extends SDFDatatype {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = -6367377458534943179L;

    /**
     * Constructor.
     * @param uri
     * URI.
     */
    public SDFKinectDatatype(String uri) {
        super(uri, true);
    }

    /** Data type: Color map. */
    public static final SDFDatatype KINECT_COLOR_MAP = new SDFKinectDatatype(
            "ColorMap");

    /** Data type: Depth map. */
    public static final SDFDatatype KINECT_DEPTH_MAP = new SDFKinectDatatype(
            "DepthMap");

    /** Data type: Point cloud. */
    public static final SDFDatatype KINECT_POINT_CLOUD = new SDFKinectDatatype(
            "PointCloud");
    
    /** Data type: Skeleton map. */
    public static final SDFDatatype KINECT_SKELETON_MAP = new SDFKinectDatatype(
            "SkeletonMap");
    
    /** Data type: Rectangles to detected faces. */
    public static final SDFDatatype BUFFERED_IMAGE = new SDFKinectDatatype(
            "DetectedFace");   
}
