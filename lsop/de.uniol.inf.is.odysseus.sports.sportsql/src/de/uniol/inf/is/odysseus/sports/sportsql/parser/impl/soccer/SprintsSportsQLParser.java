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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
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
 * @author Simon Küspert, Pascal Schmedt, Thore Stratmann
 * 
 */
public class SprintsSportsQLParser  {

	private static String ATTRIBUTE_AVG_V = "avg_v";

	private static String ATTRIBUTE_STANDING_TIME = "standing_time";
	private static String ATTRIBUTE_STANDING_DISTANCE = "standing_distance";
	private static String ATTRIBUTE_TROT_TIME = "trot_time";
	private static String ATTRIBUTE_TROT_DISTANCE = "trot_distance";
	private static String ATTRIBUTE_LOW_TIME = "low_time";
	private static String ATTRIBUTE_LOW_DISTANCE = "low_distance";
	private static String ATTRIBUTE_MEDIUM_TIME = "medium_time";
	private static String ATTRIBUTE_MEDIUM_DISTANCE = "medium_distance";
	private static String ATTRIBUTE_HIGH_TIME = "hight_time";
	private static String ATTRIBUTE_HIGH_DISTANCE = "high_distance";

	public static String ATTRIBUTE_SPEED = "speed";
	public static String ATTRIBUTE_SPRINT_DISTANCE = "sprint_distance";
	public static String ATTRIBUTE_SPRINT_TIME = "sprint_time";


	private static final String STANDING_SPEED_UPPERBORDER = "1";
	private static final String TROT_SPEED_UPPERBORDER = "11";
	private static final String LOW_SPEED_UPPERBORDER = "14";
	private static final String MEDIUM_SPEED_UPPERBORDER = "17";
	private static final String HIGH_SPEED_UPPERBORDER = "24";

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

		ProjectAO streamProject = OperatorBuildHelper.createProjectAO(
				streamProjectAttributes, spaceSelect);
		allOperators.add(streamProject);

		// 3. TimeWindow 1 second
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(1, 1,
				"SECONDS", streamProject);
		allOperators.add(timeWindow);

		// 4. Aggregate to calculate average velocity of 1 second
		List<String> aggregateFunctions = new ArrayList<String>();
		aggregateFunctions.add("AVG");

		List<String> aggregateInputAttributeNames = new ArrayList<String>();
		aggregateInputAttributeNames.add(IntermediateSchemaAttributes.V);

		List<String> aggregateOutputAttributeNames = new ArrayList<String>();
		aggregateOutputAttributeNames.add(ATTRIBUTE_AVG_V);

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
				ATTRIBUTE_AVG_V + " / 1000000000 * 360", ATTRIBUTE_SPEED,
				aggregate));

		MapAO toKmhMap = OperatorBuildHelper.createMapAO(toKmhMapExpressions,
				aggregate, 0, 0, false);
		allOperators.add(toKmhMap);

		// 6. Set speed category (standing, trot, low, medium, high, sprint) and
		// calculate sprint distance
		List<SDFExpressionParameter> statemapExpressions = new ArrayList<SDFExpressionParameter>();
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.ENTITY_ID, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				IntermediateSchemaAttributes.TEAM_ID, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_SPEED, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " <= " + STANDING_SPEED_UPPERBORDER
						+ ", 1, 0)", ATTRIBUTE_STANDING_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " <= " + STANDING_SPEED_UPPERBORDER
						+ ", " + ATTRIBUTE_SPEED + " /3.6, 0)",
				ATTRIBUTE_STANDING_DISTANCE, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + STANDING_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ TROT_SPEED_UPPERBORDER + ", 1, 0)",
				ATTRIBUTE_TROT_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + STANDING_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ TROT_SPEED_UPPERBORDER + ", " + ATTRIBUTE_SPEED
						+ " /3.6, 0)", ATTRIBUTE_TROT_DISTANCE, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + TROT_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ LOW_SPEED_UPPERBORDER + ", 1, 0)",
				ATTRIBUTE_LOW_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + TROT_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ LOW_SPEED_UPPERBORDER + ", " + ATTRIBUTE_SPEED
						+ " /3.6, 0)", ATTRIBUTE_LOW_DISTANCE, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", 1, 0)",
				ATTRIBUTE_MEDIUM_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + LOW_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ MEDIUM_SPEED_UPPERBORDER + ", " + ATTRIBUTE_SPEED
						+ " /3.6, 0)", ATTRIBUTE_MEDIUM_DISTANCE, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + MEDIUM_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", 1, 0)",
				ATTRIBUTE_HIGH_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + MEDIUM_SPEED_UPPERBORDER
						+ " AND " + ATTRIBUTE_SPEED + " <= "
						+ HIGH_SPEED_UPPERBORDER + ", " + ATTRIBUTE_SPEED
						+ " /3.6, 0)", ATTRIBUTE_HIGH_DISTANCE, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + HIGH_SPEED_UPPERBORDER
						+ ", 1, 0)", ATTRIBUTE_SPRINT_TIME, toKmhMap));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_SPEED + " > " + HIGH_SPEED_UPPERBORDER
						+ ", " + ATTRIBUTE_SPEED + " /3.6, 0)",
				ATTRIBUTE_SPRINT_DISTANCE, toKmhMap));

		StateMapAO divideSpeedStateMapAO = OperatorBuildHelper
				.createStateMapAO(statemapExpressions, "", toKmhMap);
		allOperators.add(divideSpeedStateMapAO);

		// 7.
		List<String> coalesceAOAttributes = new ArrayList<String>();
		coalesceAOAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		coalesceAOAttributes.add(IntermediateSchemaAttributes.TEAM_ID);

		List<String> coalesceAOFunctions = new ArrayList<String>();
		coalesceAOFunctions.add("SUM");
		coalesceAOFunctions.add("SUM");
		coalesceAOFunctions.add("MAX");

		List<String> coalesceAOInputAttributes = new ArrayList<String>();
		coalesceAOInputAttributes.add(ATTRIBUTE_SPRINT_TIME);
		coalesceAOInputAttributes.add(ATTRIBUTE_SPRINT_DISTANCE);
		coalesceAOInputAttributes.add(ATTRIBUTE_SPEED);

		List<String> coalesceAOOutputAttributes = new ArrayList<String>();
		coalesceAOOutputAttributes.add(ATTRIBUTE_SPRINT_TIME);
		coalesceAOOutputAttributes.add(ATTRIBUTE_SPRINT_DISTANCE);
		coalesceAOOutputAttributes.add(ATTRIBUTE_SPEED);

		IPredicate<?> startPredicate = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_SPRINT_TIME + "> 0");
		IPredicate<?> endPredicate = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_SPRINT_TIME + "= 0");

		CoalesceAO coalesceAO = OperatorBuildHelper.createCoalesceAO(
				coalesceAOAttributes, coalesceAOFunctions,
				coalesceAOInputAttributes, coalesceAOOutputAttributes,
				startPredicate, endPredicate, divideSpeedStateMapAO);
		allOperators.add(coalesceAO);


		return coalesceAO;
	}

}
