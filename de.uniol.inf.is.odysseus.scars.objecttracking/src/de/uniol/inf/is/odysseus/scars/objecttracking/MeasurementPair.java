package de.uniol.inf.is.odysseus.scars.objecttracking;

public class MeasurementPair {

	private MeasurementObject leftObject = null;
	private MeasurementObject rightObject = null;
	
	public MeasurementPair() {
		
	}
	
	public MeasurementPair(MeasurementObject leftObject , MeasurementObject rightObject) {
		this.setLeftObject(leftObject);
		this.setRightObject(rightObject);
	}

	public void setLeftObject(MeasurementObject leftObject) {
		this.leftObject = leftObject;
	}

	public MeasurementObject getLeftObject() {
		return leftObject;
	}

	public void setRightObject(MeasurementObject rightObject) {
		this.rightObject = rightObject;
	}

	public MeasurementObject getRightObject() {
		return rightObject;
	}
	
}
