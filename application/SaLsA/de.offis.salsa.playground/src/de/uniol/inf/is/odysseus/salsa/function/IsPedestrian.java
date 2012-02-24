package de.uniol.inf.is.odysseus.salsa.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IsPedestrian extends AbstractFunction<Boolean> {
    /**
     * 
     */
    private static final long serialVersionUID = 572304260665128827L;

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(IsPedestrian.class);

    public static final SDFDatatype[] accTypes0 = new SDFDatatype[] {
        SDFSpatialDatatype.SPATIAL_GEOMETRY
    };
    public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
        SDFDatatype.DOUBLE
    };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry and an angle in degree.");
        }
        else {
            switch (argPos) {
                case 0:
                    return IsPedestrian.accTypes0;
                case 1:
                default:
                    return IsPedestrian.accTypes1;
            }
        }
    }

    @Override
    public String getSymbol() {
        return "IsPedestrian";
    }

    @Override
    public Boolean getValue() {
//        this.getInputValue(0);
//        final Double threshold = (Double) this.getInputValue(1);
//
//        final double theta = 0.0;
//        final double alpha = 0.0;
//
//        double similarityTheta;
//        double similarityAlpha;
//        double similarity;
//
//        // TODO Implement Least Square for line detection
//        if ((alpha != 0.0) && (theta != 0.0)) {
//            similarityTheta = 1 - (Math.PI / 2 - theta) / (Math.PI / 2);
//            similarityAlpha = 1 - (Math.PI / 2 - alpha) / (Math.PI / 2);
//            similarity = similarityAlpha * similarityTheta;
//            if (similarity >= threshold) {
//                return true;
//            }
//            else {
//                return false;
//            }
//        }
        return false;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.BOOLEAN;
    }
}
