package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.ProbabilisticDiscreteSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

//@Test(singleThreaded = true)
public class TestSelectPO extends
		ProbabilisticDiscreteSelectPO<ProbabilisticTuple<IProbabilistic>> {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestSelectPO.class);

	public TestSelectPO() {
		super(getTestPredicate(), getProbabilisticAttributePos());
	}

	@Override
	public void transfer(ProbabilisticTuple<IProbabilistic> object) {
		System.out.println(object);
		Assert.assertTrue(object.getMetadata().getExistence() <= 1.0);
	}

	@Test(dataProvider = "discreteProbabilisticTuple")
	public final void testProcess(
			final ProbabilisticTuple<IProbabilistic> object) {
		object.setMetadata(new Probabilistic());
		process_next(object, 0);

	}

	@DataProvider(name = "discreteProbabilisticTuple")
	public final Object[][] provideDiscreteProbabilisticTuple() {
		return new Object[][] {
				{ new ProbabilisticTuple<IProbabilistic>(new Object[] {
						new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 0.0 },
								new Double[] { 1.0 }),
						new ProbabilisticDouble(
								new Double[] { 10.0, 11.0, 12.0 },
								new Double[] { 0.25, 0.25, 0.5 }) }, true)

				},
				{ new ProbabilisticTuple<IProbabilistic>(new Object[] {
						new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(
								new Double[] { 10.0, 11.0, 12.0 },
								new Double[] { 0.25, 0.25, 0.5 }) }, true)

				},
				{ new ProbabilisticTuple<IProbabilistic>(
						new Object[] {
								new ProbabilisticDouble(new Double[] { 1.0,
										2.0, 3.0, 5.0 }, new Double[] { 0.25,
										0.25, 0.25, 0.25 }),
								new ProbabilisticDouble(new Double[] { 4.0,
										5.0, 6.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0,
										8.0, 9.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 10.0,
										11.0, 12.0 }, new Double[] { 0.25,
										0.25, 0.5 }) }, true)

				} };
	}

	// @DataProvider(name = "discreteSmallerThanDouble")
	// public final Object[][] provideDiscreteSmallerThanDoubleValues() {
	// return new Object[][] {
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.375 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
	// 1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.375 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
	// 11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.5625 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
	// new Double[] { 0.5, 0.5 }), 0.125 } };
	// }
	//
	// @DataProvider(name = "discreteSmallerEqualsDouble")
	// public final Object[][] provideDiscreteSmallerEqualsDoubleValues() {
	// return new Object[][] {
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.625 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
	// 1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.625 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
	// 11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.6875 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
	// new Double[] { 0.5, 0.5 }), 0.375 } };
	// }
	//
	// @DataProvider(name = "discreteEqualsDouble")
	// public final Object[][] provideDiscreteEqualsDoubleValues() {
	// return new Object[][] {
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.25 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
	// 1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.25 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
	// 11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.125 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
	// new Double[] { 0.5, 0.5 }), 0.25 } };
	// }
	//
	// @DataProvider(name = "discreteGreaterEqualsDouble")
	// public final Object[][] provideDiscreteGreaterEqualsDoubleValues() {
	// return new Object[][] {
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.625 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
	// 1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.625 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
	// 11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.4375 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
	// new Double[] { 0.5, 0.5 }), 0.875 } };
	// }
	//
	// @DataProvider(name = "discreteGreaterThanDouble")
	// public final Object[][] provideDiscreteGreaterThanDoubleValues() {
	// return new Object[][] {
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.375 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
	// 1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.375 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
	// 11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// 0.3125 },
	// {
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
	// 7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
	// new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
	// new Double[] { 0.5, 0.5 }), 0.625 } };
	// }

	private static RelationalPredicate getTestPredicate() {
		SDFSchema schema = getSchema();
		DirectAttributeResolver resolver = new DirectAttributeResolver(
				getSchema());
		SDFExpression expression = new SDFExpression("",
				"a < 3.0 && b > 4.0 && c < 9.0", resolver, MEP.getInstance());
		RelationalPredicate predicate = new RelationalPredicate(expression);
		predicate.init(schema, null, false);
		return predicate;
	}

	private static int[] getProbabilisticAttributePos() {
		return SchemaUtils.getAttributePos(getSchema(), SchemaUtils
				.getDiscreteProbabilisticAttributes(getTestPredicate()
						.getAttributes()));
	}

	private static SDFSchema getSchema() {
		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "b",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "c",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "d",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		SDFSchema schema = new SDFSchema("", attr);
		return schema;
	}
}
