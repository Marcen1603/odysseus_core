package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "goals", parameters = {})
public class GoalsSportsQLParser implements ISportsQLParser {

	// / time (seconds) between goal and kickoff
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

		// get only ball
		List<IPredicate> ballPredicates = new ArrayList<IPredicate>();
		for(int entityId : AbstractSportsDDCAccess.getBallEntityIds()) {
			IPredicate ballPredicate = OperatorBuildHelper.createRelationalPredicate(SoccerGameAttributes.ENTITY_ID + " = " + entityId);
			ballPredicates.add(ballPredicate);
		}		
		
		SelectAO ball = OperatorBuildHelper.createSelectAO(OperatorBuildHelper.createOrPredicate(ballPredicates),
				soccerGameStreamAO);
		ball.setHeartbeatRate(10);
		allOperators.add(ball);
		predicates.clear();

		// ball in game field
		String predicate = " y > " + AbstractSportsDDCAccess.getFieldYMin() + " AND y < " + AbstractSportsDDCAccess.getFieldYMax() + " AND x > " + SoccerDDCAccess.getGoalareaLeftX()
				+ " AND x < " + SoccerDDCAccess.getGoalareaRightX() + " ";
		SelectAO ball_in_game_field = OperatorBuildHelper.createSelectAO(
				predicate, ball);
		allOperators.add(ball_in_game_field);
		predicates.clear();


		// ball in goal or field stream	
		String predicateGoalAndField = "(y > " + AbstractSportsDDCAccess.getFieldYMin() + " AND y < " + AbstractSportsDDCAccess.getFieldYMax() + " AND x > "
				+ SoccerDDCAccess.getGoalareaLeftX() + " AND x < " + SoccerDDCAccess.getGoalareaRightX() + ")";
		predicateGoalAndField +=  " OR (x < " + SoccerDDCAccess.getGoalareaLeftX()
				+ " AND x > ((" + (SoccerDDCAccess.getGoalareaLeftX() - GOAL_DEPTH)
				+ ")) AND y > " + SoccerDDCAccess.getGoalareaLeftYMin() + " AND y < "
				+ SoccerDDCAccess.getGoalareaLeftYMax() + ")";
		predicateGoalAndField += " OR (x > " + SoccerDDCAccess.getGoalareaRightX() + " AND x < (("
				+ (SoccerDDCAccess.getGoalareaRightX() + GOAL_DEPTH) + ")) AND y < "
				+ SoccerDDCAccess.getGoalareaRightYMax() + " AND y > " + SoccerDDCAccess.getGoalareaRightYMin() + ")";
		
		SelectAO ball_in_goal_or_field = OperatorBuildHelper.createSelectAO(
				predicateGoalAndField, ball);
		allOperators.add(ball_in_goal_or_field);
		predicates.clear();

		// add inGoal1 / inGoal2
		MapAO inGoal = OperatorBuildHelper.createMapAO(
				getExpressionForInGoal(ball_in_goal_or_field),
				ball_in_goal_or_field, 0, 0, false);
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
				ball_in_game_field, 0, 0, false);
		allOperators.add(onCentreSpot);

		// onCentreSpot_filter: filter duplicates
		attributes.add("onCentreSpot");
		// Create ChangeDetectAO with HEARTBEATRATE = 100
		ChangeDetectAO onCentreSpot_filter = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, onCentreSpot), 0.0, 100, onCentreSpot);
		allOperators.add(onCentreSpot_filter);
		attributes.clear();
		
		// 
		SelectAO inGoalSelect = OperatorBuildHelper.createSelectAO("inGoal1 = 1 OR inGoal2 = 1", inGoal_filter);
		allOperators.add(inGoalSelect);

		SelectAO onSpotSelect = OperatorBuildHelper.createSelectAO("onCentreSpot = 1", onCentreSpot_filter);
		allOperators.add(onSpotSelect);
		
		// window for goals and spot
		TimeWindowAO goalWindow = OperatorBuildHelper.createTimeWindowAO(30, "SECONDS", inGoalSelect);
		allOperators.add(goalWindow);
		
		TimeWindowAO spotWindow = OperatorBuildHelper.createTimeWindowAO(5, "SECONDS", onSpotSelect);
		allOperators.add(spotWindow);
		
		
		// join goals of last x seconds with centre spot stream
		JoinAO goals_join = OperatorBuildHelper.createJoinAO("true", "MANY_MANY", goalWindow, spotWindow);
		allOperators.add(goals_join);
		
		
		attributes.add("goal_ts");
		ChangeDetectAO goal_changedetect = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, goals_join), 0.0, 100, goals_join);
		goal_changedetect.setDeliverFirstElement(true);
		allOperators.add(goal_changedetect);
		attributes.clear();
		
		// aggregate goals
		AggregateAO goals_aggregate = createGoalsAggregate(goal_changedetect);
		allOperators.add(goals_aggregate);
		
		return OperatorBuildHelper.finishQuery(goals_aggregate, allOperators,
				sportsQL.getName());
	}
	
	private AggregateAO createGoalsAggregate(ILogicalOperator input) {
		List<String> functions3 = new ArrayList<String>();
		functions3.add("SUM");
		functions3.add("SUM");

		List<String> inputAttributeNames3 = new ArrayList<String>();
		inputAttributeNames3.add("inGoal1");
		inputAttributeNames3.add("inGoal2");

		List<String> outputAttributeNames3 = new ArrayList<String>();
		outputAttributeNames3.add("sum_inGoal1");
		outputAttributeNames3.add("sum_inGoal2");
		
		List<String> outputTypes = new ArrayList<String>();
		outputTypes.add("Integer");
		outputTypes.add("Integer");

		return OperatorBuildHelper.createAggregateAO(
				functions3, null, inputAttributeNames3, outputAttributeNames3,
				outputTypes, input, -1);
	}

	private List<SDFExpressionParameter> getExpressionForInGoal(
			ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("toInteger(ts)", "goal_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("x", "goal_x", source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("y", "goal_y", source);
		SDFExpressionParameter ex4 = OperatorBuildHelper
				.createExpressionParameter("z", "goal_z", source);
		SDFExpressionParameter ex5 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif( x < " + SoccerDDCAccess.getGoalareaLeftX()
						+ " AND x > " + (SoccerDDCAccess.getGoalareaLeftX() - GOAL_DEPTH)
						+ " AND y > " + SoccerDDCAccess.getGoalareaLeftYMin()+ " AND y < "
						+ SoccerDDCAccess.getGoalareaLeftYMax() + " , 1, 0))", "inGoal1", source);
		SDFExpressionParameter ex6 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif( x > " + SoccerDDCAccess.getGoalareaRightX()
						+ " AND x < " + (SoccerDDCAccess.getGoalareaRightX() + GOAL_DEPTH)
						+ " AND y < " + SoccerDDCAccess.getGoalareaRightYMax() + " AND y > "
						+ SoccerDDCAccess.getGoalareaRightYMin() + " , 1, 0))", "inGoal2", source);
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
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("toInteger(ts - 30000000)", "n_ts", source);	
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);

		return expressions;
	}

}
