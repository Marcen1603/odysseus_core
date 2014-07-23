package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "goals")
public class GoalsSportsQLParser implements ISportsQLParser {

	// goal 1 (left goal)
	private static final int GOALPOST1_RIGHT 	= 22560;
	private static final int GOALPOST1_LEFT 	= 29880;
	private static final int GOALLINE1 			= -33968;

	/// goal 2 (right goal)
	private static final int GOALPOST2_RIGHT 	= 29898;
	private static final int GOALPOST2_LEFT 	= 22578;
	private static final int GOALLINE2 			= 33941;  

	/// goal dimensions
	private static final int GOAL_HEIGHT 		= 2440;
	private static final int GOAL_DEPTH 		= 2500;

	/// centre spot area
	private static final int CENTRESPOT_X1 		= 24000;	/// upper border
	private static final int CENTRESPOT_X2 		= 30000;	/// lower border
	private static final int CENTRESPOT_Y1 		= -2000;	/// left border
	private static final int CENTRESPOT_Y2 		= 2000;		/// right border

	/// time (seconds) between goal and kickoff
	private static final int T_GOAL_KICKOFF 	= 30;		
	
	private static final int MIN_X 				= OperatorBuildHelper.LOWERLEFT_X;
	private static final int MAX_X 				= OperatorBuildHelper.LOWERRIGHT_X;
	private static final int MIN_Y 				= GOALLINE1;
	private static final int MAX_Y 				= GOALLINE2;
	
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<String> predicates = new ArrayList<String>();
		ArrayList<String> attributes = new ArrayList<String>();
		
		String soccerGameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		AccessAO soccerGameAccessAO = OperatorBuildHelper.createAccessAO(soccerGameSourceName);
		
		// count goals with the beginning of the game
		predicates.add("ts >= "+ OperatorBuildHelper.TS_GAME_START);
		SelectAO soccergame_after_start = OperatorBuildHelper.createSelectAO(
				predicates, soccerGameAccessAO);
		allOperators.add(soccergame_after_start);
		predicates.clear();
		
		// get only ball
		predicates.add("sid=12 OR sid=8 OR sid=10 OR sid=4");
		RouteAO ball = OperatorBuildHelper.createRouteAO(predicates, soccergame_after_start);
		allOperators.add(ball);
		predicates.clear();
		
		// ball in game field
		predicates.add(" x > " + MIN_X + " AND x < " + MAX_X +
				" AND y > " + MIN_Y + " AND y < " + MAX_Y + " ");
		RouteAO ball_in_game_field = OperatorBuildHelper.createRouteAO(predicates, ball);
		allOperators.add(ball_in_game_field);
		predicates.clear();
		
		// ball in goal or field stream
		predicates.add(" (x > " + MIN_X + " AND x < " + MAX_X + " AND y > " + MIN_Y +
				" AND y < " + MAX_Y + ") OR (y < " + GOALLINE1 + 
				" AND y > ((" + GOALLINE1 + ")-(" + GOAL_DEPTH + ")) AND x > " + GOALPOST1_RIGHT + 
				" AND x < " + GOALPOST1_LEFT + ") OR (y > " + GOALLINE2 + 
				" AND y < ((" + GOALLINE2 + ")+(" + GOAL_DEPTH + ")) AND x < " + GOALPOST2_RIGHT + 
				" AND x > " + GOALPOST2_LEFT + ")");
		RouteAO ball_in_goal_or_field = OperatorBuildHelper.createRouteAO(
				predicates, soccergame_after_start);
		allOperators.add(ball_in_goal_or_field);
		predicates.clear();
		
		// add inGoal1 / inGoal2 
		MapAO inGoal = OperatorBuildHelper.createMapAO(getExpressionForInGoal(ball_in_goal_or_field), 
				ball_in_goal_or_field, 0, 0);
		allOperators.add(inGoal);

		// inGoal filter: remove duplicates
		attributes.add("inGoal1");
		attributes.add("inGoal2");
		ChangeDetectAO inGoal_filter = OperatorBuildHelper.createChangeDetectAO(
				OperatorBuildHelper.createAttributeList(attributes, inGoal), 0.0, inGoal);
		allOperators.add(inGoal_filter);
		attributes.clear();
		
		// onCentreSpot: Ball inside centre spot region
		MapAO onCentreSpot = OperatorBuildHelper.createMapAO(getExpressionForOnCentreSpot(
				ball_in_game_field), ball_in_game_field, 0, 0);
		allOperators.add(onCentreSpot);
		
		// onCentreSpot_filter: filter duplicates
		attributes.add("onCentreSpot");
		ChangeDetectAO onCentreSpot_filter = OperatorBuildHelper.createChangeDetectAO(
				OperatorBuildHelper.createAttributeList(attributes, onCentreSpot), 0.0, onCentreSpot);
		allOperators.add(onCentreSpot_filter);
		attributes.clear();
		
		// join goals of last x seconds with centre spot stream
		predicates.add(" goal_ts > (spot_ball_ts - (" + T_GOAL_KICKOFF + " * 1000000000000)) AND (inGoal1 = 1 OR inGoal2 = 1) ");
		JoinAO goals_join = OperatorBuildHelper.createJoinAO(predicates, inGoal_filter, onCentreSpot_filter);
		allOperators.add(goals_join);
		predicates.clear();
		
		// remove duplicates of detected goals
		attributes.add("goal_ts");
		ChangeDetectAO goals = OperatorBuildHelper.createChangeDetectAO(
				OperatorBuildHelper.createAttributeList(attributes, goals_join), 0.0, goals_join);
		goals.setDeliverFirstElement(true);
		allOperators.add(goals);
		attributes.clear();
		
		
		attributes.add("goal_ts");
		attributes.add("inGoal1");
		attributes.add("inGoal2");
		ProjectAO goals_project = OperatorBuildHelper.createProjectAO(attributes, goals);
		allOperators.add(goals_project);
		attributes.clear();
		
		return OperatorBuildHelper.finishQuery(goals_project, allOperators, sportsQL.getName());
	}
	
	private List<SDFExpressionParameter> getExpressionForInGoal(ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper.createExpressionParameter("ts", "goal_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper.createExpressionParameter("x", "goal_x", source);
		SDFExpressionParameter ex3 = OperatorBuildHelper.createExpressionParameter("y", "goal_y", source);
		SDFExpressionParameter ex4 = OperatorBuildHelper.createExpressionParameter("z", "goal_z", source);
		SDFExpressionParameter ex5 = OperatorBuildHelper.createExpressionParameter("eif( y < " + GOALLINE1 + 
				" AND y > (" + GOALLINE1 + ")-(" + GOAL_DEPTH + ") AND x > " + GOALPOST1_RIGHT + " AND x < " + GOALPOST1_LEFT + " , 1, 0)", "inGoal1", source);
		SDFExpressionParameter ex6 = OperatorBuildHelper.createExpressionParameter("eif( y > " + GOALLINE2 +
				" AND y < (" + GOALLINE2 + ")+(" + GOAL_DEPTH + ") AND x < " + GOALPOST2_RIGHT + " AND x > " + GOALPOST2_LEFT + " , 1, 0)","inGoal2", source);
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
		expressions.add(ex4);
		expressions.add(ex5);
		expressions.add(ex6);
		
		return expressions;
	}
	
	private List<SDFExpressionParameter> getExpressionForOnCentreSpot(ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper.createExpressionParameter("ts", "spot_ball_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper.createExpressionParameter("eif( x >= " + CENTRESPOT_X1 +
				" AND x <= " + CENTRESPOT_X2 + " AND y >= " + CENTRESPOT_Y1 + " AND y <= " + CENTRESPOT_Y2 + " , 1, 0)", "onCentreSpot", source);
		expressions.add(ex1);
		expressions.add(ex2);
		
		return expressions;
	}

}
