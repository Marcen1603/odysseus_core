package de.uniol.inf.is.odysseus.spatial.grid.model;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

public class PolarGrid implements Cloneable {
	public final Coordinate origin;
	public final double radius;
	private double cellradius;
	private double cellangle;
	private double[][] buffer;

	public PolarGrid(Coordinate origin, double radius, double cellangle,
			double cellradius) {
		this.origin = origin;
		this.radius = radius;
		this.cellangle = cellangle;
		this.cellradius = cellradius;
		this.buffer = new double[(int) ((radius / cellradius) + 0.5)][(int) ((360 / cellangle) + 0.5)];
	}

	public PolarGrid(Coordinate origin, double radius, double cellangle,
			double cellradius, double[][] buffer) {
		this(origin, radius, cellangle, cellradius);
		this.buffer = Arrays.copyOf(buffer, buffer.length);
	}

	public double get(int r, int a) {
		return this.buffer[r][a];
	}

	public double get(double radius, double angle) {
		int gridR = (int) ((radius / cellradius) + 0.5);
		int gridA = (int) ((angle / cellangle) + 0.5);
		return get(gridR, gridA);
	}

	public void set(int r, int a, double value) {
		this.buffer[r][a] = value;
	}

	public void set(double radius, double angle, double value) {
		int gridR = (int) ((radius / cellradius) + 0.5);
		int gridA = (int) ((angle / cellangle) + 0.5);
		set(gridR, gridA, value);
	}

	@Override
	public PolarGrid clone() {
		PolarGrid grid = new PolarGrid((Coordinate) origin.clone(),
				this.radius, this.cellangle, this.cellradius, this.buffer);
		return grid;
	}

	@Override
	public String toString() {
		return "{Origin: " + origin + ", Radius: " + radius + " Cell-Angle: "
				+ cellangle + " Cell-Radius: " + cellradius + "}";
	}
}
