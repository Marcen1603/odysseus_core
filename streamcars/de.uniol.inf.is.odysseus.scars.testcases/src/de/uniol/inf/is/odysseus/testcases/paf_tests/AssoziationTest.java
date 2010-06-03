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
		int i = 0;
		for (MeasurementPair measurementPair : pairs) {
			switch (i) {
			case 0:
				assertEquals(20.0, measurementPair.getLeftObject().getPosition().getX());
				assertEquals(5.0, measurementPair.getLeftObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getLeftObject().getPosition().getZ());
				
				assertEquals(20.0, measurementPair.getRightObject().getPosition().getX());
				assertEquals(4.0, measurementPair.getRightObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getRightObject().getPosition().getZ());
				break;
				
			case 1:
				assertEquals(10.0, measurementPair.getLeftObject().getPosition().getX());
				assertEquals(3.0, measurementPair.getLeftObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getLeftObject().getPosition().getZ());
				
				assertEquals(10.0, measurementPair.getRightObject().getPosition().getX());
				assertEquals(1.0, measurementPair.getRightObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getRightObject().getPosition().getZ());
				break;
				
			case 2:
				assertEquals(5.0, measurementPair.getLeftObject().getPosition().getX());
				assertEquals(7.0, measurementPair.getLeftObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getLeftObject().getPosition().getZ());
				
				assertEquals(6.0, measurementPair.getRightObject().getPosition().getX());
				assertEquals(3.0, measurementPair.getRightObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getRightObject().getPosition().getZ());
				break;
				
			case 3:
				assertEquals(20.0, measurementPair.getLeftObject().getPosition().getX());
				assertEquals(1.0, measurementPair.getLeftObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getLeftObject().getPosition().getZ());
				
				assertEquals(20.0, measurementPair.getRightObject().getPosition().getX());
				assertEquals(4.0, measurementPair.getRightObject().getPosition().getY());
				assertEquals(0.0, measurementPair.getRightObject().getPosition().getZ());
				break;

			default:
				break;
			}
			i++;
		}

	}

}
