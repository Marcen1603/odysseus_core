package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;

/**
 * Parser for Query sprints.
 * 
 * 
 * @author Simon K�spert, Pascal Schmedt, Thore Stratmann
 * 
 */
public class SprintsSportsQLParser  {
	
	// in milliseconds
	private static long MEASSURE_INTERVALL = 1000;
	private static long MEASSURE_INTERVALL_ADVANCE = 200;
	private static int SPRINT_MIN_DISTANCE = 8;
	private static int SPRINT_SPEED = 24;
	
	private static String ATTRIBUTE_AVG_V = "avg_v";
	private static String ATTRIBUTE_MIN_TS = "min_ts";
	private static String ATTRIBUTE_MAX_TS = "max_ts";
	private static String ATTRIBUTE_AVG_SPEED = "avg_speed";

	public static String ATTRIBUTE_SPEED = "speed";
	public static String ATTRIBUTE_SPRINT_DISTANCE = "sprint_distance";
	public static String ATTRIBUTE_AVG_DISTANCE = "avg_distance";
	public static String ATTRIBUTE_SPRINTS_COUNT = "count";
	public static String ATTRIBUTE_MAX_SPEED = "max_speed";


	public ILogicalOperator getSprints(ISession session,
			SportsQLQuery sportsQL, List<ILogicalOperator> allOperators)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {

		StreamAO soccerGameStreamAO = OperatorBuildHelper
				.createGameStreamAO(session);

		// 1. Time Parameter
		SportsQLTimeParameter gameTimeSelectParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				gameTimeSelectParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);

		// 2. Space Parameter
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, false, gameTimeSelect);
		allOperators.add(spaceSelect);

		// 3. Project necessary attributes : entity_id, team_id, velocity
		List<String> streamProjectAttributes = new ArrayList<String>();
		streamProjectAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		streamProjectAttributes.add(IntermediateSchemaAttributes.TEAM_ID);
		streamProjectAttributes.add(IntermediateSchemaAttributes.V);
		streamProjectAttributes.add(IntermediateSchemaAttributes.TS);

		ProjectAO streamProject = OperatorBuildHelper.createProjectAO(
				streamProjectAttributes, spaceSelect);
		allOperators.add(streamProject);

		// 3. TimeWindow 1 second
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAOWithAdvance(MEASSURE_INTERVALL, MEASSURE_INTERVALL_ADVANCE,"MILLISECONDS", streamProject);
		allOperators.add(timeWindow);

		// 4. Aggregate to calculate average velocity of 1 second
		List<String> aggregateFunctions = new ArrayList<String>();
		aggregateFunctions.add("AVG");
		aggregateFunctions.add("MIN");
		aggregateFunctions.add("MAX");

		List<String> aggregateInputAttributeNames = new ArrayList<String>();
		aggregateInputAttributeNames.add(IntermediateSchemaAttributes.V);
		aggregateInputAttributeNames.add(IntermediateSchemaAttributes.TS);
		aggregateInputAttributeNames.add(IntermediateSchemaAttributes.TS);

		List<String> aggregateOutputAttributeNames = new ArrayList<String>();
		aggregateOutputAttributeNames.add(ATTRIBUTE_AVG_V);
		aggregateOutputAttributeNames.add(ATTRIBUTE_MIN_TS);
		aggregateOutputAttributeNames.add(ATTRIBUTE_MAX_TS);

		List<String> aggregateGroupBys = new ArrayList<String>();
		aggregateGroupBys.add(IntermediateSchemaAttributes.ENTITY_ID);
		aggregateGroupBys.add(IntermediateSchemaAttributes.TEAM_ID);

		AggregateAO aggregate = OperatorBuildHelper.createAggregateAO(
				aggregateFunctions, aggregateGroupBys,
				aggregateInputAttributeNames, aggregateOutputAttributeNames,
				null, timeWindow, -1);
		allOperators.add(aggregate);

		// 5. Calculate speed in km/h
		ArrayList<SDFExpressionParameter> toKmhMapExpressions = new ArrayList<SDFExpressionParameter>();
		toKmhMapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.ENTITY_ID, aggregate));
		toKmhMapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.TEAM_ID, aggregate));
		toKmhMapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_MIN_TS, aggregate));
		toKmhMapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_MAX_TS, aggregate));
		toKmhMapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_AVG_V + " * 360  / 1000000000", ATTRIBUTE_SPEED,
				aggregate));

		MapAO toKmhMap = OperatorBuildHelper.createMapAO(toKmhMapExpressions,
				aggregate, 0, 0, false);
		allOperators.add(toKmhMap);
		

		// 6.
		List<String> coalesceAOAttributes = new ArrayList<String>();
		coalesceAOAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		coalesceAOAttributes.add(IntermediateSchemaAttributes.TEAM_ID);

		List<String> coalesceAOFunctions = new ArrayList<String>();
		coalesceAOFunctions.add("AVG");
		coalesceAOFunctions.add("MIN");
		coalesceAOFunctions.add("MAX");
		coalesceAOFunctions.add("MAX");

		List<String> coalesceAOInputAttributes = new ArrayList<String>();
		coalesceAOInputAttributes.add(ATTRIBUTE_SPEED);
		coalesceAOInputAttributes.add(ATTRIBUTE_MIN_TS);
		coalesceAOInputAttributes.add(ATTRIBUTE_MAX_TS);
		coalesceAOInputAttributes.add(ATTRIBUTE_SPEED);

		List<String> coalesceAOOutputAttributes = new ArrayList<String>();
		coalesceAOOutputAttributes.add(ATTRIBUTE_AVG_SPEED);
		coalesceAOOutputAttributes.add(ATTRIBUTE_MIN_TS);
		coalesceAOOutputAttributes.add(ATTRIBUTE_MAX_TS);
		coalesceAOOutputAttributes.add(ATTRIBUTE_MAX_SPEED);

		IPredicate<?> startPredicate = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_SPEED + "> "+SPRINT_SPEED);
		IPredicate<?> endPredicate = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_SPEED + "< "+(SPRINT_SPEED-5));

		CoalesceAO coalesceAO = OperatorBuildHelper.createCoalesceAO(
				coalesceAOAttributes, coalesceAOFunctions,
				coalesceAOInputAttributes, coalesceAOOutputAttributes,
				startPredicate, endPredicate, toKmhMap);
		allOperators.add(coalesceAO);
		
		// 7		
		ArrayList<SDFExpressionParameter> resultMapExpressions = new ArrayList<SDFExpressionParameter>();
		resultMapExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, coalesceAO));
		resultMapExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, coalesceAO));
		resultMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_AVG_SPEED, coalesceAO));
		resultMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_MAX_SPEED, coalesceAO));
		resultMapExpressions.add(OperatorBuildHelper.createExpressionParameter("(("+ATTRIBUTE_AVG_SPEED+"*1000) / 60 / 60 / 1000 / 1000) * ("+ATTRIBUTE_MAX_TS+"-"+ATTRIBUTE_MIN_TS+")", ATTRIBUTE_SPRINT_DISTANCE,coalesceAO));

		MapAO resultMap = OperatorBuildHelper.createMapAO(resultMapExpressions,	coalesceAO, 0, 0, false);
		allOperators.add(resultMap);
		
		// 8
		String selectPredicate = ATTRIBUTE_SPRINT_DISTANCE+">"+SPRINT_MIN_DISTANCE;
		SelectAO selectAO = OperatorBuildHelper.createSelectAO(selectPredicate, resultMap);
		allOperators.add(resultMap);

		return selectAO;
	}

}