package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class DoubleGridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

    private Double[][] grid;

    public DoubleGridPartialAggregate(Double[][] grid) {
        this.grid = grid;
    }

    public DoubleGridPartialAggregate(DoubleGridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Double[][] getGrid() {
        return grid;
    }

    public void merge(Double[][] grid) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if ((i < grid.length) && (j < grid[i].length)) {
                  //FIXME Implement DST
                    if (this.grid[i][j] == UNKNOWN) {
                        this.grid[i][j] = grid[i][j];
                    }
                    else if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == OBSTACLE)) {
                        this.grid[i][j] = OBSTACLE;
                    }
                    else if ((this.grid[i][j] == FREE) && (grid[i][j] == OBSTACLE)) {
                        this.grid[i][j] = 0.5;
                    }
                    else if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == FREE)) {
                        this.grid[i][j] = 0.5;
                    }
                    else if ((this.grid[i][j] == FREE) && (grid[i][j] == FREE)) {
                        this.grid[i][j] = FREE;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer("DoubleGridPartialAggregate (").append(hashCode())
                .append(")").append(grid);
        return ret.toString();
    }

    @Override
    public DoubleGridPartialAggregate<T> clone() {
        return new DoubleGridPartialAggregate<T>(this);
    }
}
