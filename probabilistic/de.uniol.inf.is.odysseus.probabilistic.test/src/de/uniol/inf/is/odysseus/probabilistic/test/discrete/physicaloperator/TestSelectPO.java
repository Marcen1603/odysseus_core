package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

@Test(singleThreaded = true)
public class TestSelectPO {

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "discreteProbabilisticTuple")
	public final void testUnnest(final ProbabilisticTuple<?> object) {
		RelationalPredicate predicate = getPredicate();
		SDFSchema schema = getSchema();

		ProbabilisticTuple<?> outputVal = object.clone();

		ProbabilisticTuple<?> selectObject = object.clone();

		List<SDFAttribute> attributes = predicate.getAttributes();

		int[] probabilisticAttributePos = SchemaUtils.getAttributePos(schema,
				SchemaUtils.getDiscreteProbabilisticAttributes(attributes));

		Iterator<?>[] attributeIters = new Iterator<?>[probabilisticAttributePos.length];
		int worldNum = 1;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			((AbstractProbabilisticValue<?>) outputVal
					.getAttribute(probabilisticAttributePos[i])).getValues()
					.clear();
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) object
					.getAttribute(probabilisticAttributePos[i]);
			worldNum *= attribute.getValues().size();
			attributeIters[i] = attribute.getValues().entrySet().iterator();
		}
		System.out.println("init: ");
		System.out.println("Input: " + object);
		System.out.println("Eval: " + selectObject);

		Object[][] worlds = new Object[worldNum][probabilisticAttributePos.length];
		double instances = 0;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) object
					.getAttribute(probabilisticAttributePos[i]);
			int world = 0;
			if (instances == 0.0) {
				instances = 1.0;
			} else {
				instances *= attribute.getValues().size();
			}
			int num = (int) (worlds.length / (attribute.getValues().size() * instances));
			while (num > 0) {
				Iterator<?> iter = attribute.getValues().entrySet().iterator();
				while (iter.hasNext()) {
					Entry<?, Double> entry = ((Map.Entry<?, Double>) iter
							.next());
					for (int j = 0; j < instances; j++) {
						if (world == worlds.length) {
							System.out.println("ERROR");
						}
						worlds[world][i] = entry.getKey();
						world++;
					}
				}
				num--;
			}
		}

		for (int w = 0; w < worlds.length; w++) {
			for (int i = 0; i < probabilisticAttributePos.length; i++) {
				selectObject.setAttribute(probabilisticAttributePos[i],
						worlds[w][i]);
			}
			boolean result = predicate.evaluate(selectObject);

			if (result) {
				for (int a = 0; a < probabilisticAttributePos.length; a++) {
					AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) object
							.getAttribute(probabilisticAttributePos[a]);
					AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal
							.getAttribute(probabilisticAttributePos[a]);

					outAttribute.getValues().put((Double) worlds[w][a],
							inAttribute.getValues().get(worlds[w][a]));

				}
			}
		}
		System.out.println("Transfer-> " + outputVal);
	}

	@DataProvider(name = "discreteProbabilisticTuple")
	public final Object[][] provideDiscreteDivisionDoubleValues() {
		return new Object[][] {
				{ new ProbabilisticTuple(new Object[] {
						new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 0.0 },
								new Double[] { 1.0 }) }, true)

				},
				{ new ProbabilisticTuple(new Object[] {
						new ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 },
								new Double[] { 0.25, 0.25, 0.5 }),
						new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 },
								new Double[] { 0.25, 0.25, 0.5 }) }, true)

				} };
	}

	@DataProvider(name = "discreteSmallerThanDouble")
	public final Object[][] provideDiscreteSmallerThanDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.5625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.125 } };
	}

	@DataProvider(name = "discreteSmallerEqualsDouble")
	public final Object[][] provideDiscreteSmallerEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.6875 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.375 } };
	}

	@DataProvider(name = "discreteEqualsDouble")
	public final Object[][] provideDiscreteEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.25 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.125 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.25 } };
	}

	@DataProvider(name = "discreteGreaterEqualsDouble")
	public final Object[][] provideDiscreteGreaterEqualsDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.625 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.4375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.875 } };
	}

	@DataProvider(name = "discreteGreaterThanDouble")
	public final Object[][] provideDiscreteGreaterThanDoubleValues() {
		return new Object[][] {
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 7.0, 5.0, 3.0,
								1.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.375 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 9.0,
								11.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						0.3125 },
				{
						new ProbabilisticDouble(new Double[] { 1.0, 3.0, 5.0,
								7.0 }, new Double[] { 0.25, 0.25, 0.25, 0.25 }),
						new ProbabilisticDouble(new Double[] { 1.0, 3.0 },
								new Double[] { 0.5, 0.5 }), 0.625 } };
	}

	private RelationalPredicate getPredicate() {
		SDFSchema schema = getSchema();
		DirectAttributeResolver resolver = new DirectAttributeResolver(
				getSchema());
		SDFExpression expression = new SDFExpression("",
				"a < 3.0 && b > 4.0 && c < 9.0", resolver, MEP.getInstance());
		RelationalPredicate predicate = new RelationalPredicate(expression);
		predicate.init(schema, null, false);
		return predicate;
	}

	private SDFSchema getSchema() {
		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "b",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		attr.add(new SDFAttribute("", "c",
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE));
		SDFSchema schema = new SDFSchema("", attr);
		return schema;
	}
}
