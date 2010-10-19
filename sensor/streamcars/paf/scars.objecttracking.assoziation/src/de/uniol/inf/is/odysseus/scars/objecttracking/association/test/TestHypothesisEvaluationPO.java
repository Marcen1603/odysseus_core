package de.uniol.inf.is.odysseus.scars.objecttracking.association.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.algorithms.MahalanobisDistanceAssociation;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.HypothesisEvaluationPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

public class TestHypothesisEvaluationPO extends TestCase{

	@Test
	public void testPO() {
		MVRelationalTuple<StreamCarsMetaData<Object>> tupleNew = createObjectTuple();
		MVRelationalTuple<StreamCarsMetaData<Object>> tupleOld = createObjectTuple1();
		double[] paths = new double[4];
		paths[0] = 0;
		paths[1] = 1;
		paths[2] = 2;
		paths[3] = 1;
		HypothesisEvaluationPO<StreamCarsMetaData<Object>> po = new HypothesisEvaluationPO<StreamCarsMetaData<Object>>();
		po.setAssociationAlgorithm(new MahalanobisDistanceAssociation());
		assertEquals(0d, po.getAssociationAlgorithm().evaluate(tupleNew.getMetadata().getCovariance(), paths, tupleOld.getMetadata().getCovariance(), paths, 0));
	}

	private MVRelationalTuple<StreamCarsMetaData<Object>> createObjectTuple() {
		MVRelationalTuple<StreamCarsMetaData<Object>> obj = new MVRelationalTuple<StreamCarsMetaData<Object>>(2);

		MVRelationalTuple<StreamCarsMetaData<Object>> pos = new MVRelationalTuple<StreamCarsMetaData<Object>>(3);
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

		StreamCarsMetaData<Object> p = new StreamCarsMetaData<Object>();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((StreamCarsMetaData<Object>) p);

		return obj;
	}

	private MVRelationalTuple<StreamCarsMetaData<Object>> createObjectTuple1() {
		MVRelationalTuple<StreamCarsMetaData<Object>> obj = new MVRelationalTuple<StreamCarsMetaData<Object>>(2);

		MVRelationalTuple<StreamCarsMetaData<Object>> pos = new MVRelationalTuple<StreamCarsMetaData<Object>>(3);
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

		StreamCarsMetaData<Object> p = new StreamCarsMetaData<Object>();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((StreamCarsMetaData<Object>) p);

		return obj;
	}

}
