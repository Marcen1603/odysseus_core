package de.uniol.inf.is.odysseus.assoziation.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import de.uniol.inf.is.odysseus.assoziation.physicaloperator.MahalanobisDistanceEvaluationPO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

public class TestHypothesisEvaluationPO extends TestCase{

	@Test
	public void testPO() {
		MVRelationalTuple<StreamCarsMetaData> tupleNew = createObjectTuple();
		MVRelationalTuple<StreamCarsMetaData> tupleOld = createObjectTuple1();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		paths.add(new int[] {0, 0});
		paths.add(new int[] {0, 1});
		paths.add(new int[] {0, 2});
		paths.add(new int[] {1});
		MahalanobisDistanceEvaluationPO<StreamCarsMetaData> po = new MahalanobisDistanceEvaluationPO<StreamCarsMetaData>();
		assertEquals(0d, po.evaluate(tupleNew, tupleOld, paths, paths));
	}
	
	private MVRelationalTuple<StreamCarsMetaData> createObjectTuple() {
		MVRelationalTuple<StreamCarsMetaData> obj = new MVRelationalTuple<StreamCarsMetaData>(2);
		
		MVRelationalTuple<StreamCarsMetaData> pos = new MVRelationalTuple<StreamCarsMetaData>(3);
		pos.setAttribute(0, 111.0);
		pos.setAttribute(1, 111.0);
		pos.setAttribute(2, 111.0);
		
		obj.setAttribute(0, pos);
		obj.setAttribute(1, 45.0);
		
		
		double[][] cov = new double[4][4];
		cov[0][0] = 110.5;
		cov[0][1] = 110.3;
		cov[0][2] = 110.2;
		cov[0][3] = 110.9;
		
		cov[1][0] = 110.1;
		cov[1][1] = 110.6;
		cov[1][2] = 110.7;
		cov[1][3] = 110.8;
		
		cov[2][0] = 110.1;
		cov[2][1] = 110.3;
		cov[2][2] = 110.5;
		cov[2][3] = 110.9;
		
		cov[3][0] = 110.9;
		cov[3][1] = 110.6;
		cov[3][2] = 110.3;
		cov[3][3] = 110.4;
		
		ArrayList<int[]> paths = new ArrayList<int[]>();
		paths.add(new int[] {0, 0});
		paths.add(new int[] {0, 1});
		paths.add(new int[] {0, 2});
		paths.add(new int[] {1});
		
		StreamCarsMetaData p = new StreamCarsMetaData();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((StreamCarsMetaData) p);
		
		return obj;
	}
	
	private MVRelationalTuple<StreamCarsMetaData> createObjectTuple1() {
		MVRelationalTuple<StreamCarsMetaData> obj = new MVRelationalTuple<StreamCarsMetaData>(2);
		
		MVRelationalTuple<StreamCarsMetaData> pos = new MVRelationalTuple<StreamCarsMetaData>(3);
		pos.setAttribute(0, 1.0);
		pos.setAttribute(1, 0.5);
		pos.setAttribute(2, 0.0);
		
		obj.setAttribute(0, pos);
		obj.setAttribute(1, 45.0);
		
		
		double[][] cov = new double[4][4];
		cov[0][0] = 0.4;
		cov[0][1] = 0.4;
		cov[0][2] = 0.4;
		cov[0][3] = 0.4;
		
		cov[1][0] = 0.3;
		cov[1][1] = 0.3;
		cov[1][2] = 0.3;
		cov[1][3] = 0.3;
		
		cov[2][0] = 0.2;
		cov[2][1] = 0.2;
		cov[2][2] = 0.2;
		cov[2][3] = 0.2;
		
		cov[3][0] = 0.1;
		cov[3][1] = 0.1;
		cov[3][2] = 0.1;
		cov[3][3] = 0.1;
		
		ArrayList<int[]> paths = new ArrayList<int[]>();
		paths.add(new int[] {0, 0});
		paths.add(new int[] {0, 1});
		paths.add(new int[] {0, 2});
		paths.add(new int[] {1});
		
		StreamCarsMetaData p = new StreamCarsMetaData();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((StreamCarsMetaData) p);
		
		return obj;
	}
	
}
