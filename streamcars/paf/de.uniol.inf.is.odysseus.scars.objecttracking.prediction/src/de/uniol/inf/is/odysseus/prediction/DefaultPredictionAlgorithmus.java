package de.uniol.inf.is.odysseus.prediction;

import org.apache.commons.math.geometry.Vector3D;

public class DefaultPredictionAlgorithmus implements IPredictionAlgorithmus{

	TestObject brokerObject;
	double sourceObjectTimeStamp;
	
	public DefaultPredictionAlgorithmus(TestObject brokerObject, long sourceObjectTimeStamp) {
		this.brokerObject = brokerObject;
		this.sourceObjectTimeStamp = sourceObjectTimeStamp;
	}

	@Override
	public TestObject executePredAlgorithmus() {
		TestObject newTestObject = new TestObject();
		newTestObject.setDirection(this.brokerObject.getDirection());
		newTestObject.setSpeed(this.brokerObject.getSpeed());
		newTestObject.setTimeStamp(this.brokerObject.getTimeStamp());
		newTestObject.setPosition(calcNewPos());
		return newTestObject;
	}
	
	private Vector3D calcNewPos() {
		double passedTime = sourceObjectTimeStamp-brokerObject.getTimeStamp();
		Vector3D newPos = new Vector3D();
		newPos = brokerObject.getDirection();
		newPos = newPos.scalarMultiply(passedTime*brokerObject.getSpeed());
		newPos = newPos.add(brokerObject.getPosition());
		return newPos;
	}
}
