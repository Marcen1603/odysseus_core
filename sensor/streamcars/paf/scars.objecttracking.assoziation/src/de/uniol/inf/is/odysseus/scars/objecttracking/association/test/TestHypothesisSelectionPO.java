package de.uniol.inf.is.odysseus.scars.objecttracking.association.test;

import junit.framework.TestCase;

import org.junit.Test;

import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.HypothesisSelectionPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

public class TestHypothesisSelectionPO extends TestCase{

	@Test
	public void testPO() {
		HypothesisSelectionPO<StreamCarsMetaData<Object>> po = new HypothesisSelectionPO<StreamCarsMetaData<Object>>();

		double[][] evalMatrix = new double[3][3];
		evalMatrix[0][0] = 0;
		evalMatrix[0][1] = 10;
		evalMatrix[0][2] = 20;

		evalMatrix[1][0] = 30;
		evalMatrix[1][1] = 40;
		evalMatrix[1][2] = 50;

		evalMatrix[2][0] = 60;
		evalMatrix[2][1] = 70;
		evalMatrix[2][2] = 70;

//		evalMatrix = po.singleMatchingEvaluation(evalMatrix);

		double[][] resMatrix = new double[3][3];
		resMatrix[0][0] = 0;
		resMatrix[0][1] = 0;
		resMatrix[0][2] = 0;

		resMatrix[1][0] = 0;
		resMatrix[1][1] = 0;
		resMatrix[1][2] = 50;

		resMatrix[2][0] = 0;
		resMatrix[2][1] = 70;
		resMatrix[2][2] = 0;

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				assertEquals(evalMatrix[i][j], resMatrix[i][j]);
			}
		}
	}
}
