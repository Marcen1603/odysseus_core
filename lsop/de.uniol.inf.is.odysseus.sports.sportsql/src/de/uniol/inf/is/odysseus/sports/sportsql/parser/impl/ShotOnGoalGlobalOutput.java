package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Creates all the calculations to forecast shots on the goal. This is not meant
 * to be used directly with sportsQL. Count with
 * {@link ShotOnGoalPlayerSportsQLParser} or
 * {@link ShotOnGoalTeamSportsQLParser} instead.
 * 
 * 
 * @author Michael (all the hard PQL stuff), Tobias (only the translation into
 *         logical query)
 *
 */
public class ShotOnGoalGlobalOutput {

	/**
	 * The maximal timeshift to detect shooters [ps]
	 */
	public final static String MAX_TIMESHIFT_SHOOTER = "5000000000.0";

	/**
	 * The maximal distance of players to be shooters [mm]
	 */
	public final static String MAX_DISTANCE_SHOOTER = "1000.0";

	/**
	 * The maximal shot duration [ps]
	 */
	public static final String MAX_SHOT_DURATION = "1500000000000.0";

	/**
	 * The minimal length of a shot [mm]
	 */
	public static final String MIN_SHOT_LENGTH = "1000.0";

	/**
	 * The minimal acceleration of balls, being shot [m/s²] and [micro m / s²]
	 */
	public static final String MIN_ACCELERATION = "55000000.0";

	@SuppressWarnings({ "rawtypes" })
	public ILogicalOperator createGlobalOutput(SportsQLQuery sportsQL,
			List<ILogicalOperator> allOperators) throws SportsQLParseException {

		// ---------------------
		// Access to the Streams
		// ---------------------

		// GameStream and MetaDataStream
		StreamAO gameStream = OperatorBuildHelper.createGameStreamAO();
		StreamAO metaDataStream = OperatorBuildHelper.createMetadataStreamAO();

		// Add to list
		allOperators.add(gameStream);
		allOperators.add(metaDataStream);

		// -------------------------------------------------------------------
		// First part
		// Filter the sensor data stream for events on the field and enrich it
		// with the metadata stream
		// -------------------------------------------------------------------

		// 1. Select for time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				timeParam, gameStream);

		// 2. Select for space
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParam, false, timeSelect);

		// 3. Enrich with the metastream
		EnrichAO enrichedStream = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", spaceSelect, metaDataStream);

		// Add to the list
		allOperators.add(spaceSelect);
		allOperators.add(enrichedStream);

		// -------------------------------------------------------------------
		// Second part
		// Filter the sensor data stream for ball events
		// -------------------------------------------------------------------

		// 1. Select for ball
		SelectAO ballSelect = OperatorBuildHelper.createEntitySelectByName(
				OperatorBuildHelper.BALL_ENTITY, enrichedStream);

		// 2. Project for important variables
		List<String> ballProjectList = new ArrayList<String>();
		ballProjectList.add("ts");
		ballProjectList.add("x");
		ballProjectList.add("y");
		ballProjectList.add("z");
		ballProjectList.add("a");
		ProjectAO activeBall = OperatorBuildHelper.createProjectAO(
				ballProjectList, ballSelect);

		// Add to list
		allOperators.add(ballSelect);
		allOperators.add(activeBall);

		// -------------------------------------------------------------------
		// Third part
		// Shots begin with a minimal acceleration of 55 m/s
		// -------------------------------------------------------------------

		// 1. Map for acceleration
		List<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter mapParam1 = OperatorBuildHelper
				.createExpressionParameter("ts", "shot_ts", activeBall);
		SDFExpressionParameter mapParam2 = OperatorBuildHelper
				.createExpressionParameter("x", "shot_x", activeBall);
		SDFExpressionParameter mapParam3 = OperatorBuildHelper
				.createExpressionParameter("y", "shot_y", activeBall);
		SDFExpressionParameter mapParam4 = OperatorBuildHelper
				.createExpressionParameter("z", "shot_z", activeBall);
		SDFExpressionParameter mapParam5 = OperatorBuildHelper
				.createExpressionParameter("eif(a >= " + MIN_ACCELERATION
						+ ", 1, 0)", "accelerated", activeBall);
		mapExpressions.add(mapParam1);
		mapExpressions.add(mapParam2);
		mapExpressions.add(mapParam3);
		mapExpressions.add(mapParam4);
		mapExpressions.add(mapParam5);
		MapAO accelerationMap = OperatorBuildHelper.createMapAO(mapExpressions,
				activeBall, 0, 0);

		// 2. ChangeDetect
		List<String> attr = new ArrayList<String>();
		attr.add("accelerated");

		List<ILogicalOperator> attrOps = new ArrayList<ILogicalOperator>();
		attrOps.add(accelerationMap);

		List<SDFAttribute> changeAttributes = OperatorBuildHelper
				.createAttributeList(attr, attrOps);
		ChangeDetectAO accelerationChanged = OperatorBuildHelper
				.createChangeDetectAO(changeAttributes, 0, true,
						accelerationMap);

		// 3. Select for accelerated tuples
		SelectAO accelerationCriteria = OperatorBuildHelper.createSelectAO(
				"accelerated = 1", accelerationChanged);

		// Add to list
		allOperators.add(accelerationMap);
		allOperators.add(accelerationChanged);
		allOperators.add(accelerationCriteria);

		// -------------------------------------------------------------------
		// Fourth part
		// Filter the sensor data stream for player feet events
		// -------------------------------------------------------------------

		// 1. Select for the feet
		String feetSelectPredicate = "entity != \""
				+ OperatorBuildHelper.REFEREE_ENTITY + "\"";
		feetSelectPredicate += " AND entity != \""
				+ OperatorBuildHelper.REFEREE_ENTITY + "\"";
		feetSelectPredicate += " AND (remark = \""
				+ OperatorBuildHelper.LEFT_LEG_REMARK + "\" OR remark = \""
				+ OperatorBuildHelper.RIGHT_LEG_REMARK + "\")";

		SelectAO playerLegSelect = OperatorBuildHelper.createSelectAO(
				feetSelectPredicate, enrichedStream);

		// 2. Project
		List<String> feetProjectList = new ArrayList<String>();
		feetProjectList.add("ts");
		feetProjectList.add("x");
		feetProjectList.add("y");
		feetProjectList.add("z");
		feetProjectList.add("entity_id");
		feetProjectList.add("team_id");
		ProjectAO players = OperatorBuildHelper.createProjectAO(
				feetProjectList, playerLegSelect);

		// Add to list
		allOperators.add(playerLegSelect);
		allOperators.add(players);

		// -------------------------------------------------------------------
		// Fifth part
		// Calculate the distance of the players to the ball
		// -------------------------------------------------------------------

		// 1. Join
		RelationalPredicate firstPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("ts >= shot_ts - "
						+ MAX_TIMESHIFT_SHOOTER);
		RelationalPredicate secondPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("ts >= shot_ts + "
						+ MAX_TIMESHIFT_SHOOTER);
		RelationalPredicate thirdPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("sqrt((x - shot_x)^2 + (y - shot_y)^2) < "
						+ MAX_DISTANCE_SHOOTER);

		List<IPredicate> playerBallJoinPredicates = new ArrayList<IPredicate>();
		playerBallJoinPredicates.add(firstPlayerBallJoinPredicate);
		playerBallJoinPredicates.add(secondPlayerBallJoinPredicate);
		playerBallJoinPredicates.add(thirdPlayerBallJoinPredicate);

		IPredicate playerBallJoinAndPredicate = OperatorBuildHelper
				.createAndPredicate(playerBallJoinPredicates);
		JoinAO playerBallJoinAO = OperatorBuildHelper.createJoinAO(
				playerBallJoinAndPredicate, "ONE_MANY", accelerationCriteria,
				players);

		// 2. Map
		SDFExpressionParameter playerBallJoinExp1 = OperatorBuildHelper
				.createExpressionParameter("shot_ts", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp2 = OperatorBuildHelper
				.createExpressionParameter("shot_x", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp3 = OperatorBuildHelper
				.createExpressionParameter("shot_y", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp4 = OperatorBuildHelper
				.createExpressionParameter("shot_z", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp5 = OperatorBuildHelper
				.createExpressionParameter("entity_id", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp6 = OperatorBuildHelper
				.createExpressionParameter("team_id", playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp7 = OperatorBuildHelper
				.createExpressionParameter(
						"sqrt((x - shot_x)^2 + (y - shot_y)^2)", "distance",
						playerBallJoinAO);

		List<SDFExpressionParameter> playerBallJoinExpressions = new ArrayList<SDFExpressionParameter>();
		playerBallJoinExpressions.add(playerBallJoinExp1);
		playerBallJoinExpressions.add(playerBallJoinExp2);
		playerBallJoinExpressions.add(playerBallJoinExp3);
		playerBallJoinExpressions.add(playerBallJoinExp4);
		playerBallJoinExpressions.add(playerBallJoinExp5);
		playerBallJoinExpressions.add(playerBallJoinExp6);
		playerBallJoinExpressions.add(playerBallJoinExp7);

		MapAO playerBallJoin = OperatorBuildHelper.createMapAO(
				playerBallJoinExpressions, playerBallJoinAO, 0, 0);

		// Add to list
		allOperators.add(playerBallJoinAO);
		allOperators.add(playerBallJoin);

		// -------------------------------------------------------------------
		// Sixth part
		// Determine the player closest to the ball
		// -------------------------------------------------------------------

		// 1. Rename
		List<String> renameAliases = new ArrayList<String>();
		renameAliases.add("shot_ts");
		renameAliases.add("shot_ts2");
		RenameAO renameAO = OperatorBuildHelper.createRenameAO(renameAliases,
				true, playerBallJoin);

		// 2. Coalesce
		List<String> coalesceAttributes = new ArrayList<String>();
		coalesceAttributes.add("shot_ts");
		CoalesceAO clostestPlayerCoalesce = OperatorBuildHelper
				.createCoalesceAO(coalesceAttributes, "min", "distance",
						"minDistance", playerBallJoin);

		// 3. Join
		JoinAO clostedPlayerJoin = OperatorBuildHelper.createJoinAO(
				"distance = minDistance", "ONE_ONE", clostestPlayerCoalesce,
				renameAO);

		// 4. Project
		List<String> clostestPlayerProjectAttributes = new ArrayList<String>();
		clostestPlayerProjectAttributes.add("shot_ts");
		clostestPlayerProjectAttributes.add("shot_x");
		clostestPlayerProjectAttributes.add("shot_y");
		clostestPlayerProjectAttributes.add("shot_z");
		clostestPlayerProjectAttributes.add("entity_id");
		clostestPlayerProjectAttributes.add("team_id");
		ProjectAO clostestPlayer = OperatorBuildHelper.createProjectAO(
				clostestPlayerProjectAttributes, clostedPlayerJoin);

		// Add to list
		allOperators.add(renameAO);
		allOperators.add(clostestPlayerCoalesce);
		allOperators.add(clostedPlayerJoin);
		allOperators.add(clostestPlayer);

		// -------------------------------------------------------------------
		// Seventh part
		// Shots have a maximal duration and a minimal length
		// -------------------------------------------------------------------

		// 1. Join
		RelationalPredicate joinPredicate1 = OperatorBuildHelper
				.createRelationalPredicate("ts >= shot_ts");
		RelationalPredicate joinPredicate2 = OperatorBuildHelper
				.createRelationalPredicate("ts < shot_ts + "
						+ MAX_SHOT_DURATION);
		RelationalPredicate joinPredicate3 = OperatorBuildHelper
				.createRelationalPredicate("sqrt((x - shot_x)^2 + (y - shot_y)^2) >= "
						+ MIN_SHOT_LENGTH);

		List<IPredicate> joinPredicates = new ArrayList<IPredicate>();
		joinPredicates.add(joinPredicate1);
		joinPredicates.add(joinPredicate2);
		joinPredicates.add(joinPredicate3);

		IPredicate joinAndPredicate = OperatorBuildHelper
				.createAndPredicate(joinPredicates);
		JoinAO closestBallActivePlayerJoin = OperatorBuildHelper.createJoinAO(
				joinAndPredicate, "ONE_MANY", clostestPlayer, activeBall);

		// 2. StateMap
		List<SDFExpressionParameter> durationLengthStatemapParams = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter sdfParam1 = OperatorBuildHelper
				.createExpressionParameter("shot_ts",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam2 = OperatorBuildHelper
				.createExpressionParameter("shot_x",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam3 = OperatorBuildHelper
				.createExpressionParameter("shot_y",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam4 = OperatorBuildHelper
				.createExpressionParameter("shot_z",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam5 = OperatorBuildHelper
				.createExpressionParameter("ts", closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam6 = OperatorBuildHelper
				.createExpressionParameter("x", closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam7 = OperatorBuildHelper
				.createExpressionParameter("y", closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam8 = OperatorBuildHelper
				.createExpressionParameter("z", closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam9 = OperatorBuildHelper
				.createExpressionParameter("entity_id",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam10 = OperatorBuildHelper
				.createExpressionParameter("team_id",
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam11 = OperatorBuildHelper
				.createExpressionParameter(
						"eif(shot_ts = __last_1.shot_ts, 1, 0)", "sameShotTS",
						closestBallActivePlayerJoin);

		durationLengthStatemapParams.add(sdfParam1);
		durationLengthStatemapParams.add(sdfParam2);
		durationLengthStatemapParams.add(sdfParam3);
		durationLengthStatemapParams.add(sdfParam4);
		durationLengthStatemapParams.add(sdfParam5);
		durationLengthStatemapParams.add(sdfParam6);
		durationLengthStatemapParams.add(sdfParam7);
		durationLengthStatemapParams.add(sdfParam8);
		durationLengthStatemapParams.add(sdfParam9);
		durationLengthStatemapParams.add(sdfParam10);
		durationLengthStatemapParams.add(sdfParam11);
		StateMapAO durantionLengthStatemapAO = OperatorBuildHelper
				.createStateMapAO(durationLengthStatemapParams,
						closestBallActivePlayerJoin);

		// 3. PredicateWindow
		PredicateWindowAO durationLengthPredicateAO = OperatorBuildHelper
				.createPredicateWindowAO("sameShotTS = 1", null, true, 1500L,
						"Milliseconds", durantionLengthStatemapAO);

		// 4. TupleAggregate
		TupleAggregateAO durationAndLengthCriteria = OperatorBuildHelper
				.createTupleAggregateAO("first", "shot_ts",
						durationLengthPredicateAO);

		// Add to list
		allOperators.add(closestBallActivePlayerJoin);
		allOperators.add(durantionLengthStatemapAO);
		allOperators.add(durationLengthPredicateAO);
		allOperators.add(durationAndLengthCriteria);

		// -------------------------------------------------------------------
		// Eighth part
		// The sign of vx determines the desired goal area
		// -------------------------------------------------------------------

		// 1. Map
		List<SDFExpressionParameter> goalAreaMapParams = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter goalAreaMapParam1 = OperatorBuildHelper
				.createExpressionParameter("shot_ts", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam2 = OperatorBuildHelper
				.createExpressionParameter("shot_x", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam3 = OperatorBuildHelper
				.createExpressionParameter("shot_y", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam4 = OperatorBuildHelper
				.createExpressionParameter("shot_z", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam5 = OperatorBuildHelper
				.createExpressionParameter("ts", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam6 = OperatorBuildHelper
				.createExpressionParameter("x", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam7 = OperatorBuildHelper
				.createExpressionParameter("y", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam8 = OperatorBuildHelper
				.createExpressionParameter("z", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam9 = OperatorBuildHelper
				.createExpressionParameter("entity_id",
						durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam10 = OperatorBuildHelper
				.createExpressionParameter("team_id", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam11 = OperatorBuildHelper
				.createExpressionParameter("(x - shot_x) / (ts - shot_ts)",
						"vx", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam12 = OperatorBuildHelper
				.createExpressionParameter("(y - shot_y) / (ts - shot_ts)",
						"vy", durationAndLengthCriteria);
		SDFExpressionParameter goalAreaMapParam13 = OperatorBuildHelper
				.createExpressionParameter("(z - shot_z) / (ts - shot_ts)",
						"vz", durationAndLengthCriteria);

		goalAreaMapParams.add(goalAreaMapParam1);
		goalAreaMapParams.add(goalAreaMapParam2);
		goalAreaMapParams.add(goalAreaMapParam3);
		goalAreaMapParams.add(goalAreaMapParam4);
		goalAreaMapParams.add(goalAreaMapParam5);
		goalAreaMapParams.add(goalAreaMapParam6);
		goalAreaMapParams.add(goalAreaMapParam7);
		goalAreaMapParams.add(goalAreaMapParam8);
		goalAreaMapParams.add(goalAreaMapParam9);
		goalAreaMapParams.add(goalAreaMapParam10);
		goalAreaMapParams.add(goalAreaMapParam11);
		goalAreaMapParams.add(goalAreaMapParam12);
		goalAreaMapParams.add(goalAreaMapParam13);

		MapAO goalAreaMap = OperatorBuildHelper.createMapAO(goalAreaMapParams,
				durationAndLengthCriteria, 0, 0);

		// 2. Route
		List<String> goalDetectionPredicates = new ArrayList<String>();
		goalDetectionPredicates.add("vy >= 0");
		RouteAO goalAreaDetection = OperatorBuildHelper.createRouteAO(
				goalDetectionPredicates, goalAreaMap);

		// Add to list
		allOperators.add(goalAreaMap);
		allOperators.add(goalAreaDetection);

		// -------------------------------------------------------------------
		// Ninth part
		// Shots have to be forcasted, if they would reach the goalline
		// -------------------------------------------------------------------

		// LOWER PART (in PQL): GOAL AREA 2
		// --------------------------------

		// 1. Map
		MapAO timeToGoalLine2MapAO = createGoalAreaMapAO(1, goalAreaDetection,
				"(y + " + OperatorBuildHelper.GOAL_AREA_2_Y + " ) / vy");

		// 2. Select
		SelectAO timeToGoal2SelectAO = createTimeToGoalSelectAO(
				OperatorBuildHelper.TEAM_ID_LEFT_GOAL_SHOOTERS,
				timeToGoalLine2MapAO);

		// 3. Map
		MapAO forecastMapGoal2 = createForecastMapAO(timeToGoal2SelectAO);

		// 4. Select
		SelectAO foreCastSelectGoal2 = createForecastSelectAO(
				OperatorBuildHelper.GOAL_AREA_2_X_MIN,
				OperatorBuildHelper.GOAL_AREA_2_X_MAX,
				OperatorBuildHelper.GOAL_AREA_2_Z_MAX, forecastMapGoal2);

		// UPPER PART (in PQL): GOAL AREA 1
		// --------------------------------

		// 1. Map
		MapAO timeToGoalLine1MapAO = createGoalAreaMapAO(0, goalAreaDetection,
				"(" + OperatorBuildHelper.GOAL_AREA_1_Y + " - y) / vy");

		// 2. Select
		SelectAO timeToGoal1SelectAO = createTimeToGoalSelectAO(
				OperatorBuildHelper.TEAM_ID_RIGHT_GOAL_SHOOTERS,
				timeToGoalLine1MapAO);

		// 3. Map
		MapAO forecastMapGoal1 = createForecastMapAO(timeToGoal1SelectAO);

		// 4. Select
		SelectAO foreCastSelectGoal1 = createForecastSelectAO(
				OperatorBuildHelper.GOAL_AREA_1_X_MIN,
				OperatorBuildHelper.GOAL_AREA_1_X_MAX,
				OperatorBuildHelper.GOAL_AREA_1_Z_MAX, forecastMapGoal1);

		// MERGE BOTH PARTS
		// ----------------
		MergeAO forecastCriteria = OperatorBuildHelper.createMergeAO(
				foreCastSelectGoal1, foreCastSelectGoal2);

		// Add to list
		allOperators.add(timeToGoalLine2MapAO);
		allOperators.add(timeToGoal2SelectAO);
		allOperators.add(forecastMapGoal2);
		allOperators.add(foreCastSelectGoal2);

		allOperators.add(timeToGoalLine1MapAO);
		allOperators.add(timeToGoal1SelectAO);
		allOperators.add(forecastMapGoal1);
		allOperators.add(foreCastSelectGoal1);

		allOperators.add(forecastCriteria);

		return forecastCriteria;
	}

	/**
	 * Calculates the x and z coordinates of the forecast
	 * 
	 * @param source
	 * @return
	 */
	private static MapAO createForecastMapAO(ILogicalOperator source) {
		List<SDFExpressionParameter> mapParams = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter mapParam1 = OperatorBuildHelper
				.createExpressionParameter("shot_ts", source);
		SDFExpressionParameter mapParam2 = OperatorBuildHelper
				.createExpressionParameter("shot_x", source);
		SDFExpressionParameter mapParam3 = OperatorBuildHelper
				.createExpressionParameter("shot_y", source);
		SDFExpressionParameter mapParam4 = OperatorBuildHelper
				.createExpressionParameter("shot_z", source);
		SDFExpressionParameter mapParam5 = OperatorBuildHelper
				.createExpressionParameter("ts", source);
		SDFExpressionParameter mapParam6 = OperatorBuildHelper
				.createExpressionParameter("entity_id", source);
		SDFExpressionParameter mapParam7 = OperatorBuildHelper
				.createExpressionParameter("team_id", source);
		SDFExpressionParameter mapParam8 = OperatorBuildHelper
				.createExpressionParameter("x + vx * timeToGoalline",
						"forecast_x", source);
		SDFExpressionParameter mapParam9 = OperatorBuildHelper
				.createExpressionParameter("z + vz * timeToGoalline",
						"forecast_z", source);
		SDFExpressionParameter mapParam10 = OperatorBuildHelper
				.createExpressionParameter("1", "shot", source);

		mapParams.add(mapParam1);
		mapParams.add(mapParam2);
		mapParams.add(mapParam3);
		mapParams.add(mapParam4);
		mapParams.add(mapParam5);
		mapParams.add(mapParam6);
		mapParams.add(mapParam7);
		mapParams.add(mapParam8);
		mapParams.add(mapParam9);
		mapParams.add(mapParam10);

		return OperatorBuildHelper.createMapAO(mapParams, source, 0, 0);
	}

	/**
	 * Checks, if the time to the goal line is ok and if the right team is
	 * shooting
	 * 
	 * @param teamId
	 * @param source
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static SelectAO createTimeToGoalSelectAO(int teamId,
			ILogicalOperator source) {
		IPredicate predicate1 = OperatorBuildHelper
				.createRelationalPredicate("team_id = " + teamId);
		IPredicate predicate2 = OperatorBuildHelper
				.createRelationalPredicate("timeToGoalLine >= 0");
		IPredicate predicate3 = OperatorBuildHelper
				.createRelationalPredicate("timeToGoalLine <= "
						+ MAX_SHOT_DURATION + " - ts + shot_ts");
		List<IPredicate> predicates = new ArrayList<IPredicate>();
		predicates.add(predicate1);
		predicates.add(predicate2);
		predicates.add(predicate3);
		IPredicate andPredicate = OperatorBuildHelper
				.createAndPredicate(predicates);
		return OperatorBuildHelper.createSelectAO(andPredicate, source);
	}

	/**
	 * Calculates, if the ball would reach the goal
	 * 
	 * @param xMin
	 * @param xMax
	 * @param zMax
	 * @param source
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static SelectAO createForecastSelectAO(String xMin, String xMax,
			String zMax, ILogicalOperator source) {
		IPredicate predicate1 = OperatorBuildHelper
				.createRelationalPredicate("forecast_x > " + xMin);
		IPredicate predicate2 = OperatorBuildHelper
				.createRelationalPredicate("forecast_x < " + xMax);
		IPredicate predicate3 = OperatorBuildHelper
				.createRelationalPredicate("forecast_z < " + zMax);
		List<IPredicate> predicates = new ArrayList<IPredicate>();
		predicates.add(predicate1);
		predicates.add(predicate2);
		predicates.add(predicate3);
		IPredicate andPredicate = OperatorBuildHelper
				.createAndPredicate(predicates);
		return OperatorBuildHelper.createSelectAO(andPredicate, source);
	}

	/**
	 * Calculates the time until the ball reaches the goal line
	 * 
	 * @param sourceOutputPort
	 * @param source
	 * @param timeToGoalLineExpression
	 * @return
	 */
	private static MapAO createGoalAreaMapAO(int sourceOutputPort,
			ILogicalOperator source, String timeToGoalLineExpression) {

		List<SDFExpressionParameter> mapParams = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter goalAreaMapParam1 = OperatorBuildHelper
				.createExpressionParameter("shot_ts", source);
		SDFExpressionParameter goalAreaMapParam2 = OperatorBuildHelper
				.createExpressionParameter("shot_x", source);
		SDFExpressionParameter goalAreaMapParam3 = OperatorBuildHelper
				.createExpressionParameter("shot_y", source);
		SDFExpressionParameter goalAreaMapParam4 = OperatorBuildHelper
				.createExpressionParameter("shot_z", source);
		SDFExpressionParameter goalAreaMapParam5 = OperatorBuildHelper
				.createExpressionParameter("ts", source);
		SDFExpressionParameter goalAreaMapParam6 = OperatorBuildHelper
				.createExpressionParameter("x", source);
		SDFExpressionParameter goalAreaMapParam7 = OperatorBuildHelper
				.createExpressionParameter("y", source);
		SDFExpressionParameter goalAreaMapParam8 = OperatorBuildHelper
				.createExpressionParameter("z", source);
		SDFExpressionParameter goalAreaMapParam9 = OperatorBuildHelper
				.createExpressionParameter("vx", source);
		SDFExpressionParameter goalAreaMapParam10 = OperatorBuildHelper
				.createExpressionParameter("vz", source);
		SDFExpressionParameter goalAreaMapParam11 = OperatorBuildHelper
				.createExpressionParameter("entity_id", source);
		SDFExpressionParameter goalAreaMapParam12 = OperatorBuildHelper
				.createExpressionParameter("team_id", source);
		SDFExpressionParameter goalAreaMapParam13 = OperatorBuildHelper
				.createExpressionParameter(timeToGoalLineExpression,
						"timeToGoalLine", source);

		mapParams.add(goalAreaMapParam1);
		mapParams.add(goalAreaMapParam2);
		mapParams.add(goalAreaMapParam3);
		mapParams.add(goalAreaMapParam4);
		mapParams.add(goalAreaMapParam5);
		mapParams.add(goalAreaMapParam6);
		mapParams.add(goalAreaMapParam7);
		mapParams.add(goalAreaMapParam8);
		mapParams.add(goalAreaMapParam9);
		mapParams.add(goalAreaMapParam10);
		mapParams.add(goalAreaMapParam11);
		mapParams.add(goalAreaMapParam12);
		mapParams.add(goalAreaMapParam13);

		return OperatorBuildHelper.createMapAO(mapParams, source, 0,
				sourceOutputPort);
	}
}
