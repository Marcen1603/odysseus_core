package de.uniol.inf.is.odysseus.assoziation;

import java.util.ArrayList;

public class TestObjectList {

	private ArrayList<TestObject> objectList = new ArrayList<TestObject>();

	public void addObject(TestObject obj) {
		this.objectList.add(obj);
	}
	
	public void setObjectList(ArrayList<TestObject> objectList) {
		this.objectList = objectList;
	}

	public ArrayList<TestObject> getObjectList() {
		return objectList;
	}
	
}
