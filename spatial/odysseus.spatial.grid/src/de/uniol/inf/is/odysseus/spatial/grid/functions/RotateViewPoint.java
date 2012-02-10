package de.uniol.inf.is.odysseus.spatial.grid.functions;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RotateViewPoint extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long serialVersionUID = -6834872922674099184L;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
    	{
        	SDFSpatialDatatype.SPATIAL_POINT, 
        	SDFSpatialDatatype.SPATIAL_LINE_STRING, 
        	SDFSpatialDatatype.SPATIAL_POLYGON,
        	SDFSpatialDatatype.SPATIAL_MULTI_POINT,
        	SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, 
        	SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, 
        	SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
        	SDFSpatialDatatype.SPATIAL_GEOMETRY 
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
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        final List<Point> coordinates = new ArrayList<Point>(geometry.getCoordinates().length);
        for (final Coordinate coordinate : geometry.getCoordinates()) {
            final double x = coordinate.x;
            final double y = coordinate.y;
            coordinates.add(this.geometryFactory.createPoint(new Coordinate(x * cos - y * sin, x
                    * sin + y * cos)));
        }
        return this.geometryFactory.createMultiPoint(coordinates.toArray(new Point[] {}));
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFSpatialDatatype.SPATIAL_GEOMETRY;
    }

}
