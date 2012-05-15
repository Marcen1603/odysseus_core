package de.uniol.inf.is.odysseus.spatial.grid.model;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class PolarGrid implements Cloneable {
	public final Coordinate origin;
	public final int radius;
	public final int angle;
	private final double cellradius;
	private final double cellangle;
	private DoubleBuffer buffer;
	private final int size;

	public PolarGrid(Coordinate origin, int radius, double cellangle,
			double cellradius) {
		this.origin = origin;
		this.radius = radius;
		this.angle = (int) (360.0 / cellangle + 1);
		this.cellangle = cellangle;
		this.cellradius = cellradius;
		this.size = this.radius * this.angle;
		this.buffer = DoubleBuffer.allocate(size);
	}

	public PolarGrid(Coordinate origin, int radius, double cellangle,
			double cellradius, DoubleBuffer buffer) {
		this(origin, radius, cellangle, cellradius);
		setBuffer(buffer);
	}

	public double get(int r, int a) {
		return this.buffer.get(a * this.radius + r);
	}

	public double get(double radius, double angle) {
		double tmpAngle = angle;
		if (tmpAngle < 0.0) {
			tmpAngle += 360;
		}
		if (tmpAngle >= 360.0) {
			tmpAngle -= ((int) (angle / 360.0)) * 360;
		}
		int gridR = (int) (radius / cellradius);
		int gridA = (int) (tmpAngle / cellangle);
		return get(gridR, gridA);
	}

	public double[] get() {
		return this.buffer.array();
	}

	public DoubleBuffer getBuffer() {
		this.buffer.rewind();
		return this.buffer;
	}

	public void set(int r, int a, double value) {
		this.buffer.put(a * this.radius + r, value);
	}

	public void set(double radius, double angle, double value) {
		int gridR = (int) (radius / cellradius);
		int gridA = (int) (angle / cellangle);
		set(gridR, gridA, value);
	}

	public void setBuffer(DoubleBuffer value) {
		this.buffer.clear();
		value.rewind();
		this.buffer.put(value);
		this.buffer.flip();
	}

	public void fill(double value) {
		Arrays.fill(this.buffer.array(), value);
	}

	@Override
	public PolarGrid clone() {
		PolarGrid grid = new PolarGrid(new Coordinate(this.origin.x,
				this.origin.y), this.radius, this.cellangle, this.cellradius,
				this.buffer);
		return grid;
	}

	@Override
	public String toString() {
		return "{Origin: " + origin + ", Radius: " + radius + " Cell-Angle: "
				+ cellangle + " Cell-Radius: " + cellradius + "}";
	}
}
