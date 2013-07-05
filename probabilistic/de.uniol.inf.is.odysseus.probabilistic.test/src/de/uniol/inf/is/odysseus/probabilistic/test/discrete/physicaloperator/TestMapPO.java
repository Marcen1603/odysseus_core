package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.ProbabilisticFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.ProbabilisticDiscreteMapPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class TestMapPO extends ProbabilisticDiscreteMapPO<IMetaAttribute> {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestSelectPO.class);

	public TestMapPO() {
		super(getSchema(), getTestExpression(), false, false);
	}

	@Override
	public void transfer(Tuple<IMetaAttribute> object) {
		System.out.println(object);
		Assert.assertTrue(((IProbabilistic) object.getMetadata())
				.getExistence() <= 1.0);
	}

	@Test(dataProvider = "discreteProbabilisticTuple")
	public final void testProcess(final Tuple<IMetaAttribute> object) {
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

	private static SDFExpression[] getTestExpression() {
		DirectAttributeResolver resolver = new DirectAttributeResolver(
				getSchema());
		MEP mep = MEP.getInstance();
		mep.addFunctionProvider(new ProbabilisticFunctionProvider());
		SDFExpression[] expressions = new SDFExpression[] {
				new SDFExpression("", "a * b", resolver, MEP.getInstance()),
				new SDFExpression("", "b * c", resolver, MEP.getInstance()),
				new SDFExpression("", "c * toProbabilisticDouble([1.0,0.5])", resolver, MEP.getInstance()) };
		return expressions;
	}

	// private static int[] getProbabilisticAttributePos() {
	// return SchemaUtils.getAttributePos(getSchema(), SchemaUtils
	// .getDiscreteProbabilisticAttributes(getTestPredicate()
	// .getAttributes()));
	// }

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
