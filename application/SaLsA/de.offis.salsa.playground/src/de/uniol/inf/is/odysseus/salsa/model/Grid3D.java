package de.uniol.inf.is.odysseus.salsa.model;

import java.util.Arrays;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Grid3D {
    private final double x;
    private final double y;
    private final double z;
    private final double cellsize;
    private final Double[][][] grid;

    public Grid3D(double x, double y, double z, double width, double height, double depth, double cellsize) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.cellsize = cellsize;
        this.grid = new Double[(int) (width / cellsize) + 1][(int) (height / cellsize) + 1][(int) (depth / cellsize) + 1];
    }

    public double getCellSize() {
        return cellsize;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
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
}
