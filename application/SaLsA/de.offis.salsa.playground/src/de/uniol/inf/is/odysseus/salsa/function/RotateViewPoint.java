package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RotateViewPoint extends AbstractFunction<Geometry> {

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry and an angle.");
        }
        else {
            SDFDatatype[] accTypes = null;
            switch (argPos) {
                case 1:
                    accTypes = new SDFDatatype[7];
                    accTypes[0] = SDFDatatype.SPATIAL_POINT;
                    accTypes[1] = SDFDatatype.SPATIAL_MULTI_POINT;
                    accTypes[2] = SDFDatatype.SPATIAL_LINE;
                    accTypes[3] = SDFDatatype.SPATIAL_MULTI_LINE;
                    accTypes[4] = SDFDatatype.SPATIAL_MULTI_POLYGON;
                    accTypes[5] = SDFDatatype.SPATIAL_POLYGON;
                    accTypes[6] = SDFDatatype.SPATIAL;
                    break;
                case 2:
                    accTypes = new SDFDatatype[1];
                    accTypes[0] = SDFDatatype.DOUBLE;
                    break;
            }
            return accTypes;
        }
    }

    @Override
    public String getSymbol() {
        return "RotateViewPoint";
    }

    @Override
    public Geometry getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double angle = (Double) this.getInputValue(1);
        for (Coordinate coordinate : geometry.getCoordinates()) {
            double x = coordinate.x;
            double y = coordinate.y;
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
