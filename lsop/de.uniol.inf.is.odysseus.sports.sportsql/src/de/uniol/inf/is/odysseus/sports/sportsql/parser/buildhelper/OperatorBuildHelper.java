package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * Class with static methods which help you to build the AOs easily.
 * 
 * @author Tobias Brandt
 * @since 10.07.2014
 *
 */
public class OperatorBuildHelper {

	/**
	 * Names of streams
	 */
	public static final String MAIN_STREAM_NAME = "soccergame";
	public static final String METADATA_STREAM_NAME = "metadata";

	/**
	 * Conversion variables
	 */
	public static final long TS_TO_MS_FACTOR = 1000000000;
	// ts in picoseconds for DEBS Grand Challenge 2013 soccer data
	public static final String TS_GAME_START = "10753295594424116.0";

	/**
	 * Game field borders
	 */
	public static final int UPPERLEFT_X = 0;
	public static final int UPPERLEFT_Y = 33965;
	public static final int LOWERLEFT_X = -50;
	public static final int LOWERLEFT_Y = -33960;
	public static final int UPPERRIGHT_X = 52477;
	public static final int UPPERRIGHT_Y = 33941;
	public static final int LOWERRIGHT_X = 52489;
	public static final int LOWERRIGHT_Y = -33939;

	/**
	 * Creates a MapAP with a list of expressions. To create such expressions,
	 * see {@link createExpressionParameter}.
	 * 
	 * @param expressions
	 *            List of expressions for this MAP-Operator
	 * @param source
	 *            Source for this operator
	 * @param sinkInPort
	 *            Port of sink to which the result will be send
	 * @param sourceOutPort
	 *            Port of source from which the data will be received
	 * @return A MapAO with the given expressions
	 */
	public static MapAO createMapAO(List<SDFExpressionParameter> expressions,
			ILogicalOperator source, int sinkInPort, int sourceOutPort) {
		MapAO mapAO = new MapAO();

		List<NamedExpressionItem> expressionItems = new ArrayList<NamedExpressionItem>();
		for (SDFExpressionParameter param : expressions) {
			expressionItems.add(param.getValue());
		}

		mapAO.setExpressions(expressionItems);
		mapAO.subscribeToSource(source, sinkInPort, sourceOutPort,
				source.getOutputSchema());
		return mapAO;
	}

	/**
	 * Creates a StateMapAO with the list of expressions. To create such
	 * expressions, see {@link createExpressionParameter}. Optional you can
	 * group in this operator.
	 * 
	 * @param expressions
	 *            List of expressions for this StateMap-Operator
	 * @param groupBy
	 *            The variable you want to group by (e.g., "sensorid"). Enter
	 *            null if you don't want to group
	 * @param source
	 *            Source for this operator
	 * @return A StateMapAO with given expressions and grouping
	 */
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
			stateMapAO.setGroupingAttributes(createAttributeList(groupBy,
					source));
		}

		stateMapAO.subscribeTo(source, source.getOutputSchema());
		return stateMapAO;
	}

	/**
	 * Creates a Select Operator to Filter for space parameter
	 * 
	 * @param parameter
	 *            according Space param.
	 * @param source
	 *            Source Operator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static SelectAO createSpaceSelect(SportsQLSpaceParameter parameter,
			ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// TODO: Do the right thing if timeParameter says "all"

		int startX = parameter.getStartx();
		int startY = parameter.getStarty();
		int endX = parameter.getEndx();
		int endY = parameter.getEndy();

		// Predicate we want to produce:
		// x >= startX AND x <= endX AND y>= startY AND y<= startY

		String firstPredicateString = "x >= " + startX;
		String secondPredicateString = "x <= " + endX;
		String thirdPredicateString = "y >= " + startY;
		String fourthPredicateString = "y <= " + endY;

		// Create Predicates from Strings

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
				.createAndPredicate(thirdPredicate, fourthPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstAndPredicate, secondAndPredicate);

		selectAO.setPredicate(fullAndPredicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	/**
	 * Creates a SelectAO with given timeParameter. Automatically build a
	 * correct Select for the given timeParameter. Assumes that the given input
	 * stream has "minute" and "second" in the tuples
	 * 
	 * @param timeParameter
	 *            timeParameter with the information which time should be
	 *            selected
	 * @param source
	 *            Source which should at least contain "minute" and "second"
	 * @return SelectAO which selects just the time-range you configured in the
	 *         timeParameter.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static SelectAO createTimeSelect(
			SportsQLTimeParameter timeParameter, ILogicalOperator source) {
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
	 * 
	 * @param entityId
	 *            Id to filter for.
	 * @param source
	 *            Source Operator
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
	 * 
	 * @param entityName
	 *            Name of Entity to Filter, e.g. 'Ball'
	 * @param source
	 *            Source Operator which is the source for this operator
	 * @return
	 */
	public static SelectAO createEntitySelectByName(String entityName,
			ILogicalOperator source) {
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
	 * @param sampleRate
	 *            sampleRate determines which n'th tuples are processed.
	 * @param source
	 *            Source Operator to Link to.
	 * @return
	 */
	public static SampleAO createSampleAO(int sampleRate,
			ILogicalOperator source) {
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

		// We need this to get the AttributeResolver
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(streamToEnrich);
		sources.add(metaStream);

		// Create parameter
		PredicateParameter predicateParameter = new PredicateParameter();
		predicateParameter.setAttributeResolver(OperatorBuildHelper
				.createAttributeResolver(sources));

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
	 * @param source
	 *            The operator before this one
	 * @return
	 */
	public static AggregateAO createAggregateAO(String aggregationFunction,
			String groupBy, String inputAttributeName,
			String outputAttributeName, String outputType,
			ILogicalOperator source) {
		AggregateAO aggregateAO = new AggregateAO();

		// Fill parameter
		AggregateItemParameter param = new AggregateItemParameter();
		List<String> aggregateOptions = new ArrayList<String>();
		aggregateOptions.add(aggregationFunction);
		aggregateOptions.add(inputAttributeName);
		aggregateOptions.add(outputAttributeName);
		if (outputType != null)
			aggregateOptions.add(outputType);
		param.setInputValue(aggregateOptions);

		// Attribute resolver and datadictionary for parameter
		IAttributeResolver resolver = OperatorBuildHelper
				.createAttributeResolver(source);
		param.setAttributeResolver(resolver);
		IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();
		param.setDataDictionary(dataDict);

		// Use parameter to get information for AO
		List<AggregateItem> aggregateItems = new ArrayList<AggregateItem>();
		aggregateItems.add(param.getValue());
		aggregateAO.setAggregationItems(aggregateItems);

		// GroupBy
		if (groupBy != null) {
			aggregateAO.setGroupingAttributes(createAttributeList(groupBy,
					source));
		}

		aggregateAO.subscribeTo(source, source.getOutputSchema());

		return aggregateAO;
	}

	/**
	 * Returns routeAO with a list of predicates
	 * 
	 * @param listOfPredicates
	 * @return
	 */
	public static RouteAO createRouteAO(ArrayList<String> listOfPredicates,
			ILogicalOperator source) {
		RouteAO rAO = new RouteAO();

		// Add predicates to the routeAO operator
		for (String predicate : listOfPredicates) {
			SDFExpression predicateExpression = new SDFExpression(predicate,
					MEP.getInstance());
			RelationalPredicate p = new RelationalPredicate(
					predicateExpression);

			rAO.addPredicate(p);
		}
		// TODO different ports for different results?
		rAO.subscribeTo(source, source.getOutputSchema());
		return rAO;
	}

	/**
	 * Returns changeDetectAO with list of Attributes, groupBy, relative
	 * tolerance and absolute tolerance
	 * 
	 * @param attributes
	 * @param groupBy
	 * @param relativeTolerance
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<String> attributes,
			List<SDFAttribute> groupBy, boolean relativeTolerance,
			double tolerance, ILogicalOperator source) {

		ArrayList<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source);

		List<SDFAttribute> sdfAttributes = OperatorBuildHelper
				.createAttributeList(attributes, sources);
		ChangeDetectAO cAO = new ChangeDetectAO();
		cAO.setAttr(sdfAttributes);
		cAO.setGroupingAttributes(groupBy);
		cAO.setRelativeTolerance(relativeTolerance);
		cAO.setTolerance(tolerance);

		// cAO.subscribeToSource(source, 0, 1, inputSchema);
		cAO.subscribeTo(source, source.getOutputSchema());
		return cAO;
	}

	/**
	 * Returns changeDetectAO with list of Attributes, relative tolerance and
	 * absolute tolerance
	 * 
	 * @param attributes
	 * @param relativeTolerance
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(
			List<SDFAttribute> attributes, boolean relativeTolerance,
			double tolerance, ILogicalOperator source) {
		ChangeDetectAO changeDetectAO = new ChangeDetectAO();
		changeDetectAO.setAttr(attributes);
		changeDetectAO.setRelativeTolerance(relativeTolerance);
		changeDetectAO.setTolerance(tolerance);
		changeDetectAO.subscribeTo(source, source.getOutputSchema());
		return changeDetectAO;
	}

	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute
	 * tolerance
	 * 
	 * @param attributes
	 *            List of Attributes where changes should occur
	 * @param tolerance
	 *            (Absolute) Tolerance applied to changeDetection.
	 * @param source
	 *            Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(
			List<SDFAttribute> attributes, double tolerance,
			ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();

		cAO.setAttr(attributes);
		cAO.setTolerance(tolerance);
		cAO.subscribeTo(source, source.getOutputSchema());
		return cAO;
	}

	/**
	 * Returns selectAO with a list of predicates
	 * 
	 * @param listOfPredicates
	 * @param source
	 * @return
	 */
	public static SelectAO createSelectAO(ArrayList<String> listOfPredicates,
			ILogicalOperator source) {
		SelectAO sAO = new SelectAO();
		for (String predicateString : listOfPredicates) {
			SDFExpression predicateExpression = new SDFExpression(predicateString,
					MEP.getInstance());
			RelationalPredicate predicate = new RelationalPredicate(
					predicateExpression);
			sAO.addPredicate(predicate);
		}
		sAO.subscribeTo(source, source.getOutputSchema());
		return sAO;
	}

	/***
	 * Creates a Tuple Window
	 * 
	 * @param size
	 *            Size of the window
	 * @param advance
	 *            Advance value of the window
	 * @param source
	 *            Source Operator.
	 * @return
	 */
	public static WindowAO createTupleWindowAO(int size, int advance,
			ILogicalOperator source) {
		WindowAO windowAO = new WindowAO();
		windowAO.setWindowType(WindowType.TUPLE);
		TimeValueItem windowSize = new TimeValueItem(size, null);
		TimeValueItem windowAdvance = new TimeValueItem(advance, null);
		windowAO.setWindowSize(windowSize);
		windowAO.setWindowAdvance(windowAdvance);
		windowAO.subscribeTo(source, source.getOutputSchema());
		return windowAO;
	}

	/**
	 * Returns windowAO
	 * 
	 * @param windowSize
	 * @param windowType
	 * @param windowAdvance
	 * @param source
	 * @return
	 */
	public static WindowAO createWindowAO(TimeValueItem windowSize,
			WindowType windowType, TimeValueItem windowAdvance,
			ILogicalOperator source) {
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
	 * 
	 * @param listOfPredicates
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static JoinAO createJoinAO(ArrayList<String> listOfPredicates,
			ILogicalOperator source1, ILogicalOperator source2) {
		JoinAO jAO = new JoinAO();
		ArrayList<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source1);
		sources.add(source2);

		IAttributeResolver resolver = OperatorBuildHelper
				.createAttributeResolver(sources);
		for (String predicate : listOfPredicates) {
			PredicateParameter param = new PredicateParameter();
			param.setAttributeResolver(resolver);
			param.setInputValue(predicate);
			jAO.addPredicate(param.getValue());
		}
		jAO.subscribeToSource(source1, 0, 0, source1.getOutputSchema());
		jAO.subscribeToSource(source2, 1, 0, source2.getOutputSchema());

		return jAO;
	}

	/**
	 * Method to get AccessAO. You'll need this to access sources.
	 * 
	 * @param sourcename
	 *            Name of source
	 * @return
	 */
	public static AccessAO createAccessAO(String sourcename) {
		// TODO Does this work? Probably not.
		// We need the dataDictionary for this parameter
		IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();

		SourceParameter source = new SourceParameter();
		source.setInputValue(sourcename);
		source.setDataDictionary(dataDict);

		// TODO We need to set the caller
		ISession session = OperatorBuildHelper.getActiveSession();
		source.setCaller(session);

		return source.getValue();
	}

	/**
	 * Creates a simple TopAO which indicates the top node in the query (maybe
	 * necessary for Odysseus: This could / should be the operator you return in
	 * the plan.)
	 * 
	 * @param source
	 *            The top logical operator in your query is the source of this
	 *            operator. You can imagine that you just put this as a hat on
	 *            top of your finished plan.
	 * @return A simple TopAO.
	 */
	public static TopAO createTopAO(ILogicalOperator source) {
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source);
		return OperatorBuildHelper.createTopAO(sources);
	}

	/**
	 * If you have more than one Operator at the end of your query you can use
	 * this. It will put the TopAO on top of all the operators, with the input-
	 * and output-port beginning with 0 and increasing up to the number of the
	 * operators -1.
	 * 
	 * @param sources List of sources you want to be under the TopAO.
	 * @return A TopAO which has all the given operators under it.
	 */
	public static TopAO createTopAO(List<ILogicalOperator> sources) {
		TopAO topAO = new TopAO();
		for (int i = 0; i < sources.size(); i++) {
			topAO.subscribeToSource(sources.get(i), i, i, sources.get(i)
					.getOutputSchema());
		}
		return topAO;
	}

	/**
	 * Creates an expressionParameter with a name which you can use to create
	 * some AOs, especially MapAOs.
	 * 
	 * @param expression
	 *            Expression as a String as you would type it in PQL (e.g. just
	 *            "x" or more complex things like "toDate(ts/100000)")
	 * @param name
	 *            The name the calculated value from this expression should have
	 *            (e.g., "minutes"). This name will appear in the OutputSchema
	 *            of the operator you put the expression in
	 * @return An expression which can be used in various AOs, especially MapAOs
	 */
	public static SDFExpressionParameter createExpressionParameter(
			String expression, String name, ILogicalOperator source) {

		IAttributeResolver attributeResolver = OperatorBuildHelper
				.createAttributeResolver(source);

		SDFExpressionParameter param = new SDFExpressionParameter();
		List<String> paramValue = new ArrayList<String>();
		paramValue.add(expression);
		paramValue.add(name);
		param.setInputValue(paramValue);
		param.setAttributeResolver(attributeResolver);

		return param;
	}

	/**
	 * Creates an expressionParameter without a name (the expression will be the
	 * name) which you can use to create some AOs, especially MapAOs.
	 * 
	 * @param expression
	 *            Expression as a String as you would type it in PQL (e.g. just
	 *            "x" or more complex things like "toDate(ts/100000)")
	 * @return An expression which can be used in various AOs, especially MapAOs
	 */
	public static SDFExpressionParameter createExpressionParameter(
			String expression, ILogicalOperator source) {
		IAttributeResolver attributeResolver = OperatorBuildHelper
				.createAttributeResolver(source);
		SDFExpressionParameter param = new SDFExpressionParameter();
		param.setInputValue(expression);
		param.setAttributeResolver(attributeResolver);
		return param;
	}

	/**
	 * Calls "initialize()" for all given AOs. Some AOs maybe need this call so
	 * it recommended to initialize the AOs.
	 * 
	 * @param operators
	 */
	public static void initializeOperators(List<ILogicalOperator> operators) {
		for (ILogicalOperator op : operators) {
			op.initialize();
		}
	}

	/**
	 * Creates a list of attributes from a list of strings representing
	 * attributes. Such a string could be something easy as "x". Used to create
	 * a ChangeDetectAO for example.
	 * 
	 * @param listOfAttributes
	 *            List of Strings representing attributes (e.g., just "x")
	 * @return A list of attributes which can be used e.g. to create a
	 *         ChangeDetectAO.
	 */
	public static List<SDFAttribute> createAttributeList(
			List<String> listOfAttributes, List<ILogicalOperator> sources) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

		IAttributeResolver resolver = createAttributeResolver(sources);

		for (String attribute : listOfAttributes) {
			attributes.add(resolver.getAttribute(attribute));
		}

		return attributes;
	}

	/**
	 * You maybe need the DataDictionary to get sources or other things
	 * installed in Odysseus. Maybe a parameter or something else need a
	 * DataDictionary to work.
	 * 
	 * @return The DataDictionary
	 */
	public static IDataDictionary getDataDictionary() {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
		return dd;
	}

	/**
	 * Creates a session if no exists, uses session if there is already one.
	 * 
	 * @return The current session
	 */
	public static ISession getActiveSession() {
		return OdysseusRCPPlugIn.getActiveSession();
	}

	/**
	 * Creates Attribute Resolver to find Attributes by String
	 * 
	 * @param source
	 *            incoming Operator
	 * @return Attribute Resolver.
	 */
	public static IAttributeResolver createAttributeResolver(
			ILogicalOperator source) {
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source);
		return OperatorBuildHelper.createAttributeResolver(sources);
	}

	/**
	 * Creates Attribute Resolver to find Attributes by String
	 * 
	 * @param sources
	 *            incoming Operators
	 * @return Attribute Resolver.
	 */
	public static IAttributeResolver createAttributeResolver(
			List<ILogicalOperator> sources) {
		List<SDFSchema> inputSchema = new LinkedList<>();
		for (ILogicalOperator source : sources) {
			inputSchema.add(source.getOutputSchema());
		}

		IAttributeResolver attributeResolver = new DirectAttributeResolver(
				inputSchema);
		return attributeResolver;
	}

	/**
	 * Creates SDFAttributes from a single String, e.g. to use as groupBy
	 * attribute.
	 * 
	 * @param groupBy
	 *            String to group By
	 * @param source
	 *            Source Operator.
	 * @return List of SDFAttribtues
	 */
	public static List<SDFAttribute> createAttributeList(String groupBy,
			ILogicalOperator source) {
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		attributes.add(groupBy);
		operators.add(source);
		return createAttributeList(attributes, operators);

	}

	/**
	 * Creates SDFAttributes from Strings, e.g. to use as groupBy attributes.
	 * 
	 * @param groupBy
	 *            List of Strings
	 * @param source
	 *            Source Operator.
	 * @return List of SDFAttribtues
	 */
	public static List<SDFAttribute> createAttributeList(List<String> groupBy,
			ILogicalOperator source) {
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		operators.add(source);
		return createAttributeList(groupBy, operators);
	}

	/**
	 * Creates an ILogicalQuery with an TopAO on top of the query. Initialized
	 * all operators and gives the query a name
	 * 
	 * @param topSource
	 *            Top source of your query
	 * @param allOperators
	 *            List of all operators which should be initialized
	 * @param queryName
	 *            The name this query shall get
	 * @return A finished logical query.
	 */
	public static ILogicalQuery finishQuery(ILogicalOperator topSource,
			List<ILogicalOperator> allOperators, String queryName) {
		// TopAO (for Odysseus - it wants to know which operator is the top)
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(topSource);

		return finishQuery(sources, allOperators, queryName);
	}
	
	/**
	 * Creates an ILogicalQuery with an TopAO on top of the query. Initialized
	 * all operators and gives the query a name
	 * 
	 * @param topSources
	 *            Top sources of your query
	 * @param allOperators
	 *            List of all operators which should be initialized
	 * @param queryName
	 *            The name this query shall get
	 * @return A finished logical query.
	 */
	public static ILogicalQuery finishQuery(List<ILogicalOperator> topSources,
			List<ILogicalOperator> allOperators, String queryName) {
		// TopAO (for Odysseus - it wants to know which operator is the top)
		TopAO topAO = OperatorBuildHelper.createTopAO(topSources);

		// Initialize all AOs
		OperatorBuildHelper.initializeOperators(allOperators);

		// Create plan
		ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(topAO, true);
		query.setName(queryName);

		return query;
	}
}
