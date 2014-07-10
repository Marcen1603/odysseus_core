package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class OperatorBuildHelper {

	public static MapAO getMapAO(List<SDFExpressionParameter> expressions,
			ILogicalOperator source) {
		MapAO mapAO = new MapAO();

		List<NamedExpressionItem> expressionItems = new ArrayList<NamedExpressionItem>();
		for (SDFExpressionParameter param : expressions) {
			expressionItems.add(param.getValue());
		}

		mapAO.setExpressions(expressionItems);
		mapAO.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return mapAO;
	}

	public static StateMapAO getStateMapAO(
			List<NamedExpressionItem> expressions, ILogicalOperator source) {
		StateMapAO stateMapAO = new StateMapAO();
		stateMapAO.setExpressions(expressions);
		stateMapAO.subscribeTo(source, source.getOutputSchema());
		return stateMapAO;
	}

	@SuppressWarnings({ "rawtypes" })
	public static SelectAO getTimeSelect(int startMinute, int endMinute,
			ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// minute >= ${parameterTimeStart_minute} AND minute <=
		// ${parameterTimeEnd_minute} AND second >= 0

		// 1. minute >= ${parameterTimeStart_minute}
		String firstPredicateString = "minute >= " + startMinute;
		SDFExpression firstPredicateExpression = new SDFExpression(
				firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(
				firstPredicateExpression);

		// 2. minute <= ${parameterTimeEnd_minute}
		String secondPredicateString = "minute <= " + endMinute;
		SDFExpression secondPredicateExpression = new SDFExpression(
				secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(
				secondPredicateExpression);

		// 3. second >= 0
		String thirdPredicateString = "second >= 0";
		SDFExpression thirdPredicateExpression = new SDFExpression(
				thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(
				thirdPredicateExpression);

		IPredicate firstAndPrdicate = ComplexPredicateHelper
				.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstAndPrdicate, thirdPredicate);

		selectAO.setPredicate(fullAndPredicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	public static SelectAO getEntitySelect(int entityId, ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// 'entity_id = ${entity_id}'

		// 1. minute >= ${parameterTimeStart_minute}
		String predicateString = "entity_id = " + entityId;
		SDFExpression predicateExpression = new SDFExpression(predicateString,
				MEP.getInstance());
		RelationalPredicate predicate = new RelationalPredicate(
				predicateExpression);

		selectAO.setPredicate(predicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	/**
	 * 
	 * @param joinPredicate
	 *            The predicate to join both streams (e.g. sensorid = sid)
	 * @param metaStram
	 *            Stream with probably limited metadata to enrich the other
	 *            stream
	 * @param streamToEnrich
	 *            Normal stream that should be enriched
	 * @return
	 */
	public static EnrichAO getEnrichAO(String joinPredicate,
			ILogicalOperator metaStram, ILogicalOperator streamToEnrich) {
		EnrichAO enrichAO = new EnrichAO();

		PredicateParameter predicateParameter = new PredicateParameter();
		predicateParameter.setInputValue(joinPredicate);
		enrichAO.setPredicate(predicateParameter.getValue());
		enrichAO.subscribeToSource(streamToEnrich, 0, 0,
				streamToEnrich.getOutputSchema());
		enrichAO.subscribeToSource(metaStram, 1, 1, metaStram.getOutputSchema());

		return enrichAO;
	}

	/**
	 * Creates an AggregateAO with standard output-type as double (or what
	 * Odysseus use as standard the time you use this function)
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @return
	 */
	public static AggregateAO getAggregateAO(String aggregationFunction,
			String inputAttributeName, String outputAttributeName,
			ILogicalOperator source) {
		return getAggregateAO(aggregationFunction, inputAttributeName,
				outputAttributeName, null, source);
	}

	/**
	 * Creates an AggregateAO with the specified output type
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @param outputType
	 *            The optional type of output (null, if you don't want to
	 *            specify, should then be double)
	 * @return
	 */
	public static AggregateAO getAggregateAO(String aggregationFunction,
			String inputAttributeName, String outputAttributeName,
			String outputType, ILogicalOperator source) {
		AggregateAO aggregateAO = new AggregateAO();

		AggregateItemParameter param = new AggregateItemParameter();
		List<String> aggregateOptions = new ArrayList<String>();
		aggregateOptions.add(aggregationFunction);
		aggregateOptions.add(inputAttributeName);
		aggregateOptions.add(outputAttributeName);
		if (outputType != null)
			aggregateOptions.add(outputType);
		param.setInputValue(aggregateOptions);
		List<AggregateItem> aggregateItems = new ArrayList<AggregateItem>();
		aggregateItems.add(param.getValue());
		aggregateAO.setAggregationItems(aggregateItems);

		aggregateAO.subscribeTo(source, source.getOutputSchema());

		return aggregateAO;
	}

	public static SDFExpressionParameter getExpressionParameter(
			String expression, String name) {

		SDFExpressionParameter param = new SDFExpressionParameter();
		List<String> paramValue = new ArrayList<String>();
		paramValue.add(expression);
		paramValue.add(name);
		param.setInputValue(paramValue);

		return param;
	}
	
	public static SDFExpressionParameter getExpressionParameter(
			String expression) {
		SDFExpressionParameter param = new SDFExpressionParameter();
		param.setInputValue(expression);
		return param;
	}
	

}
