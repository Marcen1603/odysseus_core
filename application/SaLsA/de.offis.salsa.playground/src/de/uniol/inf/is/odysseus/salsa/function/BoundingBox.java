package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class BoundingBox extends AbstractFunction<Geometry> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
                SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
        }
};
    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): Two geometries.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "BoundingBox";
    }

    @Override
    public Geometry getValue() {
        return ((Geometry) this.getInputValue(0)).getEnvelope();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.SPATIAL;
    }

}
