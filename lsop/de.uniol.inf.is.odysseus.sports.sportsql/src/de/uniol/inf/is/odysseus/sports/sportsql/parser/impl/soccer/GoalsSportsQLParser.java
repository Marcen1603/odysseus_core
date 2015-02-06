package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "goals", parameters = {})
public class GoalsSportsQLParser implements ISportsQLParser {

	// / Goal values
	private static final int GOAL_DEPTH = 2400;
	private final int GOAL_HEIGHT = 2300;

	// time in seconds between goal and kickoff
	private final BigInteger TIME_BETWEEN = new BigInteger("30");

	// TODO:
	// this is the kickoff area around the center spot
	// usually it has a size of 400mm but in DEBS data stream they lay the ball
	// in a area of 2m around the center
	private static final int KICKOFF_AREA_RADIUS = 2000;
	private String ATT_TEAM1;
	private String ATT_TEAM2;

	private final String ATT_GOALTS = "goal_ts";
	private final String ATT_N_TS = "n_ts";
	private final String ATT_ONCENTRESPOT = "onCentreSpot";


	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {

		ATT_TEAM1 = "GoalsTeam" + SoccerDDCAccess.getLeftGoalTeamId();
		ATT_TEAM2 = "GoalsTeam" + SoccerDDCAccess.getRightGoalTeamId();


		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<String> predicates = new ArrayList<String>();
		ArrayList<String> attributes = new ArrayList<String>();

		ILogicalOperator soccerGameStreamAO = OperatorBuildHelper
				.createGameSource(session);
		//allOperators.add(soccerGameStreamAO);
		
		for(Integer ballId : AbstractSportsDDCAccess.getBallEntityIds()){
			predicates.add("entity_id = " + ballId);
		}
		
		SelectAO ball = OperatorBuildHelper.createSelectAO(predicates, soccerGameStreamAO);

		allOperators.add(ball);

		// ball in goal or field stream
		String predicateGoalAndField = "(" + IntermediateSchemaAttributes.Y
				+ " > " + AbstractSportsDDCAccess.getFieldYMin() + " AND "
				+ IntermediateSchemaAttributes.Y + " < "
				+ AbstractSportsDDCAccess.getFieldYMax() + " AND "
				+ IntermediateSchemaAttributes.X + "> "
				+ SoccerDDCAccess.getGoalareaLeftX() + " AND "
				+ IntermediateSchemaAttributes.X + "< "
				+ SoccerDDCAccess.getGoalareaRightX() + ")";
		predicateGoalAndField += " OR (" + IntermediateSchemaAttributes.X
				+ " < " + SoccerDDCAccess.getGoalareaLeftX() + " AND "
				+ IntermediateSchemaAttributes.X + " > (("
				+ (SoccerDDCAccess.getGoalareaLeftX() - GOAL_DEPTH) + ")) AND "
				+ IntermediateSchemaAttributes.Y + " > "
				+ SoccerDDCAccess.getGoalareaLeftYMin() + " AND "
				+ IntermediateSchemaAttributes.Y + " < "
				+ SoccerDDCAccess.getGoalareaLeftYMax() + ")";
		predicateGoalAndField += " OR (" + IntermediateSchemaAttributes.X
				+ " > " + SoccerDDCAccess.getGoalareaRightX() + " AND "
				+ IntermediateSchemaAttributes.X + " < (("
				+ (SoccerDDCAccess.getGoalareaRightX() + GOAL_DEPTH)
				+ ")) AND " + IntermediateSchemaAttributes.Y + " < "
				+ SoccerDDCAccess.getGoalareaRightYMax() + " AND "
				+ IntermediateSchemaAttributes.Y + " > "
				+ SoccerDDCAccess.getGoalareaRightYMin() + ")";


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
		attributes.add(ATT_TEAM1);
		attributes.add(ATT_TEAM2);
		// Create ChangeDetectAO with HEARTBEATRATE = 100
		ChangeDetectAO inGoal_filter = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, inGoal), 0.0, 100, inGoal);
		allOperators.add(inGoal_filter);
		attributes.clear();

		// onCentreSpot: Ball inside centre spot region
		MapAO onCentreSpot = OperatorBuildHelper.createMapAO(
				getExpressionForOnCentreSpot(ball_in_goal_or_field),
				ball_in_goal_or_field, 0, 0, false);
		allOperators.add(onCentreSpot);

		// onCentreSpot_filter: filter duplicates
		attributes.add(ATT_ONCENTRESPOT);
		// Create ChangeDetectAO with HEARTBEATRATE = 100
		ChangeDetectAO onCentreSpot_filter = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, onCentreSpot), 0.0, 100, onCentreSpot);
		allOperators.add(onCentreSpot_filter);
		attributes.clear();

		//
		SelectAO inGoalSelect = OperatorBuildHelper.createSelectAO(ATT_TEAM1
				+ " = 1 OR " + ATT_TEAM2 + "= 1", inGoal_filter);
		allOperators.add(inGoalSelect);

		SelectAO onSpotSelect = OperatorBuildHelper.createSelectAO(
				ATT_ONCENTRESPOT + "= 1", onCentreSpot_filter);
		allOperators.add(onSpotSelect);

		// window for goals and spot
		TimeWindowAO goalWindow = OperatorBuildHelper.createTimeWindowAO(30,
				"SECONDS", inGoalSelect);
		allOperators.add(goalWindow);

		TimeWindowAO spotWindow = OperatorBuildHelper.createTimeWindowAO(5,
				"SECONDS", onSpotSelect);
		allOperators.add(spotWindow);

		// join goals of last x seconds with centre spot stream
		JoinAO goals_join = OperatorBuildHelper.createJoinAO("true",
				"ONE_ONE", goalWindow, spotWindow);

		allOperators.add(goals_join);


		TimestampAO clearEndTS = OperatorBuildHelper.clearEndTimestamp(goals_join);
		allOperators.add(clearEndTS);

		AssureHeartbeatAO heart = OperatorBuildHelper.createHeartbeat(5000, clearEndTS);
		allOperators.add(heart);

		// aggregate goals
		AggregateAO goals_aggregate = createGoalsAggregate(heart);
		allOperators.add(goals_aggregate);

		return OperatorBuildHelper.finishQuery(goals_aggregate, allOperators,
				sportsQL.getDisplayName());
	}

	private AggregateAO createGoalsAggregate(ILogicalOperator input) {

		List<String> functions3 = new ArrayList<String>();
		functions3.add("SUM");
		functions3.add("SUM");

		List<String> inputAttributeNames3 = new ArrayList<String>();
		List<String> outputAttributeNames3 = new ArrayList<String>();
		
		Integer nr1 = Integer.valueOf(ATT_TEAM1.substring(ATT_TEAM1.length()-1));
		Integer nr2 = Integer.valueOf(ATT_TEAM2.substring(ATT_TEAM2.length()-1));
		
		if(nr1 < nr2){
			outputAttributeNames3.add("sum_" + ATT_TEAM1);
			outputAttributeNames3.add("sum_" + ATT_TEAM2);
			inputAttributeNames3.add(ATT_TEAM1);
			inputAttributeNames3.add(ATT_TEAM2);
		}else{
			outputAttributeNames3.add("sum_" + ATT_TEAM2);
			outputAttributeNames3.add("sum_" + ATT_TEAM1);
			inputAttributeNames3.add(ATT_TEAM2);
			inputAttributeNames3.add(ATT_TEAM1);
		}
		

		List<String> outputTypes = new ArrayList<String>();
		outputTypes.add("Integer");
		outputTypes.add("Integer");

		return OperatorBuildHelper.createAggregateAO(functions3, null,
				inputAttributeNames3, outputAttributeNames3, outputTypes,
				input, 1);
	}

	private List<SDFExpressionParameter> getExpressionForInGoal(
			ILogicalOperator source) throws NumberFormatException,
			MissingDDCEntryException {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("toDouble("
						+ IntermediateSchemaAttributes.TS + ")", ATT_GOALTS,
						source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.X,
						"goal_x", source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Y,
						"goal_y", source);
		SDFExpressionParameter ex4 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.Z,
						"goal_z", source);
		SDFExpressionParameter ex5 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif( x < "
						+ SoccerDDCAccess.getGoalareaLeftX() + " AND "
						+ IntermediateSchemaAttributes.X + " > "
						+ (SoccerDDCAccess.getGoalareaLeftX() - GOAL_DEPTH)
						+ " AND " + IntermediateSchemaAttributes.Y + " > "
						+ SoccerDDCAccess.getGoalareaLeftYMin() + " AND "
						+ IntermediateSchemaAttributes.Y + " < "
						+ SoccerDDCAccess.getGoalareaLeftYMax() + "AND "
						+ IntermediateSchemaAttributes.Z + " < " + GOAL_HEIGHT
						+ " , 1, 0))", ATT_TEAM2, source);
		SDFExpressionParameter ex6 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif( "
						+ IntermediateSchemaAttributes.X + " > "
						+ SoccerDDCAccess.getGoalareaRightX() + " AND x < "
						+ (SoccerDDCAccess.getGoalareaRightX() + GOAL_DEPTH)
						+ " AND " + IntermediateSchemaAttributes.Y + " < "
						+ SoccerDDCAccess.getGoalareaRightYMax() + " AND "
						+ IntermediateSchemaAttributes.Y + " > "
						+ SoccerDDCAccess.getGoalareaRightYMin() + "AND "
						+ IntermediateSchemaAttributes.Z + " < " + GOAL_HEIGHT
						+ " , 1, 0))", ATT_TEAM1, source);
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
		expressions.add(ex4);
		expressions.add(ex5);
		expressions.add(ex6);

		return expressions;
	}

	private List<SDFExpressionParameter> getExpressionForOnCentreSpot(
			ILogicalOperator source) throws NumberFormatException,
			MissingDDCEntryException {

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
				.createExpressionParameter("eif( "
						+ IntermediateSchemaAttributes.X + " >= " + spotTopX
						+ " AND " + IntermediateSchemaAttributes.X + " <= "
						+ spotBottomX + " AND "
						+ IntermediateSchemaAttributes.Y + " >= " + spotTopY
						+ " AND " + IntermediateSchemaAttributes.Y + " <= "
						+ spotBottomY + " , 1, 0)", ATT_ONCENTRESPOT, source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter(
						"toDouble("
								+ IntermediateSchemaAttributes.TS
								+ " - "
								+ String.valueOf(TimeUnitHelper
										.getMicroseconds(TIME_BETWEEN,
												TimeUnit.seconds)) + ")",
												ATT_N_TS, source);
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);

		return expressions;
	}

}
