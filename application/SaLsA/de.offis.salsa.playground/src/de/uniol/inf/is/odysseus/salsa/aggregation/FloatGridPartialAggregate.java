package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FloatGridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static float FREE = 0.0f;
    private final static float UNKNOWN = -1.0f;
    private final static float OBSTACLE = 1.0f;

    private Float[][] grid;

    public FloatGridPartialAggregate(Float[][] grid) {
        this.grid = grid;
    }

    public FloatGridPartialAggregate(FloatGridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Float[][] getGrid() {
        return grid;
    }

    public void merge(Float[][] grid) {
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
                        this.grid[i][j] = 0.5f;
                    }
                    else if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == FREE)) {
                        this.grid[i][j] = 0.5f;
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
        StringBuffer ret = new StringBuffer("FloatGridPartialAggregate (").append(hashCode())
                .append(")").append(grid);
        return ret.toString();
    }

    @Override
    public FloatGridPartialAggregate<T> clone() {
        return new FloatGridPartialAggregate<T>(this);
    }
}
