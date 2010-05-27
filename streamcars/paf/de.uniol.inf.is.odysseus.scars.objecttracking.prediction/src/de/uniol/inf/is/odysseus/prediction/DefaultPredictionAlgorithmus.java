package de.uniol.inf.is.odysseus.prediction;

import javax.vecmath.Vector3d;

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
	
	private Vector3d calcNewPos() {
		double passedTime = sourceObjectTimeStamp-brokerObject.getTimeStamp();
		Vector3d newPos = new Vector3d();
		newPos = brokerObject.getDirection();
		newPos.scale(passedTime*brokerObject.getSpeed());
		newPos.add(brokerObject.getPosition());
		return newPos;
	}
}
