package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ObjectSize extends AbstractFunction<Double> {
    /**
     * 
     */
    private static final long serialVersionUID = -2116045364398530128L;
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
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry.");
        }
        else {
            return ObjectSize.accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "ObjectSize";
    }

    @Override
    public Double getValue() {
        return ((Geometry) this.getInputValue(0)).getArea();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }
}
