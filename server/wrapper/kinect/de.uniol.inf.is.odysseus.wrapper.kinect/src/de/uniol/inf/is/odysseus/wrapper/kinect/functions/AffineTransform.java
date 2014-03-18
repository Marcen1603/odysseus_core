package de.uniol.inf.is.odysseus.wrapper.kinect.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

/**
 * Function for affine transformation.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class AffineTransform extends AbstractFunction<KinectColorMap> {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = -7492013009681687773L;

    /** Supported input types. */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
            {SDFKinectDatatype.KINECT_COLOR_MAP},
            {SDFDatatype.MATRIX_DOUBLE, SDFDatatype.MATRIX_FLOAT}};

    public AffineTransform() {
    	super("AffineTransform",2,ACC_TYPES,SDFKinectDatatype.KINECT_COLOR_MAP);
    }

    @Override
    public KinectColorMap getValue() {
        final KinectColorMap colorMap = (KinectColorMap) this.getInputValue(0);
        final double[][] mat = (double[][]) getInputValue(1);
        KinectColorMap res = new KinectColorMap(colorMap);
        res.affineTransform(mat);
        return res;
    }

}
