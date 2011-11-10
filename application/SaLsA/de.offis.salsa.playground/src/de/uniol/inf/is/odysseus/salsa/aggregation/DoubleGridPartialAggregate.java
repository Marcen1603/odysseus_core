package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class DoubleGridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

    private Grid2D grid;

    public DoubleGridPartialAggregate(Grid2D grid) {
        this.grid = grid;
    }

    public DoubleGridPartialAggregate(DoubleGridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Grid2D getGrid() {
        return grid;
    }

    public void merge(Grid2D grid) {
        for (int i = 0; i < this.grid.grid.length; i++) {
            for (int j = 0; j < this.grid.grid[i].length; j++) {
                if ((i < grid.grid.length) && (j < grid.grid[i].length)) {
                    // FIXME Implement DST
                    if (this.grid.get(i, j) == UNKNOWN) {
                        this.grid.set(i, j, grid.get(i, j));
                    }
                    else if ((this.grid.get(i, j) == OBSTACLE) && (grid.get(i, j) == OBSTACLE)) {
                        this.grid.set(i, j, OBSTACLE);
                    }
                    else if ((this.grid.get(i, j) == FREE) && (grid.get(i, j) == OBSTACLE)) {
                        this.grid.set(i, j, 0.5);
                    }
                    else if ((this.grid.get(i, j) == OBSTACLE) && (grid.get(i, j) == FREE)) {
                        this.grid.set(i, j, 0.5);
                    }
                    else if ((this.grid.get(i, j) == FREE) && (grid.get(i, j) == FREE)) {
                        this.grid.set(i, j, FREE);
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
