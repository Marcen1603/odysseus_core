/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.ProbabilisticFunctionProvider;
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

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TestJoinPO extends ProbabilisticDiscreteJoinTIPO<ITimeIntervalProbabilistic, Tuple<ITimeIntervalProbabilistic>> {
	/** The logger for debug purpose. */
	private static final Logger LOG = LoggerFactory.getLogger(TestJoinPO.class);

	/**
	 * Test constructor of the Join PO.
	 */
	@SuppressWarnings("unchecked")
	public TestJoinPO() {
		super();

		this.setMetadataMerge(TestJoinPO.getTestMetadataMerge());
		this.setTransferFunction(TestJoinPO.getTestTransferFunction());
		this.setJoinPredicate(TestJoinPO.getTestPredicate());
		this.setAreas(TestJoinPO.getTestAreas());
		this.setDataMerge(TestJoinPO.getTestDataMerge());

	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#transfer(java.lang.Object)
	 */
	@Override
	public final void transfer(final Tuple<ITimeIntervalProbabilistic> object) {
		TestJoinPO.LOG.debug(object.toString());
		Assert.assertTrue(((IProbabilistic) object.getMetadata()).getExistence() <= 1.0);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO#isDone()
	 */
	@Override
	protected final boolean isDone() {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#isOpen()
	 */
	@Override
	public final boolean isOpen() {
		return true;
	}

	/**
	 * Test the process method of the JoinPO.
	 * 
	 * @param left
	 *            The tuple from the left stream
	 * @param right
	 *            The tuple from the right stream
	 */
	@Test(dataProvider = "discreteProbabilisticTuple")
	public final void testProcess(final ProbabilisticTuple<ITimeIntervalProbabilistic> left, final ProbabilisticTuple<ITimeIntervalProbabilistic> right) {
		// object.getMetadata().setExistence(1.0);
		final TimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
		final PointInTime start = new PointInTime(1L);
		final PointInTime end = new PointInTime(3L);
		metadata.setStartAndEnd(start, end);
		metadata.setExistence(1.0);
		left.setMetadata(metadata.clone());
		right.setMetadata(metadata.clone());
		this.process_next(left, 0);
		this.process_next(right, 1);
	}

	/**
	 * Data provider for prob. tuples.
	 * 
	 * @return An array of prob. tuples.
	 */
	@DataProvider(name = "discreteProbabilisticTuple")
	public final Object[][] provideDiscreteProbabilisticTuple() {
		return new Object[][] {
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 0.0 }, new Double[] { 1.0 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 0.0 }, new Double[] { 1.0 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "RIGHT" }, true)

				},
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "RIGHT" }, true)

				},
				{
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0, 5.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "LEFT" }, true),
						new ProbabilisticTuple<IProbabilistic>(new Object[] { new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0, 5.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }), new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[] { 0.25, 0.25, 0.5 }),
								new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[] { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new Double[] { 0.25, 0.25, 0.5 }), "RIGHT" }, true)

				} };
	}

	/**
	 * 
	 * @return The predicate used for testing
	 */
	private static RelationalPredicate getTestPredicate() {
		MEP.getInstance().addFunctionProvider(new ProbabilisticFunctionProvider());
		final SDFSchema schema = SDFSchema.union(TestJoinPO.getSchema1(), TestJoinPO.getSchema2());
		final DirectAttributeResolver resolver = new DirectAttributeResolver(schema);
		final SDFExpression expression = new SDFExpression("", "a < 3.0 && b > 4.0 && g < 9.0", resolver, MEP.getInstance());
		final RelationalPredicate predicate = new RelationalPredicate(expression);
		predicate.init(TestJoinPO.getSchema1(), TestJoinPO.getSchema2(), true);
		return predicate;
	}

	/**
	 * 
	 * @return The schema of the left input stream for testing
	 */
	private static SDFSchema getSchema1() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "d", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "name", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		final SDFSchema schema = new SDFSchema("", ProbabilisticTuple.class, attr);
		return schema;
	}

	/**
	 * 
	 * @return The schema of the right input stream for testing
	 */
	private static SDFSchema getSchema2() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "e", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "f", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "g", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "h", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "name", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		final SDFSchema schema = new SDFSchema("", ProbabilisticTuple.class, attr);
		return schema;
	}

	/**
	 * 
	 * @return The sweep area
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ProbabilisticDiscreteJoinTISweepArea[] getTestAreas() {
		final ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>[] areas = new ProbabilisticDiscreteJoinTISweepArea[2];

		final Collection<SDFAttribute> attributes = SchemaUtils.getDiscreteProbabilisticAttributes(TestJoinPO.getTestPredicate().getAttributes());
		final SDFSchema leftSchema = TestJoinPO.getSchema1();
		final SDFSchema rightSchema = TestJoinPO.getSchema2();

		final List<SDFAttribute> leftAttributes = new ArrayList<SDFAttribute>(leftSchema.getAttributes());
		leftAttributes.retainAll(attributes);

		final List<SDFAttribute> rightAttributes = new ArrayList<SDFAttribute>(rightSchema.getAttributes());
		rightAttributes.retainAll(attributes);
		rightAttributes.removeAll(leftAttributes);

		final int[] rightProbabilisticAttributePos = SchemaUtils.getAttributePos(rightSchema, rightAttributes);
		final int[] leftProbabilisticAttributePos = SchemaUtils.getAttributePos(leftSchema, leftAttributes);

		areas[0] = new ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>(rightProbabilisticAttributePos, leftProbabilisticAttributePos, TestJoinPO.getTestDataMerge(), TestJoinPO.getTestMetadataMerge());
		areas[1] = new ProbabilisticDiscreteJoinTISweepArea<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>(leftProbabilisticAttributePos, rightProbabilisticAttributePos, TestJoinPO.getTestDataMerge(), TestJoinPO.getTestMetadataMerge());
		return areas;
	}

	/**
	 * 
	 * @return The merge function for meta data
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static CombinedMergeFunction getTestMetadataMerge() {
		final CombinedMergeFunction metadataMerge = new CombinedMergeFunction();
		metadataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
		return metadataMerge;
	}

	/**
	 * 
	 * @return The merge function for payload data
	 */
	public static IDataMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic> getTestDataMerge() {
		return new ProbabilisticMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic>(2 * TestJoinPO.getSchema1().size());
	}

	/**
	 * 
	 * @return The transfer function for join results
	 */
	@SuppressWarnings("rawtypes")
	public static TITransferArea getTestTransferFunction() {
		return new TITransferArea<Tuple<ITimeIntervalProbabilistic>, Tuple<ITimeIntervalProbabilistic>>() {
			@Override
			public void transfer(final Tuple<ITimeIntervalProbabilistic> object) {
				System.out.println(object);
				Assert.assertTrue(((IProbabilistic) object.getMetadata()).getExistence() <= 1.0);
				Assert.assertEquals(object.getAttribute(4), "LEFT");
				Assert.assertEquals(object.getAttribute(9), "RIGHT");
			}
		};
	}
}
