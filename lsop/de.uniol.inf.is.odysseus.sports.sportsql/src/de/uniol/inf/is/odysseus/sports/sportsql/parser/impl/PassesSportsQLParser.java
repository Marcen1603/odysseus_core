package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.SpaceUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.model.Space;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLArrayParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLDistanceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;


/**
 * Parser for SportsQL:
 * Query: Player, Team, Global passes.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
{
    "statisticType": "global",
    "gameType": "soccer",
    "name": "passes",
}
 * 
 * @author Thore Stratmann
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = { StatisticType.PLAYER, StatisticType.TEAM, StatisticType.GLOBAL}, 
		name = "passes",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false),
				@SportsQLParameter(name = "entityIdIsPassReceiver", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "distance", parameterClass = SportsQLDistanceParameter.class, mandatory = false),		
				@SportsQLParameter(name = "doublePasses", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "directPasses", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "passDirection", parameterClass = SportsQLArrayParameter.class, mandatory = false),		
		}
		)
public class PassesSportsQLParser implements ISportsQLParser {
		
	
	// Distances for short pass (in millimeters)
	private static final int MIN_DISTANCE_SHORT_PASS = 1000;
	private static final int MAX_DISTANCE_SHORT_PASS = 10000;	
	
	// Max time between two passes to detect a direct pass
	private static final String MAX_TIME_DIFF_DIRECT_PASS = "200000000000.0";
	
	// angles for pass direction
	private static final int FORWARD_A_1 = -75;
	private static final int FORWARD_A_2 = 75;
	private static final int CROSS_A_1 = 75;
	private static final int CROSS_A_2 = 105;
	private static final int CROSS_A_3 = -75;
	private static final int CROSS_A_4 = -105;
	//private static final int BACK_A_1 = 105;
	//private static final int BACK_A_2 = 180;
	//private static final int BACK_A_3 = -105;
	//private static final int BACK_A_4 = -180;
	
	// Min velocity change to select important tuples
	private static final double BALL_VELOCITY_CHANGE_IN_PERCENT= 0.15;
	
	// Radius to detect a ball contact
	private static final int RADIUS= 400;
	
	// labels
	private static final String SHORT_PASS = "short";
	private static final String LONG_PASS = "long";
	private static final String FORWARDS_PASS = "forwards";
	private static final String BACK_PASS = "back";
	private static final String CROSS_PASS = "cross";
	
	
	//Attributes
	private static String ATTRIBUTE_BALL_POS_X = "ball_pos_x";
	private static String ATTRIBUTE_BALL_POS_Y = "ball_pos_y";
	private static String ATTRIBUTE_BALL_POS_Z = "ball_pos_z";
	private static String ATTRIBUTE_PLAYER_POS_X = "player_pos_x";
	private static String ATTRIBUTE_PLAYER_POS_Y = "player_pos_y";
	private static String ATTRIBUTE_PLAYER_POS_Z = "player_pos_z";
	private static String ATTRIBUTE_P1_TEAM_ID = "p1_team_id";
	private static String ATTRIBUTE_P2_TEAM_ID = "p2_team_id";
	private static String ATTRIBUTE_P1_ENTITY_ID = "p1_entity_id";
	private static String ATTRIBUTE_P2_ENTITY_ID = "p2_entity_id";
	//private static String ATTRIBUTE_P1_ENTITY = "p1_entity";
	//private static String ATTRIBUTE_P2_ENTITY = "p2_entity";
	//private static String ATTRIBUTE_P1_REMARK = "p1_remark";
	//private static String ATTRIBUTE_P2_REMARK = "p2_remark";	
	private static String ATTRIBUTE_P1_BALL_POS_X = "p1_x";
	private static String ATTRIBUTE_P1_BALL_POS_Y = "p1_y";
	private static String ATTRIBUTE_P1_BALL_POS_Z = "p1_z";	
	private static String ATTRIBUTE_P2_BALL_POS_X = "p2_x";
	private static String ATTRIBUTE_P2_BALL_POS_Y = "p2_y";
	private static String ATTRIBUTE_P2_BALL_POS_Z = "p2_z";
	private static String ATTRIBUTE_PASS_START_TS = "pass_start_ts";
	private static String ATTRIBUTE_PASS_END_TS = "pass_end_ts";	
	private static String ATTRIBUTE_PASS_ANGLE = "pass_angle";
	private static String ATTRIBUTE_PLAYER_SID = "player_sid";
	private static String ATTRIBUTE_PASS_DISTANCE= "pass_distance";	
	private static String ATTRIBUTE_PASS_DIRECTION= "pass_direction";
	private static String ATTRIBUTE_PASS_LENGTH= "pass_length";
	private static String ATTRIBUTE_DIRECT_PASS= "direct_pass";
	private static String ATTRIBUTE_DOUBLE_PASS= "double_pass";
	
	private static String ATTRIBUTE_VX= "vx";
	private static String ATTRIBUTE_VY= "vy";
	private static String ATTRIBUTE_VZ= "vz";
	
	

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {
			
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();
		//StreamAO metadataStreamAO = OperatorBuildHelper.createMetadataStreamAO();
		
		// 1. Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);
		
		
		// 2. Split the data stream to one ball (port 0) and one player (port 1) data stream
		RouteAO splitSoccerDataRoute = createSplitSoccerDataRouteAO(gameTimeSelect);
		allOperators.add(splitSoccerDataRoute);

		// 2.1 Statemap to add the following attributes: vx, vy and vz
		List<SDFExpressionParameter> statemapExpressions = new ArrayList<SDFExpressionParameter>();
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.ENTITY_ID, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				OperatorBuildHelper.ATTRIBUTE_MINUTE, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				OperatorBuildHelper.ATTRIBUTE_SECOND, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.X, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.Y, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.Z, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.V, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.A, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.TS, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.TEAM_ID, splitSoccerDataRoute));
		
		// new attributes
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"(" + SoccerGameAttributes.X + "/1000) / ((" + SoccerGameAttributes.TS + "- __last_1." + SoccerGameAttributes.TS + ")/" + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase())) + ")",
				ATTRIBUTE_VX, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"(" + SoccerGameAttributes.Y + "/1000) / ((" + SoccerGameAttributes.TS + "- __last_1." + SoccerGameAttributes.TS + ")/" + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase())) + ")",
				ATTRIBUTE_VY, splitSoccerDataRoute));
		statemapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"(" + SoccerGameAttributes.Z + "/1000) / ((" + SoccerGameAttributes.TS + "- __last_1." + SoccerGameAttributes.TS + ")/" + TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase())) + ")",
				ATTRIBUTE_VZ, splitSoccerDataRoute));
		
		StateMapAO addAttributesStateMapAO = OperatorBuildHelper
				.createStateMapAO(statemapExpressions, SoccerGameAttributes.ENTITY_ID, splitSoccerDataRoute);
		allOperators.add(addAttributesStateMapAO);
		
		// 3. Check if velocity of the balls has changed
		List<String> ballVelocityChangeDetectAttributes = new ArrayList<String>();
		ballVelocityChangeDetectAttributes.add(ATTRIBUTE_VX);
		ballVelocityChangeDetectAttributes.add(ATTRIBUTE_VY);
		ballVelocityChangeDetectAttributes.add(ATTRIBUTE_VZ);	
		
		List<String> ballVelocityChangeDetectGroupByAttributes = new ArrayList<String>();
		ballVelocityChangeDetectGroupByAttributes.add(SoccerGameAttributes.ENTITY_ID);	
		
		ChangeDetectAO ballVelocityChangeDetect = OperatorBuildHelper.createChangeDetectAO(ballVelocityChangeDetectAttributes, OperatorBuildHelper.createAttributeList(ballVelocityChangeDetectGroupByAttributes, addAttributesStateMapAO), true, BALL_VELOCITY_CHANGE_IN_PERCENT, addAttributesStateMapAO, -1);
		allOperators.add(ballVelocityChangeDetect);		

		
		// 4. Rename x, y and z of the ball data stream
		ArrayList<SDFExpressionParameter> ballPosExpressions = new ArrayList<SDFExpressionParameter>();
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, ballVelocityChangeDetect));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.X, ATTRIBUTE_BALL_POS_X, ballVelocityChangeDetect));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.Y, ATTRIBUTE_BALL_POS_Y, ballVelocityChangeDetect));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.Z, ATTRIBUTE_BALL_POS_Z, ballVelocityChangeDetect));

		MapAO ballPosMap = OperatorBuildHelper.createMapAO(ballPosExpressions, ballVelocityChangeDetect, 0, 0, false);
		allOperators.add(ballPosMap);

		// 5. ElementWindow with tuple size 1 for the ball data stream
		ElementWindowAO ballWindow = OperatorBuildHelper.createElementWindowAO(1, 1, ballPosMap);
		allOperators.add(ballWindow);

		// 6.  Rename x, y and z of the player data stream
		ArrayList<SDFExpressionParameter> playerPosExpressions = new ArrayList<SDFExpressionParameter>();
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.X, ATTRIBUTE_PLAYER_POS_X, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.Y, ATTRIBUTE_PLAYER_POS_Y, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.Z, ATTRIBUTE_PLAYER_POS_Z, splitSoccerDataRoute));

		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.ENTITY_ID,ATTRIBUTE_PLAYER_SID, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TEAM_ID, splitSoccerDataRoute));
		
		MapAO playerPosMap = OperatorBuildHelper.createMapAO(playerPosExpressions, splitSoccerDataRoute, 0, 1, false);
		allOperators.add(playerPosMap);

		// 7. ElementWindow with tuple size 1 for the player data stream
		ElementWindowAO playerWindow = OperatorBuildHelper.createElementWindowAO(1,1, playerPosMap);
		allOperators.add(playerWindow);
		

		// 8. Join ball and player data stream to get the ball contacts. Player has a ball contact if distance between the ball and the player position is less than the specified radius
		ArrayList<String> ballContactJoinPredicates = new ArrayList<String>();
		ballContactJoinPredicates.add("SpatialDistance(ToPoint("+ATTRIBUTE_BALL_POS_X+","+ATTRIBUTE_BALL_POS_Y+","+ATTRIBUTE_BALL_POS_Z+"), ToPoint("+ATTRIBUTE_PLAYER_POS_X+","+ATTRIBUTE_PLAYER_POS_Y+","+ATTRIBUTE_PLAYER_POS_Z+"))<"+RADIUS);
		JoinAO ballContactJoin = OperatorBuildHelper.createJoinAO(ballContactJoinPredicates, ballWindow, playerWindow);
		allOperators.add(ballContactJoin);
				
		// 9. Get meta data for the player sids
		//EnrichAO playerDataEnrichAO = OperatorBuildHelper.createEnrichAO(ATTRIBUTE_PLAYER_SID+"="+MetadataAttributes.SENSOR_ID, ballContactJoin, metadataStreamAO);
		//allOperators.add(playerDataEnrichAO);
		ArrayList<SDFExpressionParameter> mapExpressions = new ArrayList<SDFExpressionParameter>();
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.TS, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_BALL_POS_X, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_BALL_POS_Y, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_BALL_POS_Z, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_PLAYER_POS_X, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_PLAYER_POS_Y, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_PLAYER_POS_Z, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_PLAYER_SID, ballContactJoin));
		mapExpressions.add(OperatorBuildHelper.createExpressionParameter(
				SoccerGameAttributes.TEAM_ID, ballContactJoin));
		
		//mapExpressions.add(OperatorBuildHelper.createExpressionParameter("toInteger(fromDDC(concat(concat(\"sensorid.\"," + ATTRIBUTE_PLAYER_SID + "),\",remark\")))", SoccerGameAttributes.REMARK, ballContactJoin));
		//mapExpressions.add(OperatorBuildHelper.createExpressionParameter("toInteger(fromDDC(concat(concat(\"sensorid.\"," + ATTRIBUTE_PLAYER_SID + "),\",entity\")))", SoccerGameAttributes.ENTITY, ballContactJoin));
		//mapExpressions.add(OperatorBuildHelper.createExpressionParameter("toInteger(fromDDC(concat(concat(\"sensorid.\"," + ATTRIBUTE_PLAYER_SID + "),\",team_id\")))", SoccerGameAttributes.TEAM_ID, ballContactJoin));

		
		MapAO enrich = OperatorBuildHelper.createMapAO(mapExpressions,
				ballContactJoin, 0, 0, false);
		allOperators.add(enrich);
		
		// 10. Two following ball contacts as one tuple
		List<SDFExpressionParameter> ballContactsStateMapAOExpressions = new ArrayList<SDFExpressionParameter>();
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_PLAYER_SID, ATTRIBUTE_P1_ENTITY_ID, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_SID, ATTRIBUTE_P2_ENTITY_ID, enrich));
//		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+SoccerGameAttributes.ENTITY, ATTRIBUTE_P1_ENTITY, enrich));
//		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.ENTITY, ATTRIBUTE_P2_ENTITY, enrich));
//		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+SoccerGameAttributes.REMARK, ATTRIBUTE_P1_REMARK, enrich));
//		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.REMARK,ATTRIBUTE_P2_REMARK, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+SoccerGameAttributes.TEAM_ID, ATTRIBUTE_P1_TEAM_ID, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TEAM_ID, ATTRIBUTE_P2_TEAM_ID, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_POS_X, ATTRIBUTE_P1_BALL_POS_X, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_POS_X, ATTRIBUTE_P2_BALL_POS_X, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_POS_Y, ATTRIBUTE_P1_BALL_POS_Y, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_POS_Y, ATTRIBUTE_P2_BALL_POS_Y, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_POS_Z, ATTRIBUTE_P1_BALL_POS_Z, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_POS_Z, ATTRIBUTE_P2_BALL_POS_Z, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+SoccerGameAttributes.TS, ATTRIBUTE_PASS_START_TS, enrich));
		ballContactsStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, ATTRIBUTE_PASS_END_TS, enrich));
		
		StateMapAO ballContactsStateMapAO = OperatorBuildHelper.createStateMapAO(ballContactsStateMapAOExpressions, "", enrich);
		allOperators.add(ballContactsStateMapAO);	
		
		
		// 11. Select tuples with different players (=> pass)
		ArrayList<String> passesSelectPredicates = new ArrayList<String>();
		passesSelectPredicates.add(ATTRIBUTE_P1_ENTITY_ID+" != "+ATTRIBUTE_P2_ENTITY_ID);
		SelectAO passesSelect = OperatorBuildHelper.createSelectAO(passesSelectPredicates, ballContactsStateMapAO);
		allOperators.add(passesSelect);	
		
		
		//12. Calculate pass distance and pass angle
		List<SDFExpressionParameter> passesStateMapAOExpressions = new ArrayList<SDFExpressionParameter>();

		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_ENTITY_ID, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_ENTITY_ID, passesSelect));
//		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_ENTITY, passesSelect));
//		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_ENTITY, passesSelect));
//		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_REMARK, passesSelect));
//		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_REMARK, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_TEAM_ID, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_TEAM_ID, passesSelect));		
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_X, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_Y, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_Z, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_X, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_Y, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_Z, passesSelect));		
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_START_TS, passesSelect));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_END_TS, passesSelect));

		// pass distance
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("SpatialDistance(ToPoint("+ATTRIBUTE_P2_BALL_POS_X+","+ATTRIBUTE_P2_BALL_POS_Y+","+ATTRIBUTE_P2_BALL_POS_Z+"),ToPoint("+ATTRIBUTE_P1_BALL_POS_X+","+ATTRIBUTE_P1_BALL_POS_Y+","+ATTRIBUTE_P1_BALL_POS_Z+"))", ATTRIBUTE_PASS_DISTANCE, passesSelect));
		
		// pass angle
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("ATan2("+ATTRIBUTE_P2_BALL_POS_X+"-"+ATTRIBUTE_P1_BALL_POS_X+","+ATTRIBUTE_P2_BALL_POS_Y+"-"+ATTRIBUTE_P1_BALL_POS_Y+")*180/PI()", ATTRIBUTE_PASS_ANGLE, passesSelect));
			
		StateMapAO passesStateMapAO = OperatorBuildHelper.createStateMapAO(passesStateMapAOExpressions, "", passesSelect);
		allOperators.add(passesStateMapAO);	
		
		//13.Remove all tuples with distance less than MIN_DISTANCE_SHORT_PASS
		ArrayList<String> passesWithMinDistanceSelectPredicates = new ArrayList<String>();
		passesWithMinDistanceSelectPredicates.add(ATTRIBUTE_PASS_DISTANCE+" > "+MIN_DISTANCE_SHORT_PASS);
		SelectAO passesWithMinDistanceSelect = OperatorBuildHelper.createSelectAO(passesWithMinDistanceSelectPredicates, passesStateMapAO);
		allOperators.add(passesWithMinDistanceSelect);	
		
		//14. Calculate direct passes, double passes, short and long passes, and passes direction 
		List<SDFExpressionParameter> passesResultStateMapAOExpressions = new ArrayList<SDFExpressionParameter>();

		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_ENTITY_ID, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_ENTITY_ID, passesWithMinDistanceSelect));
//		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_ENTITY, passesWithMinDistanceSelect));
//		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_ENTITY, passesWithMinDistanceSelect));
//		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_REMARK, passesWithMinDistanceSelect));
//		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_REMARK, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_TEAM_ID, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_TEAM_ID, passesWithMinDistanceSelect));		
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_X, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_Y, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P1_BALL_POS_Z, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_X, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_Y, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_P2_BALL_POS_Z, passesWithMinDistanceSelect));		
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_START_TS, passesWithMinDistanceSelect));
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_END_TS, passesWithMinDistanceSelect));		
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_DISTANCE, ATTRIBUTE_PASS_DISTANCE, passesWithMinDistanceSelect));
		
		
		// Specifies in which direction the teams play
		// TODO Find a better way to determine this (e.g. with the metadata stream)
		int teamIdLeftGoal = 1;
		
		// pass direction is dependent on pass angle and in which direction the teams play
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_PASS_ANGLE+" >= "+FORWARD_A_1+" AND "+ATTRIBUTE_PASS_ANGLE+" <= "+FORWARD_A_2+" , eif("+ATTRIBUTE_P1_TEAM_ID+" = "+teamIdLeftGoal+",'"+FORWARDS_PASS+"','"+BACK_PASS+"'), eif(("+ATTRIBUTE_PASS_ANGLE+" >= "+CROSS_A_1+" AND "+ATTRIBUTE_PASS_ANGLE+" <= "+CROSS_A_2+") OR ("+ATTRIBUTE_PASS_ANGLE+" <= "+CROSS_A_3+" AND "+ATTRIBUTE_PASS_ANGLE+" >= "+CROSS_A_4+"), '"+CROSS_PASS+"', eif("+ATTRIBUTE_P1_TEAM_ID+" = "+teamIdLeftGoal+",'"+BACK_PASS+"','"+FORWARDS_PASS+"')))", ATTRIBUTE_PASS_DIRECTION, passesWithMinDistanceSelect));
		
		// Long pass if distance > MAX_DISTANCE_SHORT_PASS, otherwise short pass
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_PASS_DISTANCE+" > "+MAX_DISTANCE_SHORT_PASS+" ,'"+LONG_PASS+"' ,'"+SHORT_PASS+"')", ATTRIBUTE_PASS_LENGTH, passesWithMinDistanceSelect));
		
		// double pass if ATTRIBUTE_P2_ENTITY_ID and __last_1.ATTRIBUTE_P1_ENTITY_ID are equal 
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_P2_ENTITY_ID+" == __last_1."+ATTRIBUTE_P1_ENTITY_ID+",true,false)", ATTRIBUTE_DOUBLE_PASS, passesWithMinDistanceSelect));
		
		// direct pass if difference between pass_start and __last_1.pass_end is less than MAX_TIME_DIFF_DIRECT_PASS
		passesResultStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("eif(("+ATTRIBUTE_PASS_START_TS+" - __last_1."+ATTRIBUTE_PASS_END_TS+")<"+MAX_TIME_DIFF_DIRECT_PASS+",true,false)", ATTRIBUTE_DIRECT_PASS, passesWithMinDistanceSelect));
		StateMapAO passesResultStateMapAO = OperatorBuildHelper.createStateMapAO(passesResultStateMapAOExpressions, "", passesWithMinDistanceSelect);
		allOperators.add(passesResultStateMapAO);	
											
				
		//15. Select successful passes	
		SelectAO successfulPassesSelect = OperatorBuildHelper.createSelectAO(ATTRIBUTE_P1_TEAM_ID+" = "+ATTRIBUTE_P2_TEAM_ID, passesResultStateMapAO);
		allOperators.add(successfulPassesSelect);
		
		
		//16. Select pass distance	
		ILogicalOperator passDistanceSelect = createPassDistanceSelectAO(successfulPassesSelect, sportsQL);
		if (passDistanceSelect != null) {
			allOperators.add(passDistanceSelect);		
		} else {
			passDistanceSelect = successfulPassesSelect;
		}
		
		
		//17. Select space
		ILogicalOperator spaceSelect = createSpaceSelectAO(passDistanceSelect, sportsQL);
		if (spaceSelect != null) {
			allOperators.add(spaceSelect);		
		} else {
			spaceSelect = passDistanceSelect;
		}
		
		//18. Select statistic type
		ILogicalOperator statisticTypeSelect = createStatisticTypeSelectAO(spaceSelect, sportsQL);
		if (statisticTypeSelect != null) {
			allOperators.add(statisticTypeSelect);		
		} else {
			statisticTypeSelect = spaceSelect;
		}
		
		//19. Select pass direction
		ILogicalOperator passDirectionSelect = createPassDirectionSelectAO(statisticTypeSelect, sportsQL);
		if (passDirectionSelect != null) {
			allOperators.add(passDirectionSelect);		
		} else {
			passDirectionSelect = statisticTypeSelect;
		}
		
		//20. Select direct passes
		ILogicalOperator directPassSelect = createDirectPassSelectAO(passDirectionSelect, sportsQL);
		if (directPassSelect != null) {
			allOperators.add(directPassSelect);		
		} else {
			directPassSelect = passDirectionSelect;
		}
		
		//21. Select double passes
		ILogicalOperator doublePassSelect = createDoublePassSelectAO(directPassSelect, sportsQL);
		if (doublePassSelect != null) {
			allOperators.add(doublePassSelect);		
		} else {
			doublePassSelect = directPassSelect;
		}
		

		
		//22. Project for result stream
		List<String> resultStreamAttributes = new ArrayList<String>();
		resultStreamAttributes.add(ATTRIBUTE_P1_ENTITY_ID);
		resultStreamAttributes.add(ATTRIBUTE_P2_ENTITY_ID);
//		resultStreamAttributes.add(ATTRIBUTE_P1_REMARK);
//		resultStreamAttributes.add(ATTRIBUTE_P2_REMARK);
		resultStreamAttributes.add(ATTRIBUTE_P1_TEAM_ID);
		resultStreamAttributes.add(ATTRIBUTE_P2_TEAM_ID);
		resultStreamAttributes.add(ATTRIBUTE_PASS_LENGTH);
		resultStreamAttributes.add(ATTRIBUTE_PASS_DIRECTION);
		resultStreamAttributes.add(ATTRIBUTE_DOUBLE_PASS);
		resultStreamAttributes.add(ATTRIBUTE_DIRECT_PASS);
		
		ProjectAO resultStreamProject = OperatorBuildHelper.createProjectAO(resultStreamAttributes, doublePassSelect);
		allOperators.add(resultStreamProject);	
		
		// 23. Finish		
		return OperatorBuildHelper.finishQuery(resultStreamProject, allOperators, sportsQL.getName());		
	}
	
	private ILogicalOperator createDoublePassSelectAO(ILogicalOperator source, SportsQLQuery query) {
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLBooleanParameter parameter = (SportsQLBooleanParameter) parameters.get("doublePasses");
		if (parameter != null) {
			ArrayList<String> selectPredicates = new ArrayList<String>();
			selectPredicates.add(ATTRIBUTE_DOUBLE_PASS +" = "+parameter.getValue());
			return OperatorBuildHelper.createSelectAO(selectPredicates, source);
		}else {
			return null;	
		}
	}

	private ILogicalOperator createDirectPassSelectAO(ILogicalOperator source, SportsQLQuery query) {
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLBooleanParameter parameter = (SportsQLBooleanParameter) parameters.get("directPasses");
		if (parameter != null) {
			ArrayList<String> selectPredicates = new ArrayList<String>();
			selectPredicates.add(ATTRIBUTE_DIRECT_PASS +" = "+parameter.getValue());
			return OperatorBuildHelper.createSelectAO(selectPredicates, source);
		}else {
			return null;	
		}
	}

	private ILogicalOperator createPassDirectionSelectAO(ILogicalOperator source, SportsQLQuery query) {
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLArrayParameter parameter = (SportsQLArrayParameter) parameters.get("passDirection");
		if (parameter != null) {
			ArrayList<String> selectPredicates = new ArrayList<String>();
			StringBuilder predicateBuilder = new StringBuilder();
			for (int i = 0; i < parameter.getValue().length; i++) {
				String passDir = parameter.getValue()[i].toString();
				if (i == 0) {
					predicateBuilder.append(ATTRIBUTE_PASS_DIRECTION +" = '" + passDir+"'");
				} else {
					predicateBuilder.append(" OR " + ATTRIBUTE_PASS_DIRECTION +" = '" + passDir+"'");
				}
			}
			String predicate = predicateBuilder.toString();
			selectPredicates.add(predicate);
			return OperatorBuildHelper.createSelectAO(selectPredicates, source);
		}else {
			return null;	
		}
	}

	private  SelectAO createStatisticTypeSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException {	
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLBooleanParameter parameter = (SportsQLBooleanParameter) parameters.get("entityIdIsPassReceiver");
		boolean entityIdIsPassReceiver = false;
		if (parameter != null) {
			entityIdIsPassReceiver = parameter.getValue();
		}		
		
		ArrayList<String> statisticTypePredicates = new ArrayList<String>();
		SelectAO statisticTypeSelect = null;

		if (query.getStatisticType() == StatisticType.PLAYER && entityIdIsPassReceiver) {
			statisticTypePredicates.add(ATTRIBUTE_P1_ENTITY_ID +" = " + query.getEntityId());
			statisticTypeSelect = OperatorBuildHelper.createSelectAO(statisticTypePredicates, source);
		} else if(query.getStatisticType() == StatisticType.PLAYER && !entityIdIsPassReceiver) {
			statisticTypePredicates.add(ATTRIBUTE_P2_ENTITY_ID +" = " + query.getEntityId());
			statisticTypeSelect = OperatorBuildHelper.createSelectAO(statisticTypePredicates, source);
		} else if(query.getStatisticType() == StatisticType.TEAM && entityIdIsPassReceiver) {
			statisticTypePredicates.add(ATTRIBUTE_P1_TEAM_ID +" = " + query.getEntityId());
			statisticTypeSelect = OperatorBuildHelper.createSelectAO(statisticTypePredicates, source);
		} else if(query.getStatisticType() == StatisticType.TEAM && !entityIdIsPassReceiver) {
			statisticTypePredicates.add(ATTRIBUTE_P2_TEAM_ID +" = " + query.getEntityId());
			statisticTypeSelect = OperatorBuildHelper.createSelectAO(statisticTypePredicates, source);
		}
		return statisticTypeSelect;		
	}
	
	
	private SelectAO createSpaceSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {	
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLSpaceParameter spaceParameter = (SportsQLSpaceParameter) parameters.get("space");
		
		double startX = 0;
		double startY = 0;
		double endX = 0;
		double endY = 0;		
		
		if(spaceParameter != null && spaceParameter.getSpace() != null) {
			Space space = AbstractSportsDDCAccess.getSpace(spaceParameter.getSpace());
			startX = space.getXMin();
			startY = space.getYMin();
			endX = space.getXMax();
			endY = space.getYMax();
		} else if(spaceParameter != null && spaceParameter.getUnit() != null) {
			SpaceUnit unit = spaceParameter.getUnit();
			
			startX = SpaceUnitHelper.getMillimeters(spaceParameter.getStartx(), unit);
			startY = SpaceUnitHelper.getMillimeters(spaceParameter.getStarty(), unit);
			endX = SpaceUnitHelper.getMillimeters(spaceParameter.getEndx(), unit);
			endY = SpaceUnitHelper.getMillimeters(spaceParameter.getEndy(), unit);
		} else {
			Space space = AbstractSportsDDCAccess.getSpace(SpaceType.field);
			startX = space.getXMin();
			startY = space.getYMin();
			endX = space.getXMax();
			endY = space.getYMax();
		}
		
		ArrayList<String> spaceSelectPredicates = new ArrayList<String>();
		spaceSelectPredicates.add(ATTRIBUTE_P1_BALL_POS_X+ ">= "+startX+" AND "+ATTRIBUTE_P2_BALL_POS_X+ ">= "+startX+" AND "+ ATTRIBUTE_P1_BALL_POS_Y+ ">= "+startY+" AND "+ATTRIBUTE_P2_BALL_POS_Y+ ">= "+startY +"AND "+ATTRIBUTE_P1_BALL_POS_X+ "<= "+endX+" AND "+ATTRIBUTE_P2_BALL_POS_X+ "<= "+endX+" AND "+ ATTRIBUTE_P1_BALL_POS_Y+ "<= "+endY+" AND "+ATTRIBUTE_P2_BALL_POS_Y+ "<= "+endY);
		
		SelectAO spaceSelect = OperatorBuildHelper.createSelectAO(spaceSelectPredicates, source);
		return spaceSelect;
	}
	
	private SelectAO createPassDistanceSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException {	
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLDistanceParameter distanceParameter = (SportsQLDistanceParameter) parameters.get("distance");
		ArrayList<String> passesDistancePredicates = new ArrayList<String>();
		if (distanceParameter != null && distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_SHORT)) {
			passesDistancePredicates.add(ATTRIBUTE_PASS_DISTANCE +" >= "+MIN_DISTANCE_SHORT_PASS +" AND " + ATTRIBUTE_PASS_DISTANCE +" <= " + MAX_DISTANCE_SHORT_PASS);
		} else if (distanceParameter != null && distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_LONG)) {
			passesDistancePredicates.add(ATTRIBUTE_PASS_DISTANCE +" > "+MAX_DISTANCE_SHORT_PASS);
		} else if (distanceParameter == null || (distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_ALL))) {
			passesDistancePredicates.add(ATTRIBUTE_PASS_DISTANCE +" > "+MIN_DISTANCE_SHORT_PASS);
		}else if (distanceParameter != null) {
			int minDistance = distanceParameter.getMinDistance();
			int maxDistance = distanceParameter.getMaxDistance();
			passesDistancePredicates.add(ATTRIBUTE_PASS_DISTANCE +" >= "+minDistance +" AND " + ATTRIBUTE_PASS_DISTANCE +" <= " + maxDistance);
		} 		
		SelectAO passesDistanceSelect = OperatorBuildHelper.createSelectAO(passesDistancePredicates, source);
		return passesDistanceSelect;			
	}
	
	
	private RouteAO createSplitSoccerDataRouteAO(ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {				
		StringBuilder predicateSb = new StringBuilder();
		
		Iterator<Integer> ballSensorIterator = AbstractSportsDDCAccess.getBallEntityIds().iterator();
		while(ballSensorIterator.hasNext()) {
			int sensorId = ballSensorIterator.next();
			predicateSb.append(SoccerGameAttributes.ENTITY_ID + " = " + sensorId);
			if(ballSensorIterator.hasNext()) {
				predicateSb.append(" OR ");
			}
		}
		
		ArrayList<String> splitBallRoutePredicates = new ArrayList<String>();
		splitBallRoutePredicates.add(predicateSb.toString());
		RouteAO splitRoute = OperatorBuildHelper.createRouteAO(splitBallRoutePredicates, source);
		return splitRoute;
	}
	
}
