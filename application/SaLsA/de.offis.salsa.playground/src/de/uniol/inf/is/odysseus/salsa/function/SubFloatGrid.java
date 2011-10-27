package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SubFloatGrid extends AbstractFunction<Float[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = 7686151757720213650L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_FLOAT
            },
            {
                SDFDatatype.INTEGER
            },
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.INTEGER
            }
    };

    @Override
    public int getArity() {
        return 4;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A grid, the grid size, a point, and a distance.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "SubFloatGrid";
    }

    @Override
    public Float[][] getValue() {
        final Float[][] grid = (Float[][]) this.getInputValue(0);
        final Double cellsize = (Double) this.getInputValue(1);
        final Coordinate point = (Coordinate) this.getInputValue(2);
        final Double width = (Double) this.getInputValue(3);

        final int positionX = (int) Math.ceil(point.x / cellsize);
        final int positionY = (int) Math.ceil(point.y / cellsize);
        int startX = (int) Math.max(positionX - width / 2, 0);
        int startY = (int) Math.max(positionY - width / 2, 0);
        final int endX = (int) Math.min(startX + width, grid.length);
        final int endY = (int) Math.min(startY + width, grid[0].length);
        final Float[][] subgrid;
        if ((startX >= endX) && (startY >= endY)) {
            subgrid = new Float[1][1];
            startX = endX - 1;
            startY = endY - 1;
        }
        else if (startX >= endX) {
            subgrid = new Float[1][endY - startY];
            startX = endX - 1;
        }
        else if (startY >= endY) {
            subgrid = new Float[endX - startX][1];
            startY = endY - 1;
        }
        else {
            subgrid = new Float[endX - startX][endY - startY];
        }
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                subgrid[i - startX][j - startY] = grid[i][j];
            }
        }
        return subgrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_FLOAT;
    }

}
