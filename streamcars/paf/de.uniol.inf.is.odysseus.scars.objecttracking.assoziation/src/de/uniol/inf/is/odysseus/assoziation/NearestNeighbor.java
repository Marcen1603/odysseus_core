package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.assoziation.TestObject;

public class NearestNeighbor implements IAssoziationAlgorithmus {
	
	TestObjectList brokerObjects;
	TestObject newObject;
	
	public NearestNeighbor(TestObject newObject, TestObjectList brokerObjects) {
		this.brokerObjects = brokerObjects;
		this.newObject = newObject;
	}

	@Override
	public Object executeAssoAlgorithmus() {
		TestObject obj = new TestObject();
		for (TestObject brokerObj : brokerObjects.getObjectList()) {
			
		}
		return null;
	}
	
	private double calcDistance(TestObject obj1, TestObject obj2) {
		
		return 0;
	}
}
