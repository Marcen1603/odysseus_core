package de.uniol.inf.is.odysseus.scars.objecttracking.prediction;

import java.util.ArrayList;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;

public class DefaultPredictionAlgorithmus implements IPredictionAlgorithmus{

	ArrayList<MeasurementObject> oldObjects;
	double sourceObjectTimeStamp;
	
	public DefaultPredictionAlgorithmus(ArrayList<MeasurementObject> oldObjects, long sourceObjectTimeStamp) {
		this.oldObjects = oldObjects;
		this.sourceObjectTimeStamp = sourceObjectTimeStamp;
	}
	
	@Override
	public ArrayList<MeasurementObject> executePredAlgorithmus() {
		ArrayList<MeasurementObject> predictedObjects = new ArrayList<MeasurementObject>();
		for (MeasurementObject obj : this.oldObjects) {
			MeasurementObject newObject = new MeasurementObject();
			newObject.setDirection(obj.getDirection());
			newObject.setSpeed(obj.getSpeed());
			newObject.setTimeStamp(obj.getTimeStamp());
			newObject.setPosition(calcNewPos(obj));
			predictedObjects.add(newObject);
		}
		
		return predictedObjects;
	}
	
	private Vector3D calcNewPos(MeasurementObject obj) {
		double passedTime = sourceObjectTimeStamp-obj.getTimeStamp();
		Vector3D newPos = new Vector3D();
		newPos = obj.getDirection();
		newPos = newPos.scalarMultiply(passedTime*obj.getSpeed());
		newPos = newPos.add(obj.getPosition());
		return newPos;
	}
}
