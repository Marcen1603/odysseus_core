package de.uniol.inf.is.odysseus.testcases.paf_tests;

import junit.framework.TestCase;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.scars.objecttracking.MeasurementObject;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.DefaultPredictionAlgorithmus;

public class PredictionTest extends TestCase {
	
	public void testPredAlgo() {
		MeasurementObject obj = new MeasurementObject(5, new Vector3D(10, 20, 0), new Vector3D(1, 0, 0), 5);
//		DefaultPredictionAlgorithmus prdAlgo = new DefaultPredictionAlgorithmus(obj, 10);
//		MeasurementObject newObj = prdAlgo.executePredAlgorithmus();
		
//		assertEquals(newObj.getSpeed(), obj.getSpeed());
//		assertEquals(newObj.getDirection(), obj.getDirection());
//		assertEquals(newObj.getTimeStamp(), obj.getTimeStamp());
//		
//		assertEquals(35.0d, newObj.getPosition().getX());
//		assertEquals(20.0d, newObj.getPosition().getY());
//		assertEquals(0.0d, newObj.getPosition().getZ());
		
	}
	
}
