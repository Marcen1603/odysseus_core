package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ByteGridPartialAggregate<T> implements IPartialAggregate<T> {
    private final static byte FREE = (byte) 0x0;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static byte OBSTACLE = (byte) 0x64;

    private Byte[][] grid;

    public ByteGridPartialAggregate(Byte[][] grid) {
        this.grid = grid;
    }

    public ByteGridPartialAggregate(ByteGridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid;
    }

    public Byte[][] getGrid() {
        return grid;
    }

    public void merge(Byte[][] grid) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if ((i < grid.length) && (j < grid[i].length)) {
                    // FIXME Implement DST
                    if (this.grid[i][j] == UNKNOWN) {
                        this.grid[i][j] = grid[i][j];
                    }
                    else if ((this.grid[i][j] == OBSTACLE) && (grid[i][j] == OBSTACLE)) {
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
        StringBuffer ret = new StringBuffer("ByteGridPartialAggregate (").append(hashCode())
                .append(")").append(grid);
        return ret.toString();
    }

    @Override
    public ByteGridPartialAggregate<T> clone() {
        return new ByteGridPartialAggregate<T>(this);
    }
}
