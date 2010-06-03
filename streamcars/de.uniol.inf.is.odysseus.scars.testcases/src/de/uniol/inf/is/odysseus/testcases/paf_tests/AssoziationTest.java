package de.uniol.inf.is.odysseus.testcases.paf_tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.assoziation.NearestNeighbor;
import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;
import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementPair;

public class AssoziationTest extends TestCase{
	
	public void testAssoAlgo() {
		MeasurementObject obj;
		ArrayList<MeasurementObject> newlist = new ArrayList<MeasurementObject>();
		ArrayList<MeasurementObject> brokerlist = new ArrayList<MeasurementObject>();
		
		// filling the newObjectList
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(20, 5, 0));
		newlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(10, 3, 0));
		newlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(5, 7, 0));
		newlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(20, 1, 0));
		newlist.add(obj);
		
		// filling the newObjectList
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(20, 4, 0));
		brokerlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(10, 1, 0));
		brokerlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(5, 1, 0));
		brokerlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(20, 7, 0));
		brokerlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(15, 5, 0));
		brokerlist.add(obj);
		
		obj = new MeasurementObject();
		obj.setPosition(new Vector3D(6, 3, 0));
		brokerlist.add(obj);
		
		NearestNeighbor assoAlgo = new NearestNeighbor(newlist, brokerlist);
		
		ArrayList<MeasurementPair> pairs = assoAlgo.executeAssoAlgorithmus();

	}

}
