package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeGeometries extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long serialVersionUID = -6160624202251051944L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
                    SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY
            },
            {
                    SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
                    SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY
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
                    + " argument(s): Two geometries.");
        }
        return MergeGeometries.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "MergeGeometries";
    }

    @Override
    public Geometry getValue() {
        final Geometry[] geometrys = new Geometry[2];

        geometrys[0] = (Geometry) this.getInputValue(0);
        geometrys[1] = (Geometry) this.getInputValue(1);

        final GeometryFactory geometryFactory = new GeometryFactory();

        return geometryFactory.createGeometryCollection(geometrys);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFSpatialDatatype.SPATIAL_GEOMETRY;
    }
}
