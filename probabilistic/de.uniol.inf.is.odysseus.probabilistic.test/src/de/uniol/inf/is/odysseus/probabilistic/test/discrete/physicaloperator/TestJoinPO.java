package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteJoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteJoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class TestJoinPO
		extends
		ProbabilisticDiscreteJoinTIPO<ITimeIntervalProbabilistic, Tuple<ITimeIntervalProbabilistic>> {

	public TestJoinPO() {
		super();

		this.setMetadataMerge(TestJoinPO.getTestMetadataMerge());
		this.setTransferFunction(TestJoinPO.getTestTransferFunction());
		this.setJoinPredicate(TestJoinPO.getTestPredicate());
		this.setAreas(TestJoinPO.getTestAreas());
		this.setDataMerge(TestJoinPO.getTestDataMerge());

	}

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	/**
 * 
 */
	@BeforeTest
	public void setUpAll() {

	}

	/**
 * 
 */
	@AfterTest
	public void tearDownAll() {

	}

	/**
 * 
 */
	@AfterMethod
	public void tearDown() {

	}

	@Override
	public void transfer(final Tuple<ITimeIntervalProbabilistic> object) {
		System.out.println(object);
		Assert.assertTrue(((IProbabilistic) object.getMetadata())
				.getExistence() <= 1.0);
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO#isDone()
	 */
	@Override
	protected boolean isDone() {
		return false;
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Test(dataProvider = "discreteProbabilisticTuple")
	public final void testProcess(
			final ProbabilisticTuple<ITimeIntervalProbabilistic> left,
			final ProbabilisticTuple<ITimeIntervalProbabilistic> right) {
		// object.getMetadata().setExistence(1.0);
		final TimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
		final PointInTime start = new PointInTime(1l);
		final PointInTime end = new PointInTime(3l);
		metadata.setStartAndEnd(start, end);
		metadata.setExistence(1.0);
		left.setMetadata(metadata.clone());
		right.setMetadata(metadata.clone());
		this.process_next(left, 0);
		this.process_next(right, 1);
	}

	@DataProvider(name = "discreteProbabilisticTuple")
	public final Object[][] provideDiscreteProbabilisticTuple() {
		return new Object[][] {
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
								new ProbabilisticDouble(new Double[] { 1.0,
										2.0, 3.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 4.0,
										5.0, 6.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 0.0 },
										new Double[] { 1.0 }),
								new ProbabilisticDouble(new Double[] { 10.0,
										11.0, 12.0 }, new Double[] { 0.25,
										0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
								new ProbabilisticDouble(new Double[] { 1.0,
										2.0, 3.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 4.0,
										5.0, 6.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 0.0 },
										new Double[] { 1.0 }),
								new ProbabilisticDouble(new Double[] { 10.0,
										11.0, 12.0 }, new Double[] { 0.25,
										0.25, 0.5 }), "RIGHT" }, true)

				},
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
								new ProbabilisticDouble(new Double[] { 1.0,
										2.0, 3.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 4.0,
										5.0, 6.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0,
										8.0, 9.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 10.0,
										11.0, 12.0 }, new Double[] { 0.25,
										0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
								new ProbabilisticDouble(new Double[] { 1.0,
										2.0, 3.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 4.0,
										5.0, 6.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0,
										8.0, 9.0 }, new Double[] { 0.25, 0.25,
										0.5 }),
								new ProbabilisticDouble(new Double[] { 10.0,
										11.0, 12.0 }, new Double[] { 0.25,
										0.25, 0.5 }), "RIGHT" }, true)

				},
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
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
										0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] {
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
										0.25, 0.5 }), "RIGHT" }, true)

				} };
	}

	private static RelationalPredicate getTestPredicate() {
		final SDFSchema schema = SDFSchema.union(TestJoinPO.getSchema1(),
				TestJoinPO.getSchema2());
		final DirectAttributeResolver resolver = new DirectAttributeResolver(
				schema);
		final SDFExpression expression = new SDFExpression("",
				"a < 3.0 && b > 4.0 && g < 9.0", resolver, MEP.getInstance());
		final RelationalPredicate predicate = new RelationalPredicate(
				expression);
		predicate.init(TestJoinPO.getSchema1(), TestJoinPO.getSchema2(), true);
		return predicate;
	}

	private static int[] getProbabilisticAttributePos() {
		return SchemaUtils.getAttributePos(TestJoinPO.getSchema1(), SchemaUtils
				.getDiscreteProbabilisticAttributes(TestJoinPO
						.getTestPredicate().getAttributes()));
	}

	private static SDFSchema getSchema1() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "b",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "c",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "d",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "name",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		final SDFSchema schema = new SDFSchema("", attr);
		return schema;
	}

	private static SDFSchema getSchema2() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "e",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "f",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "g",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "h",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "name",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		final SDFSchema schema = new SDFSchema("", attr);
		return schema;
	}

	private static ProbabilisticDiscreteJoinTISweepArea[] getTestAreas() {
		final ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>[] areas = new ProbabilisticDiscreteJoinTISweepArea[2];

		final List<SDFAttribute> attributes = SchemaUtils
				.getDiscreteProbabilisticAttributes(TestJoinPO
						.getTestPredicate().getAttributes());
		final SDFSchema leftSchema = TestJoinPO.getSchema1();
		final SDFSchema rightSchema = TestJoinPO.getSchema2();

		final List<SDFAttribute> leftAttributes = new ArrayList<SDFAttribute>(
				leftSchema.getAttributes());
		leftAttributes.retainAll(attributes);

		final List<SDFAttribute> rightAttributes = new ArrayList<SDFAttribute>(
				rightSchema.getAttributes());
		rightAttributes.retainAll(attributes);
		rightAttributes.removeAll(leftAttributes);

		final int[] rightProbabilisticAttributePos = SchemaUtils
				.getAttributePos(rightSchema, rightAttributes);
		final int[] leftProbabilisticAttributePos = SchemaUtils
				.getAttributePos(leftSchema, leftAttributes);

		areas[0] = new ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>(
				rightProbabilisticAttributePos, leftProbabilisticAttributePos,
				TestJoinPO.getTestDataMerge(),
				TestJoinPO.getTestMetadataMerge());
		areas[1] = new ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>(
				leftProbabilisticAttributePos, rightProbabilisticAttributePos,
				TestJoinPO.getTestDataMerge(),
				TestJoinPO.getTestMetadataMerge());
		return areas;
	}

	public static CombinedMergeFunction getTestMetadataMerge() {
		final CombinedMergeFunction metadataMerge = new CombinedMergeFunction();
		metadataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
		return metadataMerge;
	}

	public static IDataMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic> getTestDataMerge() {
		return new ProbabilisticMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic>(
				2 * TestJoinPO.getSchema1().size());
	}

	public static TITransferArea getTestTransferFunction() {
		return new TITransferArea<Tuple<ITimeIntervalProbabilistic>, Tuple<ITimeIntervalProbabilistic>>() {
			@Override
			public void transfer(final Tuple<ITimeIntervalProbabilistic> object) {
				System.out.println(object);
				Assert.assertTrue(((IProbabilistic) object.getMetadata())
						.getExistence() <= 1.0);
				Assert.assertEquals(object.getAttribute(4), "LEFT");
				Assert.assertEquals(object.getAttribute(9), "RIGHT");
			}
		};
	}
}
