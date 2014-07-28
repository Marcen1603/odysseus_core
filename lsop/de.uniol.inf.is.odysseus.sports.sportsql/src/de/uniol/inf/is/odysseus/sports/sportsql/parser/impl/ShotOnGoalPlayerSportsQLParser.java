package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;

public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

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

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		// TODO Include what Michael said about this task on Jira

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		// TODO Add all operators to this list

		// ---------------------
		// Access to the Streams
		// ---------------------

		// GameStream and MetaDataStream
		AccessAO gameAccess = OperatorBuildHelper.createGameStreamAccessAO();
		AccessAO metaDataAccess = OperatorBuildHelper
				.createMetaStreamAccessAO();

		// -------------------------------------------------------------------
		// First part
		// Filter the sensor data stream for events on the field and enrich it
		// with the metadata stream
		// -------------------------------------------------------------------

		// 1. Select for space
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				spaceParam, false, gameAccess);

		// 2. Enrich with the metastream
		EnrichAO enrichedStream = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", spaceSelect, metaDataAccess);

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
				.createExpressionParameter(
						"eif(a >= ${minAcceleration}, 1, 0)", "accelerated",
						activeBall);
		mapExpressions.add(mapParam1);
		mapExpressions.add(mapParam2);
		mapExpressions.add(mapParam3);
		mapExpressions.add(mapParam4);
		mapExpressions.add(mapParam5);
		MapAO accelerationMap = OperatorBuildHelper.createMapAO(mapExpressions,
				activeBall, 0, 0);

		// 2. Changedetect
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

		// -------------------------------------------------------------------
		// Fourth part
		// Filter the sensor data stream for player feet events
		// -------------------------------------------------------------------

		// 1. Select for the feet
		List<IPredicate> playerSelectAndPredicates = new ArrayList<IPredicate>();
		List<IPredicate> playerSelectOrPredicates = new ArrayList<IPredicate>();

		RelationalPredicate firstAndPredicate = OperatorBuildHelper
				.createRelationalPredicate("entity != "
						+ OperatorBuildHelper.BALL_ENTITY);
		RelationalPredicate secondAndPredicate = OperatorBuildHelper
				.createRelationalPredicate("entity != "
						+ OperatorBuildHelper.REFEREE_ENTITY);

		RelationalPredicate leftLegOrPredicate = OperatorBuildHelper
				.createRelationalPredicate("remark = "
						+ OperatorBuildHelper.LEFT_LEG_REMARK);
		RelationalPredicate rightLegOrPredicate = OperatorBuildHelper
				.createRelationalPredicate("remark = "
						+ OperatorBuildHelper.RIGHT_LEG_REMARK);
		playerSelectOrPredicates.add(leftLegOrPredicate);
		playerSelectOrPredicates.add(rightLegOrPredicate);

		IPredicate legOrPredicate = OperatorBuildHelper
				.createOrPredicate(playerSelectOrPredicates);
		playerSelectAndPredicates.add(firstAndPredicate);
		playerSelectAndPredicates.add(secondAndPredicate);
		playerSelectAndPredicates.add(legOrPredicate);

		IPredicate legAndPredicate = OperatorBuildHelper
				.createAndPredicate(playerSelectAndPredicates);

		SelectAO playerLegSelect = OperatorBuildHelper.createSelectAO(
				legAndPredicate, enrichedStream);

		// 2. Project
		List<String> feetProjectList = new ArrayList<String>();
		ballProjectList.add("ts");
		ballProjectList.add("x");
		ballProjectList.add("y");
		ballProjectList.add("z");
		ballProjectList.add("entity_id");
		ballProjectList.add("team_id");
		ProjectAO players = OperatorBuildHelper.createProjectAO(
				feetProjectList, playerLegSelect);

		// -------------------------------------------------------------------
		// Fifth part
		// Calculate the distance of the players to the ball
		// -------------------------------------------------------------------

		// 1. Join
		RelationalPredicate firstPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("ts >= shot_ts - "
						+ ShotOnGoalPlayerSportsQLParser.MAX_TIMESHIFT_SHOOTER);
		RelationalPredicate secondPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("ts >= shot_ts + "
						+ ShotOnGoalPlayerSportsQLParser.MAX_TIMESHIFT_SHOOTER);
		RelationalPredicate thirdPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("sqrt((x - shot_x)^2 + (y - shot_y)^2) < "
						+ ShotOnGoalPlayerSportsQLParser.MAX_DISTANCE_SHOOTER);

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
				.createStateMapAO(durationLengthStatemapParams, null,
						closestBallActivePlayerJoin);

		// 3. PredicateWindow
		String endCondition = null;
		PredicateWindowAO durationLengthPredicateAO = OperatorBuildHelper
				.createPredicateWindowAO("sameShotTS = 1", null, true, 1500L,
						"Milliseconds", durantionLengthStatemapAO);

		// 4. TupleAggregate
		TupleAggregateAO durationAndLengthCriteria = OperatorBuildHelper
				.createTupleAggregateAO("first", "shot_ts",
						durationLengthPredicateAO);

		// -------------------------------------------------------------------
		// Eighth part
		// The sign of vx determines the desired goal area
		// -------------------------------------------------------------------

		return null;
	}
}
