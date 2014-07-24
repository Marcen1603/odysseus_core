package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;


/**
 * Parser for SportsQL:
 * Query: Player, Team, Global passes.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * {
 * "statisticType": "player",
 * "gameType": "soccer",
 * "entityId": 16,
 * "name": "passes",
 * "parameters": {
 *  	"time": {
 *      	"start": 10753295594424116,
 *          "end" : 9999999999999999,   
 * 		}
 *    	"space": {
 *      	"startx":-50,
 *         	"starty":-33960
 *      	"endx":52489
 *     		"endy":33965
 *   	}
 * }
 * }
 * 
 * @author Thore Stratmann
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = { StatisticType.PLAYER, StatisticType.TEAM, StatisticType.GLOBAL}, 
		name = "passes",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = true),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = true)
			}
		)
public class PassesSportsQLParser implements ISportsQLParser {
	
	
	
	private static final String BALL_SIDS= "sid=12 OR sid=8 OR sid=10 OR sid=4";
	private static final double VELOCITY_CHANGE_IN_PERCENT= 0.15;
	private static final int RADIUS= 400;
	private static final int MIN_PASS_DISTANCE= 1000;

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {
		
		//SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);
		//SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper.getSpaceParameter(sportsQL);

		
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		AccessAO soccerGameAccessAO = OperatorBuildHelper.createAccessAO(OperatorBuildHelper.MAIN_STREAM_NAME);

		//soccergame_after_start = SELECT({predicate='ts>=${starttimestamp}'},soccergame)
		ArrayList<String> gameTimeSelectPredicates = new ArrayList<String>();
		gameTimeSelectPredicates.add("ts>="+OperatorBuildHelper.TS_GAME_START);

		SelectAO gameTimeSelect = OperatorBuildHelper.createSelectAO(gameTimeSelectPredicates,soccerGameAccessAO);
		allOperators.add(gameTimeSelect);
		
		
		// split_balls = ROUTE({PREDICATES = ['${ball_sids}']}, soccergame_after_start)
		
		ArrayList<String> splitBallRoutePredicates = new ArrayList<String>();
		splitBallRoutePredicates.add(BALL_SIDS);
		RouteAO splitBallRoute = OperatorBuildHelper.createRouteAO(splitBallRoutePredicates, gameTimeSelect);
		allOperators.add(splitBallRoute);

		
		//ball_velocity_changes = CHANGEDETECT({ATTR = ['vx','vy','vz'],GROUP_BY = ['sid'],RELATIVETOLERANCE = true,TOLERANCE = ${velocity_change_in_percent}},split_balls)
		
		ArrayList<String> ballVelocityChangesDetectAOAttributes = new ArrayList<String>();
		ballVelocityChangesDetectAOAttributes.add("vx");
		ballVelocityChangesDetectAOAttributes.add("vy");
		ballVelocityChangesDetectAOAttributes.add("vz");
		
		List<String> ballVelocityChangesDetectAOGroupByAttributes = new ArrayList<String>();
		ballVelocityChangesDetectAOGroupByAttributes.add("sid");
		ChangeDetectAO ballVelocityChangesDetectAO = OperatorBuildHelper.createChangeDetectAO(ballVelocityChangesDetectAOAttributes,OperatorBuildHelper.createAttributeList(ballVelocityChangesDetectAOGroupByAttributes, splitBallRoute), true, VELOCITY_CHANGE_IN_PERCENT, splitBallRoute);
		allOperators.add(ballVelocityChangesDetectAO);
		
		
		// balls_in_game_field = SELECT({predicate='x>${min_x} AND x<${max_x} AND y>${min_y} AND y<${max_y}'},ball_velocity_changes)
		
		ArrayList<String> ballInGameSelectPredicates = new ArrayList<String>();
		ballInGameSelectPredicates.add("x>"+OperatorBuildHelper.LOWERLEFT_X+" AND x<"+OperatorBuildHelper.LOWERRIGHT_X+" AND y>"+OperatorBuildHelper.LOWERLEFT_Y+" AND y<"+OperatorBuildHelper.UPPERLEFT_Y);

		SelectAO ballInGameSelect = OperatorBuildHelper.createSelectAO(ballInGameSelectPredicates,ballVelocityChangesDetectAO);
		allOperators.add(ballInGameSelect);
		
		
		// ballpos = MAP({EXPRESSIONS = ['ts',['ToPoint(x,y,z)','ball_pos']]}, balls_in_game_field)
		ArrayList<SDFExpressionParameter> ballPosExpressions = new ArrayList<SDFExpressionParameter>();
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter("ts", ballInGameSelect));
		ballPosExpressions.add(OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "ball_pos", ballInGameSelect));
		MapAO ballPosMap = OperatorBuildHelper.createMapAO(ballPosExpressions, ballInGameSelect, 0, 0);
		allOperators.add(ballPosMap);

		
		//balls_window = WINDOW({SIZE = 1,TYPE = 'TUPLE',ADVANCE = 1},ballpos)
			
		WindowAO ballWindow = OperatorBuildHelper.createWindowAO( new TimeValueItem(1, null),WindowType.TUPLE,  new TimeValueItem(1, null), ballPosMap);
		allOperators.add(ballWindow);

		
		// player_map = MAP({EXPRESSIONS = [['ToPoint(x,y,z)','player_pos'],['sid','sid']]},1:split_balls)
		ArrayList<SDFExpressionParameter> playerPosExpressions = new ArrayList<SDFExpressionParameter>();
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "player_pos", ballWindow));
		playerPosExpressions.add(OperatorBuildHelper.createExpressionParameter("sid", "sid", ballWindow));
		MapAO playerPosMap = OperatorBuildHelper.createMapAO(playerPosExpressions, ballWindow, 0, 0);
		allOperators.add(playerPosMap);

		// player_window = WINDOW({SIZE = 1,TYPE = 'TUPLE', ADVANCE= 1},player_map)

		WindowAO playerWindow = OperatorBuildHelper.createWindowAO( new TimeValueItem(1, null),WindowType.TUPLE,  new TimeValueItem(1, null), playerPosMap);
		allOperators.add(playerWindow);

		//proximity = JOIN({predicate='SpatialDistance(ball_pos,player_pos)<${radius}'},balls_window,player_window)
		
		ArrayList<String> proximityJoinPredicates = new ArrayList<String>();
		proximityJoinPredicates.add("SpatialDistance(ball_pos,player_pos)<"+RADIUS);
		JoinAO proximityJoin = OperatorBuildHelper.createJoinAO(proximityJoinPredicates, ballWindow, playerWindow);
		allOperators.add(proximityJoin);

		
		//delete_duplicates = CHANGEDETECT({ATTR = ['sid']}, proximity)

		ArrayList<String> deleteDuplicatesChangesDetectAOAttributes = new ArrayList<String>();
		deleteDuplicatesChangesDetectAOAttributes.add("sid");
		List<SDFAttribute> deleteDuplicatesChangesDetectAOSDFAttributes = OperatorBuildHelper.createAttributeList(deleteDuplicatesChangesDetectAOAttributes, proximityJoin);
		
		ChangeDetectAO deleteDuplicatesChangesDetectAO = OperatorBuildHelper.createChangeDetectAO(deleteDuplicatesChangesDetectAOSDFAttributes, 0, proximityJoin);
		allOperators.add(ballVelocityChangesDetectAO);
		
		
//		passes = STATEMAP({
//            expressions = [['__last_1.entity_id', 'entity_id_p1'],
//            				 ['entity_id', 'entity_id_p2'],
//            				 ['__last_1.entity', 'entity_p1'],
//            				 ['entity', 'entity_p2'],
//            				 ['__last_1.remark', 'remark_p1'],
//            				 ['remark', 'remark_p2'],
//            				 ['__last_1.team_id', 'team_id_p1'],
//            				 ['team_id', 'team_id_p2'],
//            				 ['__last_1.ball_pos', 'ball_pos_p1'],
//            				 ['ball_pos', 'ball_pos_p2'],
//            				 ['SpatialDistance(ball_pos,__last_1.ball_pos)', 'pass_distance'],
//            				 ['__last_1.ts', 'ball_ts_p1'],
//            				 ['ts', 'ball_ts_p2']
//            				]
//           }, delete_duplicate_entity_ids)

		
		List<SDFExpressionParameter> passesStateMapAOExpressions = new ArrayList<SDFExpressionParameter>();

		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.entity_id", "entity_id_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("entity_id", "entity_id_p2", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.entity", "entity_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("entity", "entity_p2", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.remark", "remark_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("remark", "remark_p2", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.team_id", "team_id_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("team_id", "team_id_p2", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.ball_pos", "ball_pos_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("ball_pos", "ball_pos_p2", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("SpatialDistance(ball_pos,__last_1.ball_pos)", "pass_distance", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("__last_1.ts", "ball_ts_p1", deleteDuplicatesChangesDetectAO));
		passesStateMapAOExpressions.add(OperatorBuildHelper.createExpressionParameter("ts", "ball_ts_p2", deleteDuplicatesChangesDetectAO));

		
		// group by ???
		StateMapAO passesStateMapAO = OperatorBuildHelper.createStateMapAO(passesStateMapAOExpressions, null, deleteDuplicatesChangesDetectAO);
		allOperators.add(passesStateMapAO);
		
		
		//enrich_passes = ENRICH({ predicate = 'gameRunning = true AND interruptionStart <= ball_ts_p1 AND ball_ts_p1 <= interruptionEnd'},gameinterruptions,passes)
		
		
		//project_passes = PROJECT({ATTRIBUTES = ['entity_id_p1','entity_id_p2', 'entity_p1','entity_p2','remark_p1','remark_p2','team_id_p1','team_id_p2','ball_pos_p1', 'ball_pos_p2', 'pass_distance']},enrich_passes)

		
		//passes_with_min_distance = SELECT({PREDICATE = 'pass_distance>${min_pass_distance}'},project_passes)

		
		ArrayList<String> passesWithMinDistancePredicates = new ArrayList<String>();
		passesWithMinDistancePredicates.add("pass_distance>"+MIN_PASS_DISTANCE);
		
		SelectAO passesWithMinDistance = OperatorBuildHelper.createSelectAO(passesWithMinDistancePredicates, passesStateMapAO);
		allOperators.add(passesWithMinDistance);
		
		//route_passes = ROUTE({PREDICATES = ['team_id_p1 = team_id_p2']}, passes_with_min_distance)
		
		ArrayList<String> successfulPassesPredicates = new ArrayList<String>();
		successfulPassesPredicates.add("team_id_p1 = team_id_p2");
	
		RouteAO successfulPasses = OperatorBuildHelper.createRouteAO(successfulPassesPredicates, passesWithMinDistance);

		return OperatorBuildHelper.finishQuery(successfulPasses, allOperators, sportsQL.getName());

	}
}
