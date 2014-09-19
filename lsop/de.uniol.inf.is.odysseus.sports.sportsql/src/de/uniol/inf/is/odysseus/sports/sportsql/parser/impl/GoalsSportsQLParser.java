package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "goals", parameters = {})
public class GoalsSportsQLParser implements ISportsQLParser {

	// / time (seconds) between goal and kickoff
	private static final int T_GOAL_KICKOFF = 30;
	private static final String ONE_SECOND = "1000000000000";
	private static final int GOAL_DEPTH = 2400;
	
	// TODO:
	// this is the kickoff area around the center spot
	// usually it has a size of 400mm but in DEBS data stream they lay the ball in a area of 2m around the center
	private static final int KICKOFF_AREA_RADIUS = 2000;
	

	@SuppressWarnings("rawtypes")
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<String> predicates = new ArrayList<String>();
		ArrayList<String> attributes = new ArrayList<String>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();

		// count goals with the beginning of the game
		predicates.add("ts >= " + OperatorBuildHelper.TS_GAME_START);
		SelectAO soccergame_after_start = OperatorBuildHelper.createSelectAO(
				predicates, soccerGameStreamAO);
		allOperators.add(soccergame_after_start);
		predicates.clear();

		// get only ball	
		List<IPredicate> ballPredicates = new ArrayList<IPredicate>();
		for(int sensorId : AbstractSportsDDCAccess.getBallSensorIds()) {
			IPredicate ballPredicate = OperatorBuildHelper.createRelationalPredicate("sid = " + sensorId);
			ballPredicates.add(ballPredicate);
		}		
		
		SelectAO ball = OperatorBuildHelper.createSelectAO(OperatorBuildHelper.createOrPredicate(ballPredicates),
				soccergame_after_start);
		allOperators.add(ball);
		predicates.clear();

		// ball in game field
		String predicate = " x > " + AbstractSportsDDCAccess.getFieldXMin() + " AND x < " + AbstractSportsDDCAccess.getFieldXMax() + " AND y > " + SoccerDDCAccess.getGoalareaLeftY()
				+ " AND y < " + SoccerDDCAccess.getGoalareaRightY() + " ";
		SelectAO ball_in_game_field = OperatorBuildHelper.createSelectAO(
				predicate, ball);
		allOperators.add(ball_in_game_field);
		predicates.clear();


		// ball in goal or field stream	
		String predicateGoalAndField = "(x > " + AbstractSportsDDCAccess.getFieldXMin() + " AND x < " + AbstractSportsDDCAccess.getFieldXMax() + " AND y > "
				+ SoccerDDCAccess.getGoalareaLeftY() + " AND y < " + SoccerDDCAccess.getGoalareaRightY() + ")";
		
		predicateGoalAndField +=  " OR (y < " + SoccerDDCAccess.getGoalareaLeftY()
				+ " AND y > ((" + (SoccerDDCAccess.getGoalareaLeftY() - GOAL_DEPTH)
				+ ")) AND x > " + SoccerDDCAccess.getGoalareaLeftXMin() + " AND x < "
				+ SoccerDDCAccess.getGoalareaLeftXMax() + ")";
		
		predicateGoalAndField += " OR (y > " + SoccerDDCAccess.getGoalareaRightY() + " AND y < (("
				+ (SoccerDDCAccess.getGoalareaRightY() + GOAL_DEPTH) + ")) AND x < "
				+ SoccerDDCAccess.getGoalareaRightXMax() + " AND x > " + SoccerDDCAccess.getGoalareaRightXMin() + ")";
		
		SelectAO ball_in_goal_or_field = OperatorBuildHelper.createSelectAO(
				predicateGoalAndField, ball);
		allOperators.add(ball_in_goal_or_field);
		predicates.clear();

		// add inGoal1 / inGoal2
		MapAO inGoal = OperatorBuildHelper.createMapAO(
				getExpressionForInGoal(ball_in_goal_or_field),
				ball_in_goal_or_field, 0, 0);
		allOperators.add(inGoal);

		// inGoal filter: remove duplicates
		attributes.add("inGoal1");
		attributes.add("inGoal2");
		// Create ChangeDetectAO with HEARTBEATRATE = 100
		ChangeDetectAO inGoal_filter = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, inGoal), 0.0, 100, inGoal);
		allOperators.add(inGoal_filter);
		attributes.clear();

		// onCentreSpot: Ball inside centre spot region
		MapAO onCentreSpot = OperatorBuildHelper.createMapAO(
				getExpressionForOnCentreSpot(ball_in_game_field),
				ball_in_game_field, 0, 0);
		allOperators.add(onCentreSpot);

		// onCentreSpot_filter: filter duplicates
		attributes.add("onCentreSpot");
		// Create ChangeDetectAO with HEARTBEATRATE = 100
		ChangeDetectAO onCentreSpot_filter = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, onCentreSpot), 0.0, 100, onCentreSpot);
		allOperators.add(onCentreSpot_filter);
		attributes.clear();
		
		// join goals of last x seconds with centre spot stream
		String joinPredicate = "goal_ts > (spot_ball_ts - (" + T_GOAL_KICKOFF + " * "
				+ ONE_SECOND + ")) AND (inGoal1 = 1 OR inGoal2 = 1)";
		JoinAO goals_join = OperatorBuildHelper.createJoinAO(joinPredicate, "MANY_MANY",
				inGoal_filter, onCentreSpot_filter);
		allOperators.add(goals_join);
		predicates.clear();

		// remove duplicates of detected goals
		attributes.add("goal_ts");
		ChangeDetectAO goals = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, goals_join), 0.0, goals_join);
		goals.setDeliverFirstElement(true);
		allOperators.add(goals);
		attributes.clear();

		attributes.add("goal_ts");
		attributes.add("inGoal1");
		attributes.add("inGoal2");
		ProjectAO goals_project = OperatorBuildHelper.createProjectAO(
				attributes, goals);
		allOperators.add(goals_project);
		attributes.clear();

		return OperatorBuildHelper.finishQuery(goals_project, allOperators,
				sportsQL.getName());
	}

	private List<SDFExpressionParameter> getExpressionForInGoal(
			ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("ts", "goal_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("x", "goal_x", source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("y", "goal_y", source);
		SDFExpressionParameter ex4 = OperatorBuildHelper
				.createExpressionParameter("z", "goal_z", source);
		SDFExpressionParameter ex5 = OperatorBuildHelper
				.createExpressionParameter("eif( y < " + SoccerDDCAccess.getGoalareaLeftY()
						+ " AND y > " + (SoccerDDCAccess.getGoalareaLeftY() - GOAL_DEPTH)
						+ " AND x > " + SoccerDDCAccess.getGoalareaLeftXMin()+ " AND x < "
						+ SoccerDDCAccess.getGoalareaLeftXMax() + " , 1, 0)", "inGoal1", source);
		SDFExpressionParameter ex6 = OperatorBuildHelper
				.createExpressionParameter("eif( y > " + SoccerDDCAccess.getGoalareaRightY()
						+ " AND y < " + (SoccerDDCAccess.getGoalareaRightY() + GOAL_DEPTH)
						+ " AND x < " + SoccerDDCAccess.getGoalareaRightXMax() + " AND x > "
						+ SoccerDDCAccess.getGoalareaRightXMin() + " , 1, 0)", "inGoal2", source);
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
		expressions.add(ex4);
		expressions.add(ex5);
		expressions.add(ex6);

		return expressions;
	}

	private List<SDFExpressionParameter> getExpressionForOnCentreSpot(
			ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {
		
		double centerX = AbstractSportsDDCAccess.calculateCenterX();
		double centerY = AbstractSportsDDCAccess.calculateCenterY();
		
		double spotTopX = centerX - KICKOFF_AREA_RADIUS;
		double spotTopY = centerY - KICKOFF_AREA_RADIUS;
		double spotBottomX = centerX + KICKOFF_AREA_RADIUS;
		double spotBottomY = centerY + KICKOFF_AREA_RADIUS;
		
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("ts", "spot_ball_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("eif( x >= " + spotTopX
						+ " AND x <= " + spotBottomX + " AND y >= "
						+ spotTopY + " AND y <= " + spotBottomY
						+ " , 1, 0)", "onCentreSpot", source);
		expressions.add(ex1);
		expressions.add(ex2);

		return expressions;
	}

}
