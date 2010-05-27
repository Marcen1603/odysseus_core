package de.uniol.inf.is.odysseus.prediction;

import javax.vecmath.Vector3d;

public class TestObject {
	
	private int speed;
	private Vector3d position;
	private Vector3d direction;
	private long timeStamp;

	public TestObject() {
		
	}
	
	public TestObject(int speed, Vector3d position, Vector3d direction, long timeStamp) {
		this.speed = speed;
		this.position = position;
		this.direction = direction;
		this.timeStamp = timeStamp;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setPosition(Vector3d position) {
		this.position = position;
	}

	public Vector3d getPosition() {
		return position;
	}

	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

	public Vector3d getDirection() {
		return direction;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
}
