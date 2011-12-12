package de.uniol.inf.is.odysseus.salsa.model;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Grid3D {
    public final Coordinate origin;
    public final double cellsize;
    public final Double[][][] grid;

    public Grid3D(Coordinate origin, double length, double width, double depth, double cellsize) {
        this.origin = origin;
        this.cellsize = cellsize;
        this.grid = new Double[(int) ((length / cellsize) + 0.5)][(int) ((width / cellsize) + 0.5)][(int) ((depth / cellsize) + 0.5)];
    }

    public double getCellSize() {
        return cellsize;
    }

    public Double get(int x, int y, int z) {
        return this.grid[x][y][z];
    }

    public void set(int x, int y, int z, Double value) {
        this.grid[x][y][z] = value;
    }

    public void fill(Double value) {
        for (Double[][] layers : grid) {
            for (Double[] cells : layers) {
                Arrays.fill(cells, value);
            }
        }
    }

    @Override
    public String toString() {
        return "{Origin: " + origin + ",Size: " + cellsize + "}";
    }
}
