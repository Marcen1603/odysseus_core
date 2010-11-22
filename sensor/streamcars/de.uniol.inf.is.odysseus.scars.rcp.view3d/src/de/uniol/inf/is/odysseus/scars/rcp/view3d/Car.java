package de.uniol.inf.is.odysseus.scars.rcp.view3d;

public class Car {

	private double x;
	private double y;
	
	public Car(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Car() {
		super();
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
