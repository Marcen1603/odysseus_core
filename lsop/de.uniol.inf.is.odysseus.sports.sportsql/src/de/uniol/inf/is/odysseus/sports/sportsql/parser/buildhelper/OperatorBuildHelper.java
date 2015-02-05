package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.logicaloperator.SportsHeatMapAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.SpaceUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.model.Space;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

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
	public static final long TS_TO_MS_FACTOR = 1000;

	/**
	 * Team Ids
	 */
	public static final int TEAM_ID_RIGHT_GOAL_SHOOTERS = 1;
	public static final int TEAM_ID_LEFT_GOAL_SHOOTERS = 2;

	/**
	 * Times
	 */
	public static final int START_MINUTE = 0;
	public static final int END_MINUTE = 90;

	/***
	 * Some Conversion Constants
	 */
	public static final int CONVERSION_DIVIDEND_POSITION_TO_METERS = 1000;

	// Attributes
	public static final String ATTRIBUTE_MINUTE = "minute";
	public static final String ATTRIBUTE_SECOND = "second";
	public static final String ATTRIBUTE_MILLISECOND = "milliseconds";
	public static final String ATTRIBUTE_X_METER = "x_meter";
	public static final String ATTRIBUTE_Y_METER = "y_meter";

	/**
	 * Creates a MapAP with a list of expressions. To create such expressions, see {@link createExpressionParameter}.
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
	public static MapAO createMapAO(List<SDFExpressionParameter> expressions, ILogicalOperator source, int sinkInPort,
			int sourceOutPort, boolean evaluateOnPunctuation) {
		MapAO mapAO = new MapAO();

		List<NamedExpression> expressionItems = new ArrayList<NamedExpression>();
		for (SDFExpressionParameter param : expressions) {
			expressionItems.add(param.getValue());
		}

		mapAO.setEvaluateOnPunctuation(evaluateOnPunctuation);

		mapAO.setExpressions(expressionItems);
		mapAO.subscribeToSource(source, sinkInPort, sourceOutPort, source.getOutputSchema());
		return mapAO;
	}

	/**
	 * Creates a StateMapAO with the list of expressions. To create such expressions, see
	 * {@link createExpressionParameter}. Optional you can group in this operator.
	 * 
	 * @param expressions
	 *            List of expressions for this StateMap-Operator
	 * @param groupBy
	 *            The variable you want to group by (e.g., "sensorid"). Enter empty String if you don't want to group
	 * @param source
	 *            Source for this operator
	 * @return A StateMapAO with given expressions and grouping
	 */
	public static StateMapAO createStateMapAO(List<SDFExpressionParameter> expressions, String groupBy,
			ILogicalOperator source) {
		List<String> groupingAttributes = new ArrayList<String>();
		if (groupBy.length() > 0) {
			groupingAttributes.add(groupBy);
		}

		return createStateMapAO(expressions, groupingAttributes, source);

	}

	/**
	 * Creates a StateMapAO with the list of expressions. To create such expressions, see
	 * {@link createExpressionParameter}.
	 * 
	 * @param expressions
	 *            List of expressions for this StateMap-Operator
	 * @param source
	 * @return
	 */
	public static StateMapAO createStateMapAO(List<SDFExpressionParameter> expressions, ILogicalOperator source) {
		return createStateMapAO(expressions, "", source);

	}

	/**
	 * Creates a StateMapAO with the list of expressions. To create such expressions, see
	 * {@link createExpressionParameter}. Optional you can group in this operator.
	 * 
	 * @param expressions
	 *            List of expressions for this StateMap-Operator
	 * @param groupBy
	 *            List of variables you want to group by
	 * @param source
	 *            Source for this operator
	 * @return A StateMapAO with given expressions and grouping
	 */
	public static StateMapAO createStateMapAO(List<SDFExpressionParameter> expressions, List<String> groupBy,
			ILogicalOperator source) {
		StateMapAO stateMapAO = new StateMapAO();

		// Expressions
		List<NamedExpression> expressionItems = new ArrayList<NamedExpression>();
		for (SDFExpressionParameter param : expressions) {
			expressionItems.add(param.getValue());
		}
		stateMapAO.setExpressions(expressionItems);

		// GroupBy
		if (groupBy != null && !groupBy.isEmpty()) {
			stateMapAO.setGroupingAttributes(createAttributeList(groupBy, source));
		}

		stateMapAO.subscribeTo(source, source.getOutputSchema());
		return stateMapAO;
	}

	/**
	 * Operator to merge multiple streams
	 * 
	 * @param sourcesToMerge
	 *            All sources to be merged. First come first serve.
	 * @return
	 */
	public static MergeAO createMergeAO(List<ILogicalOperator> sourcesToMerge) {
		MergeAO mergeAO = new MergeAO();

		for (int i = 0; i < sourcesToMerge.size(); i++) {
			ILogicalOperator source = sourcesToMerge.get(i);
			mergeAO.subscribeToSource(source, i, 0, source.getOutputSchema());
		}

		return mergeAO;
	}

	/**
	 * Operator to merge multiple streams
	 * 
	 * @param source1
	 *            First source to merge (more important)
	 * @param source2
	 *            Second source to merge (less important)
	 * @return
	 */
	public static MergeAO createMergeAO(ILogicalOperator source1, ILogicalOperator source2) {
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source1);
		sources.add(source2);
		return createMergeAO(sources);
	}

	/**
	 * Creates a Select Operator to Filter for space parameter
	 * 
	 * @param parameter
	 *            according Space param.
	 * @param inMeters
	 *            if you want to use meters instead of the original value TODO Put this into the spaceParameter?
	 * @param source
	 *            Source Operator
	 * @return
	 * @throws MissingDDCEntryException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("rawtypes")
	public static SelectAO createSpaceSelect(SportsQLSpaceParameter parameter, boolean inMeters, ILogicalOperator source)
			throws NumberFormatException, MissingDDCEntryException {

		SpaceUnit unit = parameter.getUnit();
		if (unit == null) {
			unit = SpaceUnit.millimeters;
		}

		double startX = SpaceUnitHelper.getMillimeters(parameter.getStartx(), unit);
		double startY = SpaceUnitHelper.getMillimeters(parameter.getStarty(), unit);
		double endX = SpaceUnitHelper.getMillimeters(parameter.getEndx(), unit);
		double endY = SpaceUnitHelper.getMillimeters(parameter.getEndy(), unit);

		if (parameter.getSpace() != null) {
			Space space = AbstractSportsDDCAccess.getSpace(parameter.getSpace());
			startX = space.getXMin();
			startY = space.getYMin();
			endX = space.getXMax();
			endY = space.getYMax();
		}

		// Predicate we want to produce:
		// x >= startX AND x <= endX AND y>= startY AND y<= startY

		String firstPredicateString = IntermediateSchemaAttributes.X + " >= " + startX;
		String secondPredicateString = IntermediateSchemaAttributes.X + " <= " + endX;
		String thirdPredicateString = IntermediateSchemaAttributes.Y + " >= " + startY;
		String fourthPredicateString = IntermediateSchemaAttributes.Y + " <= " + endY;

		SelectAO selectAO = new SelectAO();

		if (inMeters) {
			// Calculate meters from input stream
			List<SDFExpressionParameter> meterExpressions = new ArrayList<SDFExpressionParameter>();
			SDFExpressionParameter param1 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.ENTITY_ID, source);
			SDFExpressionParameter param2 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.X + " / 1000 - " + AbstractSportsDDCAccess.getFieldXMin() + "/"
							+ CONVERSION_DIVIDEND_POSITION_TO_METERS, ATTRIBUTE_X_METER, source);
			SDFExpressionParameter param3 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.Y + " / 1000 - " + AbstractSportsDDCAccess.getFieldYMin() + "/"
							+ CONVERSION_DIVIDEND_POSITION_TO_METERS, ATTRIBUTE_Y_METER, source);
			SDFExpressionParameter param4 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.X, source);
			SDFExpressionParameter param5 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.Y, source);
			SDFExpressionParameter param6 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.Z, source);
			SDFExpressionParameter param7 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.V, source);
			SDFExpressionParameter param8 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.A, source);
			SDFExpressionParameter param14 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.TS, source);
			SDFExpressionParameter param15 = OperatorBuildHelper.createExpressionParameter(
					IntermediateSchemaAttributes.TEAM_ID, source);

			meterExpressions.add(param1);
			meterExpressions.add(param2);
			meterExpressions.add(param3);
			meterExpressions.add(param4);
			meterExpressions.add(param5);
			meterExpressions.add(param6);
			meterExpressions.add(param7);
			meterExpressions.add(param8);
			meterExpressions.add(param14);
			meterExpressions.add(param15);
			MapAO metersMap = OperatorBuildHelper.createMapAO(meterExpressions, source, 0, 0, false);

			firstPredicateString = ATTRIBUTE_X_METER + " >= "
					+ SpaceUnitHelper.getMeters(startX, SpaceUnit.millimeters);
			secondPredicateString = ATTRIBUTE_X_METER + " <= " + SpaceUnitHelper.getMeters(endX, SpaceUnit.millimeters);
			thirdPredicateString = ATTRIBUTE_Y_METER + " >= "
					+ SpaceUnitHelper.getMeters(startY, SpaceUnit.millimeters);
			fourthPredicateString = ATTRIBUTE_Y_METER + " <= " + SpaceUnitHelper.getMeters(endY, SpaceUnit.millimeters);

			selectAO.subscribeTo(metersMap, metersMap.getOutputSchema());
		}

		// Create Predicates from Strings

		SDFExpression firstPredicateExpression = new SDFExpression(firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(firstPredicateExpression);

		SDFExpression secondPredicateExpression = new SDFExpression(secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(secondPredicateExpression);

		SDFExpression thirdPredicateExpression = new SDFExpression(thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(thirdPredicateExpression);

		SDFExpression fourthPredicateExpression = new SDFExpression(fourthPredicateString, MEP.getInstance());
		RelationalPredicate fourthPredicate = new RelationalPredicate(fourthPredicateExpression);

		IPredicate firstAndPredicate = ComplexPredicateHelper.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate secondAndPredicate = ComplexPredicateHelper.createAndPredicate(thirdPredicate, fourthPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper.createAndPredicate(firstAndPredicate, secondAndPredicate);

		selectAO.setPredicate(fullAndPredicate);

		if (!inMeters) {
			// Subscribe to the original source (not the MapAO for meters)
			selectAO.subscribeTo(source, source.getOutputSchema());
		}

		return selectAO;
	}

	/**
	 * Creates a SelectAO which selects for the given teamId
	 * 
	 * @param teamId
	 *            Of the team you want to get
	 * @param source
	 *            source with team_id
	 * @return SelectAO which filters for the given teamId
	 */
	public static SelectAO createTeamSelectAO(long teamId, ILogicalOperator source) {
		SelectAO teamSelectAO = new SelectAO();

		String predicateString = IntermediateSchemaAttributes.TEAM_ID + " = " + teamId;
		SDFExpression predicateExpression = new SDFExpression(predicateString, MEP.getInstance());

		RelationalPredicate predicate = new RelationalPredicate(predicateExpression);
		teamSelectAO.setPredicate(predicate);

		teamSelectAO.subscribeTo(source, source.getOutputSchema());

		return teamSelectAO;
	}

	/**
	 * Creates a SelectAO which selects both teams
	 * 
	 * @param source
	 *            source with team_id
	 * @return SelectAO which filters for the teams
	 */
	public static SelectAO createBothTeamSelectAO(ILogicalOperator source) {
		SelectAO teamSelectAO = new SelectAO();

		String predicateString = "(" + IntermediateSchemaAttributes.TEAM_ID + " = 1) OR ("
				+ IntermediateSchemaAttributes.TEAM_ID + " = 2)";
		SDFExpression predicateExpression = new SDFExpression(predicateString, MEP.getInstance());

		RelationalPredicate predicate = new RelationalPredicate(predicateExpression);
		teamSelectAO.setPredicate(predicate);

		teamSelectAO.subscribeTo(source, source.getOutputSchema());

		return teamSelectAO;
	}

	/**
	 * Creates a SelectAO with given timeParameter. Automatically build a correct Select for the given timeParameter.
	 * Assumes that the given input stream has "minute" and "second" in the tuples
	 * 
	 * @param timeParameter
	 *            timeParameter with the information which time should be selected
	 * @param source
	 *            Source which should at least contain "minute" and "second"
	 * @return SelectAO which selects just the time-range you configured in the timeParameter.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static SelectAO createTimeSelect(SportsQLTimeParameter timeParameter, ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// minute >= ${parameterTimeStart_minute} AND minute <=
		// ${parameterTimeEnd_minute} AND second >= 0

		int startMinute = Integer.MIN_VALUE;
		int endMinute = Integer.MAX_VALUE;

		if (timeParameter.getTime() != null
				&& timeParameter.getTime().equals(SportsQLTimeParameter.TIME_PARAMETER_ALWAYS)) {
			startMinute = Integer.MIN_VALUE;
			endMinute = Integer.MAX_VALUE;
		} else if (timeParameter.getTime() != null
				&& timeParameter.getTime().equals(SportsQLTimeParameter.TIME_PARAMTER_GAME)) {
			// Just the time in the game
			startMinute = START_MINUTE;
			endMinute = END_MINUTE;
		} else if (timeParameter.getTime() != null
				&& timeParameter.getTime().equals(SportsQLTimeParameter.TIME_PARAMETER_NOW)) {
			// TODO: Do the right thing if timeParameter says "now"

		} else if (timeParameter.getStart() != null && timeParameter.getEnd() != null
				&& timeParameter.getUnit() != null) {
			startMinute = TimeUnitHelper.getMinutes(timeParameter.getStart(), timeParameter.getUnit());
			endMinute = TimeUnitHelper.getMinutes(timeParameter.getEnd(), timeParameter.getUnit());
		}

		// 1. minute >= ${parameterTimeStart_minute}
		String firstPredicateString = ATTRIBUTE_MINUTE + " >= " + startMinute;
		SDFExpression firstPredicateExpression = new SDFExpression(firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(firstPredicateExpression);

		// 2. minute <= ${parameterTimeEnd_minute}
		String secondPredicateString = ATTRIBUTE_MINUTE + " <= " + endMinute;
		SDFExpression secondPredicateExpression = new SDFExpression(secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(secondPredicateExpression);

		// 3. second >= 0
		String thirdPredicateString = ATTRIBUTE_SECOND + " >= 0";
		SDFExpression thirdPredicateExpression = new SDFExpression(thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(thirdPredicateExpression);

		IPredicate firstAndPrdicate = ComplexPredicateHelper.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper.createAndPredicate(firstAndPrdicate, thirdPredicate);

		// String fullPredicateString = firstPredicateString; // + " AND " +
		// secondPredicateString + " AND " + thirdPredicateString;
		// SDFExpression fullExpression = new SDFExpression(fullPredicateString,
		// MEP.getInstance());
		// IPredicate fullPredicate = new RelationalPredicate(fullExpression);

		selectAO.setPredicate(fullAndPredicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	public static TimestampAO clearEndTimestamp(ILogicalOperator source) {
		TimestampAO timeStampAO = new TimestampAO();
		timeStampAO.setClearEnd(true);

		timeStampAO.subscribeTo(source, source.getOutputSchema());

		return timeStampAO;
	}

	/**
	 * Creates a SelectAO with given timeParameter. Automatically build a correct Select for the given timeParameter.
	 * Creates a map with minutes and seconds.
	 * 
	 * @param timeParameter
	 *            timeParameter with the information which time should be selected
	 * @param source
	 *            Source which should at least contain "minute" and "second"
	 * @return SelectAO which selects just the time-range you configured in the timeParameter.
	 */
	public static SelectAO createTimeMapAndSelect(SportsQLTimeParameter timeParameter, ILogicalOperator source)
			throws MissingDDCEntryException {
		return createTimeSelect(timeParameter, createTimeMap(source));

	}

	public static MapAO createTimeMap(ILogicalOperator source) throws MissingDDCEntryException {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter ex1 = OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.ENTITY_ID, source);

		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter(
						"DoubleToInteger(ts/"
								+ TimeUnitHelper
										.getBTUtoMinutesFactor(de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit
												.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))
								+ ")", ATTRIBUTE_MINUTE, source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter(
						"DoubleToInteger((ts/"
								+ TimeUnitHelper
										.getBTUtoSecondsFactor(de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit
												.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))
								+ ") % 60)", ATTRIBUTE_SECOND, source);

		SDFExpressionParameter ex5 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.X,
				source);
		SDFExpressionParameter ex6 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Y,
				source);
		SDFExpressionParameter ex7 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Z,
				source);
		SDFExpressionParameter ex8 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.V,
				source);
		SDFExpressionParameter ex9 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.A,
				source);
		SDFExpressionParameter ex10 = OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TS,
				source);
		SDFExpressionParameter ex11 = OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.TEAM_ID, source);

		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
		expressions.add(ex5);
		expressions.add(ex6);
		expressions.add(ex7);
		expressions.add(ex8);
		expressions.add(ex9);
		expressions.add(ex10);
		expressions.add(ex11);

		MapAO firstMap = OperatorBuildHelper.createMapAO(expressions, source, 0, 0, false);

		firstMap.initialize();

		return firstMap;
	}

	/**
	 * 
	 * @param sampleRate
	 *            sampleRate determines which n'th tuples are processed.
	 * @param source
	 *            Source Operator to Link to.
	 * @return
	 */
	public static SampleAO createSampleAO(int sampleRate, ILogicalOperator source) {
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
	 *            Stream with probably limited metadata to enrich the other stream. This will be on port 1 (input and
	 *            output)
	 * @return
	 */
	public static EnrichAO createEnrichAO(String joinPredicate, ILogicalOperator streamToEnrich,
			ILogicalOperator metaStream) {
		EnrichAO enrichAO = new EnrichAO();

		// We need this to get the AttributeResolver
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(streamToEnrich);
		sources.add(metaStream);

		// Create parameter
		PredicateParameter predicateParameter = new PredicateParameter();
		predicateParameter.setAttributeResolver(OperatorBuildHelper.createAttributeResolver(sources));

		predicateParameter.setInputValue(joinPredicate);
		enrichAO.setPredicate(predicateParameter.getValue());
		enrichAO.subscribeToSource(metaStream, 0, 0, metaStream.getOutputSchema());
		enrichAO.subscribeToSource(streamToEnrich, 1, 0, streamToEnrich.getOutputSchema());

		return enrichAO;
	}

	/**
	 * Creates an AggregateAO with standard output-type as double (or what Odysseus use as standard the time you use
	 * this function)
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @return
	 */
	public static AggregateAO createAggregateAO(String aggregationFunction, String inputAttributeName,
			String outputAttributeName, ILogicalOperator source) {
		return createAggregateAO(aggregationFunction, "", inputAttributeName, outputAttributeName, null, source);
	}

	/**
	 * Creates an AggregateAO with the specified output type
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param groupBy
	 *            The name of the attribute you want to group by
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @param outputType
	 *            The optional type of output (null, if you don't want to specify, should then be double)
	 * @param source
	 *            The operator before this one
	 * @return
	 */
	public static AggregateAO createAggregateAO(String aggregationFunction, String groupBy, String inputAttributeName,
			String outputAttributeName, String outputType, ILogicalOperator source) {
		List<String> groupingAttributes = new ArrayList<String>();
		groupingAttributes.add(groupBy);
		return createAggregateAO(aggregationFunction, groupingAttributes, inputAttributeName, outputAttributeName,
				outputType, source, -1);
	}

	/**
	 * Creates an AggregateAO with the specified output type
	 * 
	 * @param aggregationFunction
	 *            The name of the aggregate-function, e.g. "SUM" or "MAX"
	 * @param groupBy
	 *            The list of names of the attributes you want to group by
	 * @param inputAttributeName
	 *            The input attribute over which the aggregation should be done
	 * @param outputAttributeName
	 *            The name of the output attribute for this aggregation
	 * @param outputType
	 *            The optional type of output (null, if you don't want to specify, should then be double)
	 * @param source
	 *            The operator before this one
	 * @return
	 */
	public static AggregateAO createAggregateAO(String aggregationFunction, List<String> groupBy,
			String inputAttributeName, String outputAttributeName, String outputType, ILogicalOperator source,
			int dumpAtValueCount) {

		List<String> aggregationFunctions = new ArrayList<String>();
		aggregationFunctions.add(aggregationFunction);
		List<String> inputAttributeNames = new ArrayList<String>();
		inputAttributeNames.add(inputAttributeName);
		List<String> outputAttributeNames = new ArrayList<String>();
		outputAttributeNames.add(outputAttributeName);
		List<String> outputTypes = new ArrayList<String>();
		outputTypes.add(outputType);
		return createAggregateAO(aggregationFunctions, groupBy, inputAttributeNames, outputAttributeNames, outputTypes,
				source, dumpAtValueCount);
	}

	/**
	 * Creates an AggregateAO with the specified output type
	 * 
	 * @param aggregationFunctions
	 *            List of aggregate-functions, e.g. "SUM" or "MAX"
	 * @param groupBy
	 *            The list of names of the attributes you want to group by
	 * @param inputAttributeName
	 *            The list of input attributes over which the aggregation should be done
	 * @param outputAttributeName
	 *            The list of names of the output attribute for this aggregation
	 * @param outputType
	 *            List of optional types of output (null, if you don't want to specify, should then be double)
	 * @param source
	 *            The operator before this one
	 * @return
	 */
	public static AggregateAO createAggregateAO(List<String> aggregationFunctions, List<String> groupBys,
			List<String> inputAttributeNames, List<String> outputAttributeNames, List<String> outputTypes,
			ILogicalOperator source, int dumpAtValueCount) {
		AggregateAO aggregateAO = new AggregateAO();

		if (dumpAtValueCount != -1) {
			aggregateAO.setDumpAtValueCount(dumpAtValueCount);
		}

		List<AggregateItem> aggregateItems = new ArrayList<AggregateItem>();

		for (int i = 0; i < aggregationFunctions.size(); i++) {
			// Fill parameter
			AggregateItemParameter param = new AggregateItemParameter();
			List<String> aggregateOptions = new ArrayList<String>();
			aggregateOptions.add(aggregationFunctions.get(i));
			aggregateOptions.add(inputAttributeNames.get(i));
			aggregateOptions.add(outputAttributeNames.get(i));
			if (outputTypes != null && outputTypes.get(i) != null)
				aggregateOptions.add(outputTypes.get(i));
			param.setInputValue(aggregateOptions);

			// Attribute resolver and datadictionary for parameter
			IAttributeResolver resolver = OperatorBuildHelper.createAttributeResolver(source);
			param.setAttributeResolver(resolver);
			IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();
			param.setDataDictionary(dataDict);

			aggregateItems.add(param.getValue());
		}

		// GroupBy
		if (groupBys != null) {
			if (!(groupBys.size() == 1 && groupBys.get(0).isEmpty())) {
				// If the inly grouping-attribute is empty, the user does not
				// want to group
				aggregateAO.setGroupingAttributes(createAttributeList(groupBys, source));
			}
		}

		aggregateAO.setAggregationItems(aggregateItems);

		aggregateAO.subscribeTo(source, source.getOutputSchema());

		return aggregateAO;
	}

	public static CoalesceAO createCoalesceAO(List<String> attributes, String aggregationFunction,
			String inputAttributeName, String outputAttributeName, ILogicalOperator source) {
		CoalesceAO coalesceAO = new CoalesceAO();

		// Grouping attributes
		List<ResolvedSDFAttributeParameter> params = new ArrayList<ResolvedSDFAttributeParameter>();
		for (String attribute : attributes) {
			ResolvedSDFAttributeParameter param = new ResolvedSDFAttributeParameter();
			param.setAttributeResolver(OperatorBuildHelper.createAttributeResolver(source));
			param.setInputValue(attribute);
			params.add(param);
		}

		List<SDFAttribute> finishedParams = new ArrayList<SDFAttribute>();
		for (ResolvedSDFAttributeParameter resAttrParam : params) {
			finishedParams.add(resAttrParam.getValue());
		}
		coalesceAO.setGroupingAttributes(finishedParams);

		// Aggregations

		// 1. Fill parameter
		AggregateItemParameter param = new AggregateItemParameter();
		List<String> aggregateOptions = new ArrayList<String>();
		aggregateOptions.add(aggregationFunction);
		aggregateOptions.add(inputAttributeName);
		aggregateOptions.add(outputAttributeName);
		param.setInputValue(aggregateOptions);

		// 2. Attribute resolver and datadictionary for parameter
		IAttributeResolver resolver = OperatorBuildHelper.createAttributeResolver(source);
		param.setAttributeResolver(resolver);
		IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();
		param.setDataDictionary(dataDict);

		// 3. Use parameter to get information for AO
		List<AggregateItem> aggregateItems = new ArrayList<AggregateItem>();
		aggregateItems.add(param.getValue());
		coalesceAO.setAggregationItems(aggregateItems);

		// Subscribe to source
		coalesceAO.subscribeTo(source, source.getOutputSchema());

		return coalesceAO;
	}

	public static CoalesceAO createCoalesceAO(List<String> attributes, List<String> aggregationFunctions,
			List<String> inputAttributeNames, List<String> outputAttributeNames, IPredicate<?> startPredicate,
			IPredicate<?> endPredicate, ILogicalOperator source) {
		CoalesceAO coalesceAO = new CoalesceAO();

		// Grouping attributes
		List<ResolvedSDFAttributeParameter> params = new ArrayList<ResolvedSDFAttributeParameter>();
		for (String attribute : attributes) {
			ResolvedSDFAttributeParameter param = new ResolvedSDFAttributeParameter();
			param.setAttributeResolver(OperatorBuildHelper.createAttributeResolver(source));
			param.setInputValue(attribute);
			params.add(param);
		}

		List<SDFAttribute> finishedParams = new ArrayList<SDFAttribute>();
		for (ResolvedSDFAttributeParameter resAttrParam : params) {
			finishedParams.add(resAttrParam.getValue());
		}
		coalesceAO.setGroupingAttributes(finishedParams);

		// Aggregations
		List<AggregateItem> aggregateItems = new ArrayList<AggregateItem>();

		for (int i = 0; i < aggregationFunctions.size(); i++) {
			AggregateItemParameter param = new AggregateItemParameter();
			List<String> aggregateOptions = new ArrayList<String>();
			aggregateOptions.add(aggregationFunctions.get(i));
			aggregateOptions.add(inputAttributeNames.get(i));
			aggregateOptions.add(outputAttributeNames.get(i));
			param.setInputValue(aggregateOptions);

			IAttributeResolver resolver = OperatorBuildHelper.createAttributeResolver(source);
			param.setAttributeResolver(resolver);
			IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();
			param.setDataDictionary(dataDict);

			aggregateItems.add(param.getValue());
		}

		coalesceAO.setAggregationItems(aggregateItems);

		coalesceAO.setStartPredicate(startPredicate);
		coalesceAO.setEndPredicate(endPredicate);

		coalesceAO.subscribeTo(source, source.getOutputSchema());

		return coalesceAO;
	}

	/**
	 * 
	 * @param method
	 *            Method to use (MIN, MAX, LAST, FIRST)
	 * @param attribute
	 *            Attribute on which the method is evaluated
	 * @param source
	 * @return
	 */
	public static TupleAggregateAO createTupleAggregateAO(String method, String attribute, ILogicalOperator source) {
		TupleAggregateAO tupleAggregateAO = new TupleAggregateAO();

		// Method
		StringParameter stringParam = new StringParameter();
		stringParam.setInputValue(method);
		tupleAggregateAO.setMethod(stringParam.getValue());

		// Attribute
		ResolvedSDFAttributeParameter attributeParam = new ResolvedSDFAttributeParameter();
		attributeParam.setAttributeResolver(OperatorBuildHelper.createAttributeResolver(source));
		attributeParam.setInputValue(attribute);
		tupleAggregateAO.setAttribute(attributeParam.getValue());

		// Source
		tupleAggregateAO.subscribeToSource(source, 0, 0, source.getOutputSchema());

		return tupleAggregateAO;
	}

	/**
	 * Returns routeAO with a list of predicates
	 * 
	 * @param listOfPredicates
	 * @return
	 */
	public static RouteAO createRouteAO(List<String> listOfPredicates, ILogicalOperator source) {
		List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();
		// Add predicates to the routeAO operator
		for (String predicate : listOfPredicates) {
			SDFExpression predicateExpression = new SDFExpression(predicate, MEP.getInstance());
			RelationalPredicate p = new RelationalPredicate(predicateExpression);
			predicates.add(p);
		}
		return createRoutePredicatesAO(predicates, source);
	}

	/**
	 * Returns routeAO with a list of predicates
	 * 
	 * @param predicates
	 * @param source
	 * @return
	 */
	public static RouteAO createRoutePredicatesAO(List<IPredicate<?>> predicates, ILogicalOperator source) {
		RouteAO rAO = new RouteAO();
		rAO.setPredicates(predicates);
		rAO.subscribeTo(source, source.getOutputSchema());
		return rAO;
	}

	/**
	 * Returns changeDetectAO with list of Attributes, groupBy, relative tolerance and absolute tolerance
	 * 
	 * @param attributes
	 * @param groupBy
	 * @param relativeTolerance
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<String> attributes, List<SDFAttribute> groupBy,
			boolean relativeTolerance, double tolerance, ILogicalOperator source, int heartbeatRate) {

		ArrayList<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source);

		List<SDFAttribute> sdfAttributes = OperatorBuildHelper.createAttributeList(attributes, sources);
		ChangeDetectAO cAO = new ChangeDetectAO();
		cAO.setAttr(sdfAttributes);
		cAO.setGroupingAttributes(groupBy);
		cAO.setRelativeTolerance(relativeTolerance);
		cAO.setTolerance(tolerance);

		if (heartbeatRate != -1) {
			cAO.setHeartbeatRate(heartbeatRate);
		}

		// cAO.subscribeToSource(source, 0, 1, inputSchema);
		cAO.subscribeTo(source, source.getOutputSchema());

		return cAO;
	}

	/**
	 * Returns changeDetectAO with list of Attributes, relative tolerance and absolute tolerance
	 * 
	 * @param attributes
	 * @param relativeTolerance
	 * @param tolerance
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, boolean relativeTolerance,
			double tolerance, ILogicalOperator source) {
		ChangeDetectAO changeDetectAO = new ChangeDetectAO();
		changeDetectAO.setAttr(attributes);
		changeDetectAO.setRelativeTolerance(relativeTolerance);
		changeDetectAO.setTolerance(tolerance);
		changeDetectAO.subscribeTo(source, source.getOutputSchema());
		return changeDetectAO;
	}

	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute tolerance with deliverFirstElement set to false
	 * 
	 * @param attributes
	 *            List of Attributes where changes should occur
	 * @param tolerance
	 *            (Absolute) Tolerance applied to changeDetection.
	 * @param source
	 *            Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, double tolerance,
			ILogicalOperator source) {
		return createChangeDetectAO(attributes, tolerance, false, 0, source);
	}

	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute tolerance with deliverFirstElement set to false
	 * 
	 * @param attributes
	 *            List of Attributes where changes should occur
	 * @param tolerance
	 *            (Absolute) Tolerance applied to changeDetection.
	 * @param heartbeatRate
	 *            Heartbeatrate every x tupel send a heartbeat
	 * @param source
	 *            Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, double tolerance,
			int heartbeatRate, ILogicalOperator source) {
		return createChangeDetectAO(attributes, tolerance, false, heartbeatRate, source);
	}

	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute tolerance
	 * 
	 * @param attributes
	 *            List of Attributes where changes should occur
	 * @param tolerance
	 *            (Absolute) Tolerance applied to changeDetection.
	 * @param deliverFirstElement
	 *            If you want to deliver the first element
	 * @param source
	 *            Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, double tolerance,
			boolean deliverFirstElement, ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();

		cAO.setAttr(attributes);
		cAO.setTolerance(tolerance);
		cAO.setDeliverFirstElement(deliverFirstElement);
		cAO.subscribeTo(source, source.getOutputSchema());
		return cAO;
	}

	/**
	 * Returns changeDetectAO with a list of Attributes and an absolute tolerance
	 * 
	 * @param attributes
	 *            List of Attributes where changes should occur
	 * @param tolerance
	 *            (Absolute) Tolerance applied to changeDetection.
	 * @param deliverFirstElement
	 *            If you want to deliver the first element
	 * @param groupBy
	 *            List of Attributes to group by
	 * @param source
	 *            Source to link to.
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, double tolerance,
			boolean deliverFirstElement, List<SDFAttribute> groupBy, ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();

		cAO.setAttr(attributes);
		cAO.setTolerance(tolerance);
		cAO.setDeliverFirstElement(deliverFirstElement);
		cAO.subscribeTo(source, source.getOutputSchema());
		cAO.setGroupingAttributes(groupBy);
		return cAO;
	}

	/**
	 * 
	 * @param attributes
	 * @param tolerance
	 * @param deliverFirstElement
	 * @param heartbeatRate
	 * @param source
	 * @return
	 */
	public static ChangeDetectAO createChangeDetectAO(List<SDFAttribute> attributes, double tolerance,
			boolean deliverFirstElement, int heartbeatRate, ILogicalOperator source) {
		ChangeDetectAO cAO = new ChangeDetectAO();

		cAO.setAttr(attributes);
		cAO.setTolerance(tolerance);
		cAO.setDeliverFirstElement(deliverFirstElement);
		cAO.setHeartbeatRate(heartbeatRate);
		cAO.subscribeTo(source, source.getOutputSchema());
		return cAO;
	}

	public static SelectAO createSelectAO(String predicate, ILogicalOperator source) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(predicate);
		return createSelectAO(predicates, source);
	}

	/**
	 * Returns selectAO with a list of predicates
	 * 
	 * @param listOfPredicates
	 * @param source
	 * @return
	 */
	public static SelectAO createSelectAO(List<String> listOfPredicates, ILogicalOperator source) {
		SelectAO sAO = new SelectAO();
		for (String predicateString : listOfPredicates) {
			PredicateParameter param = new PredicateParameter();
			param.setAttributeResolver(createAttributeResolver(source));
			param.setInputValue(predicateString);
			sAO.setPredicate(param.getValue());
		}
		sAO.subscribeTo(source, source.getOutputSchema());
		return sAO;
	}

	/**
	 * If you create a IPredictate by yourself, e.g. cause it's very special, you can use this method to create a
	 * SelectAO with this predicate
	 * 
	 * @param predicate
	 *            e.g. made with createAndPredicate
	 * @param source
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static SelectAO createSelectAO(IPredicate predicate, ILogicalOperator source) {
		SelectAO selectAO = new SelectAO();
		selectAO.setPredicate(predicate);
		selectAO.subscribeTo(source, source.getOutputSchema());
		return selectAO;
	}

	/**
	 * Creates and returns a Project AO
	 * 
	 * @param attributes
	 * @param source
	 * @return
	 */
	public static ProjectAO createProjectAO(List<String> attributes, ILogicalOperator source) {
		ProjectAO projectAo = new ProjectAO();

		List<SDFAttribute> sdfAttributes = OperatorBuildHelper.createAttributeList(attributes, source);

		projectAo.subscribeTo(source, source.getOutputSchema());
		projectAo.setOutputSchemaWithList(sdfAttributes);

		return projectAo;
	}

	/**
	 * Creates a heatmap with standard settings
	 * @param source
	 * @return
	 */
	public static SportsHeatMapAO createHeatMapAO(ILogicalOperator source) {
		SportsHeatMapAO heatmap = new SportsHeatMapAO();
		heatmap.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return heatmap;
	}

	/**
	 * Creates a heatmap with custom settings
	 * @param source 
	 * @param reduceLoadFactor The factor by which the load is reduced. If "1000", the map is only send every 1000 tuples of input
	 * @param numTilesHorizontal The number of tiles used horizontal
	 * @param numTilesVertical The number of tiles used vertical
	 * @return
	 */
	public static SportsHeatMapAO createHeatMapAO(ILogicalOperator source, int reduceLoadFactor,
			int numTilesHorizontal, int numTilesVertical) {
		SportsHeatMapAO heatmap = new SportsHeatMapAO(reduceLoadFactor, numTilesHorizontal, numTilesVertical);
		heatmap.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return heatmap;
	}

	/**
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
	public static ElementWindowAO createElementWindowAO(int size, int advance, ILogicalOperator source) {
		ElementWindowAO windowAO = new ElementWindowAO();
		// null cause timeUnit is deprecated
		windowAO.setBaseTimeUnit(TimeUnit.MILLISECONDS);
		TimeValueItem windowSize = new TimeValueItem(size, TimeUnit.MILLISECONDS);
		TimeValueItem windowAdvance = new TimeValueItem(advance, TimeUnit.MILLISECONDS);
		windowAO.setWindowSize(windowSize);
		windowAO.setWindowAdvance(windowAdvance);
		windowAO.subscribeTo(source, source.getOutputSchema());
		return windowAO;
	}

	/**
	 * Creates a TimeWindow.
	 * 
	 * @param size
	 *            How big the window is. Number of seconds, minutes, etc. (depends on the timeUnit) in this Window
	 * @param timeUnit
	 *            e.g. "second" or "minute"
	 * @param source
	 *            Source operator
	 * @return
	 */
	public static TimeWindowAO createTimeWindowAO(long size, String timeUnit, ILogicalOperator source) {
		// TODO timeUnit with static finals
		TimeWindowAO timeWindowAO = new TimeWindowAO();
		TimeParameter timeParamenter = new TimeParameter();
		List<String> parameterInputValue = new ArrayList<String>();
		parameterInputValue.add(String.valueOf(size));
		parameterInputValue.add(timeUnit);
		timeParamenter.setInputValue(parameterInputValue);
		timeWindowAO.setWindowSize(timeParamenter.getValue());

		timeWindowAO.subscribeTo(source, source.getOutputSchema());

		return timeWindowAO;
	}

	/**
	 * Creates a TimeWindow.
	 * 
	 * @param size
	 *            How big the window is. Number of seconds, minutes, etc. (depends on the timeUnit) in this Window
	 * @param slide
	 *            slide of the Window
	 * @param timeUnit
	 *            e.g. "second" or "minute"
	 * @param source
	 *            Source operator
	 * @return
	 */
	public static TimeWindowAO createTimeWindowAO(long size, long slide, String timeUnit, ILogicalOperator source) {
		// TODO timeUnit with static finals
		TimeWindowAO timeWindowAO = new TimeWindowAO();
		TimeParameter timeParamenter = new TimeParameter();
		List<String> parameterInputValue = new ArrayList<String>();
		parameterInputValue.add(String.valueOf(size));
		parameterInputValue.add(timeUnit);
		timeParamenter.setInputValue(parameterInputValue);
		timeWindowAO.setWindowSize(timeParamenter.getValue());

		TimeParameter timeParamenter2 = new TimeParameter();
		List<String> parameterInputValue2 = new ArrayList<String>();
		parameterInputValue2.add(String.valueOf(slide));
		parameterInputValue2.add(timeUnit);
		timeParamenter2.setInputValue(parameterInputValue2);
		timeWindowAO.setWindowSlide(timeParamenter2.getValue());

		timeWindowAO.subscribeTo(source, source.getOutputSchema());

		return timeWindowAO;
	}

	public static TimeWindowAO createTimeWindowAOWithAdvance(long size, long advance, String timeUnit,
			ILogicalOperator source) {
		// TODO timeUnit with static finals
		TimeWindowAO timeWindowAO = new TimeWindowAO();
		TimeParameter timeParamenter = new TimeParameter();
		List<String> parameterInputValue = new ArrayList<String>();
		parameterInputValue.add(String.valueOf(size));
		parameterInputValue.add(timeUnit);
		timeParamenter.setInputValue(parameterInputValue);
		timeWindowAO.setWindowSize(timeParamenter.getValue());

		TimeParameter timeParamenter2 = new TimeParameter();
		List<String> parameterInputValue2 = new ArrayList<String>();
		parameterInputValue2.add(String.valueOf(advance));
		parameterInputValue2.add(timeUnit);
		timeParamenter2.setInputValue(parameterInputValue2);
		timeWindowAO.setWindowAdvance(timeParamenter2.getValue());

		timeWindowAO.subscribeTo(source, source.getOutputSchema());

		return timeWindowAO;
	}

	/**
	 * Creates a PredicateWindowAO
	 * 
	 * @param startCondition
	 *            When to start, e.g. "sameShotTS = 1"
	 * @param endCondition
	 *            When to stop, e.g. "sameShotTS = 100"
	 * @param sameStartTime
	 *            For predicate windows: If set to true, all produced elements get the same start timestamp
	 * @param size
	 *            The size of your window, e.g. 1000; Type -1 if you don't want to set this
	 * @param sizeUnit
	 *            The unit you want to measure your size in, e.g. "Milliseconds". default time is the base time of the
	 *            stream (typically milliseconds)
	 * @return
	 */
	public static PredicateWindowAO createPredicateWindowAO(String startCondition, String endCondition,
			boolean sameStartTime, long size, String sizeUnit, ILogicalOperator source) {
		PredicateWindowAO predicateWindowAO = new PredicateWindowAO();

		// Start
		if (startCondition != null) {
			PredicateParameter startParam = new PredicateParameter();
			startParam.setAttributeResolver(createAttributeResolver(source));
			startParam.setInputValue(startCondition);
			predicateWindowAO.setStartCondition(startParam.getValue());
		}

		// Stop
		if (endCondition != null) {
			PredicateParameter endParam = new PredicateParameter();
			endParam.setAttributeResolver(createAttributeResolver(source));
			endParam.setInputValue(endCondition);
			predicateWindowAO.setEndCondition(endParam.getValue());
		}

		// Same start time
		BooleanParameter sameParam = new BooleanParameter();
		sameParam.setAttributeResolver(createAttributeResolver(source));
		sameParam.setInputValue(sameStartTime);
		predicateWindowAO.setSameStarttime(sameParam.getValue());

		// Size
		if (size > -1) {
			TimeParameter timeParam = new TimeParameter();
			if (sizeUnit != null) {
				// Use time unit -> we need to use a List
				List<String> timeUnitList = new ArrayList<String>();
				timeUnitList.add(Long.toString(size));
				timeUnitList.add(sizeUnit);
				timeParam.setInputValue(timeUnitList);
			} else {
				// Don't use time unit -> use single long
				timeParam.setInputValue(size);
			}
			predicateWindowAO.setWindowSize(timeParam.getValue());
		}

		predicateWindowAO.subscribeTo(source, source.getOutputSchema());

		return predicateWindowAO;
	}

	/**
	 * Deprecated. Use method for TimeWindowAO, ... Returns windowAO.
	 * 
	 * @param windowSize
	 * @param windowType
	 * @param windowAdvance
	 * @param source
	 * @return
	 */
	@Deprecated
	public static WindowAO createWindowAO(TimeValueItem windowSize, WindowType windowType, TimeValueItem windowAdvance,
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
	public static JoinAO createJoinAO(List<String> listOfPredicates, ILogicalOperator source1, ILogicalOperator source2) {
		return OperatorBuildHelper.createJoinAO(listOfPredicates, null, source1, source2);
	}

	/**
	 * Creates a JoinAO
	 * 
	 * @param predicate
	 *            The predicates, e.g. "x = y"
	 * @param card
	 *            Cardinality, e.g. ONE_ONE or ONE_MANY
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static JoinAO createJoinAO(String predicate, String card, ILogicalOperator source1, ILogicalOperator source2) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(predicate);
		return OperatorBuildHelper.createJoinAO(predicates, card, source1, source2);
	}

	/**
	 * Creates a JoinAO
	 * 
	 * @param predicates
	 *            The predicates, e.g. "x = y"
	 * @param card
	 *            Cardinality, e.g. ONE_ONE or ONE_MANY
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static JoinAO createJoinAO(List<String> predicates, String card, ILogicalOperator source1,
			ILogicalOperator source2) {
		JoinAO joinAO = new JoinAO();
		ArrayList<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source1);
		sources.add(source2);

		// Predicates
		IAttributeResolver resolver = OperatorBuildHelper.createAttributeResolver(sources);
		for (String predicate : predicates) {
			PredicateParameter param = new PredicateParameter();
			param.setAttributeResolver(resolver);
			param.setInputValue(predicate);
			joinAO.setPredicate(param.getValue());
		}
		joinAO.subscribeToSource(source1, 0, 0, source1.getOutputSchema());
		joinAO.subscribeToSource(source2, 1, 0, source2.getOutputSchema());

		// Cardinality
		if (card != null) {
			EnumParameter cardParam = new EnumParameter();
			cardParam.setEnum(Cardinalities.class);
			cardParam.setInputValue(card);
			joinAO.setCard((Cardinalities) cardParam.getValue());
		}

		joinAO.subscribeToSource(source1, 0, 0, source1.getOutputSchema());
		joinAO.subscribeToSource(source2, 1, 0, source2.getOutputSchema());

		return joinAO;
	}

	/**
	 * You can use this, if you want to create a JoinAO with a predicate made by yourself
	 * 
	 * @param predicate
	 *            If you build a predicate by yourself, e.g. with createRelationalPredicate
	 * @param card
	 *            e.g. ONE_MANY, ONE_ONE, ...
	 * @param source1
	 * @param source2
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JoinAO createJoinAO(IPredicate predicate, String card, ILogicalOperator source1,
			ILogicalOperator source2) {
		JoinAO joinAO = new JoinAO();
		joinAO.setPredicate(predicate);

		// Cardinality
		EnumParameter cardParam = new EnumParameter();
		cardParam.setEnum(Cardinalities.class);
		cardParam.setInputValue(card);
		joinAO.setCard((Cardinalities) cardParam.getValue());

		joinAO.subscribeToSource(source1, 0, 0, source1.getOutputSchema());
		joinAO.subscribeToSource(source2, 1, 0, source2.getOutputSchema());

		return joinAO;
	}

	/**
	 * Creates a renameAO with which you can rename attributes
	 * 
	 * @param aliases
	 *            The list new attribute names to use from now on
	 * @param pairs
	 *            If the flag pairs is set, aliases will be interpreted as pairs of (old_name, new_name)
	 * @param source
	 * @return
	 */
	public static RenameAO createRenameAO(List<String> aliases, boolean pairs, ILogicalOperator source) {
		RenameAO renameAO = new RenameAO();

		renameAO.setAliases(aliases);
		renameAO.setPairs(pairs);
		renameAO.subscribeTo(source, source.getOutputSchema());

		// We have to initialize this cause if we don't do it, we will get
		// endless recursion when we want to get the outputSchema
		renameAO.initialize();

		return renameAO;
	}

	/**
	 * Method to get AccessAO. DO NOT USE THIS DO ACCESS SOURCES DIRECTLY. Use StreamAO instead.
	 * 
	 * @param sourcename
	 *            Name of source
	 * @return
	 */
	public static AccessAO createAccessAO(ISession session, String sourcename) {
		// We need the dataDictionary for this parameter
		IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();

		SourceParameter source = new SourceParameter();
		source.setName("Source");
		source.setInputValue(sourcename);
		source.setDataDictionary(dataDict);

		source.setCaller(session);

		return source.getValue();
	}

	/**
	 * Creates a StreamAO. You'll need this to access sources.
	 * 
	 * @param sourceName
	 * @return
	 */
	public static ILogicalOperator getLogicalOperatorsFromSource(ISession session, String sourceName) {
		IDataDictionary dataDict = OperatorBuildHelper.getDataDictionary();
		ILogicalOperator stream = dataDict.getStreamForTransformation(sourceName, session);
		
		if( stream == null ) {
			throw new RuntimeException("Could not find stream " + sourceName + "!");
		}
		
		ILogicalOperator streamCopy = copyLogicalPlan(stream);

		return streamCopy;
	}

	private static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		Preconditions.checkNotNull(originPlan, "Logical plan to copy must not be null!");

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	/**
	 * Creates a StreamAO for the game stream
	 * 
	 * @return
	 */
	public static ILogicalOperator createGameSource(ISession session) {
		String gameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		return getLogicalOperatorsFromSource(session, gameSourceName);
	}

	/**
	 * Creates a StreamAO for the metadata stream
	 * 
	 * @return
	 */
	public static ILogicalOperator createMetadataSource(ISession session) {
		String gameSourceName = OperatorBuildHelper.METADATA_STREAM_NAME;
		return getLogicalOperatorsFromSource(session, gameSourceName);
	}

	/**
	 * Creates a simple TopAO which indicates the top node in the query (maybe necessary for Odysseus: This could /
	 * should be the operator you return in the plan.)
	 * 
	 * @param source
	 *            The top logical operator in your query is the source of this operator. You can imagine that you just
	 *            put this as a hat on top of your finished plan.
	 * @return A simple TopAO.
	 */
	public static TopAO createTopAO(ILogicalOperator source) {
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(source);
		return OperatorBuildHelper.createTopAO(sources);
	}

	/**
	 * If you have more than one Operator at the end of your query you can use this. It will put the TopAO on top of all
	 * the operators, with the input-port beginning with 0 and increasing up to the number of the operators -1.
	 * 
	 * Assumes that all output-ports of the sources are 0.
	 * 
	 * @param sources
	 *            List of sources you want to be under the TopAO.
	 * @return A TopAO which has all the given operators under it.
	 */
	public static TopAO createTopAO(List<ILogicalOperator> sources) {
		TopAO topAO = new TopAO();
		for (int i = 0; i < sources.size(); i++) {
			topAO.subscribeToSource(sources.get(i), i, 0, sources.get(i).getOutputSchema());
		}
		return topAO;
	}

	/**
	 * Creates an expressionParameter with a name which you can use to create some AOs, especially MapAOs.
	 * 
	 * @param expression
	 *            Expression as a String as you would type it in PQL (e.g. just "x" or more complex things like
	 *            "toDate(ts/100000)")
	 * @param name
	 *            The name the calculated value from this expression should have (e.g., "minutes"). This name will
	 *            appear in the OutputSchema of the operator you put the expression in
	 * @return An expression which can be used in various AOs, especially MapAOs
	 */
	public static SDFExpressionParameter createExpressionParameter(String expression, String name,
			ILogicalOperator source) {

		IAttributeResolver attributeResolver = OperatorBuildHelper.createAttributeResolver(source);

		SDFExpressionParameter param = new SDFExpressionParameter();
		List<String> paramValue = new ArrayList<String>();
		paramValue.add(expression);
		paramValue.add(name);
		param.setInputValue(paramValue);
		param.setAttributeResolver(attributeResolver);

		return param;
	}

	/**
	 * Creates an expressionParameter without a name (the expression will be the name) which you can use to create some
	 * AOs, especially MapAOs.
	 * 
	 * @param expression
	 *            Expression as a String as you would type it in PQL (e.g. just "x" or more complex things like
	 *            "toDate(ts/100000)")
	 * @return An expression which can be used in various AOs, especially MapAOs
	 */
	public static SDFExpressionParameter createExpressionParameter(String expression, ILogicalOperator source) {
		IAttributeResolver attributeResolver = OperatorBuildHelper.createAttributeResolver(source);
		SDFExpressionParameter param = new SDFExpressionParameter();
		param.setInputValue(expression);
		param.setAttributeResolver(attributeResolver);
		return param;
	}

	/**
	 * Calls "initialize()" for all given AOs. Some AOs maybe need this call so it recommended to initialize the AOs.
	 * 
	 * @param operators
	 */
	private static void initializeOperators(List<ILogicalOperator> operators) {
		for (ILogicalOperator op : operators) {
			op.initialize();
		}
	}

	/**
	 * Creates a list of attributes from a list of strings representing attributes. Such a string could be something
	 * easy as "x". Used to create a ChangeDetectAO for example.
	 * 
	 * @param listOfAttributes
	 *            List of Strings representing attributes (e.g., just "x")
	 * @return A list of attributes which can be used e.g. to create a ChangeDetectAO.
	 */
	public static List<SDFAttribute> createAttributeList(List<String> listOfAttributes, List<ILogicalOperator> sources) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

		IAttributeResolver resolver = createAttributeResolver(sources);

		for (String attribute : listOfAttributes) {
			attributes.add(resolver.getAttribute(attribute));
		}

		return attributes;
	}

	/**
	 * You maybe need the DataDictionary to get sources or other things installed in Odysseus. Maybe a parameter or
	 * something else need a DataDictionary to work.
	 * 
	 * @return The DataDictionary
	 */
	public static IDataDictionary getDataDictionary() {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
		return dd;
	}

	/**
	 * Creates Attribute Resolver to find Attributes by String
	 * 
	 * @param source
	 *            incoming Operator
	 * @return Attribute Resolver.
	 */
	public static IAttributeResolver createAttributeResolver(ILogicalOperator source) {
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
	public static IAttributeResolver createAttributeResolver(List<ILogicalOperator> sources) {
		List<SDFSchema> inputSchema = new LinkedList<>();
		for (ILogicalOperator source : sources) {
			inputSchema.add(source.getOutputSchema());
		}

		IAttributeResolver attributeResolver = new DirectAttributeResolver(inputSchema);
		return attributeResolver;
	}

	/**
	 * Creates SDFAttributes from a single String, e.g. to use as groupBy attribute.
	 * 
	 * @param groupBy
	 *            String to group By
	 * @param source
	 *            Source Operator.
	 * @return List of SDFAttribtues
	 */
	public static List<SDFAttribute> createAttributeList(String groupBy, ILogicalOperator source) {
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
	public static List<SDFAttribute> createAttributeList(List<String> groupBy, ILogicalOperator source) {
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		operators.add(source);
		return createAttributeList(groupBy, operators);
	}

	/**
	 * Creates an ILogicalQuery with an TopAO on top of the query. Initialized all operators and gives the query a name
	 * 
	 * @param topSource
	 *            Top source of your query
	 * @param allOperators
	 *            List of all operators which should be initialized
	 * @param queryName
	 *            The name this query shall get
	 * @return A finished logical query.
	 */
	public static ILogicalQuery finishQuery(ILogicalOperator topSource, List<ILogicalOperator> allOperators,
			String queryName) {
		List<ILogicalOperator> sources = new ArrayList<ILogicalOperator>();
		sources.add(topSource);

		return finishQuery(sources, allOperators, queryName);
	}

	/**
	 * Creates an ILogicalQuery with an TopAO on top of the query. Initialized all operators and gives the query a name
	 * 
	 * @param topSources
	 *            Top sources of your query
	 * @param allOperators
	 *            List of all operators which should be initialized
	 * @param queryName
	 *            The name this query shall get
	 * @return A finished logical query.
	 */
	public static ILogicalQuery finishQuery(List<ILogicalOperator> topSources, List<ILogicalOperator> allOperators,
			String queryName) {
		// TopAO (for Odysseus - it wants to know which operator is the top)
		TopAO topAO = OperatorBuildHelper.createTopAO(topSources);
		allOperators.add(topAO);

		// Initialize all AOs
		OperatorBuildHelper.initializeOperators(allOperators);

		// Create plan
		ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(topAO, true);
		query.setName(queryName);

		return query;
	}

	/**
	 * If you need a connected list of AND predicates
	 * 
	 * @param predicatesToConnect
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static IPredicate createAndPredicate(List<IPredicate> predicatesToConnect) {
		if (predicatesToConnect.size() >= 2) {
			IPredicate lastAndPredicate = predicatesToConnect.get(0);
			for (int i = 1; i < predicatesToConnect.size(); i++) {
				IPredicate predicate = predicatesToConnect.get(i);
				lastAndPredicate = ComplexPredicateHelper.createAndPredicate(predicate, lastAndPredicate);
			}
			return lastAndPredicate;
		} else if (predicatesToConnect.size() > 0) {
			return predicatesToConnect.get(0);
		}
		return null;
	}

	/**
	 * If you need a connected list of OR predicates
	 * 
	 * @param predicatesToConnect
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static IPredicate createOrPredicate(List<IPredicate> predicatesToConnect) {
		if (predicatesToConnect.size() >= 2) {
			IPredicate lastOrPredicate = predicatesToConnect.get(0);
			for (int i = 1; i < predicatesToConnect.size(); i++) {
				IPredicate predicate = predicatesToConnect.get(i);
				lastOrPredicate = ComplexPredicateHelper.createOrPredicate(predicate, lastOrPredicate);
			}
			return lastOrPredicate;
		} else if (predicatesToConnect.size() > 0) {
			return predicatesToConnect.get(0);
		}
		return null;
	}

	/**
	 * If you need a predicate like x = y; x != y; x < y; ...
	 * 
	 * @param predicate
	 *            What you type in PQL
	 * @return
	 */
	public static RelationalPredicate createRelationalPredicate(String predicate) {
		SDFExpression predicateExpression = new SDFExpression(predicate, MEP.getInstance());
		RelationalPredicate finishedPredicate = new RelationalPredicate(predicateExpression);
		return finishedPredicate;
	}

	/**
	 * Returns AssuredHeartbeatAO
	 * 
	 * @param rate
	 *            Timedelay between heartbeats in milliseconds
	 * @param source
	 * @return
	 */
	public static AssureHeartbeatAO createHeartbeat(int rate, ILogicalOperator source) {
		AssureHeartbeatAO heartbeatAO = new AssureHeartbeatAO();
		heartbeatAO.setApplicationTimeDelay(rate);
		heartbeatAO.setRealTimeDelay(rate);
		heartbeatAO.subscribeTo(source, source.getOutputSchema());
		return heartbeatAO;
	}

}
