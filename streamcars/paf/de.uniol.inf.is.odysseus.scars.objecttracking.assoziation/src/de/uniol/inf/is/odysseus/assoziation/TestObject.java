package de.uniol.inf.is.odysseus.assoziation;

import org.apache.commons.math.geometry.Vector3D;

public class TestObject {
	
	private int speed;
	private Vector3D position;
	private Vector3D direction;
	private long timeStamp;

	public TestObject() {
		
	}
	
	public TestObject(int speed, Vector3D position, Vector3D direction, long timeStamp) {
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

	public void setPosition(Vector3D position) {
		this.position = position;
	}

	public Vector3D getPosition() {
		return position;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
}
