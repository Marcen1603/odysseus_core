package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;

/**
 * Creates all the calculations to forecast shots on the goal. This is not meant
 * to be used directly with sportsQL. Count with
 * {@link ShotOnGoalPlayerSportsQLParser} or
 * {@link ShotOnGoalTeamSportsQLParser} instead.
 * 
 * 
 * @author Michael (all the hard PQL stuff), Tobias (only the translation into
 *         logical query), Pascal
 *
 */
public class ShotOnGoalGlobalOutput {

	/**
	 * The maximal timeshift to detect shooters [ms]
	 */
	public final static String MAX_TIMESHIFT_SHOOTER = "100.0";

	/**
	 * The maximal distance of players to be shooters [mm]
	 */
	public final static String MAX_DISTANCE_SHOOTER = "1000.0";

	/**
	 * The maximal shot duration [sec]
	 */
	public static final String MAX_SHOT_DURATION = "1.5";
	
	/**
	 * The maximal timeshift to join the active ball to shot-data after the shot [sec]
	 */
	public static final String MAX_TIMESHIFT_SHOT_BALL_JOIN = "1.0";

	/**
	 * The minimal length of a shot [mm]
	 */
	public static final String MIN_SHOT_LENGTH = "1000.0";

	/**
	 * The minimal acceleration of balls, being shot [m/s�] and [micro m / s�]
	 */
	public static final String MIN_ACCELERATION = "55000000.0";
	
	/**
	 * gravity puffer in height-forcast[mm]
	 */
	public final static String GRAVITY_HEIGHT_PUFFER = "1660.0";

	
	// Attributes
	private static String ATTRIBUTE_SHOT_TS = "shot_ts";
	private static String ATTRIBUTE_SHOT_X = "shot_x";
	private static String ATTRIBUTE_SHOT_Y = "shot_y";
	private static String ATTRIBUTE_SHOT_Z = "shot_z";
	private static String ATTRIBUTE_ACCELERATED = "accelerated";
	private static String ATTRIBUTE_DISTANCE = "distance";
	private static String ATTRIBUTE_SHOT_TS2 = "shot_ts2";
	private static String ATTRIBUTE_MIN_DISTANCE = "minDistance";		
	private static String ATTRIBUTE_SAME_SHOT_TS = "sameShotTS";
	public static String ATTRIBUTE_SHOT = "shot";
	public static String ATTRIBUTE_SHOTS = "shots";
	private static String ATTRIBUTE_FORECAST_Y = "forecast_y";
	private static String ATTRIBUTE_FORECAST_Z = "forecast_z";
	
	//in m/sec
	private static String ATTRIBUTE_VX = "vx";
	private static String ATTRIBUTE_VY = "vy";
	private static String ATTRIBUTE_VZ = "vz";
	
	//in sec
	private static String ATTRIBUTE_TIME_TO_GOALLINE = "timeToGoalline";
		
		
	@SuppressWarnings({ "rawtypes" })
	public ILogicalOperator createGlobalOutput(ISession session,SportsQLQuery sportsQL,
			List<ILogicalOperator> allOperators) throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		// ---------------------
		// Access to the Stream
		// ---------------------

		// GameStream
		StreamAO gameStream = OperatorBuildHelper.createGameStreamAO(session);
		gameStream.setName("start");
		allOperators.add(gameStream);

		// -------------------------------------------------------------------
		// First part
		// Filter the sensor data stream for events on the field
		// -------------------------------------------------------------------

		// 1. Select for time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				timeParam, gameStream);
		timeSelect.setName("timeSelect");
		allOperators.add(timeSelect);
		
		// 1. Select for time
		SportsQLTimeParameter timeParam2 = SportsQLParameterHelper.getTimeParameter(sportsQL);
		SelectAO timeSelect2 = OperatorBuildHelper.createTimeMapAndSelect(timeParam2, gameStream);
		timeSelect2.setName("timeSelect2");
		allOperators.add(timeSelect2);
		
		// 2. Select for space
//		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
//				.getSpaceParameter(sportsQL);
//		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
//				spaceParam, false, timeSelect);
//		spaceSelect.setName("spaceSelect");

		//allOperators.add(spaceSelect);

		// -------------------------------------------------------------------
		// Second part
		// Filter the sensor data stream for ball events
		// -------------------------------------------------------------------
		// get only ball sensors
		List<IPredicate> ballPredicates = new ArrayList<IPredicate>();
		for (int sensorId : AbstractSportsDDCAccess.getBallEntityIds()) {
			IPredicate ballPredicate = OperatorBuildHelper
					.createRelationalPredicate(IntermediateSchemaAttributes.ENTITY_ID + " = " + sensorId);
			ballPredicates.add(ballPredicate);
		}
		IPredicate ballIPredicate = OperatorBuildHelper.createOrPredicate(ballPredicates);
		
		SelectAO ballSelect  = OperatorBuildHelper.createSelectAO(ballIPredicate, timeSelect);
		ballSelect.setName("ballSelect");

		allOperators.add(ballSelect);

		// 2. Project for important variables
		List<String> ballProjectList = new ArrayList<String>();
		ballProjectList.add(IntermediateSchemaAttributes.TS);
		ballProjectList.add(IntermediateSchemaAttributes.X);
		ballProjectList.add(IntermediateSchemaAttributes.Y);
		ballProjectList.add(IntermediateSchemaAttributes.Z);
		ballProjectList.add(IntermediateSchemaAttributes.A);
		ProjectAO activeBall = OperatorBuildHelper.createProjectAO(
				ballProjectList, ballSelect);
		
		activeBall.setName("activeBall");
		allOperators.add(activeBall);

		// -------------------------------------------------------------------
		// Third part
		// Shots begin with a minimal acceleration of 55 m/s
		// -------------------------------------------------------------------

		// 1. Map for acceleration
		List<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter mapParam1 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TS, ATTRIBUTE_SHOT_TS, activeBall);
		SDFExpressionParameter mapParam2 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.X, ATTRIBUTE_SHOT_X, activeBall);
		SDFExpressionParameter mapParam3 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y, ATTRIBUTE_SHOT_Y, activeBall);
		SDFExpressionParameter mapParam4 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z, ATTRIBUTE_SHOT_Z, activeBall);
		SDFExpressionParameter mapParam5 = OperatorBuildHelper
				.createExpressionParameter("eif(" + IntermediateSchemaAttributes.A + " >= " + MIN_ACCELERATION
						+ ", 1, 0)", ATTRIBUTE_ACCELERATED, activeBall);
		
		mapExpressions.add(mapParam1);
		mapExpressions.add(mapParam2);
		mapExpressions.add(mapParam3);
		mapExpressions.add(mapParam4);
		mapExpressions.add(mapParam5);
		
		MapAO accelerationMap = OperatorBuildHelper.createMapAO(mapExpressions,
				activeBall, 0, 0, false);
		accelerationMap.setName("accelerationMap");

		allOperators.add(accelerationMap);

		// 2. ChangeDetect
		List<String> attr = new ArrayList<String>();
		attr.add(ATTRIBUTE_ACCELERATED);

		List<ILogicalOperator> attrOps = new ArrayList<ILogicalOperator>();
		attrOps.add(accelerationMap);
		List<SDFAttribute> changeAttributes = OperatorBuildHelper
				.createAttributeList(attr, attrOps);
		
		ChangeDetectAO accelerationChanged = OperatorBuildHelper
				.createChangeDetectAO(changeAttributes, 0, true,
						accelerationMap);
		accelerationChanged.setName("accelerationChanged");

		allOperators.add(accelerationChanged);

		// 3. Select for accelerated tuples
		SelectAO accelerationCriteria = OperatorBuildHelper.createSelectAO(
				ATTRIBUTE_ACCELERATED + " = 1", accelerationChanged);
		accelerationCriteria.setName("accelerationCriteria");

		allOperators.add(accelerationCriteria);

		// -------------------------------------------------------------------
		// Fourth part
		// Filter the sensor data stream for player feet events
		// -------------------------------------------------------------------
	
		// 1. Select only team 1 and team 2
		SelectAO teamSelect = OperatorBuildHelper.createBothTeamSelectAO(timeSelect2);
		teamSelect.setName("teamSelect");
		allOperators.add(teamSelect);

		// 2. Project
		List<String> feetProjectList = new ArrayList<String>();
		feetProjectList.add(IntermediateSchemaAttributes.TS);
		feetProjectList.add(IntermediateSchemaAttributes.X);
		feetProjectList.add(IntermediateSchemaAttributes.Y);
		feetProjectList.add(IntermediateSchemaAttributes.Z);
		feetProjectList.add(IntermediateSchemaAttributes.ENTITY_ID);
		feetProjectList.add(IntermediateSchemaAttributes.TEAM_ID);
		
		ProjectAO players = OperatorBuildHelper.createProjectAO(
				feetProjectList, teamSelect);
		players.setName("players");

		allOperators.add(players);

		// -------------------------------------------------------------------
		// Fifth part
		// Calculate the distance of the players to the ball
		// -------------------------------------------------------------------

		// 1. Join
		RelationalPredicate firstPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate(IntermediateSchemaAttributes.TS + " >= " + ATTRIBUTE_SHOT_TS + " - ("
						+ MAX_TIMESHIFT_SHOOTER + "*"
						+ TimeUnitHelper.getBTUtoMillisecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
								.getBasetimeunit().toLowerCase())) + ")");
		RelationalPredicate secondPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate(IntermediateSchemaAttributes.TS + " <= " + ATTRIBUTE_SHOT_TS + " + ("
						+ MAX_TIMESHIFT_SHOOTER + "*"
						+ TimeUnitHelper.getBTUtoMillisecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
								.getBasetimeunit().toLowerCase())) + ")");
		RelationalPredicate thirdPlayerBallJoinPredicate = OperatorBuildHelper
				.createRelationalPredicate("sqrt((" + IntermediateSchemaAttributes.X + " - " + ATTRIBUTE_SHOT_X + ")^2 + (" + IntermediateSchemaAttributes.Y + " - " + ATTRIBUTE_SHOT_Y + ")^2) < "
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
		
		playerBallJoinAO.setName("Player Ball Join");
		allOperators.add(playerBallJoinAO);

		// 2. Map
		SDFExpressionParameter playerBallJoinExp1 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_TS, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp2 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_X, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp3 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Y, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp4 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Z, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp5 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp6 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, playerBallJoinAO);
		SDFExpressionParameter playerBallJoinExp7 = OperatorBuildHelper
				.createExpressionParameter(
						"sqrt((" + IntermediateSchemaAttributes.X + " - " + ATTRIBUTE_SHOT_X + ")^2 + (" + IntermediateSchemaAttributes.Y + " - " + ATTRIBUTE_SHOT_Y + ")^2)", ATTRIBUTE_DISTANCE,
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
				playerBallJoinExpressions, playerBallJoinAO, 0, 0, false);
		playerBallJoin.setName("playerBallJoin");

		allOperators.add(playerBallJoin);

		// -------------------------------------------------------------------
		// Sixth part
		// Determine the player closest to the ball
		// -------------------------------------------------------------------

		// 1. Rename
		List<String> renameAliases = new ArrayList<String>();
		renameAliases.add(ATTRIBUTE_SHOT_TS);
		renameAliases.add(ATTRIBUTE_SHOT_TS2);
		
		RenameAO renameAO = OperatorBuildHelper.createRenameAO(renameAliases,
				true, playerBallJoin);
		renameAO.setName("renameAO");

		allOperators.add(renameAO);

		// 2. Coalesce
		List<String> coalesceAttributes = new ArrayList<String>();
		coalesceAttributes.add(ATTRIBUTE_SHOT_TS);
		
		CoalesceAO clostestPlayerCoalesce = OperatorBuildHelper
				.createCoalesceAO(coalesceAttributes, "min", ATTRIBUTE_DISTANCE,
						ATTRIBUTE_MIN_DISTANCE, playerBallJoin);
		clostestPlayerCoalesce.setName("clostestPlayerCoalesce");

		allOperators.add(clostestPlayerCoalesce);

		// 3. Join
		JoinAO clostedPlayerJoin = OperatorBuildHelper.createJoinAO(
				ATTRIBUTE_DISTANCE + " = " + ATTRIBUTE_MIN_DISTANCE, "ONE_ONE", clostestPlayerCoalesce,
				renameAO);
		clostedPlayerJoin.setName("clostedPlayerJoin");

		allOperators.add(clostedPlayerJoin);

		// 4. Project
		List<String> clostestPlayerProjectAttributes = new ArrayList<String>();
		clostestPlayerProjectAttributes.add(ATTRIBUTE_SHOT_TS);
		clostestPlayerProjectAttributes.add(ATTRIBUTE_SHOT_X);
		clostestPlayerProjectAttributes.add(ATTRIBUTE_SHOT_Y);
		clostestPlayerProjectAttributes.add(ATTRIBUTE_SHOT_Z);
		clostestPlayerProjectAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		clostestPlayerProjectAttributes.add(IntermediateSchemaAttributes.TEAM_ID);
		
		ProjectAO clostestPlayer = OperatorBuildHelper.createProjectAO(
				clostestPlayerProjectAttributes, clostedPlayerJoin);
		clostestPlayer.setName("clostestPlayer");

		allOperators.add(clostestPlayer);

		// -------------------------------------------------------------------
		// Seventh part
		// Shots have a maximal duration and a minimal length
		// -------------------------------------------------------------------

		// 1. Join
		RelationalPredicate joinPredicate1 = OperatorBuildHelper
				.createRelationalPredicate(IntermediateSchemaAttributes.TS + " > " + ATTRIBUTE_SHOT_TS);
		RelationalPredicate joinPredicate2 = OperatorBuildHelper
				.createRelationalPredicate(IntermediateSchemaAttributes.TS + " < " + ATTRIBUTE_SHOT_TS + " + ("
						+ MAX_TIMESHIFT_SHOT_BALL_JOIN + " * " + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
								.getBasetimeunit().toLowerCase())) + ")");
		RelationalPredicate joinPredicate3 = OperatorBuildHelper
				.createRelationalPredicate("sqrt((" + IntermediateSchemaAttributes.X + " - " + ATTRIBUTE_SHOT_X + ")^2 + (" + IntermediateSchemaAttributes.Y + " - " + ATTRIBUTE_SHOT_Y + ")^2) >= "
						+ MIN_SHOT_LENGTH);

		List<IPredicate> joinPredicates = new ArrayList<IPredicate>();
		joinPredicates.add(joinPredicate1);
		joinPredicates.add(joinPredicate2);
		joinPredicates.add(joinPredicate3);

		IPredicate joinAndPredicate = OperatorBuildHelper
				.createAndPredicate(joinPredicates);
		JoinAO closestBallActivePlayerJoin = OperatorBuildHelper.createJoinAO(
				joinAndPredicate, "MANY_MANY", clostestPlayer, activeBall);
		closestBallActivePlayerJoin.setName("closestBallActivePlayerJoin");

		allOperators.add(closestBallActivePlayerJoin);

		// 2. StateMap
		List<SDFExpressionParameter> durationLengthStatemapParams = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter sdfParam1 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_TS,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam2 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_X,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam3 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Y,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam4 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Z,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam5 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TS, closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam6 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.X, closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam7 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y, closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam8 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z, closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam9 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam10 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID,
						closestBallActivePlayerJoin);
		SDFExpressionParameter sdfParam11 = OperatorBuildHelper
				.createExpressionParameter(
						"eif(" + ATTRIBUTE_SHOT_TS + " <= __last_1." + ATTRIBUTE_SHOT_TS + " OR " + ATTRIBUTE_SHOT_TS + " <= __last_2." + ATTRIBUTE_SHOT_TS + " OR " + ATTRIBUTE_SHOT_TS + " <= __last_3." + ATTRIBUTE_SHOT_TS + " OR " + ATTRIBUTE_SHOT_TS + " <= __last_4." + ATTRIBUTE_SHOT_TS + ", 1, 0)", ATTRIBUTE_SAME_SHOT_TS,
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
		durantionLengthStatemapAO.setName("durantionLengthStatemapAO");

		allOperators.add(durantionLengthStatemapAO);

		// 3. PredicateWindow
		//TODO: same_shot_ts = 0 eventuell? - als end condition!!!! size -1?? (war vorher shot_ts!)
//		long windowsize = (long)(Double.parseDouble(MAX_SHOT_DURATION) * 1000);
//		PredicateWindowAO durationLengthPredicateAO = OperatorBuildHelper
//				.createPredicateWindowAO(ATTRIBUTE_SAME_SHOT_TS + " = 0", null, true, -1,
//						"Milliseconds", durantionLengthStatemapAO);
//		durationLengthPredicateAO.setName("durationLengthPredicateAO");
//
//		allOperators.add(durationLengthPredicateAO);

		// 4. TupleAggregate
		//TODO: was macht das tupleAggregate hier??
//		TupleAggregateAO durationAndLengthCriteria = OperatorBuildHelper
//				.createTupleAggregateAO("first", ATTRIBUTE_SHOT_TS,
//						durationLengthPredicateAO);
//		durationAndLengthCriteria.setName("durationAndLengthCriteria");
//
//		allOperators.add(durationAndLengthCriteria);

		// 3. Select first tuple of each shot
				SelectAO firstTupleSelect = OperatorBuildHelper.createSelectAO(
						ATTRIBUTE_SAME_SHOT_TS + " = 0", durantionLengthStatemapAO);
				firstTupleSelect.setName("firstTupleSelect");

				allOperators.add(firstTupleSelect);
		// -------------------------------------------------------------------
		// Eighth part
		// The sign of vx determines the desired goal area
		// -------------------------------------------------------------------

		// 1. Map
		List<SDFExpressionParameter> goalAreaMapParams = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter goalAreaMapParam1 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_TS, firstTupleSelect);
		
		SDFExpressionParameter goalAreaMapParam14 = OperatorBuildHelper
				.createExpressionParameter("DoubleToInteger(" + ATTRIBUTE_SHOT_TS + "/"
						+ TimeUnitHelper.getBTUtoMinutesFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
								.getBasetimeunit().toLowerCase())) + ")", OperatorBuildHelper.ATTRIBUTE_MINUTE, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam15 = OperatorBuildHelper
				.createExpressionParameter("DoubleToInteger((" + ATTRIBUTE_SHOT_TS + "/"
						+ TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
								.getBasetimeunit().toLowerCase())) + ") % 60)",
				OperatorBuildHelper.ATTRIBUTE_SECOND, firstTupleSelect);

		SDFExpressionParameter goalAreaMapParam2 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_X, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam3 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Y, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam4 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Z, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam5 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TS, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam6 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.X, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam7 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam8 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam9 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID,
						firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam10 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam11 = OperatorBuildHelper
				.createExpressionParameter("((" + IntermediateSchemaAttributes.X + " - " + ATTRIBUTE_SHOT_X + ")/1000) / ((" + IntermediateSchemaAttributes.TS + " - " + ATTRIBUTE_SHOT_TS + ") / " + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
						.getBasetimeunit().toLowerCase())) + ")",
						ATTRIBUTE_VX, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam12 = OperatorBuildHelper
				.createExpressionParameter("((" + IntermediateSchemaAttributes.Y + " - " + ATTRIBUTE_SHOT_Y + ")/1000) / ((" + IntermediateSchemaAttributes.TS + " - " + ATTRIBUTE_SHOT_TS + ") / " + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
						.getBasetimeunit().toLowerCase())) + ")",
						ATTRIBUTE_VY, firstTupleSelect);
		SDFExpressionParameter goalAreaMapParam13 = OperatorBuildHelper
				.createExpressionParameter("((" + IntermediateSchemaAttributes.Z + " - " + ATTRIBUTE_SHOT_Z + ")/1000) / ((" + IntermediateSchemaAttributes.TS + " - " + ATTRIBUTE_SHOT_TS + ") / " + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess
						.getBasetimeunit().toLowerCase())) + ")",
						ATTRIBUTE_VZ, firstTupleSelect);

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
		goalAreaMapParams.add(goalAreaMapParam14);
		goalAreaMapParams.add(goalAreaMapParam15);

		MapAO goalAreaMap = OperatorBuildHelper.createMapAO(goalAreaMapParams,
				firstTupleSelect, 0, 0,false);
		goalAreaMap.setName("goalAreaMap");

		allOperators.add(goalAreaMap);

		// 2. Route
		List<String> goalDetectionPredicates = new ArrayList<String>();
		goalDetectionPredicates.add(ATTRIBUTE_VX + " >= 0");
		RouteAO goalAreaDetection = OperatorBuildHelper.createRouteAO(
				goalDetectionPredicates, goalAreaMap);
		goalAreaDetection.setName("goalAreaDetection");

		allOperators.add(goalAreaDetection);

		// -------------------------------------------------------------------
		// Ninth part
		// Shots have to be forcasted, if they would reach the goalline
		// -------------------------------------------------------------------

		// LOWER PART (in PQL): GOAL AREA 2
		// --------------------------------

		// 1. Map
		// Time in seconds!
		MapAO timeToGoalLineLeftMapAO = createGoalAreaMapAO(1, goalAreaDetection,
				"(" + SoccerDDCAccess.getGoalareaLeftX() + " - " + IntermediateSchemaAttributes.X + " ) / (" + ATTRIBUTE_VX + " * 1000)");
		timeToGoalLineLeftMapAO.setName("timeToGoalLineLeftMapAO");

		allOperators.add(timeToGoalLineLeftMapAO);

		// 2. Select
		SelectAO timeToGoalLeftSelectAO = createTimeToGoalSelectAO(
				SoccerDDCAccess.getRightGoalTeamId(),
				timeToGoalLineLeftMapAO);
		timeToGoalLeftSelectAO.setName("timeToGoalLeftSelectAO");

		allOperators.add(timeToGoalLeftSelectAO);

		// 3. Map
		MapAO forecastMapGoalLeft = createForecastMapAO(timeToGoalLeftSelectAO);
		forecastMapGoalLeft.setName("forecastMapGoalLeft");

		allOperators.add(forecastMapGoalLeft);

		// 4. Select
		SelectAO foreCastSelectGoalLeft = createForecastSelectAO(
				SoccerDDCAccess.getGoalareaLeftYMin(),
				SoccerDDCAccess.getGoalareaLeftYMax(),
				SoccerDDCAccess.getGoalareaLeftZMax(), forecastMapGoalLeft);
		foreCastSelectGoalLeft.setName("foreCastSelectGoalLeft");

		allOperators.add(foreCastSelectGoalLeft);

		// UPPER PART (in PQL): GOAL AREA 1
		// --------------------------------

		// 1. Map
		MapAO timeToGoalLineRightMapAO = createGoalAreaMapAO(0, goalAreaDetection,
				"(" + SoccerDDCAccess.getGoalareaRightX() + " - " + IntermediateSchemaAttributes.X + ") / (" + ATTRIBUTE_VX + " * 1000)");
		timeToGoalLineRightMapAO.setName("timeToGoalLineRightMapAO");

		allOperators.add(timeToGoalLineRightMapAO);

		// 2. Select
		SelectAO timeToGoalRightSelectAO = createTimeToGoalSelectAO(
				SoccerDDCAccess.getLeftGoalTeamId(),
				timeToGoalLineRightMapAO);
		timeToGoalRightSelectAO.setName("timeToGoalRightSelectAO");

		allOperators.add(timeToGoalRightSelectAO);

		// 3. Map
		MapAO forecastMapGoalRight = createForecastMapAO(timeToGoalRightSelectAO);
		forecastMapGoalRight.setName("forecastMapGoalRight");

		allOperators.add(forecastMapGoalRight);

		// 4. Select
		SelectAO foreCastSelectGoalRight = createForecastSelectAO(
				SoccerDDCAccess.getGoalareaRightYMin(),
				SoccerDDCAccess.getGoalareaRightYMax(),
				SoccerDDCAccess.getGoalareaRightZMax(), forecastMapGoalRight);
		foreCastSelectGoalRight.setName("foreCastSelectGoalRight");

		allOperators.add(foreCastSelectGoalRight);

		// MERGE BOTH PARTS
		// ----------------
		MergeAO forecastCriteria = OperatorBuildHelper.createMergeAO(
				foreCastSelectGoalRight, foreCastSelectGoalLeft);
		forecastCriteria.setName("forecastCriteria");

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
				.createExpressionParameter(ATTRIBUTE_SHOT_TS, source);
		SDFExpressionParameter mapParam11 = OperatorBuildHelper
				.createExpressionParameter(OperatorBuildHelper.ATTRIBUTE_MINUTE, source);
		SDFExpressionParameter mapParam12 = OperatorBuildHelper
				.createExpressionParameter(OperatorBuildHelper.ATTRIBUTE_SECOND, source);
		SDFExpressionParameter mapParam2 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_X, source);
		SDFExpressionParameter mapParam3 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Y, source);
		SDFExpressionParameter mapParam4 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Z, source);
		SDFExpressionParameter mapParam5 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TS, source);
		SDFExpressionParameter mapParam6 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, source);
		SDFExpressionParameter mapParam7 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, source);
		SDFExpressionParameter mapParam8 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y + " + (" + ATTRIBUTE_VY + " * " + ATTRIBUTE_TIME_TO_GOALLINE + " * 1000)",
						ATTRIBUTE_FORECAST_Y, source);
		SDFExpressionParameter mapParam9 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z + " + (" + ATTRIBUTE_VZ + " * " + ATTRIBUTE_TIME_TO_GOALLINE + " * 1000)",
						ATTRIBUTE_FORECAST_Z, source);
		SDFExpressionParameter mapParam10 = OperatorBuildHelper
				.createExpressionParameter("1", ATTRIBUTE_SHOT, source);

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
		mapParams.add(mapParam11);
		mapParams.add(mapParam12);

		return OperatorBuildHelper.createMapAO(mapParams, source, 0, 0, false);
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
				.createRelationalPredicate(IntermediateSchemaAttributes.TEAM_ID + " = " + teamId);
		IPredicate predicate2 = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_TIME_TO_GOALLINE + " >= 0");
		IPredicate predicate3 = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_TIME_TO_GOALLINE + " <= "
						+ MAX_SHOT_DURATION);
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
	 * @param goalArea1XMin
	 * @param xMax
	 * @param zMax
	 * @param source
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static SelectAO createForecastSelectAO(double goalAreaYMin, double goalAreaYMax,
			double goalAreaZMax, ILogicalOperator source) {
		IPredicate predicate1 = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_FORECAST_Y + " >= " + goalAreaYMin);
		IPredicate predicate2 = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_FORECAST_Y + " <= " + goalAreaYMax);
		IPredicate predicate3 = OperatorBuildHelper
				.createRelationalPredicate(ATTRIBUTE_FORECAST_Z + " <= " + goalAreaZMax + " + " + GRAVITY_HEIGHT_PUFFER);
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
				.createExpressionParameter(ATTRIBUTE_SHOT_TS, source);
		SDFExpressionParameter goalAreaMapParam14 = OperatorBuildHelper
				.createExpressionParameter(OperatorBuildHelper.ATTRIBUTE_MINUTE, source);
		SDFExpressionParameter goalAreaMapParam15 = OperatorBuildHelper
				.createExpressionParameter(OperatorBuildHelper.ATTRIBUTE_SECOND, source);
		SDFExpressionParameter goalAreaMapParam2 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_X, source);
		SDFExpressionParameter goalAreaMapParam3 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Y, source);
		SDFExpressionParameter goalAreaMapParam4 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_SHOT_Z, source);
		SDFExpressionParameter goalAreaMapParam5 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TS, source);
		SDFExpressionParameter goalAreaMapParam6 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.X, source);
		SDFExpressionParameter goalAreaMapParam7 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y, source);
		SDFExpressionParameter goalAreaMapParam8 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z, source);
		SDFExpressionParameter goalAreaMapParam9 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_VY, source);
		SDFExpressionParameter goalAreaMapParam10 = OperatorBuildHelper
				.createExpressionParameter(ATTRIBUTE_VZ, source);
		SDFExpressionParameter goalAreaMapParam11 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, source);
		SDFExpressionParameter goalAreaMapParam12 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, source);
		SDFExpressionParameter goalAreaMapParam13 = OperatorBuildHelper
				.createExpressionParameter(timeToGoalLineExpression,
						ATTRIBUTE_TIME_TO_GOALLINE, source);

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
		mapParams.add(goalAreaMapParam14);
		mapParams.add(goalAreaMapParam15);

		return OperatorBuildHelper.createMapAO(mapParams, source, 0,
				sourceOutputPort, false);
	}
}
