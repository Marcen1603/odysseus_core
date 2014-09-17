package de.uniol.inf.is.odysseus.sports.sportsql.parser.model;

public class Space {
	
	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;
	
	public Space(double xMin, double yMin, double xMax, double yMax) {
		this.xMax = xMax;
		this.xMin = xMin;
		this.yMax = yMax;
		this.yMin = yMin;
	}

	public double getXMax() {
		return xMax;
	}

	public void setXMax(double xMax) {
		this.xMax = xMax;
	}

	public double getXMin() {
		return xMin;
	}

	public void setXMin(double xMin) {
		this.xMin = xMin;
	}

	public double getYMax() {
		return yMax;
	}

	public void setYMax(double yMax) {
		this.yMax = yMax;
	}

	public double getYMin() {
		return yMin;
	}

	public void setYMin(double yMin) {
		this.yMin = yMin;
	}
	
	
	
}