package de.uniol.inf.is.odysseus.probabilistic.test.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.ProbabilisticContinuousSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class TestSelectPO extends
		ProbabilisticContinuousSelectPO<IProbabilistic> {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestSelectPO.class);

	public TestSelectPO() {
		// super(TestSelectPO.getTestPredicate(), TestSelectPO
		// .getProbabilisticAttributePos());
		super(TestSelectPO.getSchema(), TestSelectPO.getTestPredicate());
	}

	@Override
	public void transfer(final ProbabilisticTuple<IProbabilistic> object) {
		System.out.println(object);
		Assert.assertTrue(object.getMetadata().getExistence() <= 1.0);
	}

	@Test(dataProvider = "continuousProbabilisticTuple")
	public final void testProcess(
			final ProbabilisticTuple<IProbabilistic> object) {
		object.setMetadata(new Probabilistic());
		this.process_next(object, 0);

	}

	@DataProvider(name = "continuousProbabilisticTuple")
	public final Object[][] provideDiscreteProbabilisticTuple() {
		return new Object[][] { { this.provideMultivariateTuple1() },
				{ this.provideMultivariateTuple2() },
				{ this.provideMultivariateTuple3() },
				{ this.provideMultivariateTuple4() } };
	}

	private IStreamObject<?> provideMultivariateTuple1() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
		final Object[] attrs = new Object[] {
				new ProbabilisticContinuousDouble(0),
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

	private IStreamObject<?> provideMultivariateTuple2() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
		final Object[] attrs = new Object[] {
				new ProbabilisticContinuousDouble(0),
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
				new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
		final Object[] attrs = new Object[] {
				new ProbabilisticContinuousDouble(0),
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
				new double[] { 2.0 }, new double[] { 1.5 });

		mixture1.setAttributes(new int[] { 3 });
		mixture1.setScale(1.0);
		mixture1.setSupport(new Interval[] { new Interval(-3.0, 6.0) });

		final NormalDistributionMixture mixture2 = new NormalDistributionMixture(
				new double[] { 3.0 }, new double[] { 2.5 });

		mixture2.setAttributes(new int[] { 1 });
		mixture2.setScale(1.0);
		mixture2.setSupport(new Interval[] { new Interval(-7.0, 14.0) });

		final Object[] attrs = new Object[] {
				new ProbabilisticContinuousDouble(1),
				new ProbabilisticContinuousDouble(0) };

		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture1, mixture2 },
				true);
		tuple.setMetadata(new Probabilistic());
		return tuple;
	}

	private static RelationalPredicate getTestPredicate() {
		final SDFSchema schema = TestSelectPO.getSchema();
		final DirectAttributeResolver resolver = new DirectAttributeResolver(
				TestSelectPO.getSchema());
		final SDFExpression expression = new SDFExpression("",
				"b < 3.0 && b > 4.0", resolver, MEP.getInstance());
		final RelationalPredicate predicate = new RelationalPredicate(
				expression);
		predicate.init(schema, null, false);
		return predicate;
	}

	private static SDFSchema getSchema() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a",
				SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
		attr.add(new SDFAttribute("", "b",
				SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
		final SDFSchema schema = new SDFSchema("", attr);
		return schema;
	}
}
