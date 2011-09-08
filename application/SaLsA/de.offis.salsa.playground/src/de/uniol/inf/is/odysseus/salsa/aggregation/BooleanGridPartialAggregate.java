package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class BooleanGridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static boolean FREE = false;
    private final static boolean OBSTACLE = true;

    private Boolean[][] grid;

    public BooleanGridPartialAggregate(Boolean[][] grid) {
        this.grid = grid;
    }

    public BooleanGridPartialAggregate(BooleanGridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Boolean[][] getGrid() {
        return grid;
    }

    public void merge(Boolean[][] grid) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if ((i < grid.length) && (j < grid[i].length)) {
                    if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == OBSTACLE)) {
                        this.grid[i][j] = OBSTACLE;
                    }
                    else if ((this.grid[i][j] == FREE) && (grid[i][j] == OBSTACLE)) {
                        this.grid[i][j] = OBSTACLE;
                    }
                    else if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == FREE)) {
                        this.grid[i][j] = OBSTACLE;
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
        StringBuffer ret = new StringBuffer("BooleanGridPartialAggregate (").append(hashCode())
                .append(")").append(grid);
        return ret.toString();
    }

    @Override
    public BooleanGridPartialAggregate<T> clone() {
        return new BooleanGridPartialAggregate<T>(this);
    }
}
