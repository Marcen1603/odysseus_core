package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialAggregate<T> implements IPartialAggregate<T> {
    private Double[][] grid;

    public GridPartialAggregate(Double[][] grid) {
        this.grid = grid;
    }

    public GridPartialAggregate(GridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Double[][] getGrid() {
        return grid;
    }

    public void merge(Double[][] grid) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if ((i < grid.length) && (j < grid[i].length)) {
                    if (this.grid[i][j] < 0.0) {
                        this.grid[i][j] = grid[i][j];
                    }
                    else if (grid[i][j] >= 0.0) {
                        this.grid[i][j] *= grid[i][j];
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
