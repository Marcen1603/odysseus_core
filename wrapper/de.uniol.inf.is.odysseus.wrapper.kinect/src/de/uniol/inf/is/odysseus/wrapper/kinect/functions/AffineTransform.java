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
                    + this.getArity() + " argument(s): A color map, a matrix.");
        }
        return AffineTransform.ACC_TYPES[argPos];
    }

    @Override
    public String getSymbol() {
        return "AffineTransform";
    }

    @Override
    public KinectColorMap getValue() {
        final KinectColorMap colorMap = (KinectColorMap) this.getInputValue(0);
        final double[][] mat = (double[][]) getInputValue(1);
        KinectColorMap res = new KinectColorMap(colorMap);
        res.affineTransform(mat);
        return res;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFKinectDatatype.KINECT_COLOR_MAP;
    }
}
