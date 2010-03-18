package de.uniol.inf.is.odysseus.broker.movingobjects.generator;

public class Vehicle {
	private int id;
	private double positionX;
	private double positionY;
	private int interval;
	
	public Vehicle(int id, int interval, double positionX, double positionY){
		this.id = id;
		this.interval = interval;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public double getPositionX() {
		return positionX;
	}
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}
	public double getPositionY() {
		return positionY;
	}
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}	
	
	
}
