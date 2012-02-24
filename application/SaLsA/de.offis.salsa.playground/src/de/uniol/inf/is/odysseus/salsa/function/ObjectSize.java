package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

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
                SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
                SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
                SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
                SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
                SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION, SDFSpatialDatatype.SPATIAL_GEOMETRY
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
