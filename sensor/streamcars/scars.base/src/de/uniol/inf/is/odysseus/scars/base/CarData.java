package de.uniol.inf.is.odysseus.scars.base;

public class CarData {

	private int carType;
	private int carTrafficID;
	private int laneID;
	private double[] positionUTM;
	private float velocity;
	private float length;
	private float width;
	
	public CarData() {}
	
	public CarData(int pCarType, int pCarTrafficID, int pLaneID, double[] pPositionUTM, float pVelocity, float pLength, float pWidth) {
		this.setCarType(pCarType);
		this.setCarTrafficID(pCarTrafficID);
		this.setLaneID(pLaneID);
		this.setPositionUTM(pPositionUTM);
		this.setVelocity(pVelocity);
		this.setLength(pLength);
		this.setWidth(pWidth);
	}

	/**
	 * @param Setzt den carType auf carType.
	 */
	public void setCarType(int carType) {
		this.carType = carType;
	}

	/**
	 * @return carType
	 */
	public int getCarType() {
		return carType;
	}

	/**
	 * @param carTrafficID the carTrafficID to set
	 */
	public void setCarTrafficID(int carTrafficID) {
		this.carTrafficID = carTrafficID;
	}

	/**
	 * @return the carTrafficID
	 */
	public int getCarTrafficID() {
		return carTrafficID;
	}

	/**
	 * @param laneID the laneID to set
	 */
	public void setLaneID(int laneID) {
		this.laneID = laneID;
	}

	/**
	 * @return the laneID
	 */
	public int getLaneID() {
		return laneID;
	}

	/**
	 * @param positionUTM the positionUTM to set
	 */
	public void setPositionUTM(double[] positionUTM) {
		this.positionUTM = positionUTM;
	}

	/**
	 * @return the positionUTM
	 */
	public double[] getPositionUTM() {
		return positionUTM;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(float length) {
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
}
