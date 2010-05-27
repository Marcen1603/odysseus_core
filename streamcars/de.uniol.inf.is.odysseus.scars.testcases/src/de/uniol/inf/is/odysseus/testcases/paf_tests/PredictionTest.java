package de.uniol.inf.is.odysseus.testcases.paf_tests;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;
import de.uniol.inf.is.odysseus.prediction.DefaultPredictionAlgorithmus;
import de.uniol.inf.is.odysseus.prediction.TestObject;

public class PredictionTest extends TestCase {
	
	public void testPredAlgo() {
		TestObject obj = new TestObject(5, new Vector3d(10, 20, 0), new Vector3d(1, 0, 0), 5);
		DefaultPredictionAlgorithmus prdAlgo = new DefaultPredictionAlgorithmus(obj, 10);
		TestObject newObj = prdAlgo.executePredAlgorithmus();
		
		assertEquals(newObj.getSpeed(), obj.getSpeed());
		assertEquals(newObj.getDirection(), obj.getDirection());
		assertEquals(newObj.getTimeStamp(), obj.getTimeStamp());
		
		assertEquals(35.0d, newObj.getPosition().x);
		assertEquals(20.0d, newObj.getPosition().y);
		assertEquals(0.0d, newObj.getPosition().z);
		
	}
	
}
