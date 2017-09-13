/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.TestPredictionAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TTestPredictionAORule extends
		AbstractTransformationRule<TestPredictionAO> {

	private static final String SQUARE_ERROR_ATTRIBUTE_NAME = "square_error";
	// private static final String SUM_OF_SQUARE_ERROR_ATTRIBUTE_NAME =
	// "sum_of_square_error";
	private static final String COUNT_OF_SQUARE_ERROR_ATTRIBUTE_NAME = "no_of_test_tuples";
	public static final String ERROR_ATTRIBUTE_NAME = "error";
	private static final String MEAN_OF_SQUARE_ERROR_ATTRIBUTE_NAME = "mean_of_square_error";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final TestPredictionAO operator,
			final TransformationConfiguration config) throws RuleException {

		// final Map<TestPredictionTupleSchema, Integer> map = new
		// HashMap<TestPredictionTupleSchema, Integer>();
		// map.put(TestPredictionTupleSchema.ITEM,
		// FindAttributeHelper.findAttributeIndex(operator,
		// operator.getItemAttribute(), 0));
		// map.put(TestPredictionTupleSchema.USER,
		// FindAttributeHelper.findAttributeIndex(operator,
		// operator.getUserAttribute(), 0));
		// map.put(TestPredictionTupleSchema.RATING,
		// FindAttributeHelper.findAttributeIndex(operator,
		// operator.getRatingAttribute(), 0));
		// map.put(TestPredictionTupleSchema.PREDICTED_RATING,
		// FindAttributeHelper.findAttributeIndex(operator,
		// operator.getPredictedRatingAttribute(), 0));
		// final TupleSchemaHelper<ITimeInterval, TestPredictionTupleSchema> tsh
		// = new TupleSchemaHelper<>(
		// map);
		//
		// defaultExecute(operator, new TestPredictionSquareErrorPO<>(tsh,
		// operator.getOutputTestTuple()), config, true, false);

		if ("RMSE".equals(operator.getMetric())) {
			final String ratingAttrName = operator.getRatingAttribute()
					.getAttributeName();
			final String predictedRatingAttrName = operator
					.getPredictedRatingAttribute().getAttributeName();

			final String[] squareErrorMapFunction = { "(" + ratingAttrName
					+ " - " + predictedRatingAttrName + ")^2" };
			final MapAO squareErrorMapAO = insertMapAO("Square Error MAP",
					new String[] { SQUARE_ERROR_ATTRIBUTE_NAME },
					squareErrorMapFunction, operator);

			final AbstractWindowWithWidthAO windowAo = insertWindowsAO(
					operator.getAggregationWindowSize(), squareErrorMapAO);

			final AggregateAO aggregateAO = insertAggregateAO(
					"Aggregate Square Error", windowAo);

			final String[] rmseMapFunction = {
			// "sqrt(" + SUM_OF_SQUARE_ERROR_ATTRIBUTE_NAME + "/"
			// + COUNT_OF_SQUARE_ERROR_ATTRIBUTE_NAME + ")",
			"sqrt(" + MEAN_OF_SQUARE_ERROR_ATTRIBUTE_NAME + ")"
			// , COUNT_OF_SQUARE_ERROR_ATTRIBUTE_NAME
			};
			insertMapAO("RMSE MAP", new String[] { ERROR_ATTRIBUTE_NAME
			// , ERROR_ATTRIBUTE_NAME + "_2"
			// ,COUNT_OF_SQUARE_ERROR_ATTRIBUTE_NAME
					}, rmseMapFunction, aggregateAO);

			LogicalPlan.removeOperator(operator, false);
			retract(operator);
		} else {
			throw new IllegalArgumentException("Metric " + operator.getMetric()
					+ " is unknown.");
		}
	}

	/**
	 * @param timeValueItem
	 * @param squareErrorMapAO
	 * @return
	 */
	private AbstractWindowWithWidthAO insertWindowsAO(
			final TimeValueItem timeValueItem,
			final UnaryLogicalOp operatorBefore) {
		AbstractWindowWithWidthAO windowAO;
		if (timeValueItem == null) {
			windowAO = new WindowAO(WindowType.UNBOUNDED);
			windowAO.setName("Unbounded Window");
		} else {
			windowAO = new TimeWindowAO();
			windowAO.setWindowSize(timeValueItem);
		}

		LogicalPlan.insertOperatorBefore(windowAO, operatorBefore);
		insert(windowAO);
		return windowAO;
	}

	/**
	 * @param operatorName
	 * @param squareErrorMapAO
	 */
	private AggregateAO insertAggregateAO(final String operatorName,
			final UnaryLogicalOp operatorBefore) {
		final AggregateAO aggregateAO = new AggregateAO();
		aggregateAO.setName(operatorName);

		LogicalPlan.insertOperatorBefore(aggregateAO, operatorBefore);

		final SDFAttribute modelErrorAttribute = FindAttributeHelper
				.findAttributeByName(aggregateAO, SQUARE_ERROR_ATTRIBUTE_NAME);

		// final AggregateFunction sum = new AggregateFunction("SUM");
		// final SDFAttribute sumAttr = new SDFAttribute(null,
		// SUM_OF_SQUARE_ERROR_ATTRIBUTE_NAME, SDFDatatype.DOUBLE, null,
		// null, null);
		// aggregateAO.addAggregation(modelErrorAttribute, sum, sumAttr);

		final AggregateFunction count = new AggregateFunction("COUNT");
		final SDFAttribute countAttr = new SDFAttribute(null,
				COUNT_OF_SQUARE_ERROR_ATTRIBUTE_NAME, SDFDatatype.DOUBLE, null,
				null, null);
		aggregateAO.addAggregation(modelErrorAttribute, count, countAttr);

		final AggregateFunction avg = new AggregateFunction("AVG");
		final SDFAttribute avgAttr = new SDFAttribute(null,
				MEAN_OF_SQUARE_ERROR_ATTRIBUTE_NAME, SDFDatatype.DOUBLE, null,
				null, null);
		aggregateAO.addAggregation(modelErrorAttribute, avg, avgAttr);

		insert(aggregateAO);
		return aggregateAO;
	}

	/**
	 * @param string
	 * @param squareErrorMapFunction
	 * @param operatorBefore
	 * @return
	 */
	private MapAO insertMapAO(final String operatorName,
			final String[] expressionName, final String[] expression,
			final UnaryLogicalOp operatorBefore) {
		final MapAO mapAO = new MapAO();
		mapAO.setName(operatorName);

		final List<NamedExpression> namedExpressions = new ArrayList<>(
				expressionName.length);

		for (int i = 0; i < expression.length; ++i) {
			final NamedExpression namedExpression = new NamedExpression(
					expressionName[i], new SDFExpression(expression[i],null,
							MEP.getInstance()),null);
			namedExpressions.add(namedExpression);
		}

		mapAO.setExpressions(namedExpressions);
		System.out.println("mapAO: " + mapAO);
		// LogicalPlan.replace(operatorBefore, mapAO);
		LogicalPlan.insertOperatorBefore(mapAO, operatorBefore);
		insert(mapAO);
		return mapAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final TestPredictionAO operator,
			final TransformationConfiguration config) {
		if ("RMSE".equals(operator.getMetric())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public String getName() {
		return "TestPredictionAO -> PO";
	}
}
