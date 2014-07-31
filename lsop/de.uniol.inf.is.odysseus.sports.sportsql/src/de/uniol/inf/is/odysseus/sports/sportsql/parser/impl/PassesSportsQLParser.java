package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.MetadataAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLDistanceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
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
    "statisticType": "team",
    "gameType": "soccer",
    "entityId": 16,
    "name": "passes",
    "parameters": {
        "time": {
            "start": 100,
            "end": 10000 
        },
        "space": {
            "startx": 100,
            "starty": 10000,
            "endx": 1001,
            "endy": 1221
        },
        "distance": {
            "minDistance": 100,
            "maxDistance": 10000
        },
        "entityIdIsPassReceiver": true
    }
};
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
				@SportsQLParameter(name = "distance", parameterClass = SportsQLDistanceParameter.class, mandatory = false)		

		}
		)
public class PassesSportsQLParser implements ISportsQLParser {
	
	
	
	private static String ATTRIBUTE_BALL_POS = "ball_pos";
	private static String ATTRIBUTE_P1_TEAM_ID = "p1_team_id";
	private static String ATTRIBUTE_P2_TEAM_ID = "p2_team_id";
	private static String ATTRIBUTE_P1_ENTITY_ID = "p1_entity_id";
	private static String ATTRIBUTE_P2_ENTITY_ID = "p2_entity_id";
	private static String ATTRIBUTE_PLAYER_POS = "player_pos";
	private static String ATTRIBUTE_PLAYER_SID = "player_sid";
	private static String ATTRIBUTE_PASS_DISTANCE= "pass_distance";

	private static final double BALL_VELOCITY_CHANGE_IN_PERCENT= 0.15;
	private static final int RADIUS= 400;
	
	private static final int MIN_DISTANCE_SHORT_PASS = 1000;
	private static final int MAX_DISTANCE_SHORT_PASS = 5000;
	
	private static final int MIN_DISTANCE_LONG_PASS = 5000;
	private static final int MAX_DISTANCE_LONG_PASS = 100000;
	
	/**
	 * Ball sids
	 */
	public static final int BALL_SIDS[] = {4,8,10,12};
	
	

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws SportsQLParseException {
			
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();
		StreamAO metadataStreamAO = OperatorBuildHelper.createMetadataStreamAO();
		
		// 1. Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeSelect(timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);
		
		
		// 2. Split the data stream to one ball (port 0) and one player (port 1) data stream.
		RouteAO splitSoccerDataRoute = createSplitSoccerDataRouteAO(gameTimeSelect);
		allOperators.add(splitSoccerDataRoute);

		
		// 3. Check if velocity of the balls has changed
		ArrayList<String> ballVelocityChangeDetectAttributes = new ArrayList<String>();
		ballVelocityChangeDetectAttributes.add(SoccerGameAttributes.VX);
		ballVelocityChangeDetectAttributes.add(SoccerGameAttributes.VY);
		ballVelocityChangeDetectAttributes.add(SoccerGameAttributes.VZ);		
		
		ChangeDetectAO ballVelocityChangeDetect = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(ballVelocityChangeDetectAttributes, splitSoccerDataRoute), true, BALL_VELOCITY_CHANGE_IN_PERCENT, splitSoccerDataRoute);
		allOperators.add(ballVelocityChangeDetect);		

		
		// 4. Attributes x, y, z of the ball data stream as one ball position attribute
		ArrayList<SDFExpressionParameter> ballPosExpressions = new ArrayList<SDFExpressionParameter>();
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, ballVelocityChangeDetect));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter("ToPoint("+SoccerGameAttributes.X+","+SoccerGameAttributes.Y+","+SoccerGameAttributes.Z+")", ATTRIBUTE_BALL_POS, ballVelocityChangeDetect));
		
		MapAO ballPosMap = OperatorBuildHelper.createMapAO(ballPosExpressions, ballVelocityChangeDetect, 0, 0);
		allOperators.add(ballPosMap);

		// 5. Window with tuple size 1 for the ball data stream
		ElementWindowAO ballWindow = OperatorBuildHelper.createTupleWindowAO(1, 1, ballPosMap);
		allOperators.add(ballWindow);

		// 6. Attributes x, y, z of the player data stream as one player position attribute
		ArrayList<SDFExpressionParameter> playerPosExpressions = new ArrayList<SDFExpressionParameter>();
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter("ToPoint("+SoccerGameAttributes.X+","+SoccerGameAttributes.Y+","+SoccerGameAttributes.Z+")", ATTRIBUTE_PLAYER_POS, splitSoccerDataRoute));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.SID,ATTRIBUTE_PLAYER_SID, splitSoccerDataRoute));
		
		MapAO playerPosMap = OperatorBuildHelper.createMapAO(playerPosExpressions, splitSoccerDataRoute, 0, 1);
		allOperators.add(playerPosMap);

		// 7. Window with tuple size 1 for the player data stream
		ElementWindowAO playerWindow = OperatorBuildHelper.createTupleWindowAO(1,1, playerPosMap);
		allOperators.add(playerWindow);
		

		// 8. Join to get the ball contacts. Player has a ball contact if distance between the ball and player position less than the specified radius
		ArrayList<String> ballContactJoinPredicates = new ArrayList<String>();
		ballContactJoinPredicates.add("SpatialDistance("+ATTRIBUTE_BALL_POS+","+ATTRIBUTE_PLAYER_POS+")<"+RADIUS);
		
		JoinAO ballContactJoin = OperatorBuildHelper.createJoinAO(ballContactJoinPredicates, ballWindow, playerWindow);
		allOperators.add(ballContactJoin);
		
		// 9. Delete duplicate player sids
		ArrayList<String> deleteDuplicateSidsChangesDetectAOAttributes = new ArrayList<String>();
		deleteDuplicateSidsChangesDetectAOAttributes.add(ATTRIBUTE_PLAYER_SID);
		List<SDFAttribute> deleteDuplicateSidsChangesDetectAOSDFAttributes = OperatorBuildHelper.createAttributeList(deleteDuplicateSidsChangesDetectAOAttributes, ballContactJoin);
		
		ChangeDetectAO deleteDuplicateSidsChangesDetectAO = OperatorBuildHelper.createChangeDetectAO(deleteDuplicateSidsChangesDetectAOSDFAttributes, 0, ballContactJoin);
		allOperators.add(deleteDuplicateSidsChangesDetectAO);
		
		// 10. Get meta data for the player sids
		EnrichAO playerDataEnrichAO = OperatorBuildHelper.createEnrichAO(ATTRIBUTE_PLAYER_SID+"="+MetadataAttributes.SENSOR_ID, deleteDuplicateSidsChangesDetectAO, metadataStreamAO);
		allOperators.add(playerDataEnrichAO);

		// 11. Delete duplicate entity ids						
		ArrayList<String> deleteDuplicateEntityIdsChangesDetectAOAttributes = new ArrayList<String>();
		deleteDuplicateEntityIdsChangesDetectAOAttributes.add(MetadataAttributes.ENTITY_ID);
		List<SDFAttribute> deleteDuplicateEntityIdsChangesDetectAOSDFAttributes = OperatorBuildHelper.createAttributeList(deleteDuplicateEntityIdsChangesDetectAOAttributes, playerDataEnrichAO);
		
		ChangeDetectAO deleteDuplicateEntityIdsChangesDetectAO = OperatorBuildHelper.createChangeDetectAO(deleteDuplicateEntityIdsChangesDetectAOSDFAttributes, 0, playerDataEnrichAO);
		allOperators.add(deleteDuplicateEntityIdsChangesDetectAO);
		
		
		// 12. Two following ball contacts as one tuple		
		List<SDFExpressionParameter> passesStateMapAOExpressions = new ArrayList<SDFExpressionParameter>();
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+MetadataAttributes.ENTITY_ID, ATTRIBUTE_P1_ENTITY_ID, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(MetadataAttributes.ENTITY_ID, ATTRIBUTE_P2_ENTITY_ID, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+MetadataAttributes.ENTITY, "p1_"+MetadataAttributes.ENTITY, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(MetadataAttributes.ENTITY, "p2_"+MetadataAttributes.ENTITY, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+MetadataAttributes.REMARK, "p1_"+MetadataAttributes.REMARK, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(MetadataAttributes.REMARK,"p2_"+MetadataAttributes.REMARK, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+MetadataAttributes.TEAM_ID, ATTRIBUTE_P1_TEAM_ID, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(MetadataAttributes.TEAM_ID, ATTRIBUTE_P2_TEAM_ID, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+ATTRIBUTE_BALL_POS, "p1_"+ATTRIBUTE_BALL_POS, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(ATTRIBUTE_BALL_POS, "p2_"+ATTRIBUTE_BALL_POS, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("SpatialDistance("+ATTRIBUTE_BALL_POS+",__last_1."+ATTRIBUTE_BALL_POS+")", ATTRIBUTE_PASS_DISTANCE, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1."+SoccerGameAttributes.TS, "p1_"+SoccerGameAttributes.TS, deleteDuplicateEntityIdsChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, "p2_"+SoccerGameAttributes.TS, deleteDuplicateEntityIdsChangesDetectAO));
		
		StateMapAO passesStateMapAO = OperatorBuildHelper.createStateMapAO(passesStateMapAOExpressions, "", deleteDuplicateEntityIdsChangesDetectAO);
		allOperators.add(passesStateMapAO);		
		
		//13. Check if pass distance is higher than the min distance and lower than the max distance	
		ILogicalOperator passDistanceSelect = createPassDistanceSelectAO(passesStateMapAO, sportsQL);
		if (passDistanceSelect != null) {
			allOperators.add(passDistanceSelect);		
		} else {
			passDistanceSelect = passesStateMapAO;
		}
		
		
		//14. Check if pass is in the specified space
		ILogicalOperator spaceSelect = createSpaceSelectAO(passDistanceSelect, sportsQL);
		if (spaceSelect != null) {
			allOperators.add(spaceSelect);		
		} else {
			spaceSelect = passDistanceSelect;
		}
		
		//14. Check statistic type
		ILogicalOperator statisticTypeSelect = createStatisticTypeSelectAO(spaceSelect, sportsQL);
		if (statisticTypeSelect != null) {
			allOperators.add(statisticTypeSelect);		
		} else {
			statisticTypeSelect = spaceSelect;
		}
		
		//15. Check if pass is successful
		ArrayList<String> successfulPassesPredicates = new ArrayList<String>();
		successfulPassesPredicates.add(ATTRIBUTE_P1_TEAM_ID+" = "+ATTRIBUTE_P2_TEAM_ID);
	
		RouteAO successfulPasses = OperatorBuildHelper.createRouteAO(successfulPassesPredicates, statisticTypeSelect);
		allOperators.add(successfulPasses);	
		
		// 16. Finish		
		return OperatorBuildHelper.finishQuery(successfulPasses, allOperators, sportsQL.getName());		
	}
	
	public static SelectAO createStatisticTypeSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException {	
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
	
	
	public static SelectAO createSpaceSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException {	
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLSpaceParameter spaceParameter = (SportsQLSpaceParameter) parameters.get("space");
		
		int startx = 0;
		int starty = 0;
		int endx = 0;
		int endy = 0;
		if (spaceParameter == null || (spaceParameter.getSpace()!= null &&  spaceParameter.getSpace().equals(SportsQLSpaceParameter.SPACE_PARAMETER_ALL))) {
			startx = OperatorBuildHelper.LOWERLEFT_X;
			starty = OperatorBuildHelper.LOWERLEFT_Y;
			endx = OperatorBuildHelper.LOWERRIGHT_X;
			endy = OperatorBuildHelper.UPPERLEFT_Y;
		} else if (spaceParameter != null) {
			startx = spaceParameter.getStartx();
			starty = spaceParameter.getStarty();
			endx = spaceParameter.getEndx();
			endy = spaceParameter.getEndy();
		}
		ArrayList<String> spaceSelectPredicates = new ArrayList<String>();
		spaceSelectPredicates.add("p1_"+ ATTRIBUTE_BALL_POS + ">=  ToPoint("+startx+","+starty+",0) AND p1_"+ ATTRIBUTE_BALL_POS + "<=  ToPoint("+endx+","+endy+",0) AND p2_"+ ATTRIBUTE_BALL_POS + ">=  ToPoint("+startx+","+starty+",0) AND p2_"+ ATTRIBUTE_BALL_POS + "<=  ToPoint("+endx+","+endy+",0)");
		
		SelectAO spaceSelect = OperatorBuildHelper.createSelectAO(spaceSelectPredicates, source);
		return spaceSelect;
	}
	
	public static SelectAO createPassDistanceSelectAO(ILogicalOperator source, SportsQLQuery query) throws SportsQLParseException {	
		Map<String, ISportsQLParameter> parameters = query.getParameters();
		SportsQLDistanceParameter distanceParameter = (SportsQLDistanceParameter) parameters.get("distance");
		int minDistance = 0;
		int maxDistance = 0;
		if (distanceParameter != null && distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_SHORT)) {
			minDistance = MIN_DISTANCE_SHORT_PASS;
			maxDistance = MAX_DISTANCE_SHORT_PASS;
		} else if (distanceParameter != null && distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_LONG)) {
			minDistance = MIN_DISTANCE_LONG_PASS;
			maxDistance = MAX_DISTANCE_LONG_PASS;
		} else if (distanceParameter == null || (distanceParameter.getDistance() != null && distanceParameter.getDistance().equals(SportsQLDistanceParameter.DISTANCE_PARAMETER_ALL))) {
			minDistance = MIN_DISTANCE_SHORT_PASS;
			maxDistance = MAX_DISTANCE_LONG_PASS;
		}else if (distanceParameter != null) {
			minDistance = distanceParameter.getMinDistance();
			maxDistance = distanceParameter.getMaxDistance();
		} 
		ArrayList<String> passesDistancePredicates = new ArrayList<String>();
		passesDistancePredicates.add(ATTRIBUTE_PASS_DISTANCE +" >= "+minDistance +" AND " + ATTRIBUTE_PASS_DISTANCE +" <= " + maxDistance);
		
		SelectAO passesDistanceSelect = OperatorBuildHelper.createSelectAO(passesDistancePredicates, source);
		return passesDistanceSelect;			
	}
	
	
	public static RouteAO createSplitSoccerDataRouteAO(ILogicalOperator source) {				
		StringBuilder predicateSb = new StringBuilder();
		for (int i = 0; i<BALL_SIDS.length; i++) {
			if (i == 0) {
				predicateSb.append(SoccerGameAttributes.SID +"="+BALL_SIDS[i]);
			} else {
				predicateSb.append(" OR " + SoccerGameAttributes.SID +"="+BALL_SIDS[i]);
			}
		}		
		ArrayList<String> splitBallRoutePredicates = new ArrayList<String>();
		splitBallRoutePredicates.add(predicateSb.toString());
		RouteAO splitRoute = OperatorBuildHelper.createRouteAO(splitBallRoutePredicates, source);
		return splitRoute;
	}
}
