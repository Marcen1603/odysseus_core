package de.uniol.inf.is.odysseus.probabilistic.test.continuous.physicaloperator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;

public class TestProjectPO extends RelationalProjectPO<IMetaAttribute> {

	public TestProjectPO() {
		super(new int[] { 1 });
	}

	@Override
	public void transfer(final Tuple<IMetaAttribute> object) {
		System.out.println("Out: " + object);
	}

	@Test(dataProvider = "tuple")
	public final void testprocess(final ProbabilisticTuple<IMetaAttribute> tuple) {
		System.out.println("In: " + tuple);
		this.process_next(tuple, 0);
	}

	@DataProvider(name = "tuple")
	public final Object[][] provideTuple() {
		return new Object[][] { { this.provideSimpleTuple() },
				{ this.provideUnivariateTuple() },
				{ this.provideMultivariateTuple1() },
				{ this.provideMultivariateTuple2() },
				{ this.provideMultivariateTuple3() },
				{ this.provideMultivariateTuple4() } };
	}

	private IStreamObject<?> provideSimpleTuple() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0 },
				new CovarianceMatrix(new double[] { 1.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0) };
		mixture.setAttributes(new int[] { 1 });
		mixture.setScale(1.0);
		mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0) });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private IStreamObject<?> provideUnivariateTuple() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0 },
				new CovarianceMatrix(new double[] { 1.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0), "ThirdAttribute" };
		mixture.setAttributes(new int[] { 1 });
		mixture.setScale(1.0);
		mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0) });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private IStreamObject<?> provideMultivariateTuple1() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new CovarianceMatrix(new double[] {
						1.5, 2.0, 2.5 }));
		final Object[] attrs = new Object[] {
				new ProbabilisticContinuousDouble(0), "FirstAttribute",
				"ThirdAttribute", new ProbabilisticContinuousDouble(0) };
		mixture.setAttributes(new int[] { 1, 3 });
		mixture.setScale(1.0);
		mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0),
				new Interval(-7.0, 14.0) });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private IStreamObject<?> provideMultivariateTuple2() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new CovarianceMatrix(new double[] {
						1.5, 2.0, 2.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0), "ThirdAttribute",
				new ProbabilisticContinuousDouble(0) };
		mixture.setAttributes(new int[] { 1, 3 });
		mixture.setScale(1.0);
		mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0),
				new Interval(-7.0, 14.0) });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private IStreamObject<?> provideMultivariateTuple3() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new CovarianceMatrix(new double[] {
						1.5, 2.0, 2.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0), "ThirdAttribute",
				new ProbabilisticContinuousDouble(0) };
		mixture.setAttributes(new int[] { 3, 1 });
		mixture.setScale(1.0);
		mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0),
				new Interval(-7.0, 14.0) });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private IStreamObject<?> provideMultivariateTuple4() {
		final NormalDistributionMixture mixture1 = new NormalDistributionMixture(
				new double[] { 2.0 },
				new CovarianceMatrix(new double[] { 1.5 }));

		mixture1.setAttributes(new int[] { 3 });
		mixture1.setScale(1.0);
		mixture1.setSupport(new Interval[] { new Interval(-3.0, 6.0) });

		final NormalDistributionMixture mixture2 = new NormalDistributionMixture(
				new double[] { 3.0 },
				new CovarianceMatrix(new double[] { 2.5 }));

		mixture2.setAttributes(new int[] { 1 });
		mixture2.setScale(1.0);
		mixture2.setSupport(new Interval[] { new Interval(-7.0, 14.0) });

		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(1), "ThirdAttribute",
				new ProbabilisticContinuousDouble(0) };

		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture1, mixture2 },
				true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private void appendDistribution(final ProbabilisticTuple<?> tuple,
			final NormalDistributionMixture mixture) {
		final int distributionIndex = tuple.getDistributions().length;
		tuple.append(new ProbabilisticContinuousDouble(distributionIndex),
				mixture, false);
	}
}
