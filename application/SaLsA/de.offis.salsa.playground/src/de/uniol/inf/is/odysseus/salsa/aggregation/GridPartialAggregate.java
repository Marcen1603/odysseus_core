package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

    private Grid2D grid;

    public GridPartialAggregate(Grid2D grid) {
        this.grid = grid;
    }

    public GridPartialAggregate(GridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Grid2D getGrid() {
        return grid;
    }

    public void merge(Grid2D grid) {
        for (int l = 0; l < this.grid.grid.length; l++) {
            for (int w = 0; w < this.grid.grid[l].length; w++) {
                if ((l < grid.grid.length) && (w < grid.grid[l].length)) {
                    // FIXME Implement DST
                    if (this.grid.get(l, w) == UNKNOWN) {
                        this.grid.set(l, w, grid.get(l, w));
                    }
                    else if ((this.grid.get(l, w) == OBSTACLE) && (grid.get(l, w) == OBSTACLE)) {
                        this.grid.set(l, w, OBSTACLE);
                    }
                    else if ((this.grid.get(l, w) == FREE) && (grid.get(l, w) == OBSTACLE)) {
                        this.grid.set(l, w, 0.5);
                    }
                    else if ((this.grid.get(l, w) == OBSTACLE) && (grid.get(l, w) == FREE)) {
                        this.grid.set(l, w, 0.5);
                    }
                    else if ((this.grid.get(l, w) == FREE) && (grid.get(l, w) == FREE)) {
                        this.grid.set(l, w, FREE);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer("GridPartialAggregate (").append(hashCode())
                .append(")").append(grid);
        return ret.toString();
    }

    @Override
    public GridPartialAggregate<T> clone() {
        return new GridPartialAggregate<T>(this);
    }
}
