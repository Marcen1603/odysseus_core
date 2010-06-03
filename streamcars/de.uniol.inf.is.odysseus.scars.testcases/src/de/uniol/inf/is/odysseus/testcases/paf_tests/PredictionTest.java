package de.uniol.inf.is.odysseus.testcases.paf_tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.DefaultPredictionAlgorithmus;

public class PredictionTest extends TestCase {
	
	public void testPredAlgo() {
		MeasurementObject obj = new MeasurementObject(5, new Vector3D(10, 20, 0), new Vector3D(1, 0, 0), 5);
		ArrayList<MeasurementObject> objList = new ArrayList<MeasurementObject>();
		objList.add(obj);
		obj = new MeasurementObject(7, new Vector3D(10, 10, 0), new Vector3D(1, 0, 0), 3);
		objList.add(obj);
		obj = new MeasurementObject(3, new Vector3D(10, 5, 0), new Vector3D(1, 0, 0), 5);
		objList.add(obj);
		DefaultPredictionAlgorithmus prdAlgo = new DefaultPredictionAlgorithmus(objList, 10);
		ArrayList<MeasurementObject> newObjs = prdAlgo.executePredAlgorithmus();
		
		int i = 0;
		for (MeasurementObject newObj : newObjs) {
			switch (i) {
			case 0:
				assertEquals(35.0d, newObj.getPosition().getX());
				assertEquals(20.0d, newObj.getPosition().getY());
				assertEquals(0.0d, newObj.getPosition().getZ());
				break;
				
			case 1:
				assertEquals(59.0d, newObj.getPosition().getX());
				assertEquals(10.0d, newObj.getPosition().getY());
				assertEquals(0.0d, newObj.getPosition().getZ());
				break;

				
			case 2:
				assertEquals(25.0d, newObj.getPosition().getX());
				assertEquals(5.0d, newObj.getPosition().getY());
				assertEquals(0.0d, newObj.getPosition().getZ());
				break;

			default:
				break;
			}
			i++;
		}
	}
	
}
