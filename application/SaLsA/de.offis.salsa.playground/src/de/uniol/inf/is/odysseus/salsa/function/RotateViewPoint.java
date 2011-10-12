package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RotateViewPoint extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long serialVersionUID = -6834872922674099184L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.DOUBLE
            }
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
            return RotateViewPoint.accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "RotateViewPoint";
    }

    @Override
    public Geometry getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        Double angle = (Double) this.getInputValue(1);
        angle = Math.toRadians(angle);
        for (final Coordinate coordinate : geometry.getCoordinates()) {
            final double x = coordinate.x;
            final double y = coordinate.y;
            coordinate.x = x * Math.cos(angle) - y * Math.sin(angle);
            coordinate.y = x * Math.sin(angle) + y * Math.cos(angle);
        }
        return geometry;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.SPATIAL;
    }

}
