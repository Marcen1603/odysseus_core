package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.SpaceUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.model.Space;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * 
 * @author Thore Stratmann
 *
 */

public class PassesSportsQLParser {

	// Distances for short pass (in millimeters)
	private static final int MIN_DISTANCE_SHORT_PASS = 1000;
	private static final int MAX_DISTANCE_SHORT_PASS = 10000;

	// Max time between two passes to detect a direct pass
	private static final String MAX_TIME_DIFF_DIRECT_PASS = "500000.0";

	// angles for pass direction
	private static final int FORWARD_A_1 = -75;
	private static final int FORWARD_A_2 = 75;
	private static final int CROSS_A_1 = 75;
	private static final int CROSS_A_2 = 105;
	private static final int CROSS_A_3 = -75;
	private static final int CROSS_A_4 = -105;

	// Radius to detect a ball contact
	private static final int RADIUS = 800;

	// labels
	public static final String SHORT_PASS = "short";
	public static final String LONG_PASS = "long";
	public static final String FORWARDS_PASS = "forwards";
	public static final String BACK_PASS = "back";
	public static final String CROSS_PASS = "cross";

	// Attributes
	private static String ATTRIBUTE_BALL_CONTACT_DISTANCE = "distance";
	private static String ATTRIBUTE_BALL_CONTACT_MIN_DISTANCE = "min_distance";
	private static String ATTRIBUTE_TIME_BETWEEN = "time_between";

	private static String ATTRIBUTE_BALL_TS = "ball_ts";
	private static String ATTRIBUTE_BALL_X = "ball_x";
	private static String ATTRIBUTE_BALL_Y = "ball_y";
	private static String ATTRIBUTE_BALL_Z = "ball_z";

	public static String ATTRIBUTE_PLAYER_ENTITY_ID = "player_entity_id";
	public static String ATTRIBUTE_PLAYER_TEAM_ID = "player_team_id";
	private static String ATTRIBUTE_PLAYER_X = "player_x";
	private static String ATTRIBUTE_PLAYER_Y = "player_y";
	private static String ATTRIBUTE_PLAYER_Z = "player_z";

	private static String ATTRIBUTE_PASS_ANGLE = "pass_angle";
	private static String ATTRIBUTE_PASS_DISTANCE = "pass_distance";
	
	public static String ATTRIBUTE_PASS_DIRECTION = "pass_direction";	
	public static String ATTRIBUTE_PASS_LENGTH = "pass_length";
	public static String ATTRIBUTE_DIRECT_PASS = "direct_pass";
	public static String ATTRIBUTE_DOUBLE_PASS = "double_pass";

	public ILogicalOperator getPasses(ISession session, SportsQLQuery sportsQL,
			List<ILogicalOperator> allOperators) throws NumberFormatException, MissingDDCEntryException {

		ILogicalOperator soccerGameStreamAO = OperatorBuildHelper.createGameSource(session);
		
		// 1. Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);
		
		
		// 2. Split the data stream to one ball (port 0) and one player (port 1) data stream
		RouteAO splitSoccerDataRoute = createSplitSoccerDataRouteAO(gameTimeSelect);
		allOperators.add(splitSoccerDataRoute);

		// 3. Create ball position map
		ArrayList<SDFExpressionParameter> ballPosExpressions = new ArrayList<SDFExpressionParameter>();
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TS, ATTRIBUTE_BALL_TS, splitSoccerDataRoute));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.X, ATTRIBUTE_BALL_X, splitSoccerDataRoute));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Y, ATTRIBUTE_BALL_Y, splitSoccerDataRoute));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Z, ATTRIBUTE_BALL_Z, splitSoccerDataRoute));

		MapAO ballPosMap = OperatorBuildHelper.createMapAO(ballPosExpressions, splitSoccerDataRoute, 0, 0, false);
		allOperators.add(ballPosMap);

		// 4. ElementWindow with tuple size 1 for the ball data stream
		ElementWindowAO ballWindow = OperatorBuildHelper.createElementWindowAO(1, 1, ballPosMap);
		allOperators.add(ballWindow);

		// 5.  Create player position map
		ArrayList<SDFExpressionParameter> playerPosExpressions = new ArrayList<SDFExpressionParameter>();
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID,ATTRIBUTE_PLAYER_ENTITY_ID, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID,ATTRIBUTE_PLAYER_TEAM_ID, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.X, ATTRIBUTE_PLAYER_X, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Y, ATTRIBUTE_PLAYER_Y, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.Z, ATTRIBUTE_PLAYER_Z, splitSoccerDataRoute));
		
		MapAO playerPosMap = OperatorBuildHelper.createMapAO(playerPosExpressions, splitSoccerDataRoute, 0, 1, false);
		allOperators.add(playerPosMap);

		// 6. ElementWindow with tuple size 1 for the player data stream
		ElementWindowAO playerWindow = OperatorBuildHelper.createElementWindowAO(1,1, playerPosMap);
		allOperators.add(playerWindow);
		
		// 7. Join ball and player data stream to get the ball contacts. Player has a ball contact if distance between the ball and the player position is less than the specified radius
		JoinAO ballContactJoin = OperatorBuildHelper.createJoinAO(new ArrayList<String> (),ballWindow, playerWindow);
		allOperators.add(ballContactJoin);
		
		//8. Calculate distance between player and ball
		List<SDFExpressionParameter> ballContactStateMapExpressions = new ArrayList<SDFExpressionParameter>();
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID, ballContactJoin));
		ballContactStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("Sqrt((Abs("+ATTRIBUTE_BALL_X+"-"+ATTRIBUTE_PLAYER_X+")^2)+(Abs("+ATTRIBUTE_BALL_Y+"-"+ATTRIBUTE_PLAYER_Y+")^2))", ATTRIBUTE_BALL_CONTACT_DISTANCE,ballContactJoin));

		
		StateMapAO ballContactStateMap = OperatorBuildHelper.createStateMapAO(ballContactStateMapExpressions, ballContactJoin);
		allOperators.add(ballContactStateMap);

		//9. Select tuples where distance between player and ball is in the specified radius 		
		SelectAO ballContactSelect = OperatorBuildHelper.createSelectAO(ATTRIBUTE_BALL_CONTACT_DISTANCE+"<"+RADIUS, ballContactStateMap);
		allOperators.add(ballContactSelect);

		// 10. Select tuples with min distance between player and ball
		List<String> ballContactCoalesceAttributes = new ArrayList<String>();
		ballContactCoalesceAttributes.add(ATTRIBUTE_BALL_TS);
		CoalesceAO ballContactCoalesce = OperatorBuildHelper.createCoalesceAO(ballContactCoalesceAttributes, "MIN", ATTRIBUTE_BALL_CONTACT_DISTANCE, ATTRIBUTE_BALL_CONTACT_MIN_DISTANCE, ballContactSelect);
		allOperators.add(ballContactCoalesce);

		//10a.
		List<String> ballContactRenameAliases = new ArrayList<String>();
		ballContactRenameAliases.add(ATTRIBUTE_BALL_TS);
		ballContactRenameAliases.add(ATTRIBUTE_BALL_TS+"_2");
		RenameAO ballContactRename = OperatorBuildHelper.createRenameAO(ballContactRenameAliases, true, ballContactSelect);
		allOperators.add(ballContactRename);
		
		//10b.
		List<String> ballContactDistanceJoinPredicates = new ArrayList<String>();
		ballContactDistanceJoinPredicates.add(ATTRIBUTE_BALL_CONTACT_DISTANCE+" = "+ ATTRIBUTE_BALL_CONTACT_MIN_DISTANCE);
		JoinAO ballContactDistanceJoin = OperatorBuildHelper.createJoinAO(ballContactDistanceJoinPredicates,"ONE_ONE",ballContactCoalesce, ballContactRename);
		allOperators.add(ballContactDistanceJoin);
		
		//10c.
		List<String> ballContactProjectAttributes = new ArrayList<String>();
		ballContactProjectAttributes.add(ATTRIBUTE_BALL_TS);
		ballContactProjectAttributes.add(ATTRIBUTE_BALL_X);
		ballContactProjectAttributes.add(ATTRIBUTE_BALL_Y);
		ballContactProjectAttributes.add(ATTRIBUTE_BALL_Z);
		ballContactProjectAttributes.add(ATTRIBUTE_PLAYER_ENTITY_ID);
		ballContactProjectAttributes.add(ATTRIBUTE_PLAYER_TEAM_ID);
		ProjectAO ballContactProject = OperatorBuildHelper.createProjectAO(ballContactProjectAttributes, ballContactDistanceJoin);
		allOperators.add(ballContactProject);
		
		// 11. Two tuples in a row as one tuple (=> pass)
		List<SDFExpressionParameter> passesStateMapExpressions = new ArrayList<SDFExpressionParameter>();
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_TS, ATTRIBUTE_BALL_TS+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_X, ATTRIBUTE_BALL_X+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_Y, ATTRIBUTE_BALL_Y+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_Z, ATTRIBUTE_BALL_Z+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_PLAYER_ENTITY_ID, ATTRIBUTE_PLAYER_ENTITY_ID+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_PLAYER_TEAM_ID, ATTRIBUTE_PLAYER_TEAM_ID+"_1", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS, ATTRIBUTE_BALL_TS+"_2", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X, ATTRIBUTE_BALL_X+"_2", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y, ATTRIBUTE_BALL_Y+"_2", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z, ATTRIBUTE_BALL_Z+"_2", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID, ATTRIBUTE_PLAYER_ENTITY_ID+"_2", ballContactProject));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID, ATTRIBUTE_PLAYER_TEAM_ID+"_2", ballContactProject));

		StateMapAO passesStateMap = OperatorBuildHelper.createStateMapAO(passesStateMapExpressions, ballContactProject);
		allOperators.add(passesStateMap);

		// 11. Select Tuple with different entity_ids
		SelectAO passesSelect = OperatorBuildHelper.createSelectAO(ATTRIBUTE_PLAYER_ENTITY_ID+"_1 != "+ATTRIBUTE_PLAYER_ENTITY_ID+"_2", passesStateMap);
		allOperators.add(passesSelect);
		
		// 12. Calculate pass distance, pass angle and time between two passes
		List<SDFExpressionParameter> passesDetailsStateMapExpressions = new ArrayList<SDFExpressionParameter>();
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS+"_1", ATTRIBUTE_BALL_TS+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X+"_1", ATTRIBUTE_BALL_X+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y+"_1", ATTRIBUTE_BALL_Y+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z+"_1", ATTRIBUTE_BALL_Z+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID+"_1", ATTRIBUTE_PLAYER_ENTITY_ID+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_1", ATTRIBUTE_PLAYER_TEAM_ID+"_1", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS+"_2", ATTRIBUTE_BALL_TS+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X+"_2", ATTRIBUTE_BALL_X+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y+"_2", ATTRIBUTE_BALL_Y+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z+"_2", ATTRIBUTE_BALL_Z+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID+"_2", ATTRIBUTE_PLAYER_ENTITY_ID+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_2", ATTRIBUTE_PLAYER_TEAM_ID+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_2", ATTRIBUTE_PLAYER_TEAM_ID+"_2", passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("Sqrt((Abs("+ATTRIBUTE_BALL_X+"_2-"+ATTRIBUTE_BALL_X+"_1)^2)+(Abs("+ATTRIBUTE_BALL_Y+"_2-"+ATTRIBUTE_BALL_Y+"_1)^2))" , ATTRIBUTE_PASS_DISTANCE, passesSelect));
		
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("ATan2("+ATTRIBUTE_BALL_X+"_2 -"+ATTRIBUTE_BALL_X+"_1,"+ATTRIBUTE_BALL_Y+"_2 -"+ATTRIBUTE_BALL_Y+"_1)*180/PI()" , ATTRIBUTE_PASS_ANGLE, passesSelect));
		passesDetailsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS+"_1 - __last_1."+ATTRIBUTE_BALL_TS+"_2" , ATTRIBUTE_TIME_BETWEEN, passesSelect));

		StateMapAO passesDetailsStateMap = OperatorBuildHelper.createStateMapAO(passesDetailsStateMapExpressions, passesSelect);
		allOperators.add(passesDetailsStateMap);
		
		// 13. 
		List<SDFExpressionParameter> passesResultsStateMapExpressions = new ArrayList<SDFExpressionParameter>();
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS+"_1", ATTRIBUTE_BALL_TS+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X+"_1", ATTRIBUTE_BALL_X+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y+"_1", ATTRIBUTE_BALL_Y+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z+"_1", ATTRIBUTE_BALL_Z+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID+"_1", ATTRIBUTE_PLAYER_ENTITY_ID+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_1", ATTRIBUTE_PLAYER_TEAM_ID+"_1", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_TS+"_2", ATTRIBUTE_BALL_TS+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_X+"_2", ATTRIBUTE_BALL_X+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Y+"_2", ATTRIBUTE_BALL_Y+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_Z+"_2", ATTRIBUTE_BALL_Z+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_ENTITY_ID+"_2", ATTRIBUTE_PLAYER_ENTITY_ID+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_2", ATTRIBUTE_PLAYER_TEAM_ID+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PLAYER_TEAM_ID+"_2", ATTRIBUTE_PLAYER_TEAM_ID+"_2", passesDetailsStateMap));
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_PASS_DISTANCE, ATTRIBUTE_PASS_DISTANCE, passesDetailsStateMap));

		int leftgoalteamId = SoccerDDCAccess.getLeftGoalTeamId();
		
		// pass direction is dependent on pass angle and in which direction the teams play
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_PASS_ANGLE+" >= "+FORWARD_A_1+" AND "+ATTRIBUTE_PASS_ANGLE+" <= "+FORWARD_A_2+" , eif("+ATTRIBUTE_PLAYER_TEAM_ID+"_1 = "+leftgoalteamId+",'"+BACK_PASS+"','"+FORWARDS_PASS+"'), eif(("+ATTRIBUTE_PASS_ANGLE+" >= "+CROSS_A_1+" AND "+ATTRIBUTE_PASS_ANGLE+" <= "+CROSS_A_2+") OR ("+ATTRIBUTE_PASS_ANGLE+" <= "+CROSS_A_3+" AND "+ATTRIBUTE_PASS_ANGLE+" >= "+CROSS_A_4+"), '"+CROSS_PASS+"', eif("+ATTRIBUTE_PLAYER_TEAM_ID+"_1 = "+leftgoalteamId+",'"+FORWARDS_PASS+"','"+BACK_PASS+"')))", ATTRIBUTE_PASS_DIRECTION, passesDetailsStateMap));
				
		// Long pass if distance > MAX_DISTANCE_SHORT_PASS, otherwise short pass
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_PASS_DISTANCE+" > "+MAX_DISTANCE_SHORT_PASS+" ,'"+LONG_PASS+"' ,'"+SHORT_PASS+"')", ATTRIBUTE_PASS_LENGTH, passesDetailsStateMap));
				
		// double pass if ATTRIBUTE_P2_ENTITY_ID and __last_1.ATTRIBUTE_P1_ENTITY_ID are equal 
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("eif("+ATTRIBUTE_PLAYER_ENTITY_ID+"_2 == __last_1."+ATTRIBUTE_PLAYER_ENTITY_ID+"_1,true,false)", ATTRIBUTE_DOUBLE_PASS, passesDetailsStateMap));
				
		// direct pass if difference between pass_start and __last_1.pass_end is less than MAX_TIME_DIFF_DIRECT_PASS
		passesResultsStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("eif(time_between < "+MAX_TIME_DIFF_DIRECT_PASS+",true,false)", ATTRIBUTE_DIRECT_PASS, passesDetailsStateMap));

		
		StateMapAO passesResultsStateMap = OperatorBuildHelper.createStateMapAO(passesResultsStateMapExpressions, passesDetailsStateMap);
		allOperators.add(passesResultsStateMap);	
		
		// 17. Select passes with distance > MIN_DISTANCE_SHORT_PASS
		SelectAO passesDistanceSelect = OperatorBuildHelper.createSelectAO(ATTRIBUTE_PASS_DISTANCE + " > " + MIN_DISTANCE_SHORT_PASS, passesResultsStateMap);
		allOperators.add(passesDistanceSelect);
		
		//18. Select space
		ILogicalOperator spaceSelect = createSpaceSelectAO(passesDistanceSelect, sportsQL);
		if (spaceSelect != null) {
			allOperators.add(spaceSelect);		
		} else {
			spaceSelect = passesDistanceSelect;
		}



		return spaceSelect;
	}


	
	private SelectAO createSpaceSelectAO(ILogicalOperator source, SportsQLQuery query) throws NumberFormatException, MissingDDCEntryException {	
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
		spaceSelectPredicates.add(ATTRIBUTE_BALL_X+ "_1>= "+startX+" AND "+ATTRIBUTE_BALL_X+ "_2>= "+startX+" AND "+ ATTRIBUTE_BALL_Y+ "_1>= "+startY+" AND "+ATTRIBUTE_BALL_Y+ "_2>= "+startY +"AND "+ATTRIBUTE_BALL_X+ "_1<= "+endX+" AND "+ATTRIBUTE_BALL_X+ "_2<= "+endX+" AND "+ ATTRIBUTE_BALL_Y+ "_1<= "+endY+" AND "+ATTRIBUTE_BALL_Y+ "_2<= "+endY);
		
		SelectAO spaceSelect = OperatorBuildHelper.createSelectAO(spaceSelectPredicates, source);
		return spaceSelect;
	}
	
	
	private RouteAO createSplitSoccerDataRouteAO(ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {				
		StringBuilder predicateSb = new StringBuilder();
		
		Iterator<Integer> ballSensorIterator = AbstractSportsDDCAccess.getBallEntityIds().iterator();
		while(ballSensorIterator.hasNext()) {
			int sensorId = ballSensorIterator.next();
			predicateSb.append(IntermediateSchemaAttributes.ENTITY_ID + " = " + sensorId);
			if(ballSensorIterator.hasNext()) {
				predicateSb.append(" OR ");
			}
		}
		
		ArrayList<String> splitBallRoutePredicates = new ArrayList<String>();
		splitBallRoutePredicates.add(predicateSb.toString());
		
		String teamPred = IntermediateSchemaAttributes.TEAM_ID + " = " + SoccerDDCAccess.getLeftGoalTeamId() + " OR " + IntermediateSchemaAttributes.TEAM_ID + " = " +SoccerDDCAccess.getRightGoalTeamId();
		splitBallRoutePredicates.add(teamPred);
		RouteAO splitRoute = OperatorBuildHelper.createRouteAO(splitBallRoutePredicates, source);
		return splitRoute;
	}

}
