package de.uniol.inf.is.odysseus.wrapper.kinect.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectDepthMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectPointCloud;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

/**
 * Function to transform a {@link KinectColorMap} and a {@link KinectDepthMap} to a point
 * cloud, that can be viewed in OpenGL.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class ToPointCloud extends AbstractFunction<KinectPointCloud> {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = 5516954047624317429L;

    /** Supported input types. */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
            {SDFKinectDatatype.KINECT_COLOR_MAP},
            {SDFKinectDatatype.KINECT_DEPTH_MAP}};

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException(
                    "negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only "
                    + this.getArity()
                    + " argument(s): A color map, a depth map.");
        }
        return ToPointCloud.ACC_TYPES[argPos];
    }

    @Override
    public String getSymbol() {
        return "ToPointCloud";
    }

    @Override
    public KinectPointCloud getValue() {
        final KinectColorMap colorMap = (KinectColorMap) this.getInputValue(0);
        final KinectDepthMap depthMap = (KinectDepthMap) this.getInputValue(1);
        return new KinectPointCloud(colorMap, depthMap);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFKinectDatatype.KINECT_POINT_CLOUD;
    }

}
