package de.uniol.inf.is.odysseus.probabilistic.test;

import org.testng.annotations.Test;
import org.testng.asserts.*;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

public class TestProbabilisticTuple {


	@Test
	public void f() {
		final NormalDistributionMixture mixture1 = new NormalDistributionMixture(
				new double[] { 1.0, 2.0 }, new CovarianceMatrix(new double[] {
						1.0, 0.0, 1.0 }));
		mixture1.setAttributes(new int[] { 0, 4 });
		final NormalDistributionMixture mixture2 = new NormalDistributionMixture(
				new double[] { 3.0 },
				new CovarianceMatrix(new double[] { 2.0 }));
		mixture2.setAttributes(new int[] { 2 });

		final ProbabilisticContinuousDouble probAttr11 = new ProbabilisticContinuousDouble(
				0);
		final ProbabilisticContinuousDouble probAttr12 = new ProbabilisticContinuousDouble(
				0);
		final ProbabilisticContinuousDouble probAttr21 = new ProbabilisticContinuousDouble(
				1);

		final Object[] attrs = new Object[] { probAttr21, 1.0, probAttr11,
				"String", probAttr12 };

		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture1, mixture2 },
				true);
		tuple.setDistributions(new NormalDistributionMixture[] { mixture1,
				mixture2 });
		System.out.println(tuple);
		for (NormalDistributionMixture dist : tuple.getDistributions()) {
			System.out.println(dist);
		}
		final ProbabilisticTuple<IMetaAttribute> newTuple = tuple.restrict(
				new int[] { 0, 1, 2 }, true);
		System.out.println(tuple);
		for (NormalDistributionMixture dist : tuple.getDistributions()) {
			System.out.println(dist);
		}
		System.out.println(newTuple);

		for (NormalDistributionMixture dist : newTuple.getDistributions()) {
			System.out.println(dist);
		}

		// ProbabilisticTuple<IMetaAttribute> mergeTuple =
		// (ProbabilisticTuple<IMetaAttribute>) tuple.process_merge(tuple,
		// newTuple, Order.LeftRight);
		//
		// System.out.println(mergeTuple);
		// for (NormalDistributionMixture dist : mergeTuple.getDistributions())
		// {
		// System.out.println(dist);
		// }
	}
}
