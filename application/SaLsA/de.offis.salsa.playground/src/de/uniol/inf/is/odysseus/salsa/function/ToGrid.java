package de.uniol.inf.is.odysseus.salsa.function;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToGrid extends AbstractFunction<Double[][]> {

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }
    };
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public int getArity() {
        return 6;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "ToGrid";
    }

    @Override
    public Double[][] getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final int x = (Integer) this.getInputValue(1);
        final int y = (Integer) this.getInputValue(2);
        final int width = (Integer) this.getInputValue(3);
        final int height = (Integer) this.getInputValue(4);
        final int cellsize = (Integer) this.getInputValue(5);
        final Coordinate[] coordinates = geometry.getCoordinates();
        Geometry bbox = geometry.getEnvelope();

        int maxX = (int) (bbox.getCoordinates()[1].x > x + width ? x + width : bbox
                .getCoordinates()[1].x);
        int minX = (int) (bbox.getCoordinates()[0].x < x ? x : bbox.getCoordinates()[0].x);
        int maxY = (int) (bbox.getCoordinates()[2].y > y + height ? y + height : bbox
                .getCoordinates()[2].y);
        int minY = (int) (bbox.getCoordinates()[0].y < y ? y : bbox.getCoordinates()[0].y);

        Double[][] grid = new Double[width / cellsize][height / cellsize];
        Arrays.fill(grid, -1.0);
        LinearRing linearRing = this.geometryFactory.createLinearRing(coordinates);
        for (int i = minX; i < maxX; i += cellsize) {
            for (int j = minY; j < maxY; j += cellsize) {
                LinearRing cell = this.geometryFactory.createLinearRing(new Coordinate[] {
                        new Coordinate(i, j), new Coordinate(i, j + cellsize),
                        new Coordinate(i + cellsize, j), new Coordinate(i + cellsize, j + cellsize)
                });
                if (!cell.intersects(linearRing)) {
                    grid[Math.abs(i / cellsize)][Math.abs(j / cellsize)] = 0.0;
                }
                else {
                    grid[Math.abs(i / cellsize)][Math.abs(j / cellsize)] = 1.0;
                }
            }
        }
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_DOUBLE;
    }

}
