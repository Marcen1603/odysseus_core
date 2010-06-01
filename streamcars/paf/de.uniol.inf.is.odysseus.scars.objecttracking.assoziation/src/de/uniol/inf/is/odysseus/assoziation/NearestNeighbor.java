package de.uniol.inf.is.odysseus.assoziation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;

public class NearestNeighbor implements IAssoziationAlgorithmus {
	
	ArrayList<MeasurementObject> brokerObjects;
	ArrayList<MeasurementObject> newObjects;
	
	public NearestNeighbor(ArrayList<MeasurementObject> newObjects, ArrayList<MeasurementObject> brokerObjects) {
		this.brokerObjects = brokerObjects;
		this.newObjects = newObjects;
	}

	@Override
	public Object executeAssoAlgorithmus() {
		MeasurementObject obj = new MeasurementObject();
		for (MeasurementObject brokerObj : brokerObjects) {
			
		}
		return null;
	}
	
	private double calcDistance(MeasurementObject obj1, MeasurementObject obj2) {
		
		return 0;
	}
}
