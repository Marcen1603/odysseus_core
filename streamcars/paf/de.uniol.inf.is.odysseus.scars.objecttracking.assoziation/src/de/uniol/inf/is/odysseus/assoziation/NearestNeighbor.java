package de.uniol.inf.is.odysseus.assoziation;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;
import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementPair;

public class NearestNeighbor implements IAssoziationAlgorithmus {
	
	ArrayList<MeasurementObject> brokerObjects;
	ArrayList<MeasurementObject> newObjects;
	
	public NearestNeighbor(ArrayList<MeasurementObject> newObjects, ArrayList<MeasurementObject> brokerObjects) {
		this.brokerObjects = brokerObjects;
		this.newObjects = newObjects;
	}

	@Override
	public ArrayList<MeasurementPair> executeAssoAlgorithmus() {
		ArrayList<MeasurementPair> calculatedPairs = new ArrayList<MeasurementPair>();
		MeasurementPair pair = new MeasurementPair();
		HashMap<MeasurementPair, Double> distanceList = new HashMap<MeasurementPair, Double>();
		double distance = 0;
		for (MeasurementObject newObj : newObjects) {
			distance = 0;
			distanceList = new HashMap<MeasurementPair, Double>();
			for (MeasurementObject brokerObject : brokerObjects) {
				pair = new MeasurementPair();
				pair.setLeftObject(newObj);
				pair.setRightObject(brokerObject);
				distanceList.put(pair, calcDistance(newObj, brokerObject));
			}
			for (MeasurementPair measurementPair : distanceList.keySet()) {
				if(distance == 0) {
					distance = distanceList.get(measurementPair);
				}else if(distanceList.get(measurementPair) < distance) {
					distance = distanceList.get(measurementPair);
					pair = measurementPair;
				}
			}
			calculatedPairs.add(pair);
		}
		return calculatedPairs;
	}
	
	private double calcDistance(MeasurementObject newObj, MeasurementObject brokerObj) {
		Vector3D distanceVector = newObj.getPosition().subtract(brokerObj.getPosition());
		double distance = Math.sqrt(distanceVector.getX()*distanceVector.getX() + distanceVector.getY()*distanceVector.getY() + distanceVector.getZ()*distanceVector.getZ());
		return distance;
	}
}
