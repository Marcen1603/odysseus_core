/**
 * 
 */
package de.uniol.inf.is.odysseus.salsa.model;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Grid implements Cloneable {
    public final Coordinate origin;

    public final int width;
    public final int depth;
    public final int size;
    public final double cellsize;
    private final ByteBuffer buffer;

    public Grid(Coordinate origin, double width, double depth, double cellsize) {
        this.origin = origin;
        this.cellsize = cellsize;
        this.width = (int) ((width / cellsize) + 0.5);
        this.depth = (int) ((depth / cellsize) + 0.5);
        this.size = this.width * this.depth;
        this.buffer = ByteBuffer.allocate(this.size);
    }

    public Grid(Coordinate origin, double width, double depth, double cellsize, ByteBuffer buffer) {
        this(origin, width, depth, cellsize);
        this.buffer.put(buffer);
    }

    public byte get(int x, int y) {
        return this.buffer.get(y * this.width + x);
    }

    public byte get(double x, double y) {
        int gridX = (int) ((x / cellsize) + 0.5);
        int gridY = (int) ((y / cellsize) + 0.5);
        return get(gridX, gridY);
    }

    public byte[] get() {
        return this.buffer.array();
    }

    public ByteBuffer getBuffer() {
        this.buffer.rewind();
        return this.buffer;
    }

    public void set(int x, int y, byte value) {
        this.buffer.put(y * this.width + x, value);
    }

    public void set(double x, double y, byte value) {
        int gridX = (int) ((x / cellsize) + 0.5);
        int gridY = (int) ((y / cellsize) + 0.5);
        set(gridX, gridY, value);
    }

    public void setBuffer(ByteBuffer value) {
        this.buffer.clear();
        this.buffer.put(value);
    }

    public void fill(byte value) {
        Arrays.fill(this.buffer.array(), (byte) value);
    }

    @Override
    public Grid clone() {
        Grid grid = new Grid((Coordinate) origin.clone(), this.width * this.cellsize, this.depth
                * this.cellsize, this.cellsize, this.buffer);
        return grid;
    }

    @Override
    public String toString() {
        return "{Origin: " + origin + ", Width: " + width + " Depth: " + depth + " Size: "
                + cellsize * this.size + "}";
    }
    
}
