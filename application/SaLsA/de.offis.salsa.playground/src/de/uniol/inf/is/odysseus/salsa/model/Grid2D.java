/**
 * 
 */
package de.uniol.inf.is.odysseus.salsa.model;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Grid2D {
    public final Coordinate origin;

    public final double cellsize;
    public final double[][] grid;

    public Grid2D(Coordinate origin, double width, double height, double cellsize) {
        this.origin = origin;
        this.cellsize = cellsize;
        this.grid = new double[(int) Math.ceil(width / cellsize)][(int) Math.ceil(height / cellsize)];
    }

    public double get(int x, int y) {
        return this.grid[x][y];
    }

    public void set(int x, int y, double value) {
        this.grid[x][y] = value;
    }

    public void fill(double value) {
        for (double[] cells : grid) {
            Arrays.fill(cells, value);
        }
    }

    @Override
    public String toString() {
        return "{O: " + origin + ",S: " + cellsize + "}";
    }
}
