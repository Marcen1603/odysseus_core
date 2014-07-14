package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * Class with static methods which help you to build the AOs easily.
 * 
 * @author Tobias Brandt
 * @since 10.07.2014
 *
 */
public class OperatorBuildHelper {

	public static MapAO createMapAO(List<SDFExpressionParameter> expressions,
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

	public static StateMapAO createStateMapAO(
			List<SDFExpressionParameter> expressions, String groupBy,
			ILogicalOperator source) {
		StateMapAO stateMapAO = new StateMapAO();

		// Expressions
		List<NamedExpressionItem> expressionItems = new ArrayList<NamedExpressionItem>();
		for (SDFExpressionParameter param : expressions) {
			expressionItems.add(param.getValue());
		}
		stateMapAO.setExpressions(expressionItems);

		// GroupBy
		if (groupBy != null) {
			stateMapAO.setGroupingAttributes(createGroupAttributeList(groupBy));
		}

		stateMapAO.subscribeTo(source, source.getOutputSchema());
		return stateMapAO;
	}
	
	/**
	 * Creates a Select Operator to Filter for space parameter
	 * @param parameter according Space param.
	 * @param source Source Operator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static SelectAO createSpaceSelect(SportsQLSpaceParameter parameter, ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();
		
		// TODO: Do the right thing if timeParameter says "all"
		
		int startX = parameter.getStartx();
		int startY = parameter.getStarty();
		int endX = parameter.getEndx();
		int endY = parameter.getEndy();
		
		//Predicate we want to produce:
		// x >= startX AND x <= endX AND y>= startY AND y<= startY
		
		String firstPredicateString = "x >= " + startX;
		String secondPredicateString = "x <= " + endX;
		String thirdPredicateString = "y >= " + startY;
		String fourthPredicateString = "y <= " + endY;
		
		//Create Predicates from Strings
		
		SDFExpression firstPredicateExpression = new SDFExpression(
				firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(
				firstPredicateExpression);
		
		SDFExpression secondPredicateExpression = new SDFExpression(
				secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(
				secondPredicateExpression);

		SDFExpression thirdPredicateExpression = new SDFExpression(
				thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(
				thirdPredicateExpression);
		
		SDFExpression fourthPredicateExpression = new SDFExpression(
				fourthPredicateString, MEP.getInstance());
		RelationalPredicate fourthPredicate = new RelationalPredicate(
				fourthPredicateExpression);
		
		IPredicate firstAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate secondAndPredicate = ComplexPredicateHelper
				.createAndPredicate(thirdPredicate,fourthPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper.createAndPredicate(firstAndPredicate,secondAndPredicate);
		
		selectAO.setPredicate(fullAndPredicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	@SuppressWarnings({ "rawtypes" })
	public static SelectAO createTimeSelect(SportsQLTimeParameter timeParameter,
			ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// minute >= ${parameterTimeStart_minute} AND minute <=
		// ${parameterTimeEnd_minute} AND second >= 0

		// TODO: Do the right thing if timeParameter says "all" or "now"
		int startMinute = timeParameter.getStart();
		int endMinute = timeParameter.getEnd();

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

	/**
	 * Creates a Select Operator to filter Entity by Id.
	 * @param entityId Id to filter for.
	 * @param source Source Operator
	 * @return
	 */
	public static SelectAO createEntitySelect(long entityId,
			ILogicalOperator source) {
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
	 * Creates Select Operator to Filter by entity name.
	 * @param entityName Name of Entity to Filter, e.g. 'Ball'
	 * @param source Source Op
	 * @return
	 */
	public static SelectAO createEntitySelectByName(String entityName, ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// 'entity = ${entity_name}'

		String predicateString = "entity = " + entityName;
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
	 * @param sampleRate sampleRate determines which n'th tuples are processed.
	 * @param source Source Operator to Link to.
	 * @return
	 */
	public static SampleAO createSampleAO(int sampleRate,ILogicalOperator source) {
		SampleAO sampleAO = new SampleAO();
		sampleAO.setSampleRate(sampleRate);
		sampleAO.subscribeTo(source, source.getOutputSchema());
		return sampleAO;
	}

	/**
	 * 
	 * @param joinPredicate
	 *            The predicate to join both streams (e.g. sensorid = sid)
	 * @param streamToEnrich
	 *            Normal stream that should be enriched
	 * @param metaStram
	 *            Stream with probably limited metadata to enrich the other
	 *            stream. This will be on port 1 (input and output)
	 * @return
	 */
	public static EnrichAO createEnrichAO(String joinPredicate,
			ILogicalOperator streamToEnrich, ILogicalOperator metaStream) {
		EnrichAO enrichAO = new EnrichAO();

		PredicateParameter predicateParameter = new PredicateParameter();
		predicateParameter.setInputValue(joinPredicate);
		enrichAO.setPredicate(predicateParameter.getValue());
		enrichAO.subscribeToSource(streamToEnrich, 0, 0,
				streamToEnrich.getOutputSchema());
		enrichAO.subscribeToSource(metaStream, 1, 1,
				metaStream.getOutputSchema());

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
	public static AggregateAO createAggregateAO(String aggregationFunction,
			String inputAttributeName, String outputAttributeName,
			ILogicalOperator source) {
		return createAggregateAO(aggregationFunction, null, inputAttributeName,
				outputAttributeName, null, source);
	}

	/**
	 * Creates an AggregateAO with the specified output type
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param groupBy
	 *            The name of the attribute you want to group by (just one for
	 *            now)
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @param outputType
	 *            The optional type of output (null, if you don't want to
	 *            specify, should then be double)
	 * @param source The operator before this one
	 * @return
	 */
	public static AggregateAO createAggregateAO(String aggregationFunction,
			String groupBy, String inputAttributeName,
			String outputAttributeName, String outputType,
			ILogicalOperator source) {
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

		// GroupBy
		if (groupBy != null) {
			aggregateAO.setGroupingAttributes(createGroupAttributeList(groupBy));
		}

		aggregateAO.subscribeTo(source, source.getOutputSchema());

		return aggregateAO;
	}
	
	/**
	 * Returns routeAO with a list of predicates
	 * @param listOfPredicates
	 * @return
	 */
	public static RouteAO createRouteAO(ArrayList<String> listOfPredicates, ILogicalOperator source) {
		RouteAO rAO = new RouteAO();

		//Add predicates to the routeAO operator
		for (String predicate : listOfPredicates) {
			PredicateParameter param = new PredicateParameter();
			param.setInputValue(predicate);
			
			rAO.addPredicate(param.getValue());
		}
		//TODO different ports for different results?
		rAO.subscribeTo(source, source.getOutputSchema());
		return rAO;
	}
	
	/**
	 * Returns changeDetectAO with list of Attributes, groupBy, relative tolerance and absolute tolerance
	 * @param attributes
	 * @param groupBy
	 * @param relativeTolerance
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, List<SDFAttribute> groupBy, boolean relativeTolerance, double tolerance, ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();
		cAO.setAttr(attributes);
		cAO.setGroupingAttributes(groupBy);
		cAO.setRelativeTolerance(relativeTolerance);
		cAO.setTolerance(tolerance);
		cAO.subscribeTo(source,source.getOutputSchema());
		return cAO;		
	}
	
	/**
	 * Returns changeDetectAO with list of Attributes, relative tolerance and absolute tolerance
	 * @param attributes
	 * @param relativeTolerance 
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, boolean relativeTolerance, double tolerance, ILogicalOperator source) {
		ChangeDetectAO changeDetectAO = new ChangeDetectAO();
		changeDetectAO.setAttr(attributes);
		changeDetectAO.setRelativeTolerance(relativeTolerance);
		changeDetectAO.setTolerance(tolerance);
		changeDetectAO.subscribeTo(source,source.getOutputSchema());
		return changeDetectAO;
	}
	
	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute tolerance
	 * @param attributes List of Attributes where changes should occur
	 * @param tolerance (Absolute) Tolerance applied to changeDetection.
	 * @param source Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes,double tolerance,ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();
		cAO.setAttr(attributes);
		cAO.setTolerance(tolerance);
		cAO.subscribeTo(source,source.getOutputSchema());
		return cAO;
	}
	
	/**
	 * Returns selectAO with a list of predicates
	 * @param listOfPredicates
	 * @param source
	 * @return
	 */
	public static SelectAO createSelectAO(ArrayList<String> listOfPredicates, ILogicalOperator source) {
		SelectAO sAO = new SelectAO();
		for (String predicate : listOfPredicates) {
			PredicateParameter param = new PredicateParameter();
			param.setInputValue(predicate);
			sAO.addPredicate(param.getValue());
		}
		sAO.subscribeTo(source, source.getOutputSchema());
		return sAO;
	}
	
	/***
	 * Creates a Tuple Window
	 * @param size Size of the window
	 * @param advance Advance value of the window
	 * @param source Source Operator.
	 * @return
	 */
	public static WindowAO createTupleWindowAO(int size, int advance, ILogicalOperator source) {
		WindowAO windowAO = new WindowAO();
		windowAO.setWindowType(WindowType.TUPLE);
		TimeValueItem windowSize= new TimeValueItem(size,null);
		TimeValueItem windowAdvance = new TimeValueItem(advance,null);
		windowAO.setWindowSize(windowSize);
		windowAO.setWindowAdvance(windowAdvance);
		windowAO.subscribeTo(source, source.getOutputSchema());
		return windowAO;
	}
	
	/**
	 * Returns windowAO
	 * @param windowSize
	 * @param windowType
	 * @param windowAdvance
	 * @param source
	 * @return
	 */
	public static WindowAO createWindowAO(TimeValueItem windowSize, WindowType windowType, TimeValueItem windowAdvance, ILogicalOperator source) {
		WindowAO wAO = new WindowAO();
		wAO.setWindowSize(windowSize);
		wAO.setWindowType(windowType);
		wAO.setWindowAdvance(windowAdvance);
		
		if (source != null) {
			wAO.subscribeTo(source, source.getOutputSchema());
		}
		
		return wAO;
	}
	
	/**
	 * Returns joinAO
	 * @param listOfPredicates
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static JoinAO createJoinAO(ArrayList<String> listOfPredicates, ILogicalOperator source1, ILogicalOperator source2) {
		JoinAO jAO = new JoinAO();
		
		for (String predicate : listOfPredicates) {
			PredicateParameter param = new PredicateParameter();
			param.setInputValue(predicate);
			jAO.addPredicate(param.getValue());
		}
		jAO.subscribeToSource(source1, 0, 0, source1.getOutputSchema());
		jAO.subscribeToSource(source2, 1, 0, source2.getOutputSchema());
		
		return jAO;
	}
	
	/**
	 * Function to get AccessAO
	 * @param sourcename	Name of source
	 * @return
	 */
	public static AccessAO createAccessAO(String sourcename) {
		//TODO Does this work? Probably not.
		SourceParameter source = new SourceParameter();
		source.setInputValue(sourcename);
		return source.getValue();
	}

	public static SDFExpressionParameter createExpressionParameter(
			String expression, String name) {

		SDFExpressionParameter param = new SDFExpressionParameter();
		List<String> paramValue = new ArrayList<String>();
		paramValue.add(expression);
		paramValue.add(name);
		param.setInputValue(paramValue);

		return param;
	}

	public static SDFExpressionParameter createExpressionParameter(
			String expression) {
		SDFExpressionParameter param = new SDFExpressionParameter();
		param.setInputValue(expression);
		return param;
	}
	
	public static void initializeOperators(List<ILogicalOperator> operators) {
		for (ILogicalOperator op : operators) {
			op.initialize();
		}
	}

	private static List<SDFAttribute> createGroupAttributeList(String groupBy) {
		ResolvedSDFAttributeParameter groupParameter = new ResolvedSDFAttributeParameter();
		groupParameter.setInputValue(groupBy);
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(groupParameter.getValue());
		return attributes;
	}
	
	public static List<SDFAttribute> createAttributeList(ArrayList<String> listOfAttributes) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		
		for (String attribute : listOfAttributes) {
			ResolvedSDFAttributeParameter param = new ResolvedSDFAttributeParameter();
			param.setInputValue(attribute);
			attributes.add(param.getValue());
		}
		
		return attributes;
	}
}
