package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/***
 * SportsQL Parser for Crosses.
 * 
 * @author Simon Eilers
 *
 */

@SportsQL(gameTypes = { GameType.SOCCER }, name = "crosses", parameters = {}, statisticTypes = { StatisticType.GLOBAL })
public class CrossesGlobalSportsQLParser implements ISportsQLParser {

	public final static String TIME_BETWEEN_TARGETZONE_CROSSINGZONE = "2000000000000";
	
	//Borders from where the cross has to be shot
	public final static String CROSSINGZONE_RIGHT_SIDE = "11330";
	public final static String CROSSINGZONE_LEFT_SIDE = "41184";
	
	//Zone into which the ball has to fly
	public final static String TARGETZONE_LEFT_HALF = "-18983";
	public final static String TARGETZONE_RIGHT_HALF = "18983";
	
	public final static String CENTER_LINE = "0";
	
	//minimal height the ball has to have
	public final static String MIN_BALL_HEIGHT = "2500";
	
	public static final int BALL_SIDS[] = {4,8,10,12};
	
	/**
	 * Parses sportsQL
	 */
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<String> predicates = new ArrayList<>();
		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();
		
		// 1. Time Parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper.getTimeParameter(sportsQL);
		SelectAO gameTimeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParameter, soccerGameStreamAO);
		allOperators.add(gameTimeSelect);
		
		
		// 2. Split the data stream to one ball (port 0) and one player (port 1) data stream
		RouteAO splitSoccerDataRoute = createSplitSoccerDataRouteAO(gameTimeSelect);
		allOperators.add(splitSoccerDataRoute);
		
		SelectAO selectBallInField = OperatorBuildHelper.createSelectAO("x >" + OperatorBuildHelper.LOWERLEFT_X + "AND x <" + OperatorBuildHelper.LOWERRIGHT_X + "AND y>" + OperatorBuildHelper.LOWERLEFT_Y + "AND y<" + OperatorBuildHelper.UPPERLEFT_Y, splitSoccerDataRoute);
		allOperators.add(selectBallInField);
		
//		inCrossingZone = MAP({
//			expressions = [
//				['ts','cts'],
//				['eif( (y < ${crossing_bounds_center_line}) AND ((x < ${crossing_bounds_upper_x}) OR (x > ${crossing_bounds_lower_x})) , 1, 0)', 'in_crossing_zone_left'],
//				['eif( (y > ${crossing_bounds_center_line}) AND ((x < ${crossing_bounds_upper_x}) OR (x > ${crossing_bounds_lower_x})) , 1, 0)', 'in_crossing_zone_right']
//			]}, ball_in_game_field)
		
		ArrayList<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, "cts", selectBallInField));
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("eif((y < " + CENTER_LINE
						+ ") AND ((x < " + CROSSINGZONE_RIGHT_SIDE + ") OR (x > "
						+ CROSSINGZONE_LEFT_SIDE + ")), 1, 0)", "in_crossing_zone_left", selectBallInField));
		
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("eif( (y > " + CENTER_LINE
						+ ") AND ((x < " + CROSSINGZONE_RIGHT_SIDE + ") OR (x > "
						+ CROSSINGZONE_LEFT_SIDE + ")), 1, 0)", "in_crossing_zone_right", selectBallInField));

		MapAO inCrossingZoneMap = OperatorBuildHelper.createMapAO(expressions, selectBallInField, 0, 0);
		allOperators.add(inCrossingZoneMap);
		expressions.clear();
		
//		lastInBoxMarked = STATEMAP({EXPRESSIONS = ['cts',
//		       				['eif(in_crossing_zone_left = 0 AND __last_1.in_crossing_zone_left = 1, 1,0)', 'last_inBox_left'],
//		       				['eif(in_crossing_zone_right = 0 AND __last_1.in_crossing_zone_right = 1, 1,0)', 'last_inBox_right']
//		       				]}, inCrossingZone)
		expressions.add(OperatorBuildHelper.createExpressionParameter("cts", inCrossingZoneMap));
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("eif(in_crossing_zone_left = 0 AND __last_1.in_crossing_zone_left = 1, 1,0)","last_in_CrossingZone_left", inCrossingZoneMap));
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("eif(in_crossing_zone_right = 0 AND __last_1.in_crossing_zone_right = 1, 1,0)","last_in_CrossingZone_right", inCrossingZoneMap));
		StateMapAO lastInBoxMarked = OperatorBuildHelper.createStateMapAO(expressions, inCrossingZoneMap);
		allOperators.add(lastInBoxMarked);
		expressions.clear();
		
		String predicate = "(last_in_CrossingZone_right = 1) OR (last_in_CrossingZone_left = 1)";
		SelectAO lastInBox = OperatorBuildHelper.createSelectAO(predicate, lastInBoxMarked);
		
		allOperators.add(lastInBox);
//		
//		inTargetBox = MAP({
//			expressions = [
//				['ts', 'cross_ball_ts'],
//				['eif( (y < ${crossing_bounds_lower_y}) AND (x > ${crossing_bounds_upper_x}) AND (x < ${crossing_bounds_lower_x}) AND (z > ${min_ball_height}), 1, 0)', 'in_target_box_left'],
//				['eif( (y > ${crossing_bounds_upper_y}) AND (x > ${crossing_bounds_upper_x}) AND (x < ${crossing_bounds_lower_x}) AND (z > ${min_ball_height}), 1, 0)', 'in_target_box_right']
//			]}, ball_in_game_field)
		
		expressions = new ArrayList<>();
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS, "cross_ball_ts", selectBallInField));
		
		expressions.add(OperatorBuildHelper.createExpressionParameter("eif( (y <" + TARGETZONE_LEFT_HALF + ") AND ( x > " + CROSSINGZONE_RIGHT_SIDE +") AND (x <" + CROSSINGZONE_LEFT_SIDE +") AND (z >" + MIN_BALL_HEIGHT +"),1,0)","in_target_zone_left", selectBallInField));
		expressions.add(OperatorBuildHelper.createExpressionParameter("eif( (y >" + TARGETZONE_RIGHT_HALF + ") AND ( x > " + CROSSINGZONE_RIGHT_SIDE +") AND (x <" + CROSSINGZONE_LEFT_SIDE +") AND (z >" + MIN_BALL_HEIGHT +"),1,0)","in_target_zone_right", selectBallInField));
		
		MapAO inTargetZone = OperatorBuildHelper.createMapAO(expressions, selectBallInField, 0, 0);
		allOperators.add(inTargetZone);
		expressions.clear();
		
		List<String> attributes = new ArrayList<>();
		attributes.add("in_target_zone_left");
		attributes.add("in_target_zone_right");
		ChangeDetectAO inTargetZoneFilter = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes, inTargetZone), 0, 100, inTargetZone);
		allOperators.add(inTargetZoneFilter);
		attributes.clear();
		
		ElementWindowAO windowTargetZone = OperatorBuildHelper.createElementWindowAO(1, 1, inTargetZoneFilter);
		ElementWindowAO windowLastInBox = OperatorBuildHelper.createElementWindowAO(1, 1, lastInBox);
		
		allOperators.add(windowTargetZone);
		allOperators.add(windowLastInBox);
		
//		cross_join = JOIN({
//			PREDICATE = '(cross_ball_ts >= cts) AND (cross_ball_ts <= (cts + (${t_diff_cross_and_in_target} * 1000000000000))) AND ((last_inBox_left = 1 AND in_target_box_left = 1) OR (last_inBox_right = 1 AND in_target_box_right = 1))'
//		},windowLastInBox, windowTargetBox)
		
		predicates.add("(cross_ball_ts >= cts) AND (cross_ball_ts <= (cts +" + TIME_BETWEEN_TARGETZONE_CROSSINGZONE +  ")) AND ((last_in_CrossingZone_left = 1 AND in_target_zone_left = 1) OR (last_in_CrossingZone_right = 1 AND in_target_zone_right = 1))");
		JoinAO join = OperatorBuildHelper.createJoinAO(predicates, windowLastInBox, windowTargetZone);
		allOperators.add(join);
		predicates.clear();
		
		expressions = new ArrayList<>();
		expressions.add(OperatorBuildHelper.createExpressionParameter("last_in_CrossingZone_left", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("last_in_CrossingZone_right", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("in_target_zone_right", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("in_target_zone_left", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("cts", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("cross_ball_ts", join));
		expressions.add(OperatorBuildHelper.createExpressionParameter("eif(__last_1.cts = cts OR __last_1.cross_ball_ts = cross_ball_ts, 1,0)", "isNotDuplicate", join));
		StateMapAO checkForEqualTimestamp = OperatorBuildHelper.createStateMapAO(expressions, join);
		expressions.clear();
		
		allOperators.add(checkForEqualTimestamp);
		
		String predicateDuplicate = "isNotDuplicate = 0";
		SelectAO checkSelect = OperatorBuildHelper.createSelectAO(predicateDuplicate, checkForEqualTimestamp);
		
		allOperators.add(checkSelect);
		
		expressions.add(OperatorBuildHelper.createExpressionParameter("in_target_zone_left","Cross_Left_Half", checkSelect));
		expressions.add(OperatorBuildHelper.createExpressionParameter("in_target_zone_right","Cross_Right_Half", checkSelect));
		MapAO result = OperatorBuildHelper.createStateMapAO(expressions, checkSelect);
		allOperators.add(result);
		
		return OperatorBuildHelper.finishQuery(result, allOperators, sportsQL.getName());		
	}
	
	private RouteAO createSplitSoccerDataRouteAO(ILogicalOperator source) {				
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
