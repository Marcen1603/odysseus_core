package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MoveViewPoint extends AbstractFunction<Geometry> {

    /**
     * 
     */
    private static final long serialVersionUID = -8906668468905044717L;

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }
    };

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry and a x and y value.");
        }
        else {
            return MoveViewPoint.accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "MoveViewPoint";
    }

    @Override
    public Geometry getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double x = (Double) this.getInputValue(1);
        final Double y = (Double) this.getInputValue(2);
        for (final Coordinate coordinate : geometry.getCoordinates()) {
            coordinate.x = coordinate.x - x;
            coordinate.y = coordinate.y - y;
        }
        return geometry;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.SPATIAL;
    }

}
