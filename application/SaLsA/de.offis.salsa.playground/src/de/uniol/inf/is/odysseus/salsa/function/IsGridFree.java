package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.model.Grid;

public class IsGridFree extends AbstractFunction<Boolean> {
    /**
     * 
     */
    private static final long serialVersionUID = -5768528294591995540L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID
            },
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }
    };
    private final static byte UNKNOWN = (byte) 0xFF;

    @Override
    public int getArity() {
        return 5;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A grid, the x and y coordinates, the width and height.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "IsGridFree";
    }

    @Override
    public Boolean getValue() {
        final Grid grid = (Grid) this.getInputValue(0);
        final Coordinate point = (Coordinate) this.getInputValue(1);
        Double width = (Double) this.getInputValue(2);
        Double depth = (Double) this.getInputValue(3);
        Double threshold = (Double) this.getInputValue(4);

        final int positionX = (int) (((point.x - grid.origin.x) / grid.cellsize) + 0.5);
        final int positionY = (int) (((point.y - grid.origin.y) / grid.cellsize) + 0.5);

        int startX = (int) (positionX - width / 2);
        int startY = (int) (positionY - depth / 2);
        final int endX = (int) (startX + width);
        final int endY = (int) (startY + depth);

        boolean free = true;

        int startGridX = (int) Math.max(startX, 0);
        int startGridY = (int) Math.max(startY, 0);
        int endGridX = (int) Math.min(endX, grid.width);
        int endGridY = (int) Math.min(endY, grid.depth);
        for (int l = startGridX; l < endGridX; l++) {
            for (int w = startGridY; w < endGridY; w++) {
                if ((grid.get(l, w) == UNKNOWN) || (grid.get(l, w) >= threshold)) {
                    free = false;
                }
            }
        }
        return free;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.BOOLEAN;
    }

}
